package Game;

import java.util.*;

import Board.Logger;
import Board.Render;
import Board.Room;
import Board.Tracker;
import Characters.Entity;
import Characters.Enemies.*;
import Characters.Friendlies.*;
import Characters.Subject;
import Treasure.*;

import static Board.Room.mapNeighborhood;

public class Engine {


    // CONSTRUCTORS
    Engine() {
        Facility = new HashMap<>();
        Adventurers = new ArrayList<>();
        Creatures = new ArrayList<>();
        Treasures = new ArrayList<>();
        Tracker T = new Tracker();
        view = new Render();
    }

    // PUBLIC ATTRIBUTES
    // Game Board
    public static Map<String, Room> Facility;

    // PRIVATE ATTRIBUTES
    // Characters
    private static ArrayList<Entity> Adventurers;
    private static ArrayList<Entity> Creatures;
    private static ArrayList<Treasure> Treasures;
    private Render view;

    // PUBLIC METHODS
    public static ArrayList<Entity> getAdventurers() {
        return Adventurers;
    }

    public static ArrayList<Entity> getCreatures() {
        return Creatures;
    }

    public void initialize() {
        // Instantiate Adventurers
        initializeAdventurers();
        // Instantiate Creatures
        initializeCreatures();
        // Instantiate Treasures
        initializeTreasures();
        // Instantiate Game Board
        createBlankBoard();
        initializeBoard();
        mapNeighborhood();
    }

    public static String coordinateToKey(int floor, int x, int y) {
        return String.valueOf(floor) + String.valueOf(x) + String.valueOf(y);
    }

