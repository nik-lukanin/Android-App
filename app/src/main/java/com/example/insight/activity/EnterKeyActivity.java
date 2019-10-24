package com.example.insight.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.insight.Constants;
import com.example.insight.R;
import com.example.insight.dto.ConnectionsDTO;
import com.example.insight.dto.JsonDTO;
import com.example.insight.dto.UserDTO;
import com.example.insight.base.DataBase;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class EnterKeyActivity extends AppCompatActivity {

    DataBase db = new DataBase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_key);
    }

    public void onClickSendKey(View view){
        EditText edit_key = (EditText) findViewById(R.id.edit_key);
        Button btn_send_key = (Button) findViewById(R.id.btn_send_key);
        btn_send_key.setEnabled(false);
        String key = edit_key.getText().toString();

        new sendKey(key).execute();
    }

    private class sendKey extends AsyncTask<Void, Void, UserDTO> {

        private String key;

        public sendKey(String key) {
            this.key = key;
        }

        @Override
        protected UserDTO doInBackground(Void... voids) {
            RestTemplate template = new RestTemplate();
            template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            UserDTO thisUser = db.getUser(10);
            try{
                UserDTO userDTO = template.postForObject(Constants.URL.ADD_CONNECTIONS, new JsonDTO(thisUser.getId(), thisUser.getUuid(), key), UserDTO.class);
                return userDTO;
            }catch (Exception e){
                this.cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserDTO userDTO) {
            db.addConnection(new ConnectionsDTO(userDTO.getId(), key));
            end();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getApplicationContext(),  "Нет связи с сервером", Toast.LENGTH_SHORT).show();
            end();
        }

    }

    public void end(){
        finish();
    }
}
