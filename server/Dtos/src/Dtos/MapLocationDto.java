package Dtos;

import Engine.MapLocation;

public class MapLocationDto {
    private final int x;
    private final int y;

    public MapLocationDto(MapLocation location){
        this.x = location.getX();
        this.y = location.getY();
    }

    public MapLocationDto(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MapLocationDto){
            return this.x == ((MapLocationDto)obj).x && this.y == ((MapLocationDto)obj).y;
        }
        else {
            return false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "["+ x + "," + y + "]";
    }
}
