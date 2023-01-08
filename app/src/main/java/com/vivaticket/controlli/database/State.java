package com.vivaticket.controlli.database;

public class State {

    private int id;
    private String name;
    private String code;

    State(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId(){
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

}
