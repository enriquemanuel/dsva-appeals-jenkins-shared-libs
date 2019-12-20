#!/usr/bin/env groovy

def call(String buildResult) {
  if ( buildResult == "SUCCESS" ) {
    slackSend   color: "good", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was successful",
                channel: "dev-null"
  }
  else if( buildResult == "FAILURE" ) { 
    slackSend   color: "danger", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was failed",
                channel: "dev-null"
  }
  else if( buildResult == "UNSTABLE" ) { 
    slackSend   color: "warning", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was unstable",
                channel: "dev-null"
  } 
  else {
    slackSend   color: "danger", 
                message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} its resulat was unclear",
                channel: "dev-null"
  }
}