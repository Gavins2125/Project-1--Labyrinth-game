public class itemClass{
    private String description;
    private int buff;//usage
    private String name;

    // Creates an important item that detrmines ending
    public itemClass(String name, String Description){
        this.name = name;
        description = Description;
    }
    // creates an item that can give you a state buff
    public itemClass(String name, String Description, int Buff){
        this.name = name;
        description = Description;
        buff = Buff;
    }
    public int getBuff() {
        return buff;
    }
    public String getDescription(){
        return description;
    }
    public void use(playerClass player){
        player.heal(buff);
    }
    @Override
    public String toString(){
        return name;
    }
    
}