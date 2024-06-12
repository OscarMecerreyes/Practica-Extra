package com.meceextrabackgson.meceextrabackgson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
}

