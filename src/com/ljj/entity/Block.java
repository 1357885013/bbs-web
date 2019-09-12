package com.ljj.entity;

public class Block {
    private int id=-1;
    private String name=null;

    public Block() {
    }

    public Block(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Block(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
