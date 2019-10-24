package com.example.insight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ConnectionsDTO {

    private long id_connection;
    private String key;
    private String firstName;
    private String lastName;
    private double latitude;
    private double longitude;

    //Getters & Setters

    public ConnectionsDTO() {
    }

    public ConnectionsDTO(long id_connection, String key) {
        this.id_connection = id_connection;
        this.key = key;
    }

    public ConnectionsDTO(long id_connection, String key, String firstName, String lastName) {

        this.id_connection = id_connection;
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ConnectionsDTO(long id_connection, String key, String firstName, String lastName, double latitude, double longitude) {
        this.id_connection = id_connection;
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId_connection() {
        return id_connection;
    }

    public void setId_connection(long id_connection) {
        this.id_connection = id_connection;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
