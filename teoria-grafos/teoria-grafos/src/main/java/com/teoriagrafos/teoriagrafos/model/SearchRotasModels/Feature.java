package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Feature {
    private ArrayList<Double> bbox;
    private String type;
    private Properties properties;
    private Geometry geometry;
}
