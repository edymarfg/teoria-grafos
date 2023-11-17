package com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchMatrix {
    public ArrayList<ArrayList<Double>> durations;
   	public ArrayList<ArrayList<Double>> distances;
}
