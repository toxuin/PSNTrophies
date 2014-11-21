package com.example.psn_trophies.library;

import android.graphics.Bitmap;

public class RemoteResourceHandler {
    private static RemoteResourceHandler instance;
    public static final String SERVER_URL = "http://nighthunters.ca/destiny_lfg/";

    private RemoteResourceHandler() {

    }

    public static RemoteResourceHandler getInstance() {
        if (instance == null) {
            instance = new RemoteResourceHandler();
        }
        return instance;
    }

    public static Bitmap getIconForGameId(int gameId) {
        return null;
    }

    public static Bitmap getSmallIconForGameId(int gameId) {
        return null;
    }

    public static Bitmap getSmallIconForTrophy(int gameId, int trophyId) {
        return null;
    }

    public static Bitmap getIconForTrophy(int gameId, int trophyId) {
        return null;
    }
}
