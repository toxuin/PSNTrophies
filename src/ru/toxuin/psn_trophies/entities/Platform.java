package ru.toxuin.psn_trophies.entities;

public enum Platform {
    VITA, PS3, PS4;

    @Override
    public String toString() {
        switch (this) {
            case VITA:
                return "Vita";
            case PS3:
                return "PlayStation 3";
            default:
            case PS4:
                return "PlayStation 4";
        }
    }

    public int getServerInt() {
        switch (this) {
            case VITA:
                return 0;
            case PS3:
                return 1;
            default:
            case PS4:
                return 2;
        }
    }

    public static Platform getPlatformByServerInt(int serverInt) {
        for (int i = 0; i < Platform.values().length; i++) {
            if (Platform.values()[i].getServerInt() == serverInt) return Platform.values()[i];
        }
        return null;
    }
}
