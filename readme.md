# Slack Notifier for Jenkins via Shared Library

This is a shared library for the Jenkins Pipeline. It extracts building information, including a list of changes and tests summary, in the same format as a 
normal slack plugin does.

## Requirements

The [slack plugin](https://wiki.jenkins.io/display/JENKINS/Slack+Plugin) should be installed in your Jenkins instance. 

## Notifications

By default, this library always sends the following notifications:

* Failure
* Unstable
* Still Failing
* Back to normal

The "Success" notification is only sent when the environment variable "NOTIFY_SUCCESS" is set to true.

## Configuration

This library receives all its configuration through environment variables

```
* SLACK_CHANNEL:        Slack channel for sending the notifications. If not set, the global jenkins configuration will be used.
* SLACK_DOMAIN          Slack team domain. Slack channel for sending the notification. If not set, the global jenkins configuration will be used.
* SLACK_CREDENTIALS:    Identifier of the credentials entry stored in the Jenkins's credential store. If not set, the global jenkins configuration will be used.
* CHANGE_LIST:          Includes the change list. False by default.
* TEST_SUMMARY:         Includes the test summary. False by default 
* NOTIFY_SUCCESS:       If true, all succeeded builds will be notified.
```

## API Definition

```
notifyStart() :     Send a "Build started" notification.
notifyResult():     Send the result of the build (Failure, Stillfailing, etc.).
notifyResultFull(): The same as notifyResult but asumming that all configuration values are true (TEST_SUMMARY, CHANGE_LIST, NOTIFY_SUCCESS).
notifyError(error): Send a message with the value of the error message.
```
## How to use it

* Import the library as Global Shared Library [(you can follow this examples)](https://jenkins.io/blog/2017/02/15/declarative-notifications/)
* Use it in your Jenkinsfile:


```groovy
def notifier = new org.gradiant.jenkins.slack.SlackNotifier()

try {

 env.SLACK_CHANNEL = 'my-channel'
 env.SLACK_DOMAIN  = 'my-team-domain'
 env.SLACK_CREDENTIALS = 'jenkins-slack-credentials-id'
 env.CHANGE_LIST = 'true'
 env.TEST_SUMMARY = 'true'

 notifier.notifyStart()

 // ...
 // your build steps
 // ...

} catch (err) {
  // Mark the build as failure since it's actually an error
  currentBuild.result = 'FAILURE'

  notifier.notifyError(err)

  // Throw the error so jenkins can do whatever he does with it
  throw err
} finally {

  // Notify the build result with a list of changes
  notifier.notifyResult()
}
```