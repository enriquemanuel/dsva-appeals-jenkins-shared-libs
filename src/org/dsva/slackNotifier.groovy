#!/bin/groovy
package org.dsva;

def notify(message, color='good', tokenEnvVar='SLACK_TOKEN', channelEnvVar='SLACK_CHANNEL') {

  withCredentials([
    [
      // API token to integrate with Slack channel.
      $class: 'StringBinding',
      credentialsId : tokenEnvVar,
      variable: 'SLACK_TOKEN',
    ],
    [
      // Team Domain associated Slack channel.
      $class: 'StringBinding',
      credentialsId : 'SLACK_TEAM_DOMAIN',
      variable: 'SLACK_TEAM_DOMAIN',
    ],
    [
      // Target Slack Channel
      $class: 'StringBinding',
      credentialsId : channelEnvVar,
      variable: 'SLACK_CHANNEL',
    ]
  ]) {
    slackSend message: message,
              color: color,
              channel: env.SLACK_CHANNEL,
              teamDomain: env.SLACK_TEAM_DOMAIN,
              token: env.SLACK_TOKEN,
              failOnError: true
  }
}

return this;