    public void processAdventurers(Logger recorder) {
        for (Entity player : Adventurers) {

            // First and foremost, alive check
            if (((Adventurer) player).getAlive()) {
                // TODO: clean this up!!
                // Runner gets 2 "actions" per turn.
                if ("runner".equals(player.getName())) {
                    // Get current room
                    Room thisRoom = player.checkRoom();
                    for (int i = 0; i < 2; i++) {
                        recorder.activate((Subject) player);
                        // Combat check, must be done before treasure check.
                        if (thisRoom.getOccupantCreatures().size() > 0) {
                            ArrayList<Entity> mobsToKill = new ArrayList<>();
                            for (Creature target : thisRoom.getOccupantCreatures()) {
                                if (player.fight((Entity) target)) {
                                    if (player.fight((Entity) target)) {
                                        // kill target creature on player victory (i.e. remove from list)
                                        mobsToKill.add((Entity) target);
                                        target.notifyObservers("died");
                                    }
                                }
                            }
                            for (Entity k : mobsToKill) {
                                k.checkRoom().leaveRoom(k);
                                Creatures.remove(k);
                            }
                            // Combat consumes the turn.
                            recorder.deactivate((Subject) player);
                            continue;
                        }
                        // Treasure check.
                        if (thisRoom.checkIfTreasure()) {
                            if (((Adventurer) player).search()) {
                                ((Adventurer) player).collectTreasure(thisRoom);
                                // Collection consumes the turn.
                                recorder.deactivate((Subject) player);
                                continue;
                            }
                        }
                        // No treasure and no creatures, move on!
                        player.move();
                        // Final creature-combat check after final movement.
                        if (thisRoom.getOccupantCreatures().size() > 0) {
                            ArrayList<Entity> mobsToKill = new ArrayList<>();
                            for (Creature target : thisRoom.getOccupantCreatures()) {
                                if (player.fight((Entity) target)) {
                                    // kill target creature on player victory (i.e. remove from list)
                                    mobsToKill.add((Entity) target);
                                    target.notifyObservers("died");
                                }
                            }
                            for (Entity k : mobsToKill) {
                                k.checkRoom().leaveRoom(k);
                                Creatures.remove(k);
                            }

                            // Combat consumes the turn.
                            recorder.deactivate((Subject) player);
                        }
                        // End of turn, finally..
                        recorder.deactivate((Subject) player);
                    }
                } else {
                    // Everyone else only gets once chance for the song and dance.
                    recorder.activate((Subject) player);

                    // Get current room
                    Room thisRoom = player.checkRoom();

                    // Combat check, must be done before treasure check.
                    if (thisRoom.getOccupantCreatures().size() > 0) {
                        ArrayList<Entity> mobsToKill = new ArrayList<>();
                        for (Creature target : thisRoom.getOccupantCreatures()) {
                            if (player.fight((Entity) target)) {
                                // kill target creature on player victory (i.e. remove from list)
                                mobsToKill.add((Entity) target);
                                target.notifyObservers("died");
                            }
                        }
                        for (Entity k : mobsToKill) {
                            k.checkRoom().leaveRoom(k);
                            Creatures.remove(k);
                        }

                        // Combat consumes the turn.
                        recorder.deactivate((Subject) player);
                        continue;
                    }

                    // TODO: Add checks to confirm adventurer is still alive after each action

                    // Treasure check.
                    if (thisRoom.checkIfTreasure()) {
//                    ((Adventurer) player).search();
                        if (((Adventurer) player).search()) {
                            // Treasure found consumes the turn.
                            recorder.deactivate((Subject) player);
                            continue;
                        } else {
                            // Treasure not found.
                            // tracker.publishTreasureFound(Treasure);
                        }
                    }

                    // No treasure and no creatures, move on!
                    player.move();

                    // TODO: In the current structure of treasure, if an adv discovers a treasure
                    // they already own, they will
                    // TODO: not retrieve the item. This leaves it a static item belonging to the
                    // room, so there is a
                    // TODO: potential of an infinite loop of an adventurer trying to recover a
                    // treasure they can't obtain.
                    // TODO: This can be resolved by forcing the adv to ALWAYS move FIRST before
                    // SEARCHING!

                    // Final creature-combat check after final movement.
                    if (thisRoom.getOccupantCreatures().size() > 0) {
                        ArrayList<Entity> mobsToKill = new ArrayList<>();
                        for (Creature target : thisRoom.getOccupantCreatures()) {
                            if (player.fight((Entity) target)) {
                                // kill target creature on player victory (i.e. remove from list)
                                mobsToKill.add((Entity) target);
                                target.notifyObservers("died");
                            }
                        }
                        for (Entity k : mobsToKill) {
                            k.checkRoom().leaveRoom(k);
                            Creatures.remove(k);
                        }

                        // Combat consumes the turn.
                        recorder.deactivate((Subject) player);
                        continue;
                    }

                    // End of turn, finally..
                    recorder.deactivate((Subject) player);
                }
            }
        }
    }

    public void processCreatures(Logger recorder) {
        ArrayList<Entity> mobsToDie = new ArrayList<>();
        for (Entity monster : Creatures) {

            // Top of turn, BAE
            recorder.activate((Subject) monster);

            // Get current room.
            Room thisRoom = monster.checkRoom();

            // If no Adventurers to fight, move on.
            if (thisRoom.getOccupantAdventurers().size() == 0) {
                monster.move();
            }

            // After either moving or not, fight any Adventurers in the room.
            for (Adventurer target : thisRoom.getOccupantAdventurers()) {
                if (monster.fight((Entity) target)) {
                    // Monster wins! Report event
                } else {
                    // Monster lost, marked for death
                    mobsToDie.add(monster);
                }
            }

            // End of turn, finally
            recorder.deactivate((Subject) monster);
        }
        for (Entity m : mobsToDie) {
            m.checkRoom().leaveRoom(m);
            Creatures.remove(m);
        }
    }

