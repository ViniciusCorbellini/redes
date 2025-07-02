package com.mycompany.trabalho_redes.parte3.server;

import com.mycompany.trabalho_redes.parte3.common.Constants;
import com.mycompany.trabalho_redes.parte3.common.JsonUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinicius Corbellini
 */
public class UDPServer {

    private static final Logger LOG = Logger.getLogger(UDPServer.class.getName());

    private final int port;
    private final int BUFFER_SIZE;
    private final DeviceStatus dstatus;

    public UDPServer(int port) {
        LOG.info("Initializing UDP Server");
        this.BUFFER_SIZE = Constants.BUFFER_SIZE;
        this.port = port;
        this.dstatus = new DeviceStatus();
    }

    public void start() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            byte[] buffer = new byte[BUFFER_SIZE];
            
            InetAddress localAddress = InetAddress.getLocalHost();
            LOG.info(String.format("Starting udp server on ip:port -> %s:%s", localAddress.getHostAddress(), port));

            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, BUFFER_SIZE);
                LOG.info("Waiting for requisitions");
                socket.receive(dp);

                LOG.info("Converting request: packet(JSON) -> String -> Map<String, Object>");
                String json = new String(dp.getData(), 0, dp.getLength()); //String(byte[] bytes, int offset, int length)
                Map<String, Object> request = JsonUtil.fromJson(json);

                RequestHandler rh = new RequestHandler(dstatus);
                LOG.info("Handling request");
                Map<String, Object> response = rh.handleRequest(request);

                LOG.info("Converting reponse: Map<String, Object> -> String -> byte[] -> UDP packet");
                byte[] respBytes = JsonUtil.toJson(response).getBytes();
                DatagramPacket resp = new DatagramPacket(respBytes, respBytes.length, dp.getAddress(), dp.getPort());
                
                LOG.info(String.format("Sending response to ip:port -> %s:%s", dp.getAddress(), dp.getPort()));
                socket.send(resp);
            }
        } catch (SocketException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
