package org.vaadin.example;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final GreetService greetService;

    public MainView(@Autowired GreetService greetService) {
        this.greetService = greetService;

        // Crear pestañas
        Tab tabCpNational = new Tab("CP National Data");
        Tab tabMsCode = new Tab("MsCode Data");
        Tabs tabs = new Tabs(tabCpNational, tabMsCode);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        // Contenedores para cada pestaña
        VerticalLayout pageCpNational = setupCpNationalTab();
        VerticalLayout pageMsCode = setupMsCodeTab();

        // Añadir listeners para cambiar de pestaña
        tabs.addSelectedChangeListener(event -> {
            if (tabs.getSelectedTab().equals(tabCpNational)) {
                pageCpNational.setVisible(true);
                pageMsCode.setVisible(false);
            } else {
                pageCpNational.setVisible(false);
                pageMsCode.setVisible(true);
            }
        });

        // Mostrar solo la primera pestaña inicialmente
        pageCpNational.setVisible(true);
        pageMsCode.setVisible(false);

        add(tabs, pageCpNational, pageMsCode);
    }

    private VerticalLayout setupCpNationalTab() {
        VerticalLayout layout = new VerticalLayout();

        Grid<datosGenerales> grid = new Grid<>(datosGenerales.class);
        grid.setColumns("_id", "mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");

        try {
            List<datosGenerales> data = greetService.getCpNationalData();
            grid.setItems(data);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        layout.add(grid);
        return layout;
    }

    private VerticalLayout setupMsCodeTab() {
        VerticalLayout layout = new VerticalLayout();

        ComboBox<String> comboBox = new ComboBox<>("Select MsCode");
        Grid<MsCodeData> grid = new Grid<>(MsCodeData.class);
        grid.setColumns("_id", "mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");

        try {
            List<String> msCodes = greetService.getMsCodes();
            msCodes.sort(String::compareTo); // Ordenar alfabeticamente
            comboBox.setItems(msCodes);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        comboBox.addValueChangeListener(event -> {
            String selectedMsCode = event.getValue();
            if (selectedMsCode != null) {
                try {
                    List<MsCodeData> msCodeDataList = greetService.getDatosByMsCode(selectedMsCode);
                    grid.setItems(msCodeDataList);
                } catch (IOException | InterruptedException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        layout.add(comboBox, grid);
        return layout;
    }
}
