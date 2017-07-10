package org.gradiant.jenkins.slack


void send(String message, String color = '#fff') {
  def options = getOptions message, color

  slackSend options
}


def getOptions(String message = '', String color = '') {
  def obj = [
    message: message
  ]

  if (color) {
    obj.color = color
  }

  if (env.SLACK_CHANNEL) {
    obj.channel = env.SLACK_CHANNEL
  }

  if (env.SLACK_DOMAIN) {
    obj.teamDomain = env.SLACK_DOMAIN
  }

  if (env.SLACK_CREDENTIALS) {
    obj.tokenCredentialId = env.SLACK_CREDENTIALS
  }

  return obj
}