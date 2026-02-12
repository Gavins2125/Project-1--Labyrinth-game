public class playerClass{
    private String playerClass;
    private int playerHealth;
    private int baseAttack;
    private int baseDefense;
    private int tempdef = 0;
    public playerClass(String inputClass, int BaseAttack, int BaseDefense, int PlayerHealth){
        playerClass =inputClass;
        playerHealth = PlayerHealth;
        baseAttack = BaseAttack;
        baseDefense = BaseDefense;

    }
    public void displayStats(){
        System.out.println("Class: " + playerClass);
        System.out.println("Health: " + playerHealth);
        System.out.println("Attack: " + baseAttack);
        System.out.println("Defense: " + baseDefense);
    }
    public int getHealth(){
        return playerHealth;

    }
    public int getDefense(){
        return baseDefense + tempdef;
    }
    public int getAttack(){
        return baseAttack;
    }
    public void attacked(int damage){
        playerHealth -= damage;

    }
    public void guard(){
        tempdef = 5;
    }
    public void resetsDefense(){
        tempdef = 0;
    }
    public void heal(int amount){
        playerHealth += amount;
        if(playerHealth > 100){
            playerHealth = 100;
        }
    }
}