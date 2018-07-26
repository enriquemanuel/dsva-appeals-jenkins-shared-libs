#!/bin/groovy
package org.dsva;
def execute(){
  node {
    stage('Initialize') {
      checkout scm
      echo 'Loading pipeline definition'
      Yaml parser = new Yaml()
      Map pipelineDefinition = parser.load(new File(pwd() + '/pipeline.yml').text)
    }

    switch(pipelineDefinition.pipelineType){
      case 'python':
        new pythonPipeline(pipelineDefinition).executePipeline()
      case 'ruby':
        new rubyPipeline(pipelineDefinition).executePipeline()
    }
}
