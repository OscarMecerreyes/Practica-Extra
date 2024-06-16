package org.vaadin.example;

import java.io.Serializable;

import org.springframework.stereotype.Service;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

@Service
public class GreetService  {
    private static final String api = "http://localhost:8082/%s"; // URL de la API
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<CPNational> getCPNationalData() throws IOException, InterruptedException, URISyntaxException {
        String resource = String.format(api, "CP");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(resource))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type listType = new TypeToken<List<CPNational>>() {
        }.getType();
        return gson.fromJson(response.body(), listType);
    }

    public void updateCPNationalData(String id, CPNational updatedData) throws IOException, InterruptedException, URISyntaxException {
        String url = String.format(api, "CP/" + id);
        String json = gson.toJson(updatedData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteCPNationalData(String id) throws IOException, InterruptedException, URISyntaxException {
        String url = String.format(api, "CP/" + id);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .DELETE()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void addCPNationalData(CPNational newData) throws IOException, InterruptedException, URISyntaxException {
        String url = String.format(api, "CP");
        String json = gson.toJson(newData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public List<String> getAllMsCodes() throws IOException, InterruptedException, URISyntaxException {
        String resource = String.format(api, "mscodes");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(resource))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type setType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(response.body(), setType);
    }

    public List<MsCode> getDatosByMsCode(String mscode) throws IOException, InterruptedException, URISyntaxException {
        String resource = String.format(api, "mscode/" + mscode);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(resource))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type listType = new TypeToken<List<MsCode>>() {}.getType();
        return gson.fromJson(response.body(), listType);
    }
    public void usarCPNational(String id) throws IOException, InterruptedException, URISyntaxException {
        String url = String.format(api, "CP/usar/" + id);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}