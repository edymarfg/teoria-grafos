package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Segment {
    private double distance;
    private double duration;
    private ArrayList<Step> steps;

    public double getPeso() {
        return this.getDistance() + this.getDuration();
    }
}
