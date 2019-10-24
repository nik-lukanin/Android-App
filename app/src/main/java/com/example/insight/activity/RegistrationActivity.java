//Layout регистрации нового пользователя.
package com.example.insight.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.insight.R;
import com.example.insight.base.DataBase;
import com.example.insight.dto.UserDTO;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.example.insight.Constants.URL.ADD_USER;

public class RegistrationActivity extends Activity {

    Button btn_start;
    EditText edit_name;
    EditText edit_fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        btn_start = (Button) findViewById(R.id.button_start);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_fam = (EditText) findViewById(R.id.edit_fam);
    }

    public void onClick(View v) {

        String name = edit_name.getText().toString();
        String fam = edit_fam.getText().toString();

        if (name.length() > 0) {
            btn_start.setEnabled(false);
            String uuid = UUID.randomUUID().toString();
            new RegistrationTask(this, uuid, name, fam).execute();
        } else
            Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_SHORT).show();

    }

    //Task отправки данных на сервер
    public class RegistrationTask extends AsyncTask<Void, Void, UserDTO> {

        private String uuid;
        private String name;
        private String fam;
        private RestTemplate rest;
        private DataBase db;

        RegistrationTask(Context context, String uuid, String name, String fam) {
            this.uuid = uuid;
            this.name = name;
            this.fam = fam;

            db = new DataBase(context);
            rest = new RestTemplate();
            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        @Override
        protected UserDTO doInBackground(Void... params) {
            UserDTO ObjectRequest = new UserDTO(uuid, name, fam);
            try {
                return rest.postForObject(ADD_USER, ObjectRequest, UserDTO.class);
            } catch (Exception e) {
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserDTO result) {
            UserDTO user = new UserDTO(10, result.getId(), uuid, result.getKey(), name, fam, 0, 0);
            db.addUser(user);
            end();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(), "Нет связи с сервером", Toast.LENGTH_SHORT).show();
            btn_start.setEnabled(true);
        }
    }

    public void end() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
