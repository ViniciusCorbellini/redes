package com.mycompany.trabalho_redes.parte1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author manoCorbas
 */
public class Message {
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate date;
    
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime time;
    private String username;
    private String message;
    
    public Message(String username, String message) {
        date = LocalDate.now();
        time = LocalTime.now();
        this.username = username;
        this.message = message;
    }

    @JsonCreator
    public Message() {
        date = LocalDate.now();
        time = LocalTime.now();
    }
    
    @Override
    public String toString() {
        return '[' + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) 
                + ", " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " 
                + username + ": " + message;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}