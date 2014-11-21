package com.example.psn_trophies.entities;

public class HeaderItem extends SearchResultItem {
    private final String caption;

    public HeaderItem(String caption) {
        this.caption = caption;
    }

    @Override
    public String getName() {
        return caption;
    }

    @Override
    public int getId() {
        return -1;
    }

    public HeaderItem beHeader() {
        super.setIsHeader();
        return this;
    }
}
