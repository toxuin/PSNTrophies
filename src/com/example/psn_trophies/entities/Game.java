package com.example.psn_trophies.entities;

public class Game extends SearchResultItem {
    private final int id;
    private final String name;
    private final Platform[] platforms;
    private final int platinum;
    private final int gold;
    private final int silver;
    private final int bronze;

    public Game(int id, String name, Platform[] platforms, int platinum, int gold, int silver, int bronze) {
        this.id = id;
        this.name = name;
        this.platforms = platforms;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.platinum = platinum;
    }

    public Game(int id, String name) {
        this.id = id;
        this.name = name;
        this.platforms = new Platform[] {Platform.PS3};
        this.platinum = 0;
        this.gold = 0;
        this.silver = 0;
        this.bronze = 0;
    }

    public int getTotalTrophyCount() {
        return platinum + gold + silver + bronze;
    }

    public int getPoints() {
        return platinum*180 + gold*90 + silver*30 + bronze*15;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game)) return false;
        Game that = (Game) o;
        return this.id == that.id;
    }

    // ONLY GETTERS & SETTERS BEYOND THIS POINT

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    public Platform[] getPlatforms() {
        return platforms;
    }

    public int getPlatinum() {
        return platinum;
    }

    public int getGold() {
        return gold;
    }

    public int getSilver() {
        return silver;
    }

    public int getBronze() {
        return bronze;
    }
}


