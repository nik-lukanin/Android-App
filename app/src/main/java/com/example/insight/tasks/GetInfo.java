package com.example.insight.tasks;

import android.os.AsyncTask;

import com.example.insight.Constants;
import com.example.insight.activity.MainActivity;
import com.example.insight.dto.ConnectionsDTO;
import com.example.insight.dto.JsonDTO;
import com.example.insight.dto.UserDTO;
import com.example.insight.base.DataBase;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetInfo extends AsyncTask<Void, Void, List<ConnectionsDTO>> {


    private UserDTO thisUser;
    private RestTemplate rest;
    private DataBase db;
    private Callback callback;

    public interface Callback{
        void updatePosition();
    }

    public GetInfo(Callback callback, UserDTO thisUser, DataBase db) {
        this.thisUser = thisUser;
        this.db = db;
        this.callback = callback;
        rest = new RestTemplate();
        rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    protected List<ConnectionsDTO> doInBackground(Void... voids) {
        ArrayList<JsonDTO> list_json = new ArrayList<>();
        ArrayList<ConnectionsDTO> connectionsList = db.getAllConnections();
        if (!connectionsList.isEmpty()) {
            for (ConnectionsDTO c : connectionsList) {
                list_json.add(new JsonDTO(thisUser.getId(), thisUser.getUuid(), c.getId_connection(), c.getKey()));
            }

            try {
                ConnectionsDTO[] connectionsDTO = rest.postForObject(Constants.URL.GET_INFO, list_json, ConnectionsDTO[].class);
                return Arrays.asList(connectionsDTO);
            } catch (Exception e) {
                this.cancel(true);
                return null;
            }
        }else {
            this.cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ConnectionsDTO> list) {
        for(ConnectionsDTO connection : list) {
           db.updateConnections(connection);
        }
        callback.updatePosition();
    }

    @Override
    protected void onCancelled() {
    }
}
