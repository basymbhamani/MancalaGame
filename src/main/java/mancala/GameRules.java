package mancala;
import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable {
    private MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    boolean lastMoveisStore = false;
    boolean lastMoveisEmpty = false;
    int captureIndex = 0;


    /**
     * Constructor to initialize the game board.
     */

    public GameRules() {
        gameBoard = new MancalaDataStructure();
    }

    public int getPlayerNum() {
        return currentPlayer;
    }

    public void setLastMoveisStoreToFalse() {
        lastMoveisStore = false;
    }
    public boolean isLastMoveStore() {
        return lastMoveisStore;
    }

    public void setLastMoveisEmptyToFalse() {
        lastMoveisEmpty = false;
    }
    public boolean isLastMoveEmpty() {
        return lastMoveisEmpty;
    }

    
    

    public int getCaptureIndex() {
        return captureIndex;
    }

    public void resetCaptureIndex() {
        captureIndex = 0;
    }

    

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    public MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    boolean Empty(int pitNum) {
        boolean isEmpty = true;
        if (pitNum >= 1 && pitNum <= 6) {
            for (int i = 1; i <= 6; i++) {
                if (getDataStructure().getNumStones(i) != 0) {
                    isEmpty = false;
                    break;
                }
            }
        } else {
            for (int i = 1; i <= 6; i++) {
                if (getDataStructure().getNumStones(i) != 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(Player one, Player two) {
        // this method can be implemented in the abstract class.

        Store p1Store = new Store();
        Store p2Store = new Store();

        p1Store.setOwner(one);
        p2Store.setOwner(two);

        getDataStructure().setStore(p1Store, 1);
        getDataStructure().setStore(p1Store, 2);

        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();

        str.append("Player 1\'s pits: ");
        for (int i = 1; i <= 6; i++) {
            str.append(getDataStructure().getNumStones(i) + " ");
        }
        str.append("\nPlayer 1's Store: " + getDataStructure().getStoreCount(1));
        str.append("\n" + "Player 2\'s pits: ");
        for (int i = 7; i <= 12; i++) {
            str.append(getDataStructure().getNumStones(i) + " ");
        }
        str.append("\nPlayer 2's Store: " + getDataStructure().getStoreCount(2));
        return str.toString();
    }
}
