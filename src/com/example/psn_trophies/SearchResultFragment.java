package com.example.psn_trophies;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.psn_trophies.library.RemoteResourceHandler;

/**
 * Represents a view with top 10 games and trophies OR search results.
 */
public class SearchResultFragment extends Fragment {
    View rootView;
    Scope scope;
    private String query;

    public SearchResultFragment() {} // SUBCLASSES OF FRAGMENT NEED EMPTY CONSTRUCTOR

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        String title = getString(R.string.app_name);
        if (scope == Scope.GAMES) title = "Top 10 games";
        else if (scope == Scope.TROPHIES) title = "Top 10 trophies";
        getActivity().setTitle(title);
        ListView itemsList = (ListView) rootView.findViewById(R.id.itemsList);

        if (scope == null) {
            if (query == null) {
                RemoteResourceHandler.loadAdapterWithAllGamesAndTrophies(itemsList);
            } else {
                RemoteResourceHandler.loadAdapterWithQuery(query, itemsList);
            }
        } else {
            switch (scope) {
                case GAMES:
                    getActivity().setTitle("Games");
                    RemoteResourceHandler.loadAdapterWithAllGames(itemsList);
                    break;
                case TROPHIES:
                    getActivity().setTitle("Trophies");
                    RemoteResourceHandler.loadAdapterWithAllTrophies(itemsList);
                    break;
            }
        }

        return rootView;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Fragment setSearchQuery(String query) {
        this.query = query;
        return this;
    }

    enum Scope {
        GAMES, TROPHIES
    }
}
