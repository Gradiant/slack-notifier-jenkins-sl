# Slack Notifier for Jenkins via Shared Library (Unstable)

This is a shared library for Jenkins Pipeline. Extracts build information in the same format as normal slack plugin does. By now it send the following notifications:

* Failure
* Still Failing
* Back to normal

TODO: Add test report in the notification body

## How to use it

* Import the library as Global Shared Library [docs](https://jenkins.io/blog/2017/02/15/declarative-notifications/)
* Use it in your Jenkinsfile:
 
 ```groovy
def notifyBuild() {
    SLACK_TEAM = 'elearningtechies'
    CHANNEL = '#smartbook-ci'
    SLACK_CREDENTIALS = 'jenkins-slack'

    slackNotifier(SLACK_TEAM, SLACK_CREDENTIALS, CHANNEL)
}
```
