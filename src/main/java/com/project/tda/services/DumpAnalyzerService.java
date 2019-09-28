package com.project.tda.services;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DumpAnalyzerService {
    Utils utils;

    ArrayList<SingleThreadAnalyzerService> threads=new ArrayList();
    Map threadMap=new HashMap();
    Map<String,List<SingleThreadAnalyzerService>> threadsByStatus =new HashMap();
    ArrayList synchronizersArr = new ArrayList();
//    Map<String,Synchronizers> synchronizerMap = new HashMap();
    ArrayList ignoredData=new ArrayList(); //counts
    Map<String,ArrayList<String>> blockersMap=new HashMap<>();
    String deadlockStatus=null;

//    Map<String, Counter> runningMethods=new HashMap();

    public DumpAnalyzerService(){
        utils = new Utils();
    }

    public String generateAnalysis(String datas){
        String result = "none";
        String[] splittedLines=datas.split("\n");
        result = analyze(splittedLines);
        return result;
    }

    private String analyze(String[] splittedLines){
        String date_pattern="^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})$";
        String date=null;

        SingleThreadAnalyzerService currentThread = null;
        for(String line:splittedLines){
            if(date==null && line.matches(date_pattern)){
                date=line.trim();
                ignoredData.add(line);

            }
            //TODO : check for multiline header
            //TODO : check lines now for empty stacktrace and thread

            else if (line.isEmpty()||line.contains("Full thread dump")){
                //Totally ignore empty lines
                //Add other lines to ignore list
                if(!line.isEmpty())
                    ignoredData.add(line);
            }

            //If a thread is created & line starts with tab or space, analyze for stack
            else if((line.startsWith(" ") || line.startsWith("\t")) && currentThread!=null){
                currentThread.parse_Stack(line);
            }
            else{
                //create a thread obj
                //check valid

                if(currentThread!=null && currentThread.name!="undefined"){
                    currentThread.getStatus=utils.get_status(currentThread);
                    threads.add(currentThread);
                    threadMap.put(currentThread.tid,currentThread);
                }
                currentThread =new SingleThreadAnalyzerService(line);
                if(!currentThread.name.equals("undefined")){
                    continue;
                }else {
                    currentThread=null;
                }
                //add ignore data
                ignoredData.add(line);
            }

        }
        //last thread
        if(currentThread!=null ){
            if( currentThread.name!="undefined"){
                currentThread.getStatus=utils.get_status(currentThread);
                threads.add(currentThread);
                threadMap.put(currentThread.tid,currentThread);

            }

        }

        //identifyWaitedSync();
        //mapThreadsByStatus();
        //countRunningMethods();

        //analyzeSynchronizers();

        //analyzeDeadlocks();


//        threadsByStatus.forEach((s, dumpThreads) -> {
//            System.out.println(s);
//            System.out.println(dumpThreads.size());
//        });
//
//        runningMethods.forEach((s, counter) -> {
//            System.out.println(s);
//            System.out.println(counter.getCount());
//            System.out.println(counter.getSources());
//        });
//        System.out.println(ignoredData);

//        threads.forEach(thread -> {
//            if(thread.frames.size()==0){
//                System.out.println(thread.name);
//            }
//        });
        String results = "None";
        results = new Gson().toJson(threads);

        return results;
    }

}
