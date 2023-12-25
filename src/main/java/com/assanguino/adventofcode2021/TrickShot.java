package com.assanguino.adventofcode2021;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

public class TrickShot implements Executable {

    protected Part part;

    protected static final String TARGET_AREA_DEFINITION = "target area";

    Coordinate targetBoundUp;                             // right & up target square
    Coordinate targetBoundBottom;                         // left & down target square
    boolean shotToTheRight;
    Map<Coordinate, Long> attemps = new HashMap<>();

    Coordinate finalVel = new Coordinate(-1, -1);
    long finalMaxHeight = -1;
    int amountVelocities = 0;

    public TrickShot(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        int indexX = row.indexOf("x=");
        int indexSeparator = row.indexOf(",");
        int indexY = row.indexOf("y=");

        String xInterval = row.substring(indexX + 2, indexSeparator);
        String yInterval = row.substring(indexY + 2);

        int indexDotX = xInterval.indexOf("..");
        var x0 = Integer.parseInt(xInterval.substring(0, indexDotX));
        var x1 = Integer.parseInt(xInterval.substring(indexDotX + 2));
        int indexDotY = yInterval.indexOf("..");
        var y0 = Integer.parseInt(yInterval.substring(0, indexDotY));
        var y1 = Integer.parseInt(yInterval.substring(indexDotY + 2));

        targetBoundUp = new Coordinate(Math.max(x0, x1), Math.max(y0, y1));
        targetBoundBottom = new Coordinate(Math.min(x0, x1), Math.min(y0, y1));

        // shot direction
        if (x0 > 0 && x1 > 0)
            shotToTheRight = true;
        else if (x0 < 0 && x1 < 0)
            shotToTheRight = false;
        else
            logger.info("The target zone is in the middle of the axis. So maximum height is infinite");
    }

    public void execute() {
        var xMax = targetBoundUp.x;
        var yMin = targetBoundBottom.y;

        logger.printf(Level.DEBUG, "TARGET ZONE - From bottom %s to up %s", targetBoundBottom.toString(), targetBoundUp.toString());
        logger.printf(Level.DEBUG, "SHOT ZONE   - X from %d to %d", 0, xMax);
        logger.printf(Level.DEBUG, "SHOT ZONE   - Y from %d to %d", yMin, -1*yMin);

        for(long i = 0; i < 2*xMax; i++) {
            for(long j = yMin; j < -1*yMin; j++) {
                executeOneShot(new Coordinate(i, j));
            }
        }

        // check all the successful attemps
        finalMaxHeight = -1;
        for(var a : attemps.entrySet()) {
            if(a.getValue() > finalMaxHeight) {
                finalMaxHeight = a.getValue();
                finalVel = a.getKey();
            }
        }

        amountVelocities = attemps.size();
    }

    protected ShotResults executeOneShot(Coordinate velInit) {

        Coordinate lastPosition = new Coordinate(0, 0);
        var velCurrent = new Coordinate(velInit);
        long maxHeight = Long.MIN_VALUE;

        ShotResults shotType;
        do {
            // Calculate new position
            var posCurrent = lastPosition;
            posCurrent.add(velCurrent);

            // Update maximum height
            maxHeight = Math.max(maxHeight, posCurrent.y);

            // Calculate new velocity
            velCurrent.updateVelocity();

            shotType = checkShot(posCurrent);

            lastPosition = posCurrent;

        } while(shotType == ShotResults.UNDEFINED);

        if(shotType == ShotResults.TARGETED) {
            attemps.put(velInit, maxHeight);
            logger.printf(Level.DEBUG, "Targeted with velocity %s to a max. height of %d", velInit.toString(), maxHeight);
        }

        return shotType;
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Trick Shot - What is the highest y position it reaches on this trajectory ?" : 
            "How many distinct initial velocity values cause the probe to be within the target area after any step ?";
    }

    public void printResult() {
        logger.info("");
        if (part == Part.FIRST) {
            logger.info("The final height is %d (coming from a velocity of %s)", finalMaxHeight, finalVel.toString());
        } else if(part == Part.SECOND) {
            logger.info("The amount of velocities that fit the target are %d", amountVelocities);
        }
        logger.info("");
    }

    public String getResult() {
        return (part == Part.FIRST) ? Long.toString(finalMaxHeight) : Integer.toString(amountVelocities);
    }

    protected ShotResults checkShot(Coordinate coord) {
        ShotResults result = ShotResults.UNDEFINED;

        boolean shortShotCondition = coord.y < targetBoundBottom.y;

        boolean longShotCondition = 
                (shotToTheRight && coord.x > targetBoundUp.x) || 
                (!shotToTheRight && coord.x < targetBoundBottom.x);

        boolean isTargeted = coord.x >= targetBoundBottom.x && 
                              coord.x <= targetBoundUp.x &&
                              coord.y >= targetBoundBottom.y && 
                              coord.y <= targetBoundUp.y;

        if(isTargeted) {
            result = ShotResults.TARGETED;
        } else if(shortShotCondition) {
            result = ShotResults.SHORT;
        } else if(longShotCondition) {
            result = ShotResults.LONG;
        }

        return result;
    }

    enum ShotResults {
        UNDEFINED,
        TARGETED,
        SHORT,
        LONG
    }

    class Coordinate {
        long x;
        long y;

        Coordinate(Coordinate coord) {
            this.x = coord.x;
            this.y = coord.y;
        }

        Coordinate(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public void add(Coordinate coord) {
            x += coord.x;
            y += coord.y;
        }

        public void updateVelocity() {
            x = Math.max(0, x - 1);
            y--;
        }

        @Override
        public String toString() {
            return String.format("(%4d,%4d)", x, y);
        }

    }

}
