package Characters.Friendlies;

import Board.Observer;
import Board.Room;
import Characters.Action.Combat.stealth;
import Characters.Action.Move.movement;
import Characters.Entity;
import Characters.Action.Search.quick;
import Characters.Subject;
import Treasure.Treasure;

import java.util.ArrayList;
import java.util.Random;

import static Board.Room.inspectNeighbors;
import static Game.Engine.Facility;

/*
 * code example of Inheritance
 * all of the adventurer subclasses
 * inherit the superclass Adventurer
 */
public class Sneaker extends Adventurer implements Entity, Subject {

    // CONSTRUCTORS
    public Sneaker() {
        entityType = "adventurer";
        sign = "S";
        name = "Sneaker";
        health = 3;
        alive = true;
        combatStyle = new stealth();
        searchStyle = new quick();
        moveStyle = new movement();
        offenseBonus = 0;
        defenseBonus = 0;
        inventory = new ArrayList<>();
        observerList = new ArrayList<>();
    }

    // PUBLIC METHODS
    public boolean fight(Entity target) {
        int fightVal = combatStyle.fight(this, target);
        if( fightVal > 0 ){
            notifyObservers("wonCombat");
            return true;
        } else if (fightVal < 0) {
            this.takeDamage(1);
            notifyObservers("lostCombat");
        }
        return false;
    }

    public boolean search(){
        Treasure obtained = searchStyle.search(this,this.currentRoom);
        if(obtained!=null){
            if( obtained.getClass().getSimpleName().equals("Trap") ){
                this.takeDamage(1);
            }
            else{
                obtained.activate(this);
                this.inventory.add(obtained);
                notifyObservers("treasureFound");
            }
            return true;
        }
        return false;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        notifyObservers("tookDamage");
        if(this.health <= 0){
            notifyObservers("died");
            this.alive = false;
        }
    }

    @Override
    public void move() {
        if(this.getAlive()) {
            moveStyle.move(this);
        }
    }

    public void setPlayerName(String name){
        this.playerName = name;
    }
    public String getPlayerName(){
        return this.playerName;
    }

    @Override
    public Room checkRoom() {
        return this.currentRoom;
    }

    public int getHealth() {
        return health;
    }

    public String getSign() {
        return sign;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public int getOffenseBonus(){
        return offenseBonus;
    }
    public int getDefenseBonus(){ return defenseBonus; }

    @Override
    public int getTreasureCount() {
        return 0;
    }

    public ArrayList<Treasure> getInventory(){
        return inventory;
    }

    public boolean getAlive() {
        return alive;
    }

    public void setCombatStyle(Characters.Action.Combat.combatStyle cs) {
        combatStyle = cs;
    }

    public void collectTreasure(Room r) {
        inventory.add(r.takeTreasure());
    }

    public void setCurrentRoom(Room newRoom) {
        currentRoom = newRoom;
    }

    public void updateOffenseBonus(int scalar){
        this.offenseBonus += scalar;
    }

    public void updateDefenseBonus(int scalar){
        this.defenseBonus += scalar;
    }

    public void heal(int amount){
        this.health += amount;
    }

    @Override
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers(String eventID) {
        for (Observer o : observerList) {
            o.updateAdventurerStatus(this, eventID);
        }
    }
}
