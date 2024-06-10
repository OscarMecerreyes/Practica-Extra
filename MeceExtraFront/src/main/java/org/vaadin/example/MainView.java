package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final GreetService greetService;
    private List<datosGenerales> dataListGeneral;

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

        Column<datosGenerales> actionsColumn = grid.addComponentColumn(data -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> openEditDialog(data, grid));
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> deleteData(data, grid));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
            return actions;
        }).setHeader("Actions");

        try {
            dataListGeneral = greetService.getCpNationalData();
            grid.setItems(dataListGeneral);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

        layout.add(grid);
        return layout;
    }

    private void openEditDialog(datosGenerales data, Grid<datosGenerales> grid) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        TextField mscodeField = new TextField("mscode", data.getMscode());
        TextField yearField = new TextField("year", data.getYear());
        TextField estCodeField = new TextField("estCode", data.getEstCode());
        TextField estimateField = new TextField("estimate", String.valueOf(data.getEstimate()));
        TextField seField = new TextField("se", String.valueOf(data.getSe()));
        TextField lowerCIBField = new TextField("lowerCIB", String.valueOf(data.getLowerCIB()));
        TextField upperCIBField = new TextField("upperCIB", String.valueOf(data.getUpperCIB()));
        TextField flagField = new TextField("flag", data.getFlag());

        Button saveButton = new Button("Save", event -> {
            data.setMscode(mscodeField.getValue());
            data.setYear(yearField.getValue());
            data.setEstCode(estCodeField.getValue());
            data.setEstimate(Float.parseFloat(estimateField.getValue()));
            data.setSe(Float.parseFloat(seField.getValue()));
            data.setLowerCIB(Float.parseFloat(lowerCIBField.getValue()));
            data.setUpperCIB(Float.parseFloat(upperCIBField.getValue()));
            data.setFlag(flagField.getValue());

            try {
                greetService.updateCpNationalData(data);
                grid.getDataProvider().refreshItem(data);
                dialog.close();
            } catch (IOException | InterruptedException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(mscodeField, yearField, estCodeField, estimateField, seField, lowerCIBField, upperCIBField, flagField, saveButton);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void deleteData(datosGenerales data, Grid<datosGenerales> grid) {
        try {
            greetService.deleteCpNationalData(data.get_id());
            dataListGeneral.remove(data);
            grid.setItems(dataListGeneral);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
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
