import java.util.ArrayList;
import java.util.Scanner;

public class inventoryClass {
    private ArrayList<itemClass> inventory;
    // inventory constuctor
    public inventoryClass(){
        inventory = new ArrayList<>();
    }
    public void inventoryAddtion(itemClass item){
        inventory.add(item);
    }
    // display items in iventory
    public void displayInventory(){
        for(int i = 0; i < inventory.size(); ++i){
            int num = i + 1;
            System.out.println(num + ": " + inventory.get(i));
        }
    }
    public int size(){
        return inventory.size();
    }
    public void useItem(Scanner scanner, playerClass player){
        if(inventory.isEmpty()){
            System.out.println("Your inventory is empty");
            return;
        }
        System.out.println("what item would you like to use? ");
        displayInventory();

        System.out.println("Enter item number");
        String input = scanner.nextLine();

        try{
            int index = Integer.parseInt(input) - 1;
            if( index >= 0 && index < inventory.size()){
                itemClass item = inventory.get(index);
                item.use(player);
                inventory.remove(index);
            }
            else{
                System.out.println("Invalid item number");
            }
        }catch(NumberFormatException e){
            System.out.println("Please enter a valid number");

        }
    }

}
