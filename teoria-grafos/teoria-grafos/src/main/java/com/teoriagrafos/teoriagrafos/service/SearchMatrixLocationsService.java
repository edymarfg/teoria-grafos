package com.teoriagrafos.teoriagrafos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teoriagrafos.teoriagrafos.model.GrafoModels.Aresta;
import com.teoriagrafos.teoriagrafos.model.GrafoModels.Grafo;
import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.OptionLocation;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.GrafoMatrixLocations;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.PesoGrafoMatrix;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.SearchMatrix;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

@Service
public class SearchMatrixLocationsService {

    @Autowired
    private ObjectMapper objectMapper;

    private static Grafo<String> grafo;

    public List<GrafoMatrixLocations> buildSearchMatrix(List<OptionLocation> optns, String categoria) {
        var coordinates = optns.stream().map(OptionLocation::getValue).collect(Collectors.toList());
        var vertices = optns.stream().map(OptionLocation::getLabel).collect(Collectors.toList());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var matrix = SearchMatrix.builder()
        .durations(searchMatrix(coordinates, null, categoria).getDurations())
        .distances(searchMatrix(coordinates, ",\"metrics\":[\"distance\"],\"units\":\"km\"", categoria).getDistances())
        .build();
        grafo = mountGrafo(matrix, vertices);
        return buildGrafoMatrixLocation();        
    }

    private SearchMatrix searchMatrix(List<ArrayList<Double>> coordinates, String method, String categoria) {        
            StringBuilder strBuild = new StringBuilder("{\"locations\":");
            buildCoordinatesToUrl(strBuild, coordinates);
            // strBuild.append("[[9.70093,48.477473],[9.207916,49.153868]," +
            // "[37.573242,55.801281],[115.663757,38.106467]]");
            if (method != null) {
                strBuild.append(method);
            }
            strBuild.append("}");

            Client client = ClientBuilder.newClient();
            Entity<String> payload = Entity.json(strBuild.toString());
            Response response = client.target("https://api.openrouteservice.org/v2/matrix/" + categoria)
            .request()
            .header("Authorization", "5b3ce3597851110001cf6248149b01337fa5430f80e83e5889a3aa44")
            .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
            .header("Content-Type", "application/json; charset=utf-8")
            .post(payload);

            System.out.println("status: " + response.getStatus());
            System.out.println("headers: " + response.getHeaders());
           // System.out.println("body:" + response.readEntity(String.class));
            var body = response.readEntity(String.class);
            SearchMatrix search = null;
            try {
            search = objectMapper.readValue(body.toString(), SearchMatrix.class);
            } catch (JacksonException e) {
                e.printStackTrace();
            }
            return search;
    }

    private List<GrafoMatrixLocations> buildGrafoMatrixLocation() {
        List<GrafoMatrixLocations> grafoMatrix = new ArrayList<>();
        grafo.getVertices().forEach(it -> {
                grafoMatrix.add(buildGrafoMatrix(it.getArestasSaida()));        
        });
        return grafoMatrix;
    }

    private GrafoMatrixLocations buildGrafoMatrix(ArrayList<Aresta<String>> arestas) {
        var inicio = arestas.get(0).getInicio().getDado();
        return GrafoMatrixLocations.builder()
                    .inicio(inicio)
                    .fim(buildFim(inicio, arestas))
                .build();
    }

    private List<PesoGrafoMatrix> buildFim(String inicio, ArrayList<Aresta<String>> arestas) {
       return arestas.stream().map(it -> buildPesoGrafoMatrix(it))
       .filter(it -> !it.getFim().equals(inicio))
       .collect(Collectors.toList());
    }

    private PesoGrafoMatrix buildPesoGrafoMatrix(Aresta<String> aresta) {
        return PesoGrafoMatrix.builder()
                    .fim(aresta.getFim().getDado())
                    .duration(aresta.getPeso().get(0))
                    .distance(aresta.getPeso().get(1))
                .build();
    }

    private Grafo<String> mountGrafo(SearchMatrix matrix, List<String> vertices) {
        Grafo<String> grafo = new Grafo<String>();
        vertices.forEach(v -> grafo.adicionarVertice(v));
        for (int i = 0; i < matrix.distances.size(); i++) {
            for (int j = 0; j < matrix.getDistances().get(i).size(); j++) {
                ArrayList<Double> peso = new ArrayList<>();
                peso.addAll(List.of(matrix.getDurations().get(i).get(j), matrix.getDistances().get(i).get(j)));
                grafo.adicionarAresta(peso, vertices.get(i), vertices.get(j));
            }           
        }
        return grafo;
    }

    private void buildCoordinatesToUrl(StringBuilder str, List<ArrayList<Double>> coord) {
        str.append("[");
       coord.forEach(c -> {
        str.append("[");
        str.append(c.get(0));
        str.append(",");
        str.append(c.get(1));
        str.append("]");
        if (coord.indexOf(c) < coord.size() - 1) {
            str.append(",");
        }        
       });
       str.append("]");
    }
    
}