    public boolean endConditionMet() {

        // Treasure Victory Check
        int totalTreasure = 0;
        for (Entity a : Adventurers) {
            totalTreasure += ((Adventurer) a).getTreasureCount();
        }
        if (totalTreasure >= 10) {
            System.out.println("\n\nADVENTURERS WIN!\n\tThey collected: " + totalTreasure + " treasure!\n\n\tGG!");
            return true;
        }

        // Dead Creatures Victory Check
        if (Creatures.size() == 0) {
            System.out.println("\n\nADVENTURERS WIN!\n\tThey defeated every creature!\n\n\tGG!");
            return true;
        }

        // Dead Adventurers Victory Check
        int totalHealth = 0;
        for (Entity a : Adventurers) {
            totalHealth += ((Adventurer) a).getHealth();
        }
        if (totalHealth == 0) {
            System.out.println("\n\nCREATURES WIN!\n\tThey defeated every adventurer!\n\n\tGG!");
            return true;
        }

        // No Victory Condition
        return false;
    }

    // PRIVATE METHODS
    private void createBlankBoard() {
        // Spawn Room
        Facility.put("011", new Room(0, 1, 1));
        // Dungeon
        for (int i = 1; i < 5; ++i) {
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    Facility.put(coordinateToKey(i, j, k), new Room(i, j, k));
                }
            }
        }
    }

    private void initializeBoard() {
        Characters.Enemies.Orbiter O = new Orbiter();

        // Place all adventurers in the adventurer spawn room.
        for (Entity a0 : Adventurers) {
            Adventurer a = (Adventurer) a0;
            Facility.get("011").occupyAdventurer(a);
            a.setCurrentRoom(Facility.get("011"));
        }

        // Place all creatures in random rooms.
        Random r = new Random();
        for (Entity c0 : Creatures) {
            Creature c = (Creature) c0;
            int x, y, floor;

            // Selecting floor and position for a creature to spawn, excluding adventurer
            // spawn room.
            x = r.nextInt(2);
            y = r.nextInt(2);
            floor = r.nextInt(3) + 1;

            // Condition for which an orbiter can legally spawn, as they must avoid center
            // rooms.
            if ("O".equals(c.getSign())) {
                while (x == 1 && y == 1) {
                    x = r.nextInt(2);
                    y = r.nextInt(2);
                }
            }

            // Place creature in room.
            Facility.get(coordinateToKey(floor, x, y)).occupyCreature(c);
            c.setCurrentRoom(Facility.get(coordinateToKey(floor, x, y)));
        }

        // Place all Treasures in random rooms.
        Random randomTreasure = new Random();
        for (Treasure t : Treasures) {
            int a, b, level;

            // Selecting floor and position for a creature to spawn, excluding adventurer
            // spawn room.
            //while(totalTreasuresPlaced < 24)
            do {
                level = randomTreasure.nextInt(4) + 1;
                a = randomTreasure.nextInt(3);
                b = randomTreasure.nextInt(3);
                if( !Facility.get(coordinateToKey(level, a, b)).checkIfTreasure() ){
                    Facility.get(coordinateToKey(level, a, b)).occupyTreasure(t);
                    break;
                }
            } while(true);
            // Place creature in room.
        }
    }

    private void initializeAdventurers() {
        Adventurers.add(new Brawler());
        Adventurers.add(new Sneaker());
        Adventurers.add(new Runner());
        Adventurers.add(new Thief());
    }

    private void initializeCreatures() {
        for (int i = 0; i < 4; ++i) {
            Creatures.add(new Orbiter());
            Creatures.add(new Seeker());
            Creatures.add(new Blinker());
        }
    }

    private void initializeTreasures() {
        for (int i = 0; i < 4; ++i) {
            Treasures.add(new Armor());
            Treasures.add(new Gem());
            Treasures.add(new Portal());
            Treasures.add(new Potion());
            Treasures.add(new Sword());
            Treasures.add(new Trap());
        }
    }

    public void runOneTurn() {
        Logger recorder = new Logger();
        Tracker printer = new Tracker();
        processAdventurers(recorder);
        processCreatures(recorder);
        view.printFrame();
        recorder.closeFrame(view.getTurn());
    }
}
