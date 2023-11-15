package com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionLocation {
    private String label;
    private ArrayList<Double> value;
}
