package com.example.psn_trophies;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.psn_trophies.entities.Trophy;
import com.example.psn_trophies.library.RemoteResourceHandler;

/**
 * This represents a view with particular trophy
 */
public class TrophyFragment extends Fragment {
    View rootView;
    Trophy trophy;
    private ProgressBar progressBar;
    private AbsoluteLayout commentsLoadingPanel;
    private String commentsUrl;
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

        commentsLoadingPanel = (AbsoluteLayout) rootView.findViewById(R.id.comments_loading_panel);
        progressBar = (ProgressBar) rootView.findViewById(R.id.comments_loading_progressbar);
        progressBar.setMax(100);

        trophyName.setText(trophy.getName());
        trophyDescription.setText(trophy.getDescription());
        RemoteResourceHandler.loadIconForTrophy(trophy.getGame().getId(), trophy.getId(), trophyImage);
        //trophyImage.setImageBitmap(RemoteResourceHandler.getIconForTrophy(trophy.getGame().getId(), trophy.getId()));
        trophyColorImage.setImageResource(trophy.getColor().getDrawableResource());

        WebView commentPanel = (WebView) rootView.findViewById(R.id.trophy_comment_panel);
        WebSettings webSettings = commentPanel.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        commentPanel.requestFocusFromTouch();
        commentPanel.setWebViewClient(new CommentsWebViewClient());
        commentPanel.setWebChromeClient(new CommentsWebChromeClient());

        commentsUrl = RemoteResourceHandler.SERVER_URL + "comments.php?disqus_id=game" + trophy.getGame().getId() + "trophy" + trophy.getId();
        if (RemoteResourceHandler.isNetworkAvailable(getActivity())) {
            commentPanel.loadUrl(commentsUrl);
        } else {
            commentPanel.loadData("<p>Could not load comments: no internet connection.</p>",
                    "text/html; charset=UTF-8", null);
        }

        return rootView;
    }

    public void setTrophy(Trophy trophy) {
        this.trophy = trophy;
    }

    private class CommentsWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    private class CommentsWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setProgress(100);
            progressBar.setVisibility(View.GONE);
            commentsLoadingPanel.setVisibility(View.GONE);
            //Log.d("DISQUS", "URL: " + url);
            if (url.contains("logout") || url.contains("disqus.com/next/login-success")) {
                view.loadUrl(commentsUrl);
            } else if (url.contains("disqus.com/_ax/twitter/complete")
                    || url.contains("disqus.com/_ax/facebook/complete")
                    || url.contains("disqus.com/_ax/google/complete")) {
                view.loadUrl("YOUR_URL/login.php");

            /*} else if (url.contains(RemoteResourceHandler.SERVER_URL + "login.php")) {
                view.loadUrl(commentsUrl);*/
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            commentsLoadingPanel.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i("disqus error", "failed: " + failingUrl + ", error code: " + errorCode + " [" + description + "]");
        }
    }
}
