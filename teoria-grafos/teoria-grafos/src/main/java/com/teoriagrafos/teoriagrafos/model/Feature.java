package com.teoriagrafos.teoriagrafos.model;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Feature {
    public ArrayList<Double> bbox;
    public String type;
    public Properties properties;
    public Geometry geometry;
}
