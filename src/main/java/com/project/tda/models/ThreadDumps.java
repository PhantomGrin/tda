package com.project.tda.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.tda.models.security.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class ThreadDumps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int threadId;

    private String name;

    private String username;

    private String date;

    @CreationTimestamp
    private Date analysisDate;

    @Lob
    private String resultString;

    @ManyToMany
    @JsonIgnore
    @JsonBackReference
    private Set<User> sharedwithusers;

    public int getThreadId() {
        return threadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ThreadDumps(String name, String resultString, String username, String date) {
        this.name = name;
        this.resultString = resultString;
        this.username = username;
        this.date = date;
    }

    public ThreadDumps() {
    }
}
