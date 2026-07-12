class Wire {
    int length;
    int diameter;

    public Wire(int length, int diameter) {
        this.length = length;
        this.diameter = diameter;
    }

    // Wire knows how to clone itself
    public Wire(Wire other) {
        this.length = other.length;
        this.diameter = other.diameter;
    }
}

class Instrument {
    int price;
    String name;
    Wire wire;

    public Instrument(int price, String name, Wire wire) {
        this.price = price;
        this.name = name;
        this.wire = wire;
    }

    // Instrument only clones its immediate child (Wire)
    public Instrument(Instrument other) {
        this.price = other.price;
        this.name = other.name;
        this.wire = new Wire(other.wire); // Delegated!
    }
}

class NPC implements Cloneable {
    public String name;
    public int health;
    public int attack;
    public int defense;
    public Instrument instrument;

    public NPC(String name, int health, int attack, int defense, Instrument instrument) {
        this.name = name; 
        this.health = health; 
        this.attack = attack; 
        this.defense = defense;
        this.instrument = instrument;
    }

    // NPC only clones its immediate child (Instrument)
    public NPC(NPC other) {
        this.name = other.name;
        this.health = other.health;
        this.attack = other.attack;
        this.defense = other.defense;
        this.instrument = new Instrument(other.instrument); // Clean and isolated!
        System.out.println("Cloning NPC '" + name + "'");
    }

    public Cloneable clone() {
        return new NPC(this);
    }
   
   public void describe() {
       System.out.println("NPC " + name + " [HP=" + health + " ATK=" + attack 
            + " DEF=" + defense + "]" +
            instrument.name + " [Price= "+instrument.price+ " wire length= "+instrument.wire.length + " wire diameter= "+instrument.wire.diameter + " ]");
   }
   
   // setters to tweak the clone…
   public void setName(String n) { 
       name = n;
   }
   public void setHealth(int h) { 
       health = h;
   }
   public void setAttack(int a) {
        attack = a; 
   }
   public void setDefense(int d){ 
       defense = d;
   }
}

public class Main {
   public static void main(String[] args) {
       // 1) build one "heavy" template
       Instrument instrument = new Instrument(20,"violin",new Wire(10,10));
       NPC alien = new NPC("Alien", 30, 5, 2,instrument);
       
       // 2) quickly clone + tweak as many variants as you like:
       NPC alienCopied1 = (NPC)alien.clone();
       alienCopied1.describe();
       
       Instrument instrument2 = new Instrument(22,"guitar",new Wire(1,10));
       NPC alienCopied2 = (NPC)alien.clone();
       alienCopied2.setName("Powerful Alien");
       alienCopied2.setHealth(50);
       alienCopied2.instrument.name = "guitar"; 
       alienCopied2.instrument.price = 22;
       alienCopied2.instrument.wire.length = 1;
       alienCopied2.describe();
       
       // cleanup
       alien = null;
       alienCopied1 = null;
       alienCopied2 = null;
   }
}
