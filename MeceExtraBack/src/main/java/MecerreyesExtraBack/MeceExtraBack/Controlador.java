package MecerreyesExtraBack.MeceExtraBack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
public class Controlador {
    private List<cp_national_data> CPNationalList = null;
    private DatosMsCode datosMsCode = null;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Set<String> allMSCODES = new HashSet<>();
    String filePath2 = "Ficheros/cp-national-datafile.json"; // Reemplaza con la ruta correcta
    private final String sacadosFilePath = "Ficheros/sacados.json";
    private final String pdfDirectoryPath = "Ficheros/PDFs/";


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
        try {
            CPNationalList = objectMapper.readValue(new File(filePath2), new TypeReference<List<cp_national_data>>() {});
            return new ResponseEntity<>(CPNationalList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Datos2/{id}")
    public ResponseEntity<cp_national_data> updateDatosCPN(@PathVariable String id, @RequestBody cp_national_data updatedData) {
        try {
            CPNationalList = objectMapper.readValue(new File(filePath2), new TypeReference<List<cp_national_data>>() {});

            Optional<cp_national_data> existingDataOpt = CPNationalList.stream().filter(data -> data.get_id().equals(id)).findFirst();
            if (existingDataOpt.isPresent()) {
                cp_national_data existingData = existingDataOpt.get();
                existingData.setMscode(updatedData.getMscode());
                existingData.setYear(updatedData.getYear());
                existingData.setEstCode(updatedData.getEstCode());
                existingData.setEstimate(updatedData.getEstimate());
                existingData.setSe(updatedData.getSe());
                existingData.setLowerCIB(updatedData.getLowerCIB());
                existingData.setUpperCIB(updatedData.getUpperCIB());
                existingData.setFlag(updatedData.getFlag());

                objectMapper.writeValue(new File(filePath2), CPNationalList);
                return new ResponseEntity<>(existingData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/Datos2/{id}")
    public ResponseEntity<Void> deleteDatosCPN(@PathVariable String id) {
        try {
            CPNationalList = objectMapper.readValue(new File(filePath2), new TypeReference<List<cp_national_data>>() {});

            boolean removed = CPNationalList.removeIf(data -> data.get_id().equals(id));
            if (removed) {
                objectMapper.writeValue(new File(filePath2), CPNationalList);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Nuevo endpoint para añadir un elemento a cp_national_data
    @PostMapping("/Datos2")
    public ResponseEntity<cp_national_data> addDatosCPN(@RequestBody cp_national_data newData) {
        try {
            CPNationalList = objectMapper.readValue(new File(filePath2), new TypeReference<List<cp_national_data>>() {});

            newData.set_id(UUID.randomUUID().toString());
            CPNationalList.add(newData);
            objectMapper.writeValue(new File(filePath2), CPNationalList);

            return new ResponseEntity<>(newData, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Nuevo endpoint para manejar el botón "Usar"
    @PostMapping("/usar/{id}")
    public ResponseEntity<Void> usarDato(@PathVariable String id) {
        try {
            CPNationalList = objectMapper.readValue(new File(filePath2), new TypeReference<List<cp_national_data>>() {});
            Optional<cp_national_data> dataOpt = CPNationalList.stream().filter(data -> data.get_id().equals(id)).findFirst();

            if (dataOpt.isPresent()) {
                cp_national_data data = dataOpt.get();

                // Añadir el dato al archivo sacados.json
                File sacadosFile = new File(sacadosFilePath);
                List<cp_national_data> sacadosList;

                if (sacadosFile.exists()) {
                    sacadosList = objectMapper.readValue(sacadosFile, new TypeReference<List<cp_national_data>>() {});
                } else {
                    sacadosList = new ArrayList<>();
                }

                sacadosList.add(data);
                objectMapper.writeValue(sacadosFile, sacadosList);

                // Crear el PDF
                File pdfDirectory = new File(pdfDirectoryPath);
                if (!pdfDirectory.exists()) {
                    pdfDirectory.mkdirs();
                }

                File pdfFile = new File(pdfDirectory, id + ".pdf");
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                document.open();
                document.add(new Paragraph("ID: " + data.get_id()));
                document.add(new Paragraph("MsCode: " + data.getMscode()));
                document.add(new Paragraph("Year: " + data.getYear()));
                document.add(new Paragraph("EstCode: " + data.getEstCode()));
                document.add(new Paragraph("Estimate: " + data.getEstimate()));
                document.add(new Paragraph("SE: " + data.getSe()));
                document.add(new Paragraph("LowerCIB: " + data.getLowerCIB()));
                document.add(new Paragraph("UpperCIB: " + data.getUpperCIB()));
                document.add(new Paragraph("Flag: " + data.getFlag()));
                document.close();

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

