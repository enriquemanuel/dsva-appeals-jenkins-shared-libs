#!/bin/groovy
package org.dsva;

rubyPipeline(pipelineDefinition){
  pd = pipelineDefinition
}

def executePipeline(){
  node {
    agent { label 'devops-ruby' }

    if (deployToHigherEnvsUponSuccess) {
      stage('Deploying to Lower Environments First...'){
        echo 'lower first'
      }
    }

    else{
      stage('Deploying to all environments without waiting...'){
        echo 'not waiting'
      }
    }
  }


}
return this
