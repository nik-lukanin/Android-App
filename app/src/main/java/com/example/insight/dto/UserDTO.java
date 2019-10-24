package com.example.insight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserDTO {

    private long table_id;
    private long id;
    private String uuid;
    private String key;
    private String firstName;
    private String lastName;
    private double latitude;
    private double longitude;

    //Getters & Setters

    public UserDTO() {
    }

    public UserDTO(String uuid, String firstName, String lastName) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(long table_id, String uuid, String firstName, String lastName) {
        this.table_id = table_id;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(long table_id, long id, String uuid, String key, String firstName, String lastName, double latitude, double longitude) {
        this.table_id = table_id;
        this.id = id;
        this.uuid = uuid;
        this.key = key;
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getTable_id() {
        return table_id;
    }

    public void setTable_id(long table_id) {
        this.table_id = table_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
