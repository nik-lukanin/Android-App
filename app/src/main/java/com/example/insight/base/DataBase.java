package com.example.insight.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.insight.dto.ConnectionsDTO;
import com.example.insight.dto.UserDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper implements Service {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InSightBD";

    private static final String TABLE_USER = "user";
    private static final String USER_TABLE_ID = "table_id";
    private static final String USER_ID = "user_id";
    private static final String USER_UUID = "uuid";
    private static final String USER_KEY = "key";
    private static final String USER_FIRST_NAME = "firstName";
    private static final String USER_LAST_NAME = "lastName";
    private static final String USER_LATITUDE = "latitude";
    private static final String USER_LONGITUDE = "longitude";

    private static final String TABLE_CONNECTIONS = "connections";
    private static final String CONNECTIONS_COLUMN_ID = "_id";
    private static final String CONNECTIONS_ID = "id_connections";
    private static final String CONNECTIONS_KEY = "key";
    private static final String CONNECTIONS_FIRST_NAME = "firstName";
    private static final String CONNECTIONS_LAST_NAME = "lastName";
    private static final String CONNECTIONS_LATITUDE = "latitude";
    private static final String CONNECTIONS_LONGITUDE = "longitude";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USER + " ("
                + USER_TABLE_ID + " INTEGER,"
                + USER_ID + " INTEGER," + USER_UUID + " TEXT,"
                + USER_KEY + " TEXT," + USER_FIRST_NAME + " TEXT,"
                + USER_LAST_NAME + " TEXT," + USER_LATITUDE + " REAL,"
                + USER_LONGITUDE + " REAL" + ")";

        String CREATE_TABLE_CONNECTIONS = "CREATE TABLE " + TABLE_CONNECTIONS + " ("
                + CONNECTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONNECTIONS_ID + " INTEGER," + CONNECTIONS_KEY + " TEXT,"
                + CONNECTIONS_FIRST_NAME + " TEXT," + CONNECTIONS_LAST_NAME + " TEXT,"
                + CONNECTIONS_LATITUDE + " REAL," + CONNECTIONS_LONGITUDE + " REAL" + ")";

        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONNECTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void addUser(UserDTO user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_ID, user.getTable_id());
        values.put(USER_ID, user.getId());
        values.put(USER_UUID, user.getUuid());
        values.put(USER_KEY, user.getKey());
        values.put(USER_FIRST_NAME, user.getFirstName());
        values.put(USER_LAST_NAME, user.getLastName());
        values.put(USER_LATITUDE, user.getLatitude());
        values.put(USER_LONGITUDE, user.getLongitude());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    @Override
    public void updateUser(UserDTO user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_FIRST_NAME, user.getFirstName());
        values.put(USER_LAST_NAME, user.getLastName());
        values.put(USER_LATITUDE, user.getLatitude());
        values.put(USER_LONGITUDE, user.getLongitude());

        db.update(TABLE_USER, values, USER_TABLE_ID + " = 10", null);
        db.close();
    }


    @Override
    public UserDTO getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[]{USER_TABLE_ID, USER_ID, USER_UUID,
                        USER_KEY, USER_FIRST_NAME, USER_LAST_NAME, USER_LATITUDE,
                        USER_LONGITUDE}, USER_TABLE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        UserDTO userDTO = null;

        if ((cursor != null) && (cursor.moveToFirst())) {
            userDTO = new UserDTO(
                    Long.parseLong(cursor.getString(0)),
                    Long.parseLong(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    Double.parseDouble(cursor.getString(6)),
                    Double.parseDouble(cursor.getString(7))
            );
        }
        db.close();
        return userDTO;
    }

    @Override
    public void updateConnections(ConnectionsDTO connections) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONNECTIONS_FIRST_NAME, connections.getFirstName());
        values.put(CONNECTIONS_LAST_NAME, connections.getLastName());
        values.put(CONNECTIONS_LATITUDE, connections.getLatitude());
        values.put(CONNECTIONS_LONGITUDE, connections.getLongitude());

        String path = CONNECTIONS_ID + " = " + String.valueOf(connections.getId_connection());
        db.update(TABLE_CONNECTIONS, values, path, null);
        db.close();
    }

    @Override
    public void addConnection(ConnectionsDTO connections) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONNECTIONS_ID, connections.getId_connection());
        values.put(CONNECTIONS_KEY, connections.getKey());
        values.put(CONNECTIONS_FIRST_NAME, connections.getFirstName());
        values.put(CONNECTIONS_LAST_NAME, connections.getLastName());
        values.put(CONNECTIONS_LATITUDE, connections.getLatitude());
        values.put(CONNECTIONS_LONGITUDE, connections.getLongitude());

        db.insert(TABLE_CONNECTIONS, null, values);
        db.close();
    }

    @Override
    public ConnectionsDTO getConnections(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONNECTIONS, null, CONNECTIONS_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        ConnectionsDTO connection = null;

        if ((cursor != null) && (cursor.moveToFirst())) {
            connection = new ConnectionsDTO(
                    Long.parseLong(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    Double.parseDouble(cursor.getString(5)),
                    Double.parseDouble(cursor.getString(6))
            );
        }
        db.close();
        return connection;
    }

    @Override
    public ArrayList<ConnectionsDTO> getAllConnections() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONNECTIONS, null, null, null, null, null, null);

        ArrayList<ConnectionsDTO> connectionsList = new ArrayList<ConnectionsDTO>();

        if ((cursor != null) && (cursor.moveToFirst())) {
            do {
                ConnectionsDTO connection = new ConnectionsDTO(
                        Long.parseLong(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Double.parseDouble(cursor.getString(5)),
                        Double.parseDouble(cursor.getString(6))
                );
                connectionsList.add(connection);
            } while (cursor.moveToNext());
        }
        db.close();
        return connectionsList;
    }


    @Override
    public void deleteBase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS);
        onCreate(db);
    }
}
