package com.mycompany.trabalho_redes.parte3.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 * @author Vinicius Corbellini
 */
public class JsonUtil {
    private static final ObjectMapper om = new ObjectMapper();

    public static String toJson(Map<String, Object> map) {
        try {
            return om.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter para JSON", e);
        }
    }
    
    public static Map<String, Object> fromJson(String json) {
        try {
            return om.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter de JSON", e);
        }
    }
    
    public static Map<String, Object> getConfigFrom(String filePath){
        try {
            InputStream inputStream = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(inputStream);
            return om.readValue(isr, Map.class);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Erro ao converter de JSON", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao converter de JSON", ex);
        } 
    }
}
