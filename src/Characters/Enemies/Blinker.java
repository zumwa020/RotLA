package Characters.Enemies;

import Board.Room;
import Characters.Enemies.Creature;
import Characters.Entity;

import java.util.Random;

import static Board.Room.inspectNeighbors;
import static Game.Engine.Facility;
import static Game.Engine.coordinateToKey;

public class Blinker extends Creature implements Entity {

    public Blinker(){
        sign = "B";
        name = "blinker";
    }

    @Override
    public void move() {
        // Blinker moves to any random room in the Facility.
        Random r = new Random();
        int floor = r.nextInt(3)+1;
        int x = r.nextInt(2);
        int y = r.nextInt(2);
        Room newRoom = Facility.get(coordinateToKey(floor,x,y));

        // Update knowledge of position.
        this.currentRoom.leaveRoom( this );
        this.setCurrentRoom( newRoom );
        newRoom.occupyCreature(this);
    }

    @Override
    public boolean fight(Entity target) {
        return false;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public Room checkRoom() {
        return currentRoom;
    }

    @Override
    public boolean rollForTreasure() {
        return false;
    }

    public String getName(){ return name; }
}
