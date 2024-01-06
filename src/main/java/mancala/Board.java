package mancala;
import java.util.ArrayList;

public class Board {
   
    private ArrayList<Pit> pits;
    private ArrayList<Store> stores;
    private Player player1;
    private Player player2;
    private boolean lastMoveisStore = false;
    private boolean lastMoveisEmpty = false;
    private int captureIndex = 0;

    public int getCaptureIndex() {
        return captureIndex;
    }

    public void resetCaptureIndex() {
        captureIndex = 0;
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

    public Board() {
        pits = new ArrayList<>();
        stores = new ArrayList<>();
    }

    public void setUpPits() {
        for (int i = 0; i < 12; i++) {
            pits.add(new Pit());
        }
    }

    public void setUpStores() {
        for (int i = 0; i < 2; i++) {
            stores.add(new Store());
        }
    }

    public void initializeBoard() {
        for (final Pit p: pits) {
            p.setStones(4);
        }
    }

    public void resetBoard() {
        for (final Pit p: pits) {
            p.setStones(4);
        }
        for (final Store s: stores) {
            s.removeStones();
        }
    }

    public void registerPlayers(Player one, Player two) {
        player1 = one;
        player2 = two;

        player1.setStore(stores.get(0));
        player2.setStore(stores.get(1));

        stores.get(0).setOwner(player1);
        stores.get(1).setOwner(player2);
    }

    public ArrayList<Pit> getPits() {
        return pits;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public int getNumStones(int pitNum) throws PitNotFoundException {
        if (pitNum > 12 || pitNum < 1) {
            throw new PitNotFoundException();
        } else {
            return pits.get(pitNum-1).getStoneCount();
        }
    }

    //default access modifier
    boolean isSideEmpty(final int pitNum) throws PitNotFoundException {
        boolean isEmpty = true;
        if (pitNum > 12 || pitNum < 1) {
            throw new PitNotFoundException();
        } else {
            if (pitNum >= 1 && pitNum <= 6) {
                for (int i = 0; i < 6; i++) {
                    if (pits.get(i).getStoneCount() != 0) {
                        isEmpty = false;
                        break;
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    if (pits.get(i+6).getStoneCount() != 0) {
                        isEmpty = false;
                        break;
                    }
                }
            }
        }
        return isEmpty;
    }

    //default access modifier
    int moveStones(final int startPit, final Player player) throws InvalidMoveException {
        if (startPit > 12 || startPit < 1) {
            throw new InvalidMoveException();
        }

        final Pit pit = pits.get(startPit-1);
        int stoneCount = pit.getStoneCount();

        if (stoneCount == 0) {
            throw new InvalidMoveException();
        }

        if (player.getName().equals(player1.getName()) && startPit >= 7 && startPit <= 12) {
            throw new InvalidMoveException();
        }

        if (player.getName().equals(player2.getName()) && startPit >= 1 && startPit <= 6) {
            throw new InvalidMoveException();
        }

        final int beforeStoreCount = player.getStoreCount();

        try {
            distributeStones(startPit);
        } catch(Exception e) {
            throw new InvalidMoveException();
        }

        final int afterStoreCount = player.getStoreCount();

        return afterStoreCount - beforeStoreCount;

    }

    //default access modifier
    int distributeStones(final int startingPoint) throws PitNotFoundException {

        if (startingPoint < 1 || startingPoint > 12) {
            throw new PitNotFoundException();
        }

        final Pit pit = pits.get(startingPoint-1);
        int stoneCount = pit.getStoneCount();

        String playerName;

        if (startingPoint >= 0 && startingPoint <= 5) {
            playerName = player1.getName();
        } else {
            playerName = player2.getName();
        }

        pit.removeStones();

        final int count = stoneCount;

        doMove(startingPoint, stoneCount, playerName);
        
        return count;
    }

    private void doMove(int startingPoint, int stoneCount, String playerName) {
        startingPoint--;
        if (startingPoint == 5)  {
            stores.get(0).addStones(1);
            stoneCount--;
            if (stoneCount == 0) {
                lastMoveisStore = true;
            }
        } 
        if (startingPoint == 11)  {
            stores.get(1).addStones(1);
            stoneCount--;
            if (stoneCount == 0) {
                lastMoveisStore = true;
            }
        } 
        for (int i = 1; i <= stoneCount; i++) {
            int currentStone = (startingPoint + i) % 12; 
            if (currentStone == 5 && startingPoint >= 0 && startingPoint <= 5 && i != stoneCount)  {
                stores.get(0).addStones(1);
                stoneCount--;
                if (i == stoneCount) {
                    lastMoveisStore = true;
                }
            } 
            if (currentStone == 11 && startingPoint >= 6 && startingPoint <= 11 && i != stoneCount)  {
                stores.get(1).addStones(1);
                stoneCount--;
                if (i == stoneCount) {
                    lastMoveisStore = true;
                }
            } 
            pits.get(currentStone).addStone();
        }
        if (!lastMoveisStore && pits.get(startingPoint + stoneCount).getStoneCount() == 1) {
            if (startingPoint + stoneCount >= 0 && startingPoint + stoneCount <= 5 && playerName.equals(player1.getName())) {
                lastMoveisEmpty = true;
                captureIndex = startingPoint + stoneCount + 1;
            } else if (startingPoint + stoneCount >= 6 && startingPoint + stoneCount <= 11) {
                if (playerName.equals(player2.getName())) {
                    lastMoveisEmpty = true;
                    captureIndex = startingPoint + stoneCount + 1;
                }
                
            }
            
        }
    }


    //default acess modifier
    int captureStones(final int stoppingPoint) throws PitNotFoundException {
        if (stoppingPoint > 12 || stoppingPoint < 1) {
            throw new PitNotFoundException();
        } else {
            final int index = stoppingPoint - 1;
            int oppositePitIndex = 11 - index;

            final int stones = pits.get(oppositePitIndex).removeStones();
            if (stoppingPoint >= 1 && stoppingPoint <= 6) {
                stores.get(0).addStones(stones + 1);
            } else {
                stores.get(1).addStones(stones + 1);
            }
            pits.get(index).removeStones();
            return stones + 1;
        }
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(player1.getName() + "\'s pits: ");
        for (int i = 0; i < 6; i++) {
            str.append(pits.get(i).getStoneCount() + " ");
        }
        str.append("\n" + stores.get(0).toString());
        str.append("\n" + player2.getName() + "\'s pits: ");
        for (int i = 6; i < 12; i++) {
            str.append(pits.get(i).getStoneCount() + " ");
        }
        str.append("\n" + stores.get(1).toString());
        return str.toString();
    }
}