package com.teoriagrafos.teoriagrafos.model;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Rotas {
    public String type;
    public Metadata metadata;
    public ArrayList<Feature> features;
    public ArrayList<Double> bbox;
}
