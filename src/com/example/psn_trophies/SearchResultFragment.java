package com.example.psn_trophies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.psn_trophies.entities.Game;
import com.example.psn_trophies.entities.HeaderItem;
import com.example.psn_trophies.entities.Platform;
import com.example.psn_trophies.entities.SearchResultItem;
import com.example.psn_trophies.entities.Trophy;
import com.example.psn_trophies.library.SearchResultsAdapter;

import java.util.ArrayList;

/**
 * Represents a view with top 10 games and trophies OR search results.
 */
public class SearchResultFragment extends Fragment {
    View rootView;
    Scope scope;

    public SearchResultFragment() {} // SUBCLASSES OF FRAGMENT NEED EMPTY CONSTRUCTOR

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        String title = getString(R.string.app_name);
        if (scope == Scope.GAMES) title = "Top 10 games";
        else if (scope == Scope.TROPHIES) title = "Top 10 trophies";
        getActivity().setTitle(title);

        ListView itemsList = (ListView) rootView.findViewById(R.id.itemsList);

        final ArrayList<SearchResultItem> items = new ArrayList<>();

        final Game game = new Game(10, "Grand Theft Auto 5", new Platform[] {Platform.PS4}, 1, 10, 12, 34);

        if (scope == null || scope == Scope.GAMES) {
            if (scope == null) items.add(new HeaderItem("Games").beHeader());
            items.add(game);
            items.add(new Game(11, "Grand Theft Auto 4", new Platform[]{Platform.PS3}, 1, 15, 23, 43));
            items.add(new Game(12, "Grand Theft Auto 3", new Platform[]{Platform.VITA}, 1, 11, 41, 12));
        }

        if (scope == null || scope == Scope.TROPHIES) {
            if (scope == null) items.add(new HeaderItem("Trophies").beHeader());
            items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
            items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
            items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
            items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
        }

        SearchResultsAdapter adapter = new SearchResultsAdapter(getActivity().getBaseContext(), items);
        itemsList.setAdapter(adapter);
        itemsList.setOnItemClickListener(adapter.searchReslutsItemClickListener);

        return rootView;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    enum Scope {
        GAMES, TROPHIES
    }
}
