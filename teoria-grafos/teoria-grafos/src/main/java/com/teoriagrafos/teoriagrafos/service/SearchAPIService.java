package com.teoriagrafos.teoriagrafos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teoriagrafos.teoriagrafos.model.SearchAutoCompleteModels.OptionLocation;
import com.teoriagrafos.teoriagrafos.model.SearchRotasModels.Rotas;

import lombok.AllArgsConstructor;
import static utils.OpenRouteUtils.*;

@Service
@AllArgsConstructor
public class SearchAPIService {

    private static final String ROTA_URL = BASE_URL + "v2/directions/";

    private GetCoordinatesService getCoordinatesService;

    public Rotas searchAPIService(List<OptionLocation> options, String categoria) {
        Rotas rotas = null;   

        // Obtenha as coordenadas da cidade
        // double[] cityCoordinates = getCoordinatesService.getCityCoordinates("Tokyo, Japan");
        // double[] cityDestiny = getCoordinatesService.getCityCoordinates("Tokyo, Japan");

        // Verifique se as coordenadas foram obtidas com sucesso
        if (!options.get(0).getValue().isEmpty()) {
            // Parâmetros da solicitação
            String coordinates = options.get(0).getValue().get(0) + "," + options.get(0).getValue().get(1); // latitude,longitude
            String coordinatesDest = options.get(1).getValue().get(0) + "," + options.get(1).getValue().get(1);
            String profile = categoria;  // Perfil de roteamento (pode ser driving-car, etc.)

            // Construa a URL da solicitação
            String urlString = String.format("%s%s?api_key=%s&start=%s&end=%s",
                    ROTA_URL, profile, API_KEY, coordinates, coordinatesDest);

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
                    ObjectMapper objectMapper = new ObjectMapper();
                    rotas = objectMapper.readValue(response.toString(), Rotas.class);

                    // O conteúdo da resposta está em formato JSON
                    System.out.println(response.toString());
                    
                    
                } else {
                    System.out.println("Erro na solicitação: " + connection.getResponseMessage());
                }

                // Feche a conexão
                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Não foi possível obter as coordenadas da cidade.");
            throw new IllegalStateException("Não foi possível obter as coordenadas da cidade.");
        }
        return rotas;
    }
}
