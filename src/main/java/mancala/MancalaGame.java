package mancala;

import java.io.Serializable;
import java.util.ArrayList;

public class MancalaGame implements Serializable {
    private Player player1;
    private Player player2;
    private final ArrayList<Player> players;
    private Player currentPlayer;
    private int currentPlayerNum;
    private GameRules board;
    private boolean isOver;

    public MancalaGame() {
        isOver = false;
        players = new ArrayList<>();
    }

    public void setPlayers(final Player onePlayer, final Player twoPlayer) {
        player1 = onePlayer;
        player2 = twoPlayer;
        currentPlayer = player1;
        currentPlayerNum = 1;
        players.add(player1);
        players.add(player2);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public void setCurrentPlayer(final Player player) {
        currentPlayer = player;
    }

    public void setBoard(GameRules theBoard) {
        board = theBoard;
    }


    public GameRules getBoard() {
        return board;
    }

    public int getNumStones(final int pitNum) throws PitNotFoundException {
        if (pitNum > 12 || pitNum < 1) {
            throw new PitNotFoundException();
        } 
        return board.getNumStones(pitNum);
    }

    private void switchPlayers() {
        if (currentPlayer.equals(player1)) {
                currentPlayer = player2;
                currentPlayerNum = 2;
        } else {
            currentPlayer = player1;
            currentPlayerNum = 1;
        }
    }
  
    private int getP1TotalStones() {
        int sum = 0;
        for (int i = 1; i <= 6; i++) {
            sum += board.getDataStructure().getNumStones(i);
        }
        return sum;
    }

    private int getP2TotalStones() {
        int sum = 0;
        for (int i = 6; i < 12; i++) {
            sum += board.getDataStructure().getNumStones(i);
        }
        return sum;
    }
    public int move(int startPit) throws InvalidMoveException { 
        try {
            board.moveStones(startPit, currentPlayerNum);
        } catch (Exception e) {
            throw new InvalidMoveException();
        }
        int sum;
        int sum1 = getP1TotalStones();
        int sum2 = getP2TotalStones();
        if (currentPlayer.equals(player1)) {
            sum = sum1;
        } else {
            sum = sum2;
        }
        if (sum1 == 0 || sum2 == 0) {
            isOver = true;
        }
        if (board.isLastMoveStore()) {
            board.setLastMoveisStoreToFalse();
            if (board instanceof AyoRules) {
                switchPlayers();
            }
        } else if (board.isLastMoveEmpty()) {
            board.setLastMoveisEmptyToFalse();
            board.captureStones(board.getCaptureIndex());
            board.resetCaptureIndex();
            sum1 = getP1TotalStones();
            sum2 = getP2TotalStones();
            if (currentPlayer.equals(player1)) {
                sum = sum1;
            } else {
                sum = sum2;
            }
            if (sum1 == 0 || sum2 == 0) {
                isOver = true;
            }

            if (sum1 == 0) {
                player2.getStore().addStones(sum2);
            }

            if (sum2 == 0) {
                player1.getStore().addStones(sum1);
            }
            switchPlayers();
        } else {
            switchPlayers();
        }
        return sum;
        
    }

    public int getStoreCount(final Player player) throws NoSuchPlayerException {
        if (getPlayers().contains(player)) {
            return player.getStoreCount();
        } else {
           throw new NoSuchPlayerException();
        }
        
    }

   
    public Player getWinner() throws GameNotOverException {
        Player winner = null;
        if (!isOver) {
            throw new GameNotOverException();
        }


        if (player1.getStoreCount() > player2.getStoreCount()) {
            winner = player1;
        } else if (player2.getStoreCount() > player1.getStoreCount()) {
            winner = player2;
        } 

        return winner;
       
    }

    public boolean isGameOver() {
        return isOver;
    }

    public void startNewGame() {
        board.resetBoard();
        this.currentPlayer = player1;
        isOver = false;
    }

    @Override
    public String toString() {
        return board.toString(); 
    }
}

