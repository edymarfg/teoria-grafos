package com.teoriagrafos.teoriagrafos.model.SearchRotasModels;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Query {
    private ArrayList<ArrayList<Double>> coordinates;
    private String profile;
    private String format;
}
