package com.project.tda.services;

import java.util.ArrayList;

public class SynchronizerAnalysisService {
    String id;
    Object className;
    ArrayList notificationWaiters=new ArrayList();
    ArrayList lockWaiters=new ArrayList();
    SingleThreadAnalyzerService lockHolder;
    String deadlockStatus=null;

    public SynchronizerAnalysisService(String id, Object className) {
        this.id = id;
        this.className = className;
    }

    int getThreadCount(){
        int count=0;
        if (this.lockHolder != null) {
            ++count;
        }
        count += this.lockWaiters.size();
        count += this.notificationWaiters.size();
        return count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getClassName() {
        return className;
    }

    public void setClassName(Object className) {
        this.className = className;
    }

    public ArrayList getNotificationWaiters() {
        return notificationWaiters;
    }

    public void setNotificationWaiters(ArrayList notificationWaiters) {
        this.notificationWaiters = notificationWaiters;
    }

    public ArrayList getLockWaiters() {
        return lockWaiters;
    }

    public void setLockWaiters(ArrayList lockWaiters) {
        this.lockWaiters = lockWaiters;
    }

    public SingleThreadAnalyzerService getLockHolder() {
        return lockHolder;
    }

    public void setLockHolder(SingleThreadAnalyzerService lockHolder) {
        this.lockHolder = lockHolder;
    }

    public String getDeadlockStatus() {
        return deadlockStatus;
    }

    public void setDeadlockStatus(String deadlockStatus) {
        this.deadlockStatus = deadlockStatus;
    }
}
