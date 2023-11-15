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
public class GetCoordinatesService {

    public double[] getCityCoordinates(String cityName) {
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
