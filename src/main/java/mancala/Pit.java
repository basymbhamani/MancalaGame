package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable{
    
    private int stoneCount;

    Pit() {
        this(0);
    }
    //default access modifier
    Pit(int stones) {
        if (stones > 0) {
            stoneCount = stones;
        } else {
            stoneCount = 0;
        }
    }

    //default access modifier
    public int getStoneCount() {
        return stoneCount;
    }

    //default access modifier
    public void addStone() {
        stoneCount++;
    }

    public void addStones(int numToAdd) {
        stoneCount += numToAdd;
    }

    //default access modifier
    void setStones(int stones) {
        if (stones > 0) {
            stoneCount = stones;
        } else {
            stoneCount = 0;
        }
    }
    //default access modifier
    public int removeStones() {
        final int numStones = stoneCount;
        stoneCount = 0;
        return numStones;
    }

    @Override
    public String toString() {
        return "Pit: " + stoneCount + " stones";
    }
}
