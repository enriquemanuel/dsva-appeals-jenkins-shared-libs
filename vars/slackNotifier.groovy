#!/usr/bin/env groovy

def call(String buildResult, Map stageParams) {
  if ( buildResult == "SUCCESS" ) {
    slackSend   color: "good", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was successful",
                channel: stageParams.success_channel
  }
  else if( buildResult == "FAILURE" ) { 
    slackSend   color: "danger", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was failed",
                channel: stageParams.failure_channel
  }
  else if( buildResult == "UNSTABLE" ) { 
    slackSend   color: "warning", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was unstable",
                channel: stageParams.failure_channel
  } 
  else {
    slackSend   color: "danger", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} its resulat was unclear",
                channel: stageParams.failure_channel
  }
}