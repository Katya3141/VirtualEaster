package eggbonk.core;

/**
 * This class represents a game of Egg Bonk. 
 * @author ianbulovic
 */
public class Game {
	
	private Player[] players;
	private int currentPlayer;
	
	/**
	 * Creates a new game with a set of players.
	 * @param players
	 */
	public Game(Player[] players) {
		this.players = players;
		currentPlayer = 0;
	}
	
	public Player nextTurn() {
		int next = nextPlayer(currentPlayer);
		if(currentPlayer == next) return null; // only one player is still in
		
		Player winner = bonk(players[currentPlayer], players[next]);
		
		currentPlayer = players[next].isOut() ? nextPlayer(next) : next;
		
		return winner;
		
	}
	
	private int nextPlayer(int current) {
		int a = (current + 1) % players.length;
		while(players[a].isOut()) a = (a + 1) % players.length;
		return a;
	}
	
	/**
	 * Bonks the current eggs of two players. Returns the winning player.
	 * @param a the first player.
	 * @param b the second player.
	 * @return the player whose egg was not damaged as a result of this call.
	 */
	public static Player bonk(Player a, Player b) {
		if(a.currentEgg() == null) throw new IllegalArgumentException("player " + a + " has no eggs left to bonk!");
		if(b.currentEgg() == null) throw new IllegalArgumentException("player " + b + " has no eggs left to bonk!");
		int loser = (int)(Math.random() * 2);
		if(loser == 0) {
			a.currentEgg().crack();
			return b;
		} else {
			b.currentEgg().crack();
			return a;
		}
	} 
}
