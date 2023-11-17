package com.teoriagrafos.teoriagrafos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teoriagrafos.teoriagrafos.model.SearchMatrixLocationsModels.SearchMatrix;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

@Service
public class SearchMatrixLocationsService {

    @Autowired
    private ObjectMapper objectMapper;

    public SearchMatrix buildSearchMatrix() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return SearchMatrix.builder()
        .durations(searchMatrix(null).getDurations())
        .distances(searchMatrix(",\"metrics\":[\"distance\"]").getDistances())
        .build();
    }

    private SearchMatrix searchMatrix(String method) {        
            StringBuilder strBuild = new StringBuilder("{\"locations\":");
            strBuild.append("[[9.70093,48.477473],[9.207916,49.153868]," +
            "[37.573242,55.801281],[115.663757,38.106467]]");
            if (method != null) {
                strBuild.append(method);
            }
            strBuild.append("}");

            Client client = ClientBuilder.newClient();
            Entity<String> payload = Entity.json(strBuild.toString());
            Response response = client.target("https://api.openrouteservice.org/v2/matrix/driving-car")
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
    
}
