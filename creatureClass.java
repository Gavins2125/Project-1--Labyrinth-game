

public class creatureClass {
    // Stats of the monster
    private int health;
    private int damage;
    // the info of the monster
    private String info;
    // where the moster is located on the map
    private int Posx;
    private int Posy;
    //the moster diolog
    private String dialog;

    //gets the boss intro from


    // this is the method that creates the creature
    public creatureClass(int Monsterhealth, int Monsterdamage, String Monsterinfo, int MonsterPosx, int MonsterPosy, String MonsterDialog){
        health = Monsterhealth;
        damage = Monsterdamage;
        info = Monsterinfo;
        Posx = MonsterPosx;
        Posy = MonsterPosy;
        dialog = MonsterDialog;
    }
    // the moster attacks the play and the method reduce the monster attack by player defenses
    public int getDamage(){
        return damage;
    }
    public int getMosterHealth(){
        return health;
    }
    public int MonsterAttack(int playerDef){
        return damage - playerDef;

    }
    // the player attacts the moster
    public void attackMonster(int playerDamage){
        health -= playerDamage;
        if(health < 0){
            health = 0;
        }

    }
    public String monsterInfo(){
        return info;
    }
    public String getDialog(){
        return dialog;
    }
     
}
