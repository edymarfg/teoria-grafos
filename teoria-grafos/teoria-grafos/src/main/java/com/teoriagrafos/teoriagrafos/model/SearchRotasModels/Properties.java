package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Properties {
    private int transfers;
    private int fare;
    private ArrayList<Segment> segments;
    private ArrayList<Integer> way_points;
    private Summary summary;
}
