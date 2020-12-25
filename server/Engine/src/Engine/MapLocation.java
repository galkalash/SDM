package Engine;

import Dtos.MapLocationDto;
import org.w3c.dom.ranges.RangeException;

import java.util.Objects;

public class MapLocation {
    private static final int MAX_VALUE = 50;
    private static final int MIN_VALUE = 1;
    private int x;
    private int y;

    public MapLocation(int x, int y){
        if(x > MAX_VALUE || x<MIN_VALUE || y > MAX_VALUE || y <MIN_VALUE){
            throw new IllegalArgumentException("ERROR. location values should be between " + MIN_VALUE + " to " + MAX_VALUE);
        }
        this.x = x;
        this.y = y;
    }

    public MapLocation(MapLocationDto location){
        this.x = location.getX();
        this.y = location.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapLocation that = (MapLocation) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

