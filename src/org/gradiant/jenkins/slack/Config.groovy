package org.gradiant.jenkins.slack


Boolean getNotifySuccess() {
  if(env.NOTIFY_SUCCESS) return new Boolean(env.NOTIFY_SUCCESS)
  return false
}

Boolean getChangeList() {
  if(env.CHANGE_LIST) return new Boolean(env.CHANGE_LIST)
  return false
}

Boolean getTestSummary() {
  if(env.TEST_SUMMARY) return new Boolean(env.TEST_SUMMARY)
  return false
}
