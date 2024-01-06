package mancala;

import java.io.Serializable;

public class Player implements Serializable {
  
    private String playerName;
    private Store playerStore;
    private UserProfile profile;

    public Player() {
        this("");
    }

    public Player(UserProfile userProfile) {
        this.profile = userProfile;
        playerName = userProfile.getUserName();
    }

    public Player(final String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }

    public Store getStore() {
        return playerStore;
    }

    public void setName(final String name) {
        playerName = name;
    }


    //default access modifier
    void setStore(Store store) {
        playerStore = store;
    }


    //default access modifier
    int getStoreCount() {
        int storeCount;
        if (playerStore == null) {
            storeCount =  0;
        } else {
            storeCount =  playerStore.getStoneCount();
        }
        return storeCount;
    }

    @Override
    public String toString() {
        return "Player: " + playerName + " has " + getStoreCount() + "stones in store";
    }

}