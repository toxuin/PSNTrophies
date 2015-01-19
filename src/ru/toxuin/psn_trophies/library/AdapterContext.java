package ru.toxuin.psn_trophies.library;

import ru.toxuin.psn_trophies.entities.Game;
import ru.toxuin.psn_trophies.entities.Trophy;

public class AdapterContext {
    private final Game game;
    private final boolean inGame;
    final boolean hasGames;
    final boolean hasTrophies;


    public AdapterContext(boolean hasTrophies, boolean hasGames, Game game) {
        if (game != null) {
            inGame = true;
            this.game = game;
        } else {
            inGame = false;
            this.game = null;
        }
        if (inGame) hasTrophies = true;

        this.hasGames = hasGames;
        this.hasTrophies = hasTrophies;
    }


    public Game getGame() {
        return game;
    }
}
