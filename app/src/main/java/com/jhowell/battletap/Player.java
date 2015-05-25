package com.jhowell.battletap;

import java.io.Serializable;

public class Player implements Serializable {

    // Player information
    private int id;
    private String username;
    private String password;

    // Counters
    private int count;
    private int archers;
    private int knights;
    private int cavalry;

    public Player() {
        this.count = 0;
    }

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.count = 0;
    }

    public Player(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.count = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}