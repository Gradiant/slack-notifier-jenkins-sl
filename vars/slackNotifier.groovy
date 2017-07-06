#!/usr/bin/env groovy

def call(teamDomain, tokenCredentialId, channel) {
    SLACK_TEAM = teamDomain
    CHANNEL = channel
    SLACK_CREDENTIALS = tokenCredentialId
    notifyResult()
}

private String color(String result = '') {
    GREEN = "#36A64F"
    RED = "#D00000"
    YELLOW = "#DAA038"

    SUCCESS = "SUCCESS"
    FAILURE = "FAILURE"
    UNSTABLE = "UNSTABLE"

    switch (result) {
        case SUCCESS:
            return GREEN
        case FAILURE:
            return RED
        case UNSTABLE:
        default:
            return YELLOW
    }
}

/*private getTests() {
    result = currentBuild.rawBuild.getAction(hudson.tasks.junit.TestResultAction.class).result
    text = "Tests"
    if (result == null) {
        return "No Tests found."
    }
}*/

private String buildMessage(String title = "", String msg = "") {
    PROJECT = env.BUILD_URL.split('job/')[1].split('/')[0]
    return "${PROJECT} » ${env.BRANCH_NAME} - #${currentBuild.number} ${title.trim()} (<${currentBuild.absoluteUrl}|Open>)\n${msg}".trim()
}

private List changes() {
    List msgs = []

    for (int i = 0; i < currentBuild.changeSets.size(); i++) {
        def entries = currentBuild.changeSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            msgs.add("- ${entry.msg} [${entry.author}]")
        }
    }

    return msgs
}

private String getStatus() {
    currentStatus = currentBuild.currentResult;
    prevStatus = currentBuild.previousBuild.currentResult;

    SUCCESS = "SUCCESS"
    FAILURE = "FAILURE"
    UNSTABLE = "UNSTABLE"

    statusChangedToNormal = currentStatus == SUCCESS && (prevStatus == FAILURE || prevStatus == UNSTABLE)
    if (statusChangedToNormal) {
        return "Back to normal"
    }

    stillFailing = currentStatus == FAILURE && prevStatus == FAILURE
    if (stillFailing) {
        return "Still failing"
    }

    if (currentStatus == FAILURE) {
        return "Failure"
    }

    if (currentStatus == SUCCESS) {
        return "Success"
    }
}

private notifyResult() {
    def duration = currentBuild.durationString
    String status = getStatus()

    if (!status.equals("Success")) {
        String message = buildMessage("${status} after ${duration}", changes().join("\n"))
        toSlack(message, color(currentBuild.currentResult))
    }
}

private notifyError(err) {
    def duration = currentBuild.durationString
    String message = buildMessage("An errror ocurred after ${duration} :interrobang:", err.message)
    toSlack(message, color('FAILURE'))
}

private notifyStart() {
    String message = buildMessage("Build started...")
    toSlack(message, color())
}

private toSlack(String message, String color) {
    slackSend channel: CHANNEL, color: color, message: message, teamDomain: SLACK_TEAM, tokenCredentialId: SLACK_CREDENTIALS
}

