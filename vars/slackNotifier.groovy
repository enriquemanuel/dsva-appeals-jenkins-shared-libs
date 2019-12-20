#!/usr/bin/env groovy

def call(Map stageParams) {
    buildResult = stageParams.buildResult
    success_channel = stageParams.success_channel
    fail_channel = stageParams.fail_channel

    if ( buildResult == "SUCCESS" ) {
        slackSend   color: "good", 
                    message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was successful",
                    channel: success_channel
    }
    else if( buildResult == "FAILURE" ) { 
        slackSend   color: "danger", 
                    message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was failed",
                    channel: fail_channel
    }
    else if( buildResult == "UNSTABLE" ) { 
        slackSend   color: "warning", 
                    message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} was unstable",
                    channel: fail_channel
    } 
    else {
        slackSend   color: "danger", 
                    message: "Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER} its resulat was unclear",
                    channel: fail_channel
    }
}