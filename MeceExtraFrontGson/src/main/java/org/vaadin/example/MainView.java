package org.vaadin.example;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import java.util.UUID;



@Route("")
public class MainView extends VerticalLayout {
    private final GreetService greetService;
    private final Grid<CPNational> grid;
    private List<CPNational> dataListGeneral;

    @Autowired
    public MainView(GreetService greetService) {
        this.greetService = greetService;
        this.grid = new Grid<>(CPNational.class);

        setSizeFull();
        configureGrid();
        add(grid, createAddButton());

        loadData(); // Carga los datos cuando se crea la vista
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("_id", "mscode", "year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");

        // Ajustar el tamaño de las columnas
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        // Añadir columna para los botones de editar y eliminar
        grid.addComponentColumn(item -> {
            Button editButton = new Button("Edit", clickEvent -> openEditDialog(item));
            Button deleteButton = new Button("Delete", clickEvent -> deleteItem(item));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            HorizontalLayout layout = new HorizontalLayout(editButton, deleteButton);
            return layout;
        }).setHeader("Actions");
    }

    private void loadData() {
        try {
            dataListGeneral = greetService.getCPNationalData();
            grid.setItems(dataListGeneral);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Notification.show("Failed to load data");
            e.printStackTrace();
        }
    }

    private Button createAddButton() {
        return new Button("Add", clickEvent -> openAddDialog());
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
        mscodeField.setValue(item.getMscode() != null ? item.getMscode() : "");

        TextField yearField = new TextField("Year");
        yearField.setValue(item.getYear() != null ? item.getYear() : "");

        TextField estCodeField = new TextField("EstCode");
        estCodeField.setValue(item.getEstCode() != null ? item.getEstCode() : "");

        TextField estimateField = new TextField("Estimate");
        estimateField.setValue(String.valueOf(item.getEstimate()));

        TextField seField = new TextField("SE");
        seField.setValue(String.valueOf(item.getSe()));

        TextField lowerCIBField = new TextField("LowerCIB");
        lowerCIBField.setValue(String.valueOf(item.getLowerCIB()));

        TextField upperCIBField = new TextField("UpperCIB");
        upperCIBField.setValue(String.valueOf(item.getUpperCIB()));

        TextField flagField = new TextField("Flag");
        flagField.setValue(item.getFlag() != null ? item.getFlag() : "");

        formLayout.add( mscodeField, yearField, estCodeField, estimateField, seField, lowerCIBField, upperCIBField, flagField);

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
                    dataListGeneral.add(item);
                } else {
                    greetService.updateCPNationalData(item.get_id(), item);
                }
                loadData(); // Recarga los datos después de la actualización
                dialog.close();
                Notification.show("Data saved successfully");
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
            dataListGeneral.remove(item);
            grid.setItems(dataListGeneral);
            Notification.show("Data deleted successfully");
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Notification.show("Failed to delete data");
            e.printStackTrace();
        }
    }
}