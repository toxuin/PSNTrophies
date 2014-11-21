package com.example.psn_trophies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.psn_trophies.entities.Trophy;
import com.example.psn_trophies.library.RemoteResourceHandler;

/**
 * This represents a view with particular trophy
 */
public class TrophyFragment extends Fragment {
    View rootView;
    Trophy trophy;
    public TrophyFragment() { } // Empty constructor required for fragment subclasses

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trophy, container, false);
        if (trophy == null) throw new RuntimeException("USE SETTROPHY METHOD BEFORE DRAWING TROPHYFRAGMENT");
        getActivity().setTitle(trophy.getName());

        TextView trophyName = (TextView) rootView.findViewById(R.id.trophy_name);
        TextView trophyDescription = (TextView) rootView.findViewById(R.id.trophy_description);
        ImageView trophyImage = (ImageView) rootView.findViewById(R.id.trophy_image);
        ImageView trophyColorImage = (ImageView) rootView.findViewById(R.id.trophy_color_image);

        trophyName.setText(trophy.getName());
        trophyDescription.setText(trophy.getDescription());
        trophyImage.setImageResource(RemoteResourceHandler.getIconForTrophy(trophy.getGame().getId(), trophy.getId()));

        return rootView;
    }

    public void setTrophy(Trophy trophy) {
        this.trophy = trophy;
    }
}
