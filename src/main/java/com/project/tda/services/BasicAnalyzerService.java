package com.project.tda.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicAnalyzerService {
    Utils utils = new Utils();
    int id_count = 0;
    ArrayList<SingleThreadAnalyzerService> threads = new ArrayList();
    Map threadMap = new HashMap();
    ArrayList SynchronizerAnalysisServiceArr = new ArrayList();
    Map<String, SynchronizerAnalysisService> synchronizerMap = new HashMap();
    ArrayList ignoredData = new ArrayList(); //counts
    Map<String, ArrayList<String>> blockersMap = new HashMap<>();
    String deadlockStatus = null;
    String date = null;

    Map<String, Counter> runningMethods = new HashMap();

    public ArrayList<SingleThreadAnalyzerService> generateAnalysis(String datas) {
        ArrayList<SingleThreadAnalyzerService> result = null;
        String[] splittedLines = datas.split("\n");
        result = analyze(splittedLines);
        return result;
    }

    private ArrayList<SingleThreadAnalyzerService> analyze(String[] splittedLines) {
        String date_pattern = "^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})$";

        SingleThreadAnalyzerService currentThread = null;
        for (String line : splittedLines) {
            if (date == null && line.matches(date_pattern)) {
                date = line.trim();
                ignoredData.add(line);

            }
            //TODO : check for multiline header
            //TODO : check lines now for empty stacktrace and thread

            else if (line.isEmpty() || line.contains("Full thread dump")) {
                //Totally ignore empty lines
                //Add other lines to ignore list
                if (!line.isEmpty())
                    ignoredData.add(line);
            }

            //If a thread is created & line starts with tab or space, analyze for stack
            else if ((line.startsWith(" ") || line.startsWith("\t")) && currentThread != null) {
                currentThread.parse_Stack(line);
            } else {
                //create a thread obj
                //check valid

                if (currentThread != null && currentThread.name != "undefined") {
                    currentThread.getStatus = utils.get_status(currentThread);
                    if (currentThread.id == "undefined") {
                        id_count++;
                        currentThread.id = "ud" + String.valueOf(id_count);
                    }
                    threads.add(currentThread);
                    threadMap.put(currentThread.tid, currentThread);
                }
                currentThread = new SingleThreadAnalyzerService(line);
                if (!currentThread.name.equals("undefined")) {
                    continue;
                } else {
                    currentThread = null;
                }
                //add ignore data
                ignoredData.add(line);
            }

        }
        //last thread
        if (currentThread != null) {
            if (currentThread.name != "undefined") {
                currentThread.getStatus = utils.get_status(currentThread);
                threads.add(currentThread);
                threadMap.put(currentThread.tid, currentThread);

            }

        }

        identifyWaitedSync();
        countRunningMethods();
        analyzeSynchronizerAnalysisService();
        analyzeDeadlocks();

        return threads;
    }

    private void identifyWaitedSync() {
        /* Some threads are waiting for notification, but the thread dump
         * doesn't say on which object. This function guesses in the
         * simple case where those threads are holding only a single lock.
         */
        List<String> list = new ArrayList<>();
        list.add("TIMED_WAITING (on object monitor)");
        list.add("WAITING (on object monitor)");
        for (SingleThreadAnalyzerService SingleThreadAnalyzerService : threads) {
            if (list.contains(SingleThreadAnalyzerService.threadState)) {
                // Not waiting for notification
                continue;
            }
            //- waiting on <7c135ea90> (a java.util.Vector)  => wantNotificationOn- '7c135ea90'
            if (SingleThreadAnalyzerService.wantNotificationOn != null || SingleThreadAnalyzerService.classicalLockHeld == null) {
                continue;
            }
            SingleThreadAnalyzerService.setWantNotificationOn(SingleThreadAnalyzerService.classicalLockHeld);
        }
    }

    private void countRunningMethods() {

        for (SingleThreadAnalyzerService SingleThreadAnalyzerService : threads) {

            if (!SingleThreadAnalyzerService.getStatus.equals(utils.ThreadStatus_RUNNING) || SingleThreadAnalyzerService.frames.isEmpty()) {
                continue;
            }
            String currentMethod = ((String) SingleThreadAnalyzerService.frames.get(0)).replace("at", "").trim();
            addString(currentMethod, SingleThreadAnalyzerService);
        }
        //sort
    }


    private void analyzeSynchronizerAnalysisService() {
        mapSynchronizerAnalysisService();
        crossRefSync();
        analyzesyncblocks();
    }

    private void mapSynchronizerAnalysisService() {
        /**
         * Build up the synchronizer map
         */

        for (SingleThreadAnalyzerService thread : threads) {
            //wantNotificationOn=lockID;
            registerSynchronizer(thread.wantNotificationOn, thread.synchronizerClasses);
            registerSynchronizer(thread.wantToAcquire, thread.synchronizerClasses);
            for (Object lock : thread.locksHeld) {
                registerSynchronizer((String) lock, thread.synchronizerClasses);
            }
        }

        //after mapping add all synchrnoizers to arraylist

        synchronizerMap.forEach((key, values) -> {
            this.SynchronizerAnalysisServiceArr.add(values);
        });

    }

    private void registerSynchronizer(String id, Map synchronizerClasses) {
        if (id == null) {
            return;
        }
        if (!synchronizerMap.containsKey(id)) {
            synchronizerMap.put(id, new SynchronizerAnalysisService(id, synchronizerClasses.get(id)));
        }
    }

    private void crossRefSync() {
        /**
         * Cross reference the SynchronizerAnalysisService and threads
         */
        for (SingleThreadAnalyzerService thread : threads) {
            SynchronizerAnalysisService SynchronizerAnalysisService;
            if (thread.wantNotificationOn != null) {
                SynchronizerAnalysisService = synchronizerMap.get(thread.wantNotificationOn);
                SynchronizerAnalysisService.notificationWaiters.add(thread);
            }
            if (thread.wantToAcquire != null) {
                SynchronizerAnalysisService = synchronizerMap.get(thread.wantToAcquire);
                SynchronizerAnalysisService.lockWaiters.add(thread);
            }
            for (Object lock : thread.locksHeld) {
                SynchronizerAnalysisService = synchronizerMap.get(lock);
                SynchronizerAnalysisService.lockHolder = thread;
            }

        }


    }

    private void analyzesyncblocks() {

        synchronizerMap.forEach((s, SynchronizerAnalysisService) -> {
            if (SynchronizerAnalysisService.lockHolder != null && SynchronizerAnalysisService.lockWaiters.size() > 0) {
                ArrayList<String> gotblocked = new ArrayList<>();
                SynchronizerAnalysisService.lockWaiters.forEach(o -> {
                    SingleThreadAnalyzerService SingleThreadAnalyzerService = (SingleThreadAnalyzerService) o;
                    gotblocked.add(((SingleThreadAnalyzerService) o).name);
                });
                blockersMap.put(SynchronizerAnalysisService.lockHolder.name, gotblocked);
            }
        });
    }

    private void analyzeDeadlocks() {
        for (Object synchronizer : SynchronizerAnalysisServiceArr) {
            String status = determineDeadlockStatus((SynchronizerAnalysisService) synchronizer);
            ((SynchronizerAnalysisService) synchronizer).deadlockStatus = status;

        }
    }

    private String determineDeadlockStatus(SynchronizerAnalysisService sync) {
        if (sync.lockHolder == null) {
            if (sync.lockWaiters.size() > 0) {
                return "DEADLOCKED";
            } else {
                return "NONE";
            }
        }
        if (sync.lockHolder.getStatus.equals(utils.ThreadStatus_WAITING_NOTIFY) && sync.lockHolder.wantNotificationOn == null) {
            // waiting, but no notification object??
            return "High Risk DeadLock | Waiting for notification on unknown object.";
        }
        if (utils.isWaiting(sync.lockHolder.getStatus)) {
            return "NONE";
        }
        if (sync.lockWaiters.size() == 0 && sync.notificationWaiters.size() == 0) {
            // nobody is waiting for us
            return "NONE";
        }
        // If there is a loop on "waiting to acquire" then there is a deadlock
        // If a thread is "awaiting notification" then there might be a deadlock
        Stack<SingleThreadAnalyzerService> work = new Stack();
        work.push(sync.lockHolder);
        Map<String, String> visited = new HashMap();
        visited.put(sync.id, "true");
        while (work.size() > 0) {
            SingleThreadAnalyzerService thread = work.pop();
            if (thread.wantNotificationOn != null) {
                return "HIGH_RISK DeadLock";
            }
            if (thread.wantToAcquire != null) {
                if (visited.get(thread.wantToAcquire).equals("true")) {
                    return "DEADLOCKED";
                }
                visited.put(thread.wantToAcquire, "true");
                SynchronizerAnalysisService synchro = (SynchronizerAnalysisService) synchronizerMap.get(thread.wantToAcquire);
                if (synchro.lockHolder != null) {
                    work.push(synchro.lockHolder);
                }
            }
        }

        return "Dead Locked";
    }


    private void addString(String line, SingleThreadAnalyzerService source) {
        if (!runningMethods.containsKey(line)) {
            ArrayList arr = new ArrayList();
            arr.add(source);
            Counter counter = new Counter(arr);
            runningMethods.put(line, counter);
        } else {
            Counter counter = runningMethods.get(line);
            counter.increaseCount();
            counter.pushSource(source);
        }
    }

    public Map<String, SynchronizerAnalysisService> getSync (){return synchronizerMap;}
    public String getDate(){return date;}
}
