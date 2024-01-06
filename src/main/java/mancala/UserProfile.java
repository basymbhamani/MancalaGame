package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable{
    String name;
    int kalahWon;
    int ayoWon;
    int kalahPlayed;
    int ayoPlayed;

    public UserProfile(String userName) {
        this.name = userName;
        kalahWon = 0;
        ayoWon = 0;
        kalahPlayed = 0;
        ayoPlayed = 0;
    }

    public String getUserName() {
        return name;
    }

    public int getKalahGamesPlayed() {
        return kalahPlayed;
    }

    public void incrementKalahGamesPlayed() {
        kalahPlayed++;
    }

    public int getAyoGamesPlayed() {
        return ayoPlayed;
    }

    public void incrementAyoGamesPlayed() {
        ayoPlayed++;
    }

    public int getKalahGamesWon() {
        return kalahWon;
    }

    public void incrementKalahGamesWon() {
        kalahWon++;
    }

    public int getAyoGamesWon() {
        return ayoWon;
    }

    public void incrementAyoGamesWon() {
        ayoWon++;
    }
}