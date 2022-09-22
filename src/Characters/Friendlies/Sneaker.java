package Characters.Friendlies;

import Board.Room;
import Characters.Entity;

import java.util.Random;

import static Board.Room.inspectNeighbors;
import static Game.Engine.Facility;
import static Utilities.Dice.rollD6;

/*
 * code example of Inheritance
 * all of the adventurer subclasses
 * inherit the superclass Adventurer
 */
public class Sneaker extends Adventurer implements Entity {

    // CONSTRUCTORS
    public Sneaker() {
        sign = "S";
        name = "Sneaker";
        health = 3;
        alive = true;
    }

    // PUBLIC METHODS
    @Override
    public boolean fight(Entity target) {
        if(health <= 0){
            // do nothing
        }
        else {
            Random r = new Random();
            if (r.nextBoolean()) {
                int myRoll = rollD6(2);
                int targetRoll = rollD6(2);

                if (myRoll > targetRoll) {
                    // Victory
                    return true;
                    // target.die();
                } else if (myRoll == targetRoll) {
                    // Tie
                    return false;
                } else {
                    // Loss
                    this.takeDamage();
                }
            }
        }
        return false;
    }

    @Override
    public void move() {
        if(health <= 0){
            // do nothing
        }
        else {
            // check room to return valid moves
            String[] addresses = inspectNeighbors(this.currentRoom);
            // randomly select a valid move from that list
            int choice;
            if (addresses.length <= 1) {
                choice = 0;
            } else {
                Random r = new Random();
                choice = r.nextInt(0, addresses.length);
            }
            Room newRoom = Facility.get(addresses[choice]);

            // finally:
            this.currentRoom.leaveRoom(this);
            this.setCurrentRoom(newRoom);
            newRoom.occupyAdventurer(this);
        }
    }

    @Override
    public Room checkRoom() {
        return currentRoom;
    }

    @Override
    public boolean rollForTreasure() {
        if (rollD6(2) >= 10) {
            return true;
        }
        return false;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }
}
