package com.vivaticket.controlli.database;

public class Province {

    private int id;
    private String name;
    private String code;

    Province(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
