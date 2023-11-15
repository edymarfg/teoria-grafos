package com.teoriagrafos.teoriagrafos.model;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Segment {
    public double distance;
    public double duration;
    public ArrayList<Step> steps;

    public double getPeso() {
        return this.getDistance() + this.getDuration();
    }
}
