package com.assanguino.adventofcode2021;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

public class TrickShot implements Executable {

    protected Part part;

    protected final String target_area_definition = "target area";

    Coordinate target_bound_up;                             // right & up target square
    Coordinate target_bound_bottom;                         // left & down target square
    boolean shot_to_the_right;
    Map<Coordinate, Long> attemps = new HashMap<>();

    Coordinate final_vel = new Coordinate(-1, -1);
    long final_max_height = -1;
    int amount_velocities = 0;

    public TrickShot(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        int index_x = row.indexOf("x=");
        int index_separator = row.indexOf(",");
        int index_y = row.indexOf("y=");

        String x_interval = row.substring(index_x + 2, index_separator);
        String y_interval = row.substring(index_y + 2);

        int index_dot_x = x_interval.indexOf("..");
        var x0 = Integer.parseInt(x_interval.substring(0, index_dot_x));
        var x1 = Integer.parseInt(x_interval.substring(index_dot_x + 2));
        int index_dot_y = y_interval.indexOf("..");
        var y0 = Integer.parseInt(y_interval.substring(0, index_dot_y));
        var y1 = Integer.parseInt(y_interval.substring(index_dot_y + 2));

        target_bound_up = new Coordinate(Math.max(x0, x1), Math.max(y0, y1));
        target_bound_bottom = new Coordinate(Math.min(x0, x1), Math.min(y0, y1));

        // shot direction
        if (x0 > 0 && x1 > 0)
            shot_to_the_right = true;
        else if (x0 < 0 && x1 < 0)
            shot_to_the_right = false;
        else
            logger.printf(Level.INFO, "The target zone is in the middle of the axis. So maximum height is infinite");
    }

    public void execute() {
        var xMax = target_bound_up.X;
        var yMin = target_bound_bottom.Y;

        logger.printf(Level.DEBUG, "TARGET ZONE - From bottom %s to up %s", target_bound_bottom.toString(), target_bound_up.toString());
        logger.printf(Level.DEBUG, "SHOT ZONE   - X from %d to %d", 0, xMax);
        logger.printf(Level.DEBUG, "SHOT ZONE   - Y from %d to %d", yMin, -1*yMin);

        for(long i = 0; i < 2*xMax; i++) {
            for(long j = yMin; j < -1*yMin; j++) {
                executeOneShot(new Coordinate(i, j));
            }
        }

        // check all the successful attemps
        final_max_height = -1;
        for(var a : attemps.entrySet()) {
            if(a.getValue() > final_max_height) {
                final_max_height = a.getValue();
                final_vel = a.getKey();
            }
        }

        amount_velocities = attemps.size();
    }

    protected ShotResults executeOneShot(Coordinate vel_init) {

        Coordinate last_position = new Coordinate(0, 0);
        var vel_current = new Coordinate(vel_init);
        long max_height = Long.MIN_VALUE;

        ShotResults shot_type = ShotResults.Undefined;
        do {
            // Calculate new position
            var pos_current = last_position;
            pos_current.add(vel_current);

            // Update maximum height
            max_height = Math.max(max_height, pos_current.Y);

            // Calculate new velocity
            vel_current.updateVelocity();

            shot_type = checkShot(pos_current);

            last_position = pos_current;

        } while(shot_type == ShotResults.Undefined);

        if(shot_type == ShotResults.Targeted) {
            attemps.put(vel_init, max_height);
            logger.printf(Level.DEBUG, "Targeted with velocity %s to a max. height of %d", vel_init.toString(), max_height);
        }

        return shot_type;
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Trick Shot - What is the highest y position it reaches on this trajectory ?" : 
            "How many distinct initial velocity values cause the probe to be within the target area after any step ?";
    }

    public void printResult() {
        System.out.println("");
        if (part == Part.first) {
            System.out.println(String.format("The final height is %d (coming from a velocity of %s", final_max_height, final_vel.toString()));
        } else if(part == Part.second) {
            System.out.println(String.format("The amount of velocities that fit the target are %d", amount_velocities));
        }
        System.out.println("");
    }

    public String getResult() {
        return (part == Part.first) ? Long.toString(final_max_height) : Integer.toString(amount_velocities);
    }

    protected ShotResults checkShot(Coordinate coord) {
        ShotResults result = ShotResults.Undefined;

        boolean short_shot_condition = coord.Y < target_bound_bottom.Y;

        boolean long_shot_condition = 
                (shot_to_the_right && coord.X > target_bound_up.X) || 
                (!shot_to_the_right && coord.X < target_bound_bottom.X);

        boolean is_targeted = coord.X >= target_bound_bottom.X && 
                              coord.X <= target_bound_up.X &&
                              coord.Y >= target_bound_bottom.Y && 
                              coord.Y <= target_bound_up.Y;

        if(is_targeted) {
            result = ShotResults.Targeted;
        } else if(short_shot_condition) {
            result = ShotResults.Short;
        } else if(long_shot_condition) {
            result = ShotResults.Long;
        }

        return result;
    }

    enum ShotResults {
        Undefined,
        Targeted,
        Short,
        Long
    }

    class Coordinate {
        long X;
        long Y;

        Coordinate(Coordinate coord) {
            this.X = coord.X;
            this.Y = coord.Y;
        }

        Coordinate(long x, long y) {
            this.X = x;
            this.Y = y;
        }

        public void add(Coordinate coord) {
            X += coord.X;
            Y += coord.Y;
        }

        public void updateVelocity() {
            X = Math.max(0, X - 1);
            Y--;
        }

        @Override
        public String toString() {
            return String.format("(%4d,%4d)", X, Y);
        }

    }

}
