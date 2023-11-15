package com.teoriagrafos.teoriagrafos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.OptionLocation;
import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.PropertiesAuto;
import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.SearchAutocomplete;

import static utils.OpenRouteUtils.*;

@Service
public class SearchLocalAutoCompleteService {

    private static final String AUTOCOMPLETE_URL = BASE_URL + "geocode/autocomplete";

    public List<OptionLocation> getOptionsLocations(String name) {
        var search = localAutoComplete(name);
        return getOptionsLocations(search);
    }

    private List<OptionLocation> getOptionsLocations(SearchAutocomplete searchAutocomplete) {
        if (searchAutocomplete == null) {
            return List.of();
        }
        return searchAutocomplete.getFeatures().stream().map(it -> 
            buildOptionLocation(it.getProperties(), it.getGeometry().getCoordinates())
        ).collect(Collectors.toList());
    }

    private OptionLocation buildOptionLocation(PropertiesAuto properties, ArrayList<Double> coordinates) {
        return OptionLocation.builder()
        .label(properties.getLabel())
        .value(coordinates)
        .build();
    }

    private SearchAutocomplete localAutoComplete(String name) {
        name = removerAcentos(name.toLowerCase().replace(" ", ""));
        SearchAutocomplete search = null;

        String urlString = String.format("%s?api_key=%s&text=%s",
                    AUTOCOMPLETE_URL, API_KEY, name);

            try {
                // Crie um objeto URL a partir da string da URL
                URL url = new URL(urlString);

                // Abra uma conexão HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Configurar método de solicitação e tempo limite
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                // Obtenha a resposta
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Leitura da resposta
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    // O conteúdo da resposta está em formato JSON
                    System.out.println(response.toString());
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    search = mapper.readValue(response.toString(), SearchAutocomplete.class);
                    
                    
                } else {
                    System.out.println("Erro na solicitação: " + connection.getResponseMessage());
                }

                // Feche a conexão
                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return search;
    }

    public static String removerAcentos(String str) {
    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
}
    
}
