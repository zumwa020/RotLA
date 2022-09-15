package Game;

import Board.Render;

import java.util.Objects;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) {

        Render view = new Render();
        Scanner input = new Scanner(System.in);
        Engine game = new Engine();

        game.initialize();

        boolean gameOver = false;

        // Primary Game.Run Loop
        while ( !gameOver ) {

//            for (int i = 0; i < ENTITY_TOTAL; ++i) {
//                processEntity(entityList[i]);
//            }

            view.printFrame();

            // Exit Condition
            if(Objects.equals(input.nextLine(),"q")){
                gameOver = true;
            }
        }
        input.close();
    }
}