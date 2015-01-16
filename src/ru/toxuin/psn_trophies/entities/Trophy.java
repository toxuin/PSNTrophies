package ru.toxuin.psn_trophies.entities;

import ru.toxuin.psn_trophies.R;

public class Trophy extends SearchResultItem {
    private final int id;
    private final String name;
    private final String description;
    private final Game game;
    private final TrophyColor color;

    public Trophy(int id, String name, String description, Game game, TrophyColor color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.game = game;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trophy)) return false;
        Trophy that = (Trophy) o;
        return this.id == that.id;
    }

    public enum TrophyColor {
        PLATINUM, GOLD, SILVER, BRONZE;

        public int getServerInt() {
            switch (this) {
                default:
                case BRONZE:
                    return 0;
                case SILVER:
                    return 1;
                case GOLD:
                    return 2;
                case PLATINUM:
                    return 3;
            }
        }

        public static TrophyColor getColorByServerInt(int serverInt) {
            for (int i = 0; i < TrophyColor.values().length; i++) {
                if (TrophyColor.values()[i].getServerInt() == serverInt) return TrophyColor.values()[i];
            }
            return null;
        }

        public int getDrawableResource() {
            switch (this) {
                case PLATINUM:
                    return R.drawable.trophy_platinum;
                case GOLD:
                    return R.drawable.trophy_gold;
                case SILVER:
                    return R.drawable.trophy_silver;
                default:
                case BRONZE:
                    return R.drawable.trophy_bronze;
            }
        }
    }

    // ONLY GETTERS & SETTERS BEYOND THIS POINT

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Game getGame() {
        return game;
    }

    public TrophyColor getColor() {
        return color;
    }

}
