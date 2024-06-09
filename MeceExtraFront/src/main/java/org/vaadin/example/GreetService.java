package org.vaadin.example;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class GreetService implements Serializable {

    private static final String api = "http://localhost:8080/%s"; // Se declara la url de la API
    private final HttpClient client = HttpClient.newHttpClient(); // Se declara un objeto HttpClient

    public String getDatos() {
        try {
            String resource = String.format(api, "Datos2");
            HttpRequest request = HttpRequest
                    .newBuilder(new URI(resource))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
