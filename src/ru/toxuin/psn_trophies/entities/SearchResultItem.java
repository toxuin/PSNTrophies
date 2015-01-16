package ru.toxuin.psn_trophies.entities;

public abstract class SearchResultItem {
    private boolean isHeader = false;

    public abstract String getName();
    public abstract int getId();

    public boolean isGroupHeader() {
        return isHeader;
    }

    public void setIsHeader() {
        isHeader = true;
    }
}
