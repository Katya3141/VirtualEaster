package eggbonk.core;

import java.util.List;

/**
 * This class represents a game of Egg Bonk. 
 * @author ianbulovic
 */
public class Game {
	
	private List<Player> players;
	private int currentPlayer;
	
	/**
	 * Creates a new game with a set of players.
	 * @param players
	 */
	public Game(List<Player> players) {
		this.players = players;
		currentPlayer = 0;
	}
	
	public Player nextTurn() {
		int next = (currentPlayer + 1) % players.size();
		if(currentPlayer == next) return null; // only one player is still in
		
		Player player1 = players.get(currentPlayer), player2 = players.get(next);
		
		Player winner = bonk(player1, player2);
		
		return winner;
		
	}
	
	private Player loser;
	
	/**
	 * Bonks the current eggs of two players. Returns the winning player.
	 * @param a the first player.
	 * @param b the second player.
	 * @return the player whose egg was not damaged as a result of this call.
	 */
	public Player bonk(Player a, Player b) {
		if(a.currentEgg() == null) throw new IllegalArgumentException("player " + a + " has no eggs left to bonk!");
		if(b.currentEgg() == null) throw new IllegalArgumentException("player " + b + " has no eggs left to bonk!");
		int loserIdx = (int)(Math.random() * 2);
		if(loserIdx == 0) {
			this.loser = a;
			return b;
		} else {
			this.loser = b;
			return a;
		}
	} 
	
	public Player getLoser() {
		return loser;
	}
	
	public void removeExtraneousPlayers() {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).isOut()) {
				players.remove(i);
				currentPlayer = (currentPlayer + 1) % players.size();
				i--;
			}
		}
	}
}
