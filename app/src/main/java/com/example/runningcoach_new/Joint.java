package com.example.runningcoach_new;


public class Joint {
    private int index;
    private String name;
    private float x;
    private float y;

    public Joint(int index, String name, float x, float y) {
        this.index = index;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
