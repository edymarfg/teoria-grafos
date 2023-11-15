package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Rotas {
    private String type;
    private Metadata metadata;
    private ArrayList<Feature> features;
    private ArrayList<Double> bbox;
}
