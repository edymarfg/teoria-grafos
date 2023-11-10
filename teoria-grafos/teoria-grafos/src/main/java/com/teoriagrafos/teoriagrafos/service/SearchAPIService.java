package com.teoriagrafos.teoriagrafos.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class SearchAPIService {
    public void searchAPIService() {
        // Substitua 'SUA_CHAVE_DE_API' pela chave de API fornecida pelo OpenRouteService
        String apiKey = "5b3ce3597851110001cf6248149b01337fa5430f80e83e5889a3aa44";

        // Obtenha as coordenadas da cidade
        double[] cityCoordinates = getCityCoordinates("porto+alegre");

        // Verifique se as coordenadas foram obtidas com sucesso
        if (cityCoordinates != null) {
            // URL base da API
            String baseUrl = "https://api.openrouteservice.org/v2/directions/";

            // Parâmetros da solicitação
            String coordinates = cityCoordinates[1] + "," + cityCoordinates[0]; // latitude,longitude
            String profile = "walking";  // Perfil de roteamento (pode ser walking, cycling, etc.)

            // Construa a URL da solicitação
            String urlString = String.format("%s%s?api_key=%s&coordinates=%s",
                    baseUrl, profile, apiKey, coordinates);

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
        }
    }

    private double[] getCityCoordinates(String cityName) {
        try {
            // Substitua 'YOUR_NOMINATIM_API_URL' pela URL do serviço Nominatim
            String nominatimUrl = "https://nominatim.openstreetmap.org/search";

            // Construa a URL de consulta para obter as coordenadas da cidade
            String queryUrl = String.format("%s?q=%s&format=json", nominatimUrl, cityName);

            // Crie um objeto URL a partir da string da URL
            URL url = new URL(queryUrl);

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

                // Analise a resposta JSON para obter as coordenadas da cidade
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                // Use uma biblioteca JSON (por exemplo, Gson) para analisar a resposta
                // Aqui, estamos usando uma abordagem simples com Scanner
                try (Scanner scanner = new Scanner(response.toString())) {
                    if (scanner.hasNext()) {
                        // Leitura da primeira linha da resposta JSON
                        String firstLine = scanner.nextLine();

                        // Extração das coordenadas de latitude e longitude
                        int latIndex = firstLine.indexOf("\"lat\":");
                        int lonIndex = firstLine.indexOf("\"lon\":");
                        int classIndex = firstLine.indexOf("\"class\":");

                        if (latIndex != -1 && lonIndex != -1) {
                            double latitude = Double.parseDouble(firstLine.substring(latIndex + 7, lonIndex - 2));
                            double longitude = Double.parseDouble(firstLine.substring(lonIndex + 7, classIndex - 2));
                            return new double[]{latitude, longitude};
                        }
                    }
                }
            }

            // Feche a conexão
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
