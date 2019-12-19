#!/bin/groovy
package org.dsva;

def gitRef

def _runInVirtualEnv(command) {
  return "bash -c 'source venv/bin/activate && ${command}'"
}

def setup() {
  stage ('Clone DevOps Repo') {
    withCredentials([
      [
        // Token to access the appeals deployment repo.
        $class: 'StringBinding',
        credentialsId : 'GIT_CREDENTIAL',
        variable: 'GIT_CREDENTIAL',
      ]]) {
        sh "git clone -b ${params.devopsBranch} https://${env.GIT_CREDENTIAL}@github.com/department-of-veterans-affairs/devops.git"
        dir ('./devops/') {
        gitRef = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
      }
    }

    // Do we need to load an alternate commit?
    if (!"${params.commitHash}".equalsIgnoreCase('HEAD')) {
      sh(script: "git checkout ${params.commitHash}")
      gitRef = "${params.commitHash}"
    }

    dir('./devops/ansible') {
      sh 'virtualenv venv'
      sh 'venv/bin/pip install --upgrade pip'
      sh 'venv/bin/pip install -r requirements.txt'
    }
  }
}

def build(args) {
  stage ('Build') {
    echo "Building ${args.app} at ref ${gitRef}"

    if (!params.shouldBuild) {
      echo "Build skipped by job parameter"
      return;
    }

    def command = "ansible-playbook -e app=${args.app} -e ref=${gitRef} -e rebuild=true build/create.yml"
    command = _runInVirtualEnv(command)

    dir ('./devops/ansible') {
      sh "${command}"
    }
  }
}

def release(args) {
  stage ('Release') {
    echo "Releasing ${args.app} at ref ${gitRef}"

    if (!params.shouldRelease) {
      echo "Release skipped by job parameter"
      return;
    }

    def command = "ansible-playbook -e app=${args.app} -e ref=${gitRef} -i inventory release/create.yml"
    command = _runInVirtualEnv(command)

    dir ('./devops/ansible') {
      sh "${command}"
    }
  }
}

def deploy(args) {
  stage ("Deploy (${args.name})") {
    echo "Deploying ${args.config} at ref ${gitRef}"

    if (!params.shouldDeploy) {
      echo "Deploy skipped by job parameter"
      return;
    }

    def command = "ansible-playbook -e config=${args.config} -e ref=${gitRef} deployment/create.yml"
    command = _runInVirtualEnv(command)

    dir ('./devops/ansible') {
      sh "${command}"
    }
  }
}

def serverspec(args) {
  stage ("Serverspec ${args.name}") {
    echo "Testing ${args.spec} serverspec"

    if (!params.shouldTest) {
      echo "Testing skipped by job parameter"
      return;
    }

    // The docker container needs the SSH key
    dir ('./devops/serverspec') {
      sh "cp -u /home/jenkins/.ssh/id_rsa ./"
    }

    // Build docker container from Dockerfile
    def container = docker.build(
          "brd-utils-serverspec:${gitRef}",
          'jenkins/common/brd-utils')

    // Run commands inside the Docker container
    dir ('./devops/serverspec') {
      container.inside {
        sh "mkdir /tmp/.ssh/"
        sh "cp id_rsa /tmp/.ssh/"
        sh "bash -c \"HOME=/tmp/ bundle install\""
        sh "bash -c \"HOME=/tmp/ /tmp/bin/rake ${args.spec}\""
      }
    }
  }
}

return this
