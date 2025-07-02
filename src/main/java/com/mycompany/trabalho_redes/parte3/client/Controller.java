package com.mycompany.trabalho_redes.parte3.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius Corbellini
 */
public class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class.getName());
    private final UDPClient client;

    public Controller(UDPClient client) {
        LOG.info("Initializing Controller");
        this.client = client;
    }

    public Map<String, Object> list() throws UnknownHostException, IOException {
        LOG.info("List request");
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("cmd", "list_req");

        LOG.info("Mapping request to client and returning its response");
        return client.send(request);
    }

    public Map<String, Object> get(String place) throws UnknownHostException, IOException {
        LOG.info(String.format("Get request for %s", place));
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("cmd", "get_req");
        request.put("place", place);

        LOG.info("Mapping request to client and returning its response");
        return client.send(request);
    }

    public Map<String, Object> set(String locate, String value) throws UnknownHostException, IOException, Exception {
        LOG.info(String.format("Set request for %s, value: %s", locate, value));
        if (!locate.startsWith("actuator")) {
            LOG.info("Invalid device");
            throw new Exception("O dispositivo não é um actuador");
        }

        Map<String, Object> request = new LinkedHashMap<>();
        request.put("cmd", "set_req");
        request.put("locate", locate);
        request.put("value", value);

        LOG.info("Mapping request to client and returning its response");
        return client.send(request);
    }
}
