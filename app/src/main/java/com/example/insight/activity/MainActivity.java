package com.example.insight.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insight.tasks.ManagerLocation;
import com.example.insight.R;
import com.example.insight.base.DataBase;
import com.example.insight.dto.ConnectionsDTO;
import com.example.insight.dto.UserDTO;
import com.example.insight.tasks.CheckConnections;
import com.example.insight.tasks.GetInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GetInfo.Callback, CheckConnections.Callback, NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap map;
    private DataBase db;
    private UserDTO thisUser;
    SimpleAdapter adapter;
    ListView list_connections;
    TextView empty_list_contacts;
    ArrayList<Map<String, Object>> arrayConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = new DataBase(this);
        thisUser = db.getUser(10);
        arrayConnection = new ArrayList<>();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerLayout = navigationView.getHeaderView(0);
        TextView youName = (TextView) headerLayout.findViewById(R.id.youName);
        youName.setText(thisUser.getFirstName() + " " + thisUser.getLastName());
        list_connections = (ListView) headerLayout.findViewById(R.id.list_connections);
        empty_list_contacts = (TextView) headerLayout.findViewById(R.id.empty_list_contacts);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 0));
        new ManagerLocation(this, map);
        new CheckConnections(this, this, db, thisUser).execute();
        new GetInfo(this, thisUser, db).execute();

        adapter = new SimpleAdapter(this, arrayConnection,
                R.layout.list_item, new String[]{"fullName", "id", "marker"},
                new int[]{R.id.name, R.id.id, R.id.marker});

        list_connections.setAdapter(adapter);
        list_connections.setOnItemClickListener(itemClickListener);
        this.updateList();
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, Object> itemHashMap = (Map<String, Object>) parent.getItemAtPosition(position);
            String titleItem = itemHashMap.get("fullName").toString();
            Marker m = (Marker) itemHashMap.get("marker");
            DrawerLayout d = (DrawerLayout) findViewById(R.id.drover);
            if(m.getPosition().latitude != 0) {
                d.closeDrawers();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 13));
            }else
                Toast.makeText(getApplicationContext(), "Пользователь ещё не отправил координаты", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void updatePosition() {
        this.updateList();
    }

    @Override
    public void updateListView(ConnectionsDTO connections) {
        this.updateList();
    }

    public void updateList(){

        for (Map<String, Object> a : arrayConnection) {
            Marker m = (Marker) a.get("marker");
            m.remove();
        }
        arrayConnection.clear();

        Map<String, Object> hm;
        ArrayList<ConnectionsDTO> connectionsList = db.getAllConnections();
        if (!connectionsList.isEmpty()) {
            for (ConnectionsDTO c : connectionsList)
                if (c.getLastName() != null) {
                    Marker userMarker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(c.getLatitude(), c.getLongitude()))
                            .title(c.getFirstName() + " " + c.getLastName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    );

                    if (c.getLatitude() == 0) {
                        userMarker.setVisible(false);
                    }

                    hm = new HashMap<>();
                    hm.put("fullName", c.getFirstName() + " " + c.getLastName());
                    hm.put("id", c.getId_connection());
                    hm.put("marker", userMarker);
                    arrayConnection.add(hm);
                }
            adapter.notifyDataSetChanged();

            list_connections.setVisibility(View.VISIBLE);
            empty_list_contacts.setVisibility(View.INVISIBLE);
            empty_list_contacts.setHeight(0);
        }else{
            list_connections.setVisibility(View.INVISIBLE);
            empty_list_contacts.setVisibility(View.VISIBLE);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_user_button) {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        } else if (id == R.id.enter_key_button) {
            Intent intent = new Intent(this, EnterKeyActivity.class);
            startActivity(intent);
        } else if (id == R.id.sink_button){
            GetInfo getinfo = new GetInfo(this, thisUser, db);
            CheckConnections checkconnections = new CheckConnections(this, this, db, thisUser);
            if ((getinfo.getStatus().equals(AsyncTask.Status.PENDING)) || (getinfo.isCancelled())) getinfo.execute();
            if ((checkconnections.getStatus().equals(AsyncTask.Status.PENDING)) || (getinfo.isCancelled())) checkconnections.execute();
        }else if(id == R.id.exit_to_app_button){
            db.deleteBase();
            this.finish();
        }
        item.setChecked(true);
    //    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drover);
    //    drawer.closeDrawers();
        return true;
    }
}