package com.example.insight.base;

import com.example.insight.dto.ConnectionsDTO;
import com.example.insight.dto.UserDTO;

import java.util.ArrayList;

public interface Service {
    void addUser(UserDTO user);
    UserDTO getUser(int id);
    void updateUser(UserDTO user);
    void addConnection(ConnectionsDTO connections);
    void updateConnections(ConnectionsDTO connections);
    ConnectionsDTO getConnections(long id);
    ArrayList<ConnectionsDTO> getAllConnections();
    void deleteBase();
}
