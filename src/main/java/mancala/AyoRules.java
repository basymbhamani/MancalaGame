package mancala;

public class AyoRules extends GameRules {

    private boolean repeat = false;
    private int finalPoint;

    public int getCaptureIndex() {
        return captureIndex;
    }

    public void resetCaptureIndex() {
        captureIndex = 0;
    }

    public int moveStones(final int startPit, int playerNum) throws InvalidMoveException {
        if (startPit > 12 || startPit < 1) {
            throw new InvalidMoveException();
        }

        int stoneCount = getDataStructure().getNumStones(startPit);

        if (stoneCount == 0) {
            throw new InvalidMoveException();
        }

        if (getPlayerNum() == 1 && startPit >= 7 && startPit <= 12) {
            throw new InvalidMoveException();
        }

        if (getPlayerNum() == 2 && startPit >= 1 && startPit <= 6) {
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

        int total = stoneCount;

        doMove(startingPoint, stoneCount, startingPoint);
        while (repeat) {
            stoneCount = getDataStructure().getNumStones(finalPoint);
            total += stoneCount;
            doMove(finalPoint, stoneCount, startingPoint);
        }
        
        
        return total;
    }

    private void doMove(int startingPoint, int stoneCount, int initialPoint) {
        startingPoint--;
        initialPoint--;
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

            if (currentStone == initialPoint) {
                stoneCount++;
            } else {
                getDataStructure().addStones(currentStone+1, 1);
            }
            
        }

        int finalIndex = ((startingPoint + stoneCount) % 12) + 1;
        if (!lastMoveisStore && getDataStructure().getNumStones(finalIndex) != 1) {
            repeat = true;
            finalPoint = finalIndex;
            return;
        } else {
            repeat = false;
        }

        if (!lastMoveisStore && getDataStructure().getNumStones(finalIndex) == 1) {
            if (finalIndex >= 1 && finalIndex <= 6 && initialPoint >= 1 && initialPoint <= 6) {
                lastMoveisEmpty = true;
                captureIndex = finalIndex;
            } else if (finalIndex >= 7 && finalIndex <= 12 && initialPoint >= 7 && initialPoint <= 12) {
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
            getDataStructure().addToStore(1, stones);
        } else {
            getDataStructure().addToStore(2, stones);
        }
        return stones;
        }
}
