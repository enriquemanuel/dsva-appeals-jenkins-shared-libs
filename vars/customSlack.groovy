#!/usr/bin/env groovy

def call(Map stageParams) {
    buildResult = stageParams.buildResult
    message = stageParams.message ? stageParams.message : "${buildResult} | Job: ${env.JOB_NAME} with buildnumber ${env.BUILD_NUMBER}"

    try{
        if ( buildResult == "SUCCESS" ) {
            slackSend   color: "good",
                        message: message,
                        channel: stageParams.channel

        }
        else if( buildResult == "FAILURE" ) {
            slackSend   color: "danger",
                        message: message,
                        channel: stageParams.channel
        }
        else if( buildResult == "UNSTABLE" ) {
            slackSend   color: "warning",
                        message: message,
                        channel: stageParams.channel
        }
        else {
            slackSend   color: "danger",
                        message: message,
                        channel: stageParams.channel

        }
    }
    catch(err) {
                println "Failed to notify Slack: ${err}"
    }
}
