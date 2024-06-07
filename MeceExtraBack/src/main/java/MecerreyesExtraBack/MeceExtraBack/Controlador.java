package MecerreyesExtraBack.MeceExtraBack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class Controlador {
    private List<cp_national_data> CPNationalList = null;
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
    @GetMapping("/Datos1/{mscode}")
    public ResponseEntity<List<MsCode>> getDataByMsCode(@PathVariable String mscode) {
        String filePath = "Ficheros/MsCode_json.json"; // Reemplaza con la ruta correcta

        try {
            // Lee el archivo JSON y lo deserializa en un objeto DatosMsCode si no se ha hecho ya
            if (datosMsCode == null) {
                datosMsCode = objectMapper.readValue(new File(filePath), DatosMsCode.class);
            }

            // Obtener la lista de objetos asociada al MsCode indicado
            List<MsCode> msCodeList = datosMsCode.getData().get(mscode);

            if (msCodeList == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(msCodeList, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Endpoint para obtener datos de cp_national_data
    @GetMapping("/Datos2")
    public ResponseEntity<List<cp_national_data>> getDatosCPN() {
        String filePath = "Ficheros/cp-national-datafile.json"; // Reemplaza con la ruta correcta

        try {
            // Lee el archivo JSON y lo deserializa en una lista de objetos cp_national_data
            if (CPNationalList == null) {
                CPNationalList = objectMapper.readValue(new File(filePath), new TypeReference<List<cp_national_data>>() {});
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(CPNationalList, HttpStatus.OK);
    }
}