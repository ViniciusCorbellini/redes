package com.mycompany.trabalho_redes.parte3.server;

import com.mycompany.trabalho_redes.parte3.common.JsonUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius Corbellini
 */
public class DeviceStatus {

    private static final Logger LOG = Logger.getLogger(DeviceStatus.class.getName());

    private final String PATH = "src/main/java/com/mycompany/trabalho_redes/parte3/config/config.json";
    private Map<String, Object> status;

    public DeviceStatus() {
        LOG.info("Initializing DeviceStatus");
        this.status = new LinkedHashMap<>();
        status = JsonUtil.getConfigFrom(PATH);
    }

    public Set<String> list() {
        LOG.info("Returning device list");
        return status.keySet();
    }

    public Object get(String place) {
        LOG.info("Returning place's value");
        return status.get(place);
    }

    public boolean set(String locate, Object value) {
        LOG.info("Setting actuator's value");
        if (this.get(locate) == null) {
            LOG.info("Location doesn't exist");
            return false;
        }

        if (!locate.startsWith("actuator")) {
            LOG.info("Location wasn't an actuator, returning false");
            return false;
        }

        LOG.info("Putting new value and returning true");
        status.put(locate, value);
        return true;
    }

    public Map<String, Object> getAll() {
        return status;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" + "PATH=" + PATH + "\n, status=" + status + '}';
    }
}
