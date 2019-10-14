package com.project.tda.services;

import java.util.ArrayList;

public class Counter {
    int count;
    ArrayList<SingleThreadAnalyzerService> sources=new ArrayList();

    public Counter(ArrayList sources) {
        this.sources = sources;
        this.count=1;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList getSources() {
        return sources;
    }

    public void setSources(ArrayList sources) {
        this.sources = sources;
    }
    public void pushSource(SingleThreadAnalyzerService object){
        this.sources.add(object);
    }
    public void increaseCount(){
        count++;
    }
}
