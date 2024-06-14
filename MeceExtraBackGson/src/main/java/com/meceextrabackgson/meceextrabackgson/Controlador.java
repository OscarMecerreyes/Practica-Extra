package com.meceextrabackgson.meceextrabackgson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class Controlador {

    List<CPNational> CPNationalList = null;
    String file = "Ficheros/cp-national-datafile.json";
    String file2 = "Ficheros/MsCode_json.json";

    @GetMapping("/CP")
    public ResponseEntity<List<CPNational>> getAllCPNationalData() {
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CPNational>>(){}.getType();
            CPNationalList = gson.fromJson(reader, listType);
            return new ResponseEntity<>(CPNationalList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @GetMapping("/mscode/{mscode}")
    public ResponseEntity<List<MsCode>> getDataByMsCode(@PathVariable String mscode) {
        try (FileReader reader = new FileReader(file2)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, List<MsCode>>>(){}.getType();
            Map<String, List<MsCode>> msCodeMap = gson.fromJson(reader, mapType);
            List<MsCode> msCodeList = msCodeMap.get(mscode);
            if (msCodeList == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(msCodeList, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/CP/{id}")
    public ResponseEntity<CPNational> updateCPNationalData(@PathVariable String id, @RequestBody CPNational updatedData) {
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CPNational>>() {}.getType();
            List<CPNational> cpNationalList = gson.fromJson(reader, listType);

            Optional<CPNational> existingDataOpt = cpNationalList.stream()
                    .filter(data -> data.get_id().equals(id))
                    .findFirst();

            if (existingDataOpt.isPresent()) {
                CPNational existingData = existingDataOpt.get();
                existingData.setMscode(updatedData.getMscode());
                existingData.setYear(updatedData.getYear());
                existingData.setEstCode(updatedData.getEstCode());
                existingData.setEstimate(updatedData.getEstimate());
                existingData.setSe(updatedData.getSe());
                existingData.setLowerCIB(updatedData.getLowerCIB());
                existingData.setUpperCIB(updatedData.getUpperCIB());
                existingData.setFlag(updatedData.getFlag());

                try (FileWriter writer = new FileWriter(file)) {
                    gson.toJson(cpNationalList, writer);
                }

                return new ResponseEntity<>(existingData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/CP/{id}")
    public ResponseEntity<Void> deleteCPNationalData(@PathVariable String id) {
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CPNational>>() {}.getType();
            List<CPNational> cpNationalList = gson.fromJson(reader, listType);

            boolean removed = cpNationalList.removeIf(data -> data.get_id().equals(id));

            if (removed) {
                try (FileWriter writer = new FileWriter(file)) {
                    gson.toJson(cpNationalList, writer);
                }
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/CP")
    public ResponseEntity<CPNational> addCPNationalData(@RequestBody CPNational newData) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<CPNational>>() {}.getType();
            List<CPNational> cpNationalList = gson.fromJson(reader, listType);

            newData.set_id(UUID.randomUUID().toString());
            cpNationalList.add(newData);

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(cpNationalList, writer);
            }

            return new ResponseEntity<>(newData, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

