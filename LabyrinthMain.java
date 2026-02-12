//import java.util.Random;
import java.util.Scanner;

/**
 * Entry point for the Labyrinth game.
 */
public class LabyrinthMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //player creation
        playerClass player = importentMethods.characterCreation(scanner);
        inventoryClass inventory = new inventoryClass();

        //Displays stats
        System.out.println("Here is your current stats: ");
        player.displayStats();

        //Starts the game and game loop        
        LabyrinthGame game = new LabyrinthGame(player,inventory,scanner);
        game.start();

    }
}
