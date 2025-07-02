package com.mycompany.trabalho_redes.parte2.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author Vinicius Corbellini
 */
public class FileUtils {
    public static String fileToBase64(File file) throws IOException {
        //Encoda um arquivo em base64
        return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
    }

    public static void saveBase64ToFile(String base64, File file) throws IOException {
        //Decoda o base64 em um array de bytes
        byte[] data = Base64.getDecoder().decode(base64);
        
        //Escreve os bytes decodados do Base64 no arquivo
        //passando como parametro o filepath do 'file'
        Files.write(file.toPath(), data);
    }

    public static String calculateSHA256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(Files.readAllBytes(file.toPath()));
        return Base64.getEncoder().encodeToString(hash);
    }
}
