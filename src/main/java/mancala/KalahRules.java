package mancala;

public class KalahRules extends GameRules {

    public int moveStones(final int startPit, int playerNum) throws InvalidMoveException {
        

        if (startPit > 12 || startPit < 1) {
            throw new InvalidMoveException();
        }
        int stoneCount = getDataStructure().getNumStones(startPit);

        if (stoneCount == 0) {
            throw new InvalidMoveException();
        }

        if (playerNum == 1 && startPit >= 7 && startPit <= 12) {
            throw new InvalidMoveException();
        }

        if (playerNum == 2 && startPit >= 1 && startPit <= 6) {
            throw new InvalidMoveException();
        }
        final int beforeStoreCount = getDataStructure().getStoreCount(playerNum);

        distributeStones(startPit);

        final int afterStoreCount = getDataStructure().getStoreCount(playerNum);

        return afterStoreCount - beforeStoreCount;

    }

    public int distributeStones(final int startingPoint) {

        int stoneCount = getDataStructure().getNumStones(startingPoint);
        getDataStructure().removeStones(startingPoint);


        final int count = stoneCount;

        doMove(startingPoint, stoneCount);
        
        return count;
    }

    private void doMove(int startingPoint, int stoneCount) {
        final int intialPoint = startingPoint;
        startingPoint--;
        if (startingPoint == 5)  {
            getDataStructure().addToStore(1,1);
            stoneCount--;
            if (stoneCount == 0) {
                lastMoveisStore = true;
            }
        } 
        if (startingPoint == 11)  {
            getDataStructure().addToStore(2,1);
            stoneCount--;
            if (stoneCount == 0) {
                lastMoveisStore = true;
            }
        }
        for (int i = 1; i <= stoneCount; i++) {
            int currentStone = (startingPoint + i) % 12; 
            if (currentStone == 5 && startingPoint >= 0 && startingPoint <= 5 && i != stoneCount)  {
                getDataStructure().addToStore(1,1);
                stoneCount--;
                if (i == stoneCount) {
                    lastMoveisStore = true;
                }
            } 
            if (currentStone == 11 && startingPoint >= 6 && startingPoint <= 11 && i != stoneCount)  {
                getDataStructure().addToStore(2,1);
                stoneCount--;
                if (i == stoneCount) {
                    lastMoveisStore = true;
                }
            } 
            getDataStructure().addStones(currentStone+1, 1);
        }

        int finalIndex = ((startingPoint + stoneCount) % 12) + 1;
        if (!lastMoveisStore && getDataStructure().getNumStones(finalIndex) == 1) {
            if (finalIndex >= 1 && finalIndex <= 6 && intialPoint >= 1 && intialPoint <= 6) {
                lastMoveisEmpty = true;
                captureIndex = finalIndex;
            } else if (finalIndex >= 7 && finalIndex <= 12 && intialPoint >= 7 && intialPoint <= 12) {
                if (getPlayerNum() == 2) {
                    lastMoveisEmpty = true;
                    captureIndex = finalIndex;
                }
                
            }
        }
    }

    int captureStones(final int stoppingPoint) {
        final int index = stoppingPoint - 1;
        int oppositePitIndex = 11 - index;

        final int stones = getDataStructure().removeStones(oppositePitIndex + 1);
        if (stoppingPoint >= 1 && stoppingPoint <= 6) {
            getDataStructure().addToStore(1, stones + 1);
        } else {
            getDataStructure().addToStore(2, stones + 1);
        }
        getDataStructure().removeStones(index + 1);
        return stones + 1;
        }
}
