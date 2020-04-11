package eggbonk.core;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    
    public enum Phase
    {
        SETUP, TRANSITION, BONKING, VICTORY, TOTAL_VICTORY;
    }
    
    private Phase phase;
    private List<Player> players;
    private Player winner;
    private Player loser;
    
    public GameState(Phase phase, List<Player> players) {
        this.phase = phase;
        this.players = players;
        this.winner = null;
        this.loser = null;
    }
    
    public GameState(Phase phase, List<Player> players, Player winner, Player loser) {
        this.phase = phase;
        this.players = players;
        this.winner = winner;
        this.loser = loser;
    }
    
    public Phase getPhase() {
        return phase;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public Player getWinner() {
        return winner;
    }
    
    public Player getLoser() {
        return loser;
    }
}
