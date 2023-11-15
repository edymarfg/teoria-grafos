package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Step {
    private double distance;
    private double duration;
    private int type;
    private String instruction;
    private String name;
    private int exit_number;
    private ArrayList<Integer> way_points;
}
