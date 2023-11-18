package com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels;

import java.util.List;

import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.OptionLocation;

import lombok.Getter;

@Getter
public class GrafoMatrixInput {
    private List<OptionLocation> optns;
    private String categoria;
}
