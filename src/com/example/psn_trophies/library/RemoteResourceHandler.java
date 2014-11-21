package com.example.psn_trophies.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.psn_trophies.R;
import com.example.psn_trophies.entities.Game;
import com.example.psn_trophies.entities.HeaderItem;
import com.example.psn_trophies.entities.Platform;
import com.example.psn_trophies.entities.SearchResultItem;
import com.example.psn_trophies.entities.Trophy;
import com.example.psn_trophies.entities.Trophy.TrophyColor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RemoteResourceHandler {
    public static final String SERVER_URL = "http://nighthunters.ca/psn_trophies/";

    private RemoteResourceHandler() {} // NO TOUCHY

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // START BITMAP STUFF

    public static void loadIconForGame(int gameId, ImageView placeholder) {
        loadRemoteImageIntoImageView("images/game/" + gameId + ".png", placeholder);
    }

    public static void loadSmallIconForGame(int gameId, ImageView placeholder) {
        loadRemoteImageIntoImageView("images/game/" + gameId + "_small.png", placeholder);
    }

    public static void loadSmallIconForTrophy(int gameId, int trophyId, ImageView placeholder) {
        loadRemoteImageIntoImageView("images/trophy/" + gameId + "/"+ trophyId + "_small.png", placeholder);
    }

    public static void loadIconForTrophy(int gameId, int trophyId, ImageView placeholder) {
        loadRemoteImageIntoImageView("images/trophy/" + gameId + "/"+ trophyId + ".png", placeholder);
    }

    private static void loadRemoteImageIntoImageView(String url, ImageView placeholder) {
        if (placeholder == null) return;
        if (!isNetworkAvailable(placeholder.getContext().getApplicationContext())) return;
        new BitmapDownloader(new WeakReference<>(placeholder))
                .execute(SERVER_URL + url);
    }

    // END BITMAP STUFF

    // START JSON STUFF

    public static void loadAdapterWithAllGamesAndTrophies(ListView listView) {
        loadAdapterWithUrl("", listView);
    }

    public static void loadAdapterWithAllTrophies(ListView listView) {
        loadAdapterWithUrl("?trophies", listView);
    }

    public static void loadAdapterWithAllGames(ListView listView) {
        loadAdapterWithUrl("?games", listView);
    }

    public static void loadAdapterWithTrophiesForGame(int gameId, ListView listView) {
        loadAdapterWithUrl("?trophies&game="+gameId, listView);
    }

    private static void loadAdapterWithUrl(String url, ListView listView) {
        if (url == null) return;
        if (!isNetworkAvailable(listView.getContext().getApplicationContext())) return;
        new DataDownloader(new WeakReference<>(listView))
                .execute(url);
    }

    // END JSON STUFF

    // BITMAP DOWNLOADER
    // BITMAP DOWNLOADER
    // BITMAP DOWNLOADER

    private static class BitmapDownloader extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        private BitmapDownloader(WeakReference imageView) {
            this.imageViewReference = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (imageViewReference == null) return;
            ImageView imageView = imageViewReference.get();
            if (imageView == null) return;
            //imageView.setBackgroundResource(R.drawable.downloading);
            imageView.setImageResource(R.drawable.downloading);
            LayerDrawable progressAnimation = (LayerDrawable) imageView.getDrawable();
            ((Animatable) progressAnimation.getDrawable(0)).start();
            ((Animatable) progressAnimation.getDrawable(1)).start();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            if (url == null) return null;
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            final HttpGet getRequest = new HttpGet(url);
            try {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode
                            + " while retrieving bitmap from " + url);
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        return BitmapFactory.decodeStream(inputStream);
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                getRequest.abort();
                Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
            } finally {
                if (client != null) {
                    client.close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference == null) return;
            ImageView imageView = imageViewReference.get();
            if (imageView == null) return;
            //imageView.setBackgroundResource(0);
            //imageView.setBackground(null);
            if (bitmap == null) {
                imageView.setImageDrawable(imageView.getContext().getResources()
                        .getDrawable(R.drawable.no_image));
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    // JSON PARSER
    // JSON PARSER
    // JSON PARSER
    // JSON PARSER

    private static class DataDownloader extends AsyncTask<String, Void, JSONObject> {
        private static final String TAG_ROOT = "psn";
        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "name";
        private final WeakReference<ListView> listViewReference;
        private ProgressDialog pDialog;

        private DataDownloader(WeakReference<ListView> listView) {
            this.listViewReference = listView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (listViewReference == null) return;
            ListView listView = listViewReference.get();
            if (listView == null) return;

            pDialog = new ProgressDialog(listView.getContext());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            if (args[0] == null) return null;
            return jParser.getJSONFromUrl(SERVER_URL + args[0]);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (listViewReference == null) return;
            ListView listView = listViewReference.get();
            if (listView == null) return;

           // Log.d("JSON", json.toString());
            ArrayList<SearchResultItem> items = new ArrayList<>();
            try {
                // Getting JSON Array from URL
                JSONArray array = json.getJSONArray(TAG_ROOT);
                if (json.isNull(TAG_ROOT)) {
                    Log.d("JSON", json.getString("error"));
                    return;
                }

                for (int i = 0; i < array.length(); i++) {
                    JSONObject partition = array.getJSONObject(i);

                    if (!partition.isNull("games") && partition.getJSONArray("games").length() > 0) {
                        items.add(new HeaderItem("Games").beHeader());
                        JSONArray gamesArray = partition.getJSONArray("games");
                        for (int e = 0; e < gamesArray.length(); e++) {
                            JSONObject game = gamesArray.getJSONObject(e);
                            int id = game.getInt("id");
                            String name = game.getString("name");
                            JSONArray platformsArray = game.getJSONArray("platforms");
                            Platform[] platforms = new Platform[platformsArray.length()];
                            for (int r = 0; r < platformsArray.length(); r++) {
                                platforms[r] = Platform.getPlatformByServerInt(platformsArray.getInt(r));
                            }
                            int platinum = game.getInt("platinum");
                            int gold = game.getInt("gold");
                            int silver = game.getInt("silver");
                            int bronze = game.getInt("bronze");
                            items.add(new Game(id, name, platforms, platinum, gold, silver, bronze));
                        }
                    }

                    if (!partition.isNull("trophies") && partition.getJSONArray("trophies").length() > 0) {
                        items.add(new HeaderItem("Trophies").beHeader());
                        JSONArray trophiesArray = partition.getJSONArray("trophies");
                        for (int j = 0; j < trophiesArray.length(); j++) {
                            JSONObject trophy = trophiesArray.getJSONObject(j);
                            int id = trophy.getInt("id");
                            String name = trophy.getString("name");
                            String description = trophy.getString("description");
                            int gameId = trophy.getInt("game_id");
                            String gameName = trophy.getString("game_name");
                            TrophyColor color = TrophyColor.getColorByServerInt(trophy.getInt("color"));
                            items.add(new Trophy(id, name, description, new Game(gameId, gameName), color));
                        }
                    }
                }

                SearchResultsAdapter adapter = new SearchResultsAdapter<>(listView.getContext(), items);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(adapter.searchReslutsItemClickListener);
            } catch (JSONException e) {
                try {
                    if (json.get("error") != null) {
                        Toast.makeText(listView.getContext(), "ERROR: " + json.getString("error"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(listView.getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
