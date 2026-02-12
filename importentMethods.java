import java.util.Scanner;
//import java.util.Random;

public class importentMethods{
    //A method that intates combat
    public static void combat(creatureClass creature, playerClass player, Scanner in){
        //Scanner in = new Scanner(System.in);
        int turn = 2;
        while(creature.getMosterHealth() > 0 && player.getHealth() > 0){
            if(turn % 2 == 0){
                player.resetsDefense();
                System.out.println("Current health " + player.getHealth());
                System.out.println("A)ttack, D)efend, T)alk, E)xamine");
                String playerInput = in.next().toUpperCase();
                switch(playerInput){
                    case "A":
                    System.out.println("You ready your weapon and attack the monster");
                    creature.attackMonster(player.getAttack());
                    break;
                    case "D":
                    System.out.println("You brace yourself for the creatures attack");
                    player.guard();
                    break;
                    case "T":
                    System.out.println("You try to speak to the creature");
                    System.out.println(creature.getDialog());
                    break;
                    case "E":
                    System.out.println("You take a look at the moster to get a better understanding");
                    System.out.println(creature.monsterInfo());
                    break;
                    default:
                    System.out.println("You here a wisper you need to move before your killed");
                    in.nextLine();
        
                }
                turn++; 
            }
            else{
                System.out.println("The moster attacks you with all its might and delt " + creature.getDamage());
                int damage = creature.MonsterAttack(player.getDefense());
                player.attacked(damage);
                turn++;

            } 
            }
            System.out.println("the monster has been defeated.");
        }
        //Combat function end

    //character creation method
    public static playerClass characterCreation(Scanner in){
        System.out.println("You a traveler have been task to explore a mythical labyrinth, for glory or riches the choice is up to you.");
        System.out.println("When choosing your gear you could be a K)ight, V)agabond, H)ero");
        //Scanner in = new Scanner(System.in);
        playerClass player = null;
        String playerInput = in.next().toUpperCase();
        switch(playerInput){
            case "K":
            player = new playerClass("Knight", 40, 5, 100);
            break;
            case "V":
            player = new playerClass("Vagabond", 50, 10, 100);
            break;
            case "H":
            player = new playerClass("Hero", 60, 0, 100);
            break;
            default:
            System.out.println("Invalid choice defaulting to knight");
            player = new playerClass("Knight", 40, 5, 100);
            
        }
        return player;

    }
    //player creation method end

    //Puzzle room interaction start

    public static void puzzle(Scanner in, puzzleClass puzzle, itemClass item, inventoryClass inv){
        boolean clear = false;
        System.out.println("The room doors slam shut around you");
        System.out.println(puzzle.getQuestion());
        //System.out.println("Would you like to S)olve the puzzle, read the question A)gain or would you like a H)int");
        do{
            System.out.println("Would you like to S)olve the puzzle, read the question A)gain or would you like a H)int");
            String playerInput = in.nextLine().toUpperCase();
            switch (playerInput) {
                case "S":
                    String playerAnswer = in.nextLine().toUpperCase();
                    if(puzzle.checkSolution(playerAnswer)){
                        System.out.println("you have solved the puzzle");
                        clear = true;
                    }else{
                        System.out.println("you have gotten the puzzle wrong");
                    }
                        break;
                case "A":
                    System.out.println(puzzle.getQuestion());
                    break;
                case "H":
                    System.out.println(puzzle.gethint());
                    break;
                default:
                    System.out.println("Please enter a valid input.");
            }

        }while(!clear);
        System.out.println("The doors open and an item appears behind you, would you like to pick it up; Y)es or N)o )?");
        String playerChoice = in.nextLine().toUpperCase();
        switch(playerChoice){
            case "Y":
                System.out.println("You picked up the item");
                inv.inventoryAddtion(item);
                break;
            case "N":
                System.out.println("The item disappears to dust like it was never there");
                break;
            default:
                System.out.println("Please enter a valid input.");
        }
    }
    //Puzzle Method ends
    }
