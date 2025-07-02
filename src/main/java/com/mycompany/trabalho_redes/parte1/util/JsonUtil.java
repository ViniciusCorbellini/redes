package com.mycompany.trabalho_redes.parte1.util;

import com.mycompany.trabalho_redes.parte1.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

/**
 * Classe para a manipulacap de dados no formato JSON
 * 
 * @author manoCorbas
 */
public class JsonUtil {

    static ObjectMapper om = new ObjectMapper();
    
    /**
     * Transforma um objeto Mensagem em uma String no formato JSON
     * 
     * @param message mensagem a ser transformada
     * 
     * @return string da mensagem no formato JSON
     * 
     * @throws JsonProcessingException
     */
    public static String JsonStringFormat(Message message) throws JsonProcessingException {
        //Ensina o OjectMapper a lidar com os modulos 
        //de LocalDate e LocalDateTime
        om.registerModule(new JavaTimeModule());
        return om.writerWithDefaultPrettyPrinter().writeValueAsString(message);
    }
    
    /**
     * Com base em um determinado pacote (que deve seguir estritamente
     * o modelo JSON), retorna um objeto Mensagem 
     * 
     * Obtem os dados do pacote, remove o lixo do pacote (caracteres nulos),
     * e decodifica os dadsos pacote em UTF-8
     * 
     * @param p pacote recebido
     * 
     * @return Mensagem contida no pacote
     * 
     * @throws JsonProcessingException
     */
    public static Message getMessageFromPacket(DatagramPacket p) throws JsonProcessingException{
        //Garante que a mensagem:
        //Nao possua bytes de lixo
        //Seja decodificada em UTF-8
        String data = new String(p.getData(), 0, p.getLength(), StandardCharsets.UTF_8);
        
        om.registerModule(new JavaTimeModule());
        
        return om.readValue(data, Message.class);
    }
}
