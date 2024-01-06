package mancala;

import java.io.Serializable;

public class Store implements Serializable, Countable { 
    private Player owner;
    private int numStones;

    public Store() {
    }


    //default access modifier
    void setOwner(final Player player) {
        this.owner = player;
    }

    //default access modifier
    Player getOwner() {
        return owner;
    }

    //default access modifier
    public void addStones(int amount) {
        this.numStones += amount;
    }

    //default acesss modifier
    public int getStoneCount() {
        return numStones;
    }

    public void addStone() {
        this.numStones++;
    }

    public int removeStones() {
        final int stonesInStore = numStones;
        this.numStones = 0;
        return stonesInStore;
    }
    
    @Override
    public String toString() {
        return owner.getName() + "'s store: " + numStones + " stone(s)";
    }
}