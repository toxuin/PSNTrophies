package com.example.psn_trophies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.psn_trophies.entities.Game;
import com.example.psn_trophies.entities.Trophy;
import com.example.psn_trophies.library.SearchResultsAdapter;

import java.util.ArrayList;

/**
 * This represents the view where user browses all the trophies for a particular game.
 */
public class GameFragment extends Fragment {
    View rootView;
    Game game;
    public GameFragment() { } // Empty constructor required for fragment subclasses

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game, container, false);
        if (game == null) throw new RuntimeException("USE SETGAME METHOD BEFORE DRAWING GAMEFRAGMENT");
        getActivity().setTitle(game.getName());

        TextView gameNameCaption = (TextView) rootView.findViewById(R.id.game_name);
        TextView platinumCount = (TextView) rootView.findViewById(R.id.game_platinum_count);
        TextView goldCount = (TextView) rootView.findViewById(R.id.game_gold_count);
        TextView silverCount = (TextView) rootView.findViewById(R.id.game_silver_count);
        TextView bronzeCount = (TextView) rootView.findViewById(R.id.game_bronze_count);
        TextView gameTotalCaption = (TextView) rootView.findViewById(R.id.game_total);

        gameNameCaption.setText(game.getName());
        platinumCount.setText(""+game.getPlatinum());
        goldCount.setText(""+game.getGold());
        silverCount.setText(""+game.getSilver());
        bronzeCount.setText(""+game.getBronze());
        gameTotalCaption.setText("Total: " + String.format("%,d", game.getPoints()) + " points, " + game.getTotalTrophyCount() + " trophies");

        ListView trophyList = (ListView) rootView.findViewById(R.id.game_trophy_list);

        ArrayList<Trophy> items = new ArrayList<>();
        items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
        items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
        items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));
        items.add(new Trophy(12, "Red Dead Rape", description, game, Trophy.TrophyColor.BRONZE));

        SearchResultsAdapter adapter = new SearchResultsAdapter(getActivity(), items);
        trophyList.setAdapter(adapter.setIngame());
        trophyList.setOnItemClickListener(adapter.searchReslutsItemClickListener);

        return rootView;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
