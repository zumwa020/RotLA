package Board;

import Characters.Entity;
import Characters.Friendlies.Adventurer;
import Game.Engine;

import static Game.Engine.coordinateToKey;
import static Game.Engine.getAdventurers;

public class Render {

    // CONSTRUCTOR
    public Render() {
        turn = 0;
    }

    // PRIVATE ATTRIBUTES
    private int turn;

    // PUBLIC METHODS

    /*
     * Code example of Cohesion
     * All methods are relevant to Render class
     * since they are all print methods of the
     * game.
     */
    public void printFrame() {
        printTurn();
        printBoard();
        printStatus();
    }

    // PRIVATE METHODS
    // prints turn number
    private void printTurn() {
        System.out.println("RotLA Turn: " + turn);
        turn++;
    }

    // printBoard access the Map via get methods that
    // reference a stringg called a key.
    // then it calls render for adventurers or creatures found
    // in the room based on if the key matches the room key.
    private void printBoard() {
        System.out.println("+--------------------------------------------------+");
        System.out.println("| 0-1-1: " + Engine.Facility.get("011").renderOccupantAdventurers() + " |");
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (k < 2) {
                        System.out.print("| " + i
                                + "-"
                                + j + "-"
                                + k + ": "
                                + Engine.Facility.get(coordinateToKey(i, j, k)).renderOccupantAdventurers()
                                + " : "
                                + Engine.Facility.get(coordinateToKey(i, j, k)).renderOccupantCreatures()
                                + " ");
                    } else {
                        System.out.println("| " + i
                                + "-"
                                + j + "-"
                                + k + ": "
                                + Engine.Facility.get(coordinateToKey(i, j, k)).renderOccupantAdventurers()
                                + " : "
                                + Engine.Facility.get(coordinateToKey(i, j, k)).renderOccupantCreatures());
                    }
                }
            }
        }
        System.out.println("+--------------------------------------------------+");
    }

    // prints the health and treasure status of all entities
    /*
     * code example of Identity
     * Adventurer a = (Adventurer) a0
     * uses identity as a vehicle for printing proper
     * status
     */
    private void printStatus() {
        for (Entity a0 : getAdventurers()) {
            Adventurer a = (Adventurer) a0;
            System.out.println(a0.getName() + ":   \t" + a.getTreasureCount() + " Treasure(s) / " + (3 - a.getHealth())
                    + " Damage");
        }
        System.out.println("Orbiters:\t x Remaining");
        System.out.println("Seekers:\t x Remaining");
        System.out.println("Blinkers:\t x Remaining");
    }
}
