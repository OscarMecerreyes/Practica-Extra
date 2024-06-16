package org.vaadin.example;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.UUID;



@Route("")
public class MainView extends VerticalLayout {
    private final GreetService greetService;
    private final Grid<CPNational> cpNationalGrid;
    private final Grid<MsCode> msCodeGrid;
    private List<CPNational> dataListGeneral;

    @Autowired
    public MainView(GreetService greetService) {
        this.greetService = greetService;
        this.cpNationalGrid = new Grid<>(CPNational.class);
        this.msCodeGrid = new Grid<>(MsCode.class);

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

        configureCpNationalGrid();
        loadData(); // Carga los datos cuando se crea la vista

        Button addButton = new Button("Add New", e -> openAddDialog());
        layout.add(cpNationalGrid, addButton);
        return layout;
    }

    private void configureCpNationalGrid() {
        cpNationalGrid.setColumns("_id", "mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");

        // Ajustar el tamaño de las columnas
        cpNationalGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        // Añadir columna para los botones de editar, eliminar y usar
        cpNationalGrid.addComponentColumn(item -> {
            Button editButton = new Button("Edit", clickEvent -> openEditDialog(item));
            Button deleteButton = new Button("Delete", clickEvent -> deleteItem(item));
            Button usarButton = new Button("Usar", clickEvent -> usarItem(item));
            HorizontalLayout layout = new HorizontalLayout(editButton, deleteButton, usarButton);
            return layout;
        }).setHeader("Actions");
    }

    private void loadData() {
        try {
            dataListGeneral = greetService.getCPNationalData();
            cpNationalGrid.setItems(dataListGeneral);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Notification.show("Failed to load data");
            e.printStackTrace();
        }
    }

    private void openEditDialog(CPNational item) {
        Dialog dialog = createEditDialog(item, "Edit Item", false);
        dialog.open();
    }

    private void openAddDialog() {
        Dialog dialog = createEditDialog(new CPNational(), "Add Item", true);
        dialog.open();
    }

    private Dialog createEditDialog(CPNational item, String title, boolean isNew) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("500px");

        FormLayout formLayout = new FormLayout();
        TextField mscodeField = new TextField("MsCode");
        mscodeField.setValue(item.getMscode() == null ? "" : item.getMscode());

        TextField yearField = new TextField("Year");
        yearField.setValue(item.getYear() == null ? "" : item.getYear());

        TextField estCodeField = new TextField("EstCode");
        estCodeField.setValue(item.getEstCode() == null ? "" : item.getEstCode());

        TextField estimateField = new TextField("Estimate");
        estimateField.setValue(item.getEstimate() == 0 ? "" : String.valueOf(item.getEstimate()));

        TextField seField = new TextField("SE");
        seField.setValue(item.getSe() == 0 ? "" : String.valueOf(item.getSe()));

        TextField lowerCIBField = new TextField("LowerCIB");
        lowerCIBField.setValue(item.getLowerCIB() == 0 ? "" : String.valueOf(item.getLowerCIB()));

        TextField upperCIBField = new TextField("UpperCIB");
        upperCIBField.setValue(item.getUpperCIB() == 0 ? "" : String.valueOf(item.getUpperCIB()));

        TextField flagField = new TextField("Flag");
        flagField.setValue(item.getFlag() == null ? "" : item.getFlag());

        formLayout.add(mscodeField, yearField, estCodeField, estimateField, seField, lowerCIBField, upperCIBField, flagField);

        Button saveButton = new Button("Save", e -> {
            item.setMscode(mscodeField.getValue());
            item.setYear(yearField.getValue());
            item.setEstCode(estCodeField.getValue());
            item.setEstimate(Float.parseFloat(estimateField.getValue()));
            item.setSe(Float.parseFloat(seField.getValue()));
            item.setLowerCIB(Float.parseFloat(lowerCIBField.getValue()));
            item.setUpperCIB(Float.parseFloat(upperCIBField.getValue()));
            item.setFlag(flagField.getValue());

            try {
                if (isNew) {
                    item.set_id(UUID.randomUUID().toString());
                    greetService.addCPNationalData(item);
                    dataListGeneral.add(item); // Añade el nuevo dato a la lista
                } else {
                    greetService.updateCPNationalData(item.get_id(), item);
                }
                cpNationalGrid.getDataProvider().refreshAll(); // Recarga los datos después de la actualización
                dialog.close();
            } catch (IOException | InterruptedException | URISyntaxException ex) {
                Notification.show("Failed to save data");
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.add(formLayout, new HorizontalLayout(saveButton, cancelButton));
        return dialog;
    }

    private void deleteItem(CPNational item) {
        try {
            greetService.deleteCPNationalData(item.get_id());
            dataListGeneral.remove(item); // Elimina el dato de la lista
            cpNationalGrid.getDataProvider().refreshAll(); // Recarga los datos después de la eliminación
            Notification.show("Data deleted successfully");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Notification.show("Failed to delete data");
            e.printStackTrace();
        }
    }

    private void usarItem(CPNational item) {
        try {
            greetService.usarCPNational(item.get_id());
            Notification.show("Data used successfully");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Notification.show("Failed to use data");
            e.printStackTrace();
        }
    }

    private VerticalLayout setupMsCodeTab() {
        VerticalLayout layout = new VerticalLayout();

        ComboBox<String> comboBox = new ComboBox<>("Select MsCode");
        msCodeGrid.setColumns("_id", "mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");

        try {
            List<String> msCodes = greetService.getAllMsCodes();
            comboBox.setItems(msCodes);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Notification.show("Failed to load MS Codes");
            e.printStackTrace();
        }

        comboBox.addValueChangeListener(event -> {
            String selectedMsCode = event.getValue();
            if (selectedMsCode != null) {
                try {
                    List<MsCode> msCodeData = greetService.getDatosByMsCode(selectedMsCode);
                    msCodeGrid.setItems(msCodeData);
                } catch (IOException | InterruptedException | URISyntaxException e) {
                    Notification.show("Failed to load data for selected MS Code");
                    e.printStackTrace();
                }
            }
        });

        layout.add(comboBox, msCodeGrid);
        return layout;
    }
}