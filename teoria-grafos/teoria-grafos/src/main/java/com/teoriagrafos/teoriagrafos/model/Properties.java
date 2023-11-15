package com.teoriagrafos.teoriagrafos.model;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Properties {
    public int transfers;
    public int fare;
    public ArrayList<Segment> segments;
    public ArrayList<Integer> way_points;
    public Summary summary;
}
