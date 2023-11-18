package com.teoriagrafos.teoriagrafos.controller;

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

    @PostMapping
    public Rotas list(@RequestBody @Validated List<OptionLocation> options) {
        return searchAPIService.searchAPIService(options);
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
