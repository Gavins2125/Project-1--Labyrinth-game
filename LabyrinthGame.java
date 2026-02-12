import java.util.ArrayList;
//import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LabyrinthGame {
    private Maze maze;
    private int playerX, playerY;
    private final Scanner scanner;
    private boolean Cheats = false;
    private final playerClass player;
    private final inventoryClass inventory;
    private final dialogOptions dialog = new dialogOptions();
    private final Random rand = new Random();
    private boolean finalBossDefeated = false;
    private boolean enterFinalBoss = false;
    

    // Cheat code tracker
    private final String cheatCode = "<^^<>^v";
    private StringBuilder cheatInput = new StringBuilder();

    private final ArrayList<String> clearedRooms= new ArrayList<>();

    //sets up the bosses and puzzles
    private int[] bossindex = new int[10];
    private int[] puzzleindex = new int[10];
    private int bossCount = 0;
    private int puzzleCount = 0;
    public LabyrinthGame(playerClass player, inventoryClass inventory, Scanner scanner){
        this.player = player;
        this.inventory = inventory;
        this.scanner = scanner;
        
        for(int i = 0; i < 10; i++){
            bossindex[i] = rand.nextInt(6);
            puzzleindex[i] = rand.nextInt(4);
        }

    }

    public void start() {
        // Difficulty selection
        System.out.println("Select difficulty: (E)asy 3x3, (N)ormal 4x4, (H)ard 5x5");
        char diff = scanner.next().toUpperCase().charAt(0);
        int width, height;
        switch (diff) {
            case 'E' -> width = height = 3;
            case 'N' -> width = height = 4;
            case 'H' -> width = height = 5;
            default -> {
                System.out.println("Invalid choice, defaulting to Normal 4x4");
                width = height = 4;
            }
        }

        maze = new Maze(width, height);
        maze.generate();

        playerX = maze.getStart().x;
        playerY = maze.getStart().y;
        maze.getCell(playerX, playerY).traveled = true;

        gameLoop();
    }

    private void gameLoop() {
        while (true) {
            if(player.getHealth() <= 0){
                gameOver();
                return;
            }
            MazePrinter.print(maze, playerX, playerY);
            Cell c = maze.getCell(playerX, playerY);
            if(c.roomType == RoomType.FINAL_BOSS&& finalBossDefeated){
                victory();
                break;
            }

            // Flavor text for the current room
            if (c.traveled) {
                switch (c.roomType) {
                    // Put item room here
                    case ITEM -> System.out.println("You found an Item Room");
                    // Put puzzle room here
                    case PUZZLE -> System.out.println("You found a Puzzle Room");
                    // Put boss room here
                    case BOSS -> System.out.println("You found a Boss Room");
                    // Put final boss here, can be random boss
                    case FINAL_BOSS -> System.out.println("You reached the Final Boss");
                    case BLANK -> System.out.println("An empty room.");
                }
            }

            System.out.print("Move (WASD), (I)nventory: ");
            String input = scanner.next();
            scanner.nextLine();

            boolean cheatActivated = false;

            // Check for cheat code sequence
            for (char ch : input.toCharArray()) {
                cheatInput.append(ch);

                if (cheatInput.length() > cheatCode.length())
                    cheatInput.deleteCharAt(0);

                if (cheatInput.toString().equals(cheatCode)) {
                    toggleCheats();
                    cheatInput.setLength(0);
                    cheatActivated = true; // mark that cheat activated
                }
            }

            // Only process move if cheat code was not activated
            if (!cheatActivated) {
                char move = input.toUpperCase().charAt(0);

                switch (move) {
                    case 'W' : tryMove(0, -1);break;
                    case 'A' : tryMove(-1, 0);break;
                    case 'S' : tryMove(0, 1);break;
                    case 'D' : tryMove(1, 0);break;
                    case 'I' : showInventory();break;
                    default : System.out.println("Invalid input.");break;
                }
            }

            if (c.roomType == RoomType.FINAL_BOSS && !enterFinalBoss) {
                enterFinalBoss = true;
                MazePrinter.print(maze, playerX, playerY);
                System.out.println("You reached the final boss!");
                break;
            }
        }
        scanner.close();
    }

    private void tryMove(int dx, int dy) {
        Cell c = maze.getCell(playerX, playerY);
        int newX = playerX + dx;
        int newY = playerY + dy;

        // Check walls
        if (dx == 1 && c.walls[1] || dx == -1 && c.walls[3] ||
            dy == 1 && c.walls[2] || dy == -1 && c.walls[0]) {
            System.out.println("You cannot move in that direction, a wall blocks the way!");
            return;
        }

        if (newX >= 0 && newX < maze.getGrid()[0].length &&
            newY >= 0 && newY < maze.getGrid().length) {
            playerX = newX;
            playerY = newY;
            Cell next = maze.getCell(playerX, playerY);
            
        
            if(!next.traveled){
                next.traveled = true;
                roomHandler(next);
            }
        }
    }

    public void roomHandler(Cell cell){
        String roomKey = cell.x + "," + cell.y;
        if(clearedRooms.contains(roomKey)){
            return;
        }
        switch(cell.roomType){
            case ITEM:
                itemHandler();
                clearedRooms.add(roomKey);
                break;
            case PUZZLE:
                puzzleHandler();
                clearedRooms.add(roomKey);
                break;
            case BOSS:
                bossHandler(false);
                if(player.getHealth() > 0){
                clearedRooms.add(roomKey);
                }
                break;
            case FINAL_BOSS:
                bossHandler(true);
                if(player.getHealth() > 0){
                clearedRooms.add(roomKey);
                finalBossDefeated = true;
                }
                break;
            default:
                break;
        }
    }
    public void itemHandler(){
        int iteminfo = rand.nextInt(dialog.itemInfo.length);
        System.out.println("In the room you see" + dialog.itemInfo[iteminfo]);
        System.out.println("Would you like to take this item Y)es or N)o");

        String choice = scanner.nextLine().toUpperCase();

        if(choice.equals("Y")){
            itemClass item = new itemClass(dialog.itemName[iteminfo],dialog.itemInfo[iteminfo],dialog.buffs[iteminfo %2]);
            inventory.inventoryAddtion(item);
            System.out.println("You picked up the item");
        }
        else{
            System.out.println("You decide the to leave the item");
        }

    }



    //Handles the boss room encounters
    public void bossHandler(boolean final_boss){
        int monsterinfo = bossindex[bossCount%10];
        bossCount++;
        System.out.println(dialog.creatureIntro[monsterinfo]);
        System.out.println(dialog.creatureNames[monsterinfo]);

        int bossHealth = final_boss? 200 :175;
        int bossDamage = final_boss? 15:10;

        creatureClass boss = new creatureClass(bossHealth,bossDamage,dialog.creatureInfo[monsterinfo],playerX,playerY,dialog.creatureTalk[monsterinfo]);

        importentMethods.combat(boss,player,scanner);
        if(player.getHealth() > 0){
            System.out.println("You have defeated, " + dialog.creatureNames[monsterinfo]);

        }
    }
    //Hanlde the puzzle room
    public void puzzleHandler(){
        int iteminfo = rand.nextInt(dialog.itemInfo.length);
        int puzzleInfo = puzzleindex[puzzleCount % 10];
        puzzleCount++;
        puzzleClass puzzle = new puzzleClass(dialog.riddlePuzzle[puzzleInfo],dialog.riddleAnswer[puzzleInfo],dialog.hints[puzzleInfo]);
        itemClass reward = new itemClass(dialog.itemName[iteminfo],dialog.itemInfo[iteminfo],dialog.buffs[iteminfo %2]);
        importentMethods.puzzle(scanner, puzzle, reward, inventory);
    }

    

    private void showInventory() {
        while(true){
        if (inventory.size()== 0) {
            System.out.println("Inventory: Empty");
        }
        System.out.println("Inventory: ");
        inventory.displayInventory();

        System.out.println("Would you like to U)se and item or E)xit");
        String choice = scanner.nextLine().toUpperCase();
        if(choice.equals("U")){
            inventory.useItem(scanner, player);
        }
        else if(choice.equals("E")){
            return;
        }
        else{
            System.out.println("Invalid input");
        }
    }

    }

    private void gameOver(){
        System.out.println("You fall to the Labyrinth, your story ends just as many, all that is left is you and memory");
    }

    private void victory(){
        System.out.println("You escape the Labyrinth, you are endlessly prasied with wealth and fame, but at your darkest hours did you really escape the Labyrinth");
    }

    private void toggleCheats() {
        Cheats = !Cheats;
        System.out.println("Cheats: " + (Cheats ? "Enabled" : "Disabled"));
        maze.assignRoomTypes(Cheats);
    }
}
    

