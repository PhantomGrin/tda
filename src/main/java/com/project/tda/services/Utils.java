package com.project.tda.services;

import com.project.tda.services.SingleThreadAnalyzerService;

public class Utils {
    protected String ThreadStatus_RUNNING = "running";
    protected String ThreadStatus_NON_JAVA_THREAD = "non-Java thread";
    protected String ThreadStatus_TERMINATED = "terminated";
    protected String ThreadStatus_NEW = "not started";
    protected String ThreadStatus_WAITING_ACQUIRE = "waiting to acquire";
    protected String ThreadStatus_WAITING_NOTIFY = "awaiting notification";
    protected String ThreadStatus_WAITING_NOTIFY_TIMED = "awaiting notification (timed)";
    protected String ThreadStatus_SLEEPING = "sleeping";
    protected String ThreadStatus_UNKNOWN = "?unknown?";

    public String get_status(SingleThreadAnalyzerService thread){
        return determineStatus(thread);
    }

    private String determineStatus(SingleThreadAnalyzerService thread){

        String status;
        if (thread.wantNotificationOn != null) {
            status = ThreadStatus_WAITING_NOTIFY;
        } else if (thread.threadState ==null) {
            status=determineStatusStateless(thread);
        }
        else if (thread.threadState.equals("WAITING (on object monitor)")) {
            status = ThreadStatus_WAITING_NOTIFY;
        } else if (thread.threadState.equals("TIMED_WAITING (on object monitor)")) {
            status = ThreadStatus_WAITING_NOTIFY_TIMED;
        } else if (thread.wantToAcquire != null) {
            status = ThreadStatus_WAITING_ACQUIRE;
        } else if (thread.threadState.equals("TIMED_WAITING (sleeping)")) {
            status = ThreadStatus_SLEEPING;
        } else if (thread.threadState.equals("NEW")) {
            status = ThreadStatus_NEW;
        } else if (thread.threadState.equals("TERMINATED")) {
            status = ThreadStatus_TERMINATED;
        } else if (thread.frames.size() == 0) {
            status = ThreadStatus_NON_JAVA_THREAD;
        } else if (thread.threadState.equals("RUNNABLE")) {
            status = ThreadStatus_RUNNING;
        }  else {

            status = ThreadStatus_UNKNOWN;
        }

        return status;
    }

    private String determineStatusStateless(SingleThreadAnalyzerService thread) {
        String status;
        if (thread.status == "RUNNABLE") {
            status = ThreadStatus_RUNNING;
        } else if (thread.status == "TIMED_WAITING") {
            status = ThreadStatus_WAITING_NOTIFY_TIMED;
        } else if (thread.status == "WAITING") {
            status = ThreadStatus_WAITING_NOTIFY;
        } else if (thread.status == "NEW") {
            status = ThreadStatus_NEW;
        } else if (thread.status == "TERMINATED") {
            status = ThreadStatus_TERMINATED;
        } else if (thread.status == "BLOCKED") {
            status = ThreadStatus_WAITING_ACQUIRE;
        } else {
            status = ThreadStatus_NON_JAVA_THREAD;
        }
        return status;
    }

    protected boolean isWaiting(String status){
        return status.equals(ThreadStatus_WAITING_ACQUIRE) ||
                status.equals(ThreadStatus_WAITING_NOTIFY) ||
                status.equals(ThreadStatus_WAITING_NOTIFY_TIMED);
    }
}
