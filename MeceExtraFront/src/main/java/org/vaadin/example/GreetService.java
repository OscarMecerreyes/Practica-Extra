package org.vaadin.example;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class GreetService implements Serializable {

    private static final String api = "http://localhost:8080/%s"; // URL de la API
    private final HttpClient client = HttpClient.newHttpClient(); // HttpClient
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper

    public List<String> getMsCodes() throws IOException, InterruptedException, URISyntaxException {
        String resource = String.format(api, "allMsCodes");
        HttpRequest request = HttpRequest
                .newBuilder(new URI(resource))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Set<String> msCodes = objectMapper.readValue(response.body(), new TypeReference<Set<String>>() {});
        return msCodes.stream().sorted().collect(Collectors.toList());
    }

    public List<MsCodeData> getDatosByMsCode(String msCode) throws IOException, InterruptedException, URISyntaxException {
        String resource = String.format(api, "Datos1/"+msCode);
        System.out.println(resource);
        HttpRequest request = HttpRequest
                .newBuilder(new URI(resource))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<MsCodeData>>() {});
    }

    public List<datosGenerales> getCpNationalData() throws IOException, InterruptedException, URISyntaxException {
        String resource = String.format(api, "Datos2");
        HttpRequest request = HttpRequest
                .newBuilder(new URI(resource))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<datosGenerales>>() {});
    }
}
