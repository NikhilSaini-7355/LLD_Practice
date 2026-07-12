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


/*

## The Prototype Design Pattern

The **Prototype Design Pattern** is a creational design pattern that allows you to copy existing objects without making your code dependent on their concrete classes.

Instead of creating a brand-new object from scratch using the `new` keyword (which requires knowing the class details and setting up all initial states), you ask an existing object—the **Prototype**—to clone itself.

### When is it used?

* **Costly Initialization:** When creating an object from scratch is resource-intensive (e.g., requires fetching data from a database or performing complex calculations).
* **Avoiding Subclasses:** When you want to avoid a factory hierarchy of creators just to instantiate objects that only differ by their initial state configuration.
* **Preserving State History:** When you want to save a snapshot of an object's current state so you can restore it later (like an "Undo" feature).

---

## The Core Concept: Shallow Copy vs. Deep Copy

When implementing the Prototype pattern, the biggest decision you have to make is *how* the object will be cloned. This brings us to the crucial difference between a **Shallow Copy** and a **Deep Copy**.

Imagine an object `Car` that contains a reference to another object, `Engine`.

### 1. Shallow Copy

A shallow copy duplicates the top-level object, but **copies the references** to any internal nested objects.

* **How it works:** If you shallow-clone a `Car`, the new `Car` copy gets its own primitive variables (like `speed` or `color`), but it still points to the exact same `Engine` object in memory as the original car.
* **The Danger:** If the clone modifies the `Engine` (e.g., `clone.getEngine().setHorsepower(500)`), the original car's engine changes too, because they share the same reference.

### 2. Deep Copy

A deep copy duplicates the top-level object **and recursively duplicates all internal nested objects** it references.

* **How it works:** If you deep-clone a `Car`, the system creates a new `Car` object *and* a completely separate new `Engine` object in memory.
* **The Benefit:** The original and the clone are 100% disconnected. Modifying the clone’s engine will have zero impact on the original car.

---

## Quick Comparison Summary

| Feature | Shallow Copy | Deep Copy |
| --- | --- | --- |
| **Object Copied** | Only the root object is duplicated. | The root object and all nested/referenced objects are duplicated. |
| **Memory References** | Main object gets a new address; nested objects share the old addresses. | Every single component gets a brand-new memory address. |
| **Independence** | **Dependent**. Changes to nested components affect both original and clone. | **100% Independent**. Changes to one do not affect the other. |
| **Performance Speed** | Fast ($O(1)$ usually). | Slower ($O(N)$ depending on object depth). |

---

## How to Implement Prototype in Java

Java provides a native `Cloneable` interface, but it performs a **shallow copy** by default. To do a deep copy, you have to manually override the `clone()` method.

Here is a quick look at how both approaches are coded:

```java
class Engine {
    int horsepower;
    public Engine(int hp) { this.horsepower = hp; }
}

class Car implements Cloneable {
    String model;
    Engine engine; // Nested Object Reference

    public Car(String model, Engine engine) {
        this.model = model;
        this.engine = engine;
    }

    // --- OPTION A: SHALLOW COPY ---
//
 //   @Override
  //  protected Object clone() throws CloneNotSupportedException {
 //       return super.clone(); // Native Java cloning is shallow by default
//    }
//

    // --- OPTION B: DEEP COPY ---
    @Override
    protected Object clone() throws CloneNotSupportedException {
        // 1. Clone the root Car object
        Car clonedCar = (Car) super.clone();
        // 2. Manually clone the internal Engine object so they don't share references
        clonedCar.engine = new Engine(this.engine.horsepower);
        return clonedCar;
    }
}

```

Are you planning to build a system where runtime performance is critical, or is absolute object independence your primary goal?

*/
    
}
