package com.teoriagrafos.teoriagrafos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teoriagrafos.teoriagrafos.model.GrafoModels.Grafo;
import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.OptionLocation;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.GrafoMatrixInput;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.GrafoMatrixLocations;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.SearchMatrix;
import com.teoriagrafos.teoriagrafos.model.SearchRotasModels.Rotas;
import com.teoriagrafos.teoriagrafos.model.SearchRotasModels.Segment;
import com.teoriagrafos.teoriagrafos.model.SearchRotasModels.Step;
import com.teoriagrafos.teoriagrafos.service.SearchAPIService;
import com.teoriagrafos.teoriagrafos.service.SearchLocalAutoCompleteService;
import com.teoriagrafos.teoriagrafos.service.SearchMatrixLocationsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/grafos")
@AllArgsConstructor
public class GrafosController {

    private SearchAPIService searchAPIService;
    private SearchLocalAutoCompleteService searchLocalAutoCompleteService;
    private SearchMatrixLocationsService searchMatrixLocationsService;

    @PostMapping("/rotas")
    public ArrayList<Step> list(@RequestBody @Validated GrafoMatrixInput input) {
        var result = searchAPIService.searchAPIService(input.getOptns(), input.getCategoria());
        if (result != null) {
            return result.getFeatures().get(0).getProperties().getSegments().get(0).getSteps();
        }
        return null;
    }

    @PostMapping("/autocomplete")
    public List<OptionLocation> autoCompleteLocations(@RequestBody String name) {
        return searchLocalAutoCompleteService.getOptionsLocations(name);
    }

    @PostMapping("/matrix")
    public List<GrafoMatrixLocations> matrixLocations(@RequestBody GrafoMatrixInput input) {
        return searchMatrixLocationsService.buildSearchMatrix(input.getOptns(), input.getCategoria());
    }
    
}
