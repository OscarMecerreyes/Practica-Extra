package MecerreyesExtraBack.MeceExtraBack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class Controlador {

    private DatosMsCode datosMsCode = null;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Set<String> allMSCODES = new HashSet<>();

    // Endpoint para cargar todos los MSCODES
    @GetMapping("/allMsCodes")
    public ResponseEntity<Set<String>> loadAllMSCODES() {
        String filePath = "Ficheros/MsCode_json.json"; // Reemplaza con la ruta correcta

        try {
            // Lee el archivo JSON y lo deserializa en un objeto DatosMsCode
            datosMsCode = objectMapper.readValue(new File(filePath), DatosMsCode.class);

            // Almacenar los diferentes MSCODES en el conjunto global
            allMSCODES.addAll(datosMsCode.getData().keySet());

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(allMSCODES, HttpStatus.OK);
    }
    @GetMapping("/Datos1")
    // Endpoint para obtener todo el JSON
    public ResponseEntity<DatosMsCode> getAllData() {
        String filePath = "Ficheros/MsCode_json.json"; // Reemplaza con la ruta correcta

        try {
            // Lee el archivo JSON y lo deserializa en un objeto DatosMsCode si no se ha hecho ya
            if (datosMsCode == null) {
                datosMsCode = objectMapper.readValue(new File(filePath), DatosMsCode.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(datosMsCode, HttpStatus.OK);
    }
}