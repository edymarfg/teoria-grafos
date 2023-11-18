package com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PesoGrafoMatrix {
    private String fim;
    private Double duration;
    private Double distance;
    private GrafoMatrixLocations matrix;
}
