package com.example.insight.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class JsonDTO {

    private long id;
    private String uuid;
    private String key;

    private long id_connection;
    private String permission;

    //Getters & Setters

    public JsonDTO() {
    }

    public long getId() {
        return id;
    }

    public JsonDTO(long id, String uuid, String key) {
        this.id = id;
        this.uuid = uuid;
        this.key = key;
    }

    public JsonDTO(long id, String uuid, long id_connection, String key) {
        this.id = id;
        this.uuid = uuid;
        this.id_connection = id_connection;
        this.key = key;
    }

    public JsonDTO(long id, String uuid, String key, long id_connection, String permission) {
        this.id = id;
        this.uuid = uuid;
        this.key = key;
        this.id_connection = id_connection;
        this.permission = permission;
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

    public long getId_connection() {
        return id_connection;
    }

    public void setId_connection(long id_connection) {
        this.id_connection = id_connection;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
