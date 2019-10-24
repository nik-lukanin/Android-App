package com.example.insight.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.insight.Constants;
import com.example.insight.dto.ConnectionsDTO;
import com.example.insight.dto.JsonDTO;
import com.example.insight.dto.UserDTO;
import com.example.insight.base.DataBase;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class CheckConnections extends AsyncTask<Void, Void, List<ConnectionsDTO>> {

    private long userId;
    private String userUuid;
    private String userKey;
    private Context context;
    private DataBase db;
    private RestTemplate rest;
    private Callback callback;

    public interface Callback{
        void updateListView(ConnectionsDTO connections);
    }

    public CheckConnections(Context context, Callback callback, DataBase db, UserDTO thisUser) {
        this.context = context;
        this.db = db;
        this.userId = thisUser.getId();
        this.userUuid = thisUser.getUuid();
        this.userKey = thisUser.getKey();
        this.callback = callback;

        rest = new RestTemplate();
        rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    protected List<ConnectionsDTO> doInBackground(Void... voids) {
        JsonDTO ObjectRequest = new JsonDTO(userId, userUuid, userKey);
        try{
            ConnectionsDTO[] connectionsDTO = rest.postForObject(Constants.URL.CHECK_CONNECTIONS,  ObjectRequest, ConnectionsDTO[].class);
            return Arrays.asList(connectionsDTO);
        }catch (Exception e){
            this.cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ConnectionsDTO> list) {
        for(ConnectionsDTO c : list) {
            showDialog(c.getId_connection(), c.getFirstName(), c.getLastName());
        }
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context,  "Нет связи с сервером", Toast.LENGTH_SHORT).show();
    }

    private void showDialog( long id_connection, final String name, final String fam){

        final ConnectionsDTO connections = new ConnectionsDTO(id_connection, userKey, name, fam);
        final confirmLink PositiveConfirmLink = new confirmLink(id_connection, "true");
        final confirmLink NegativeConfirmLink = new confirmLink(id_connection, "false");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Добавить пользователя");
        builder.setMessage("Пользователь " + name + " " + fam + " хочет присоединиться");
        builder.setCancelable(false);
        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.addConnection(connections);
                PositiveConfirmLink.execute();
                callback.updateListView(connections);
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NegativeConfirmLink.execute();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class confirmLink extends AsyncTask<Void, Void, Void> {

        private long id_connection;
        private String permission;

        confirmLink(long id_connection, String permission) {
            this.id_connection = id_connection;
            this.permission = permission;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonDTO ObjectRequest = new JsonDTO(userId, userUuid, userKey, id_connection, permission);
            rest.put(Constants.URL.CONFIRM_LINK, ObjectRequest);
            return null;
        }
    }

}
