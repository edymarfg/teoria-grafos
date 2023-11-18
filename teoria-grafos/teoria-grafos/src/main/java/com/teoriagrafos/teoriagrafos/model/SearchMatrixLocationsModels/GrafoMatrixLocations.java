package com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GrafoMatrixLocations {
    private String inicio;
    private List<PesoGrafoMatrix> fim;
    
}
