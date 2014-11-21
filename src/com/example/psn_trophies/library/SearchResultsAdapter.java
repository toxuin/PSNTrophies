package com.example.psn_trophies.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.psn_trophies.BaseActivity;
import com.example.psn_trophies.GameFragment;
import com.example.psn_trophies.R;
import com.example.psn_trophies.TrophyFragment;
import com.example.psn_trophies.entities.Game;
import com.example.psn_trophies.entities.Platform;
import com.example.psn_trophies.entities.SearchResultItem;
import com.example.psn_trophies.entities.Trophy;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchResultsAdapter<T extends SearchResultItem> extends ArrayAdapter<T> {

    private final Context context;
    private final ArrayList<T> itemsList;
    private boolean inGame = false;

    public SearchResultsAdapter(Context context, ArrayList<T> itemsList) {
        super(context, R.layout.search_result_item, itemsList);
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        if (!itemsList.get(position).isGroupHeader()){
            rowView = inflater.inflate(R.layout.search_result_item, parent, false);

            TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
            ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
            LinearLayout platformPanel = (LinearLayout) rowView.findViewById(R.id.platfrom_panel);
            ImageView ps3platform = (ImageView) rowView.findViewById(R.id.item_platform_ps3);
            ImageView ps4platform = (ImageView) rowView.findViewById(R.id.item_platform_ps4);
            ImageView vitaPlatform = (ImageView) rowView.findViewById(R.id.item_platform_vita);

            titleView.setText(itemsList.get(position).getName());
            if (itemsList.get(position) instanceof Game) {
                platformPanel.setVisibility(View.VISIBLE);
                Game game = (Game) itemsList.get(position);
                //imgView.setImageBitmap(RemoteResourceHandler.getSmallIconForGameId(itemsList.get(position).getId()));
                if (Arrays.asList(game.getPlatforms()).contains(Platform.PS3)) {
                    ps3platform.setVisibility(View.VISIBLE);
                }
                if (Arrays.asList(game.getPlatforms()).contains(Platform.PS4)) {
                    ps4platform.setVisibility(View.VISIBLE);
                }
                if (Arrays.asList(game.getPlatforms()).contains(Platform.VITA)) {
                    vitaPlatform.setVisibility(View.VISIBLE);
                }
            } else if (itemsList.get(position) instanceof Trophy) {
                Trophy trophy = (Trophy) itemsList.get(position);
                //imgView.setImageBitmap(RemoteResourceHandler.getSmallIconForTrophy(((Trophy) itemsList.get(position)).getGame().getId(), itemsList.get(position).getId()));
                imgView.setImageResource(R.drawable.sample_trophy_icon_small);
                if (!inGame) {
                    TextView supportText = (TextView) rowView.findViewById(R.id.item_support_text);
                    supportText.setVisibility(View.VISIBLE);
                    supportText.setText(trophy.getGame().getName());
                }
                LinearLayout trophyColorPanel = (LinearLayout) rowView.findViewById(R.id.item_trophy_color_panel);
                ImageView trophyColorImage = (ImageView) rowView.findViewById(R.id.item_trophy_color_image);
                trophyColorPanel.setVisibility(View.VISIBLE);
                trophyColorImage.setVisibility(View.VISIBLE);
                trophyColorImage.setImageResource(trophy.getColor().getDrawableResource());
            }
        } else {
            rowView = inflater.inflate(R.layout.search_result_heading, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.header);
            titleView.setText(itemsList.get(position).getName());
        }

        return rowView;
    }

    public SearchResultsAdapter setIngame() {
        inGame = true;
        return this;
    }

    public OnItemClickListener searchReslutsItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (itemsList.get(position).isGroupHeader()) return;
            if (itemsList.get(position) instanceof Game) {
                GameFragment gameFragment = new GameFragment();
                gameFragment.setGame((Game) itemsList.get(position));
                BaseActivity.setContent(gameFragment);
            } else if (itemsList.get(position) instanceof Trophy) {
                TrophyFragment trophyFragment = new TrophyFragment();
                trophyFragment.setTrophy((Trophy) itemsList.get(position));
                BaseActivity.setContent(trophyFragment);
            }
        }
    };
}
