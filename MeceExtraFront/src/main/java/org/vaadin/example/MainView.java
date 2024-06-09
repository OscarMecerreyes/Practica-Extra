package org.vaadin.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final GreetService greetService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<datosGenerales> dataListGeneral;

    public MainView(@Autowired GreetService greetService) {
        this.greetService = greetService;

        // Configuración del Grid
        Grid<datosGenerales> grid = new Grid<>(datosGenerales.class);
        grid.setColumns("_id", "mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");

        // Cargar los datos iniciales
        loadInitialData();

        // Configurar los datos del Grid, manejar caso donde dataListGeneral es null
        if (dataListGeneral == null) {
            dataListGeneral = new ArrayList<>();
        }
        grid.setItems(dataListGeneral);

        // Añadir el Grid al layout
        add(grid);
    }

    private void loadInitialData() {
        try {
            String response = greetService.getDatos();
            if (response != null && !response.isEmpty()) {
                System.out.println("Response: " + response);
                dataListGeneral = objectMapper.readValue(response, new TypeReference<List<datosGenerales>>() {});
                System.out.println("Data List: " + dataListGeneral);
            } else {
                dataListGeneral = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            dataListGeneral = new ArrayList<>();
        }
    }
}
