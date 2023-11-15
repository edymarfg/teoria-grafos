package com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class FeatureAuto{
    private String type;
    private GeometryAuto geometry;
    private PropertiesAuto properties;
    private ArrayList<Double> bbox;
}
