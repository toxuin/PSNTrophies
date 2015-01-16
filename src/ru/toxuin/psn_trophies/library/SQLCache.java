package ru.toxuin.psn_trophies.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.apache.commons.lang3.StringUtils;
import ru.toxuin.psn_trophies.entities.Game;
import ru.toxuin.psn_trophies.entities.Platform;
import ru.toxuin.psn_trophies.entities.Trophy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SQLCache extends SQLiteOpenHelper {
    private static SQLCache instance;
    private static final int DB_VERSION = 1;
    private static final String DB_FILE_NAME = "TrophyDB";
    private static final String TABLE_GAMES = "Games";
    private static final String TABLE_TROPHIES = "Trophies";

    private static Map<Integer, Game> gameCache = new HashMap<>();

    private SQLCache(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    public static SQLCache getInstance(Context context) {
        if (instance == null) {
            instance = new SQLCache(context);
        }
        return instance;
    }

    /**
     * Gets a cached game if exists. If not, returns null.
     * @param id a game id that should be extracted from db
     * @return Game that is cached in db
     */
    public Game getGame(int id) {
        if (gameCache.containsKey(id)) return gameCache.get(id);

        Cursor c = getReadableDatabase().query(TABLE_GAMES,
                new String[]{"name", "platforms", "platinum", "gold", "silver", "bronze"},
                "id = " + id, null, null, null, null);
        if (!c.moveToFirst()) return null;

        String name = c.getString(c.getColumnIndex("name"));
        String platformString = c.getString(c.getColumnIndex("platforms"));
        int platinum = c.getInt(c.getColumnIndex("platinum"));
        int gold = c.getInt(c.getColumnIndex("gold"));
        int silver = c.getInt(c.getColumnIndex("silver"));
        int bronze = c.getInt(c.getColumnIndex("bronze"));

        Set<Platform> platformSet = new HashSet<>();
        for (String p : platformString.split(":")) {
            platformSet.add(Platform.getPlatformByServerInt(Integer.parseInt(p)));
        }
        Platform[] platforms = (Platform[]) platformSet.toArray();

        Game game = new Game(id, name, platforms, platinum, gold, silver, bronze);
        Log.d("SQLCACHE", "FOUND GAME FOR ID: " + id + ", THAT'S " + game.getName());
        if (!gameCache.containsKey(id)) gameCache.put(id, game);
        return game;
    }

    /**
     * Saves a game to database
     * @param game Game that should be stored to db
     */
    public void saveGame(Game game) {
        if (game == null) return;

        Set<String> platformServerIds = new HashSet<>();
        for (Platform platform : game.getPlatforms()) {
            platformServerIds.add(""+platform.getServerInt());
        }

        ContentValues values = new ContentValues();
        values.put("id", game.getId());
        values.put("name", game.getName());
        values.put("platforms", StringUtils.join(platformServerIds, ":"));
        values.put("platinum", game.getPlatinum());
        values.put("gold", game.getGold());
        values.put("silver", game.getSilver());
        values.put("bronze", game.getBronze());

        getWritableDatabase().insert(TABLE_GAMES, null, values);
        Log.d("SQLCACHE", "SAVING GAME: " + game.getId());
    }




    public Trophy getTrophy(int id) {
        Cursor c = getReadableDatabase().query(TABLE_GAMES,
                new String[]{"name", "platforms", "platinum", "gold", "silver", "bronze"},
                "id = " + id, null, null, null, null);
        if (!c.moveToFirst()) return null;

        String name = c.getString(c.getColumnIndex("name"));
        String description = c.getString(c.getColumnIndex("description"));
        int gameId = c.getInt(c.getColumnIndex("game"));
        int color = c.getInt(c.getColumnIndex("color"));

        Trophy trophy = new Trophy(id, name, description, getGame(gameId), Trophy.TrophyColor.getColorByServerInt(color));
        Log.d("SQLCACHE", "FOUND CACHED TROPHY: " + trophy.getId());
        return trophy;
    }

    public void saveTrophy(Trophy trophy) {
        if (trophy == null) return;

        ContentValues values = new ContentValues();
        values.put("id", trophy.getId());
        values.put("name", trophy.getName());
        values.put("description", trophy.getDescription());
        values.put("game", trophy.getGame().getId());
        values.put("color", trophy.getColor().getServerInt());
        getWritableDatabase().insert(TABLE_TROPHIES, null, values);
        Log.d("SQLCACHE", "SAVING TROPHY: " + trophy.getId());
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_GAMES +
                    "(id integer primary key," +
                    " name text," +
                    " platforms text," +
                    " platinum integer," +
                    " gold integer," +
                    " silver integer," +
                    " bronze integer)");

        db.execSQL("create table " + TABLE_TROPHIES +
                "(id integer primary key," +
                " name text," +
                " description text," +
                " game integer," +
                " color integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TROPHIES);
        onCreate(db);
    }
}
