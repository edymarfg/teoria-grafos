package com.teoriagrafos.teoriagrafos.model;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Step {
    public double distance;
    public double duration;
    public int type;
    public String instruction;
    public String name;
    public int exit_number;
    public ArrayList<Integer> way_points;
}
