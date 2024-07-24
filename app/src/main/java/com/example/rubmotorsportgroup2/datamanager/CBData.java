package com.example.rubmotorsportgroup2.datamanager;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class CBData {
    private ArrayList<Instant> cbStarts = new ArrayList<Instant>();
    private ArrayList<Duration> cbTimes = new ArrayList<Duration>();

    public CBData(int cellcount){
        for (int i = 0; i < cellcount; i++){
            cbStarts.add(Instant.ofEpochSecond(0));
            cbTimes.add(Duration.ofSeconds(0));
        }

    }

    public boolean hasStarted(int index){
        return !cbStarts.get(index).equals(Instant.ofEpochSecond(0));
    }
    public void setStart(int index){
        int i = index;

        cbStarts.set(index, Instant.now());
        //System.out.println("INITIATED DA START");
    }
    public void setTime(int index){
        cbTimes.set(index, Duration.between(cbStarts.get(index), Instant.now()));
        cbStarts.set(index, Instant.ofEpochSecond(0));
    }
    public Duration getTime(int index){
        return cbTimes.get(index);
    }
}
