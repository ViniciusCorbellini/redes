package com.mycompany.trabalho_redes.parte3.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius Corbellini
 */
public class RequestHandler {

    private static final Logger LOG = Logger.getLogger(RequestHandler.class.getName());

    private DeviceStatus dstatus;

    public RequestHandler(DeviceStatus dstatus) {
        LOG.info("Initializing RequestHandler");
        this.dstatus = dstatus;
    }

    public Map<String, Object> handleRequest(Map<String, Object> request) {
        LOG.info("Handling request");
        LOG.info("Instantiating response object");
        Map<String, Object> response = new LinkedHashMap<>();

        //Pega a chave "cmd" do map de <String, Object> e faz um cast pra String
        String cmd = (String) request.get("cmd");
        LOG.info(String.format("Request: %s", cmd));
        switch (cmd) {
            case "list_req" -> {
                listRequest(response);
            }
            case "get_req" -> {
                getRequest(response, request);
            }
            case "set_req" -> {
                setRequest(response, request);
            }
            default ->
                throw new AssertionError("Erro no handler");
        }
        LOG.info("Returning response");
        return response;
    }

    private void listRequest(Map<String, Object> response) {
        LOG.info("Handling list_req");
        response.put("cmd", "list_resp");
        response.put("place", dstatus.list());
    }

    private void getRequest(Map<String, Object> response, Map<String, Object> request) {
        LOG.info("Handling get_req");
        String place = (String) request.get("place");

        if ("all".equals(place)) {
            getAll(response);
            return;
        }

        response.put("cmd", "get_resp");
        response.put("place", place);
        response.put("value", dstatus.get(place));
    }
    
    private void setRequest(Map<String, Object> response, Map<String, Object> request) {
        LOG.info("Handling set_req");
        String locate = (String) request.get("locate");
        Object value = request.get("value");

        boolean success = dstatus.set(locate, value);
        
        response.put("cmd", "set_resp");
        response.put("locate", locate);
        
        if(success){
            LOG.info(String.format("Locate: %s // new value: %s", locate, value));
            response.put("value", value);
        } else{
            LOG.info("Dispositivo invalido");
            response.put("value", "Dispositivo inválido, nenhuma alteração foi feita");
        }
    }

    private void getAll(Map<String, Object> response) {
        LOG.info("Handling get all");
        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : dstatus.getAll().entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        response.put("place", keys);
        response.put("value", values);
    }
}
