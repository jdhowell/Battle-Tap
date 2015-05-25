package com.jhowell.battletap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Static variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gameManager";
    private static final String TABLE_PLAYERS = "players";

    // Table contents
    private static final String KEY_ID = "id";
    private static final String KEY_USER = "username";
    private static final String KEY_PASS = "password";
    private static final String KEY_COUNT = "count";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER + " TEXT,"
                + KEY_PASS + " TEXT,"
                + KEY_COUNT + " INTEGER"
                + ")";
        db.execSQL(CREATE_PLAYERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }

    /* CRUD Operations (Create, Read, Update, Delete) */

    public void addPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER, player.getUsername());
        values.put(KEY_PASS, player.getPassword());
        values.put(KEY_COUNT, player.getCount());

        db.insert(TABLE_PLAYERS, null, values);
        db.close();
    }

    public Player getPlayer(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYERS,
                new String[] {KEY_ID, KEY_USER, KEY_PASS, KEY_COUNT},
                KEY_USER + "=?", new String[] {username}, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        Player player = new Player(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2));
        player.setCount(Integer.parseInt(cursor.getString(3)));
        cursor.close();

        return player;
    }

    public List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through the database
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // Create a player for each row and add it to the list
                Player player = new Player();
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setUsername(cursor.getString(1));
                player.setPassword(cursor.getString(2));
                player.setCount(Integer.parseInt(cursor.getString(3)));
                playerList.add(player);

                // Update the cursor
                cursor.moveToNext();
            }
        }
        cursor.close();

        return playerList;
    }

    public int getPlayersCount() {
        String countQuery = "SELECT * FROM " + TABLE_PLAYERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public boolean isAvailable(String username) {
        List<Player> playerList = getAllPlayers();

        // If the username is in the list of players, return false
        for (int i = 0; i < playerList.size(); i++) {
            if (username.equalsIgnoreCase(playerList.get(i).getUsername())) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(String username) {
        List<Player> playerList = getAllPlayers();

        // If the username is in the list of players, return true
        for (int i = 0; i < playerList.size(); i++) {
            if (username.equalsIgnoreCase(playerList.get(i).getUsername())) {
                return true;
            }
        }
        return false;
    }

    public int updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER, player.getUsername());
        values.put(KEY_PASS, player.getPassword());
        values.put(KEY_COUNT, player.getCount());

        return db.update(TABLE_PLAYERS, values, KEY_ID + "=?",
                new String[]{String.valueOf(player.getId())});
    }

    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYERS, KEY_USER + "=?", new String[] {player.getUsername()});
        db.close();
    }
}