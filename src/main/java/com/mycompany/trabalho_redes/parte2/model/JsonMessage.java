package com.mycompany.trabalho_redes.parte2.model;

import java.util.List;

/**
 *
 * @author Vinicius Corbellini
 */
public class JsonMessage {

    private String cmd;
    private String file;
    private String hash;
    private String value;
    private List<String> files;
    private String status;

    //===== Construtores
    public JsonMessage() {
    }

    public JsonMessage(String cmd, String file, String hash, String value, List<String> files, String status) {
        this.cmd = cmd;
        this.file = file;
        this.hash = hash;
        this.value = value;
        this.files = files;
        this.status = status;
    }
    
    //===== getters e seteters
    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
