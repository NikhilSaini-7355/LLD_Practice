import java.util.*;

interface Iterator<T> {
    boolean hasNext();
    T next();
}

interface Iterabel<T> {
    Iterator<T> getIterator();
}

class LinkedList implements Iterabel<Integer>{
    int val;
    LinkedList next;
    public LinkedList(int val)
    {
        this.val = val;
        this.next = null;
    }
    
    public LinkedList(int val, LinkedList next)
    {
        this.val = val;
        this.next = next;
    }
    
    public Iterator<Integer> getIterator(){
        return new LinkedListIterator(this);
    }
}

class LinkedListIterator implements Iterator<Integer> {
    private LinkedList curr;
    public LinkedListIterator(LinkedList linkedlist)
    {
        curr = linkedlist;
    }
    
    public boolean hasNext()
    {
        if(curr==null)
        {
            return false;
        }
        return true;
    }
    
    public Integer next()
    {
        if(!(this.hasNext()))
        {
            throw new RuntimeException("no next element present");
        }
        int val = curr.val;
        curr = curr.next;
        return val;
    }
}

class BinaryTree implements Iterabel<Integer> {
    int val;
    BinaryTree left;
    BinaryTree right;
    public BinaryTree(int val)
    {
        this.val = val;
        this.left = null;
        this.right = null;
    }
    
    public Iterator<Integer> getIterator()
    {
        return new BinaryTreeIterator(this);
    }
}

class BinaryTreeIterator implements Iterator<Integer> {
    private Deque<BinaryTree> stk = new ArrayDeque<>();
    private void pushleft(BinaryTree node)
    {
            while (node != null) { // SAFE: Handles null roots perfectly
            stk.push(node);
            node = node.left;
        }
    }
    
    public BinaryTreeIterator(BinaryTree node)
    {
        pushleft(node);
    }
    
    public boolean hasNext()
    {
        if(stk.size()==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public Integer next()
    {
        if(this.hasNext()==false)
        {
            throw new RuntimeException("no next element present");
        }
        BinaryTree temp = stk.pop();
        int val = temp.val;
        if(temp.right!=null)
        {
            pushleft(temp.right);
        }
        return val;
    }
}

class Song{
    public String title;
    public String artist;

    public Song(String t, String a) {
        title = t;
        artist = a;
    }
}

class PlayList implements Iterabel<Song>{
    private List<Song> songs;
    public PlayList()
    {
        songs = new ArrayList<>();
    }
    
    public void add(Song song)
    {
        songs.add(song);
    }
    
    public void remove(Song song)
    {
        if(songs.contains(song))
        {
            songs.remove(song);
        }
    }
    
    public Iterator<Song> getIterator()
    {
        return new PlayListIterator(songs);
    }
}

class PlayListIterator implements Iterator<Song> {
    private List<Song> songs;
    private int index = 0;
    public PlayListIterator(List<Song> songs)
    {
        this.songs = songs;
    }
    
    public boolean hasNext()
    {
        if(index>=songs.size())
        {
            return false;
        }
        else{
            return true;
        }
    }
    
    public Song next()
    {
        if(this.hasNext()==false)
        {
            throw new RuntimeException("no next element present");
        }
        Song song = songs.get(index);
        index++;
        return song;
    }
}

public class Main{
    public static void main(String[] args)
    {
        LinkedList linkedlist = new LinkedList(10);
        linkedlist.next = new LinkedList(20);
        linkedlist.next.next = new LinkedList(30);
        
        Iterator<Integer> iterator = linkedlist.getIterator();
        while(iterator.hasNext())
        {
            System.out.print(iterator.next()+" , ");
        }
        System.out.println("\n============================================\n");
        
        BinaryTree binaryTree = new BinaryTree(10);
        binaryTree.left = new BinaryTree(20);
        binaryTree.right = new BinaryTree(30);
        binaryTree.left.right = new BinaryTree(40);
        binaryTree.right.left = new BinaryTree(50);
        binaryTree.right.right = new BinaryTree(60);
        
        Iterator<Integer> binaryTreeIterator = binaryTree.getIterator();
        while(binaryTreeIterator.hasNext())
        {
            System.out.print(binaryTreeIterator.next() + " , ");
        }
        System.out.println("\n============================================\n");
        
        PlayList playlist = new PlayList();
        playlist.add(new Song("cest la ve","Khaled"));
        playlist.add(new Song("Hello India","Modiji"));
        playlist.add(new Song("Skyfall","Adelle"));
        playlist.add(new Song("Dai Dai","Shakira"));
        
        Iterator<Song> playlistIterator = playlist.getIterator();
        
        while(playlistIterator.hasNext())
        {
            Song song = playlistIterator.next();
            System.out.println(song.title + " -> "+song.artist);
        }
        System.out.println();
    }
}




/*

-----------------------------------------:Generics in Java:---------------------------

Generics were introduced to Java to solve one massive problem: **Type Safety**.

Before generics, a `List` held raw `Object` references. You could accidentally put an `Integer` into a list meant for `Strings`, and the compiler wouldn't warn you. Your program would just explode with a `ClassCastException` at runtime when you tried to read it.

Generics fix this by moving the check from **runtime** to **compile-time**. They allow you to define placeholders (like `<T>`) that the compiler strictly enforces.

Here is the complete breakdown of how to read, write, and restrict generics in Java.

---

## 1. Generic Classes and Interfaces

When you define a class, you put the type parameter in angle brackets `<T>` next to the class name. The conventional names are single uppercase letters:

* `E` - Element (used extensively by the Java Collections Framework)
* `K` - Key (used in Maps)
* `V` - Value (used in Maps)
* `T` - Type (a generic type)

```java
// A generic class
public class Box<T> {
    private T item;

    public void set(T item) { this.item = item; }
    public T get() { return this.item; }
}

// Usage: The compiler now strictly enforces that this Box only holds Strings.
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
// stringBox.set(123); // ERROR: Compiler blocks this immediately.

```

## 2. Generic Methods

You don't have to make the entire class generic. You can make a single method generic. The trick is that **the type parameter `<T>` must go *before* the return type.**

```java
public class Utility {
    // <T> defines the generic type for this specific method
    public static <T> void printArray(T[] array) {
        for (T item : array) {
            System.out.println(item);
        }
    }
}

// Usage (Java infers the type automatically):
Utility.printArray(new Integer[]{1, 2, 3});
Utility.printArray(new String[]{"A", "B", "C"});

```

## 3. Bounded Types (Restricting the Generic)

Sometimes, you want to use a generic, but you want to restrict what types are allowed. For example, a math calculator shouldn't accept `String`. You restrict generics using the `extends` keyword.

```java
// T must be a subclass of Number (Integer, Double, Float, etc.)
public class Calculator<T extends Number> {
    private T number;
    
    public Calculator(T number) {
        this.number = number;
    }
    
    public double doubleValue() {
        return number.doubleValue(); // Safe because Number guarantees this method exists
    }
}

```

*Note: In generics, `extends` is used to mean both "extends a class" and "implements an interface". You can even do multiple bounds: `<T & ClassA InterfaceB extends>`.*

---

## 4. Wildcards (`?`) and the PECS Principle

This is the hardest part of generics, and a favorite interview topic.

Imagine you have a method that takes a `List<Animal>`. If you try to pass a `List<Dog>` into it, **Java will throw a compiler error**. Even though `Dog` extends `Animal`, a `List<Dog>` DOES NOT extend `List<Animal>`.

To fix this, we use the Wildcard `?` combined with bounds. The rule for when to use which bound is called **PECS: Producer Extends, Consumer Super.**

### Producer Extends (`? extends T`)

Use this when you are **reading** data out of the collection (the collection is *producing* data for you).

```java
// Accepts List<Animal>, List<Dog>, List<Cat>
public void drawAnimals(List<? extends Animal> list) {
    for (Animal a : list) {
        a.draw(); // Safe to read: We know it's AT LEAST an Animal
    }
    // list.add(new Dog()); // ERROR! The list might be a List<Cat>. You cannot write to it.
}

```

### Consumer Super (`? super T`)

Use this when you are **writing** data into the collection (the collection is *consuming* data from you).

```java
// Accepts List<Dog>, List<Animal>, List<Object>
public void addDogs(List<? super Dog> list) {
    list.add(new Dog()); // Safe to write: We know the list can hold Dogs
    
    // Animal a = list.get(0); // ERROR! We don't know what type is coming out. It could be an Object.
}

```

---

## 5. The JVM Secret: Type Erasure

Generics are entirely a **compiler trick**. The JVM running your code knows absolutely nothing about `<T>`.

When you compile your Java code, the compiler takes all your generic types and **erases** them, replacing them with standard casting.

If you write this:

```java
List<String> words = new ArrayList<>();
words.add("Hello");
String greeting = words.get(0);

```

The compiler turns it into this byte-code:

```java
List words = new ArrayList(); // Raw type
words.add("Hello");
String greeting = (String) words.get(0); // Compiler secretly inserts the cast!

```

### Why Erasure Matters

Because of Type Erasure, you **cannot** do certain things at runtime, because the type `T` doesn't exist when the program runs:

1. **You cannot instantiate a generic type:** `new T()` is illegal.
2. **You cannot create generic arrays:** `new T[10]` is illegal.
3. **You cannot use `instanceof` with generics:** `if (myList instanceof List<String>)` is illegal. You can only check `if (myList instanceof List)`.

*/








 /*
 SOLID principle:-
 
 
 The **SOLID** principles (assuming you meant SOLID over solic) are five foundational design guidelines in object-oriented programming. They were introduced by Robert C. Martin ("Uncle Bob") to help developers write code that is easy to scale, maintain, and test without breaking existing functionality.

Here is the breakdown of each principle, what it means, and how it looks in practice.

## 1. Single Responsibility Principle (SRP)

**A class should have one, and only one, reason to change.**

Every class or module should focus on a single task or responsibility. If a class is doing too many things, changing one feature might accidentally break an unrelated feature because they are tangled together.

* **The Violation:** A `User` class that holds user data (name, email), saves itself to a database, and handles password hashing.
* **The Fix:** Split it into three classes. `User` (just holds data), `UserRepository` (handles database saving), and `PasswordSecurity` (handles hashing). Now, if you change your database from SQL to MongoDB, you only touch the repository, leaving the business logic safely alone.

## 2. Open/Closed Principle (OCP)

**Software entities should be open for extension, but closed for modification.**

You should be able to add new functionality to your application without altering existing, tested code. You achieve this by relying on interfaces and abstract classes rather than concrete implementations.

* **The Violation:** A `DiscountCalculator` class with a massive `if-else` block checking if the customer is a "VIP", "Regular", or "Guest" to calculate their discount. If you add a "SuperVIP" tier, you have to open this class and modify the core logic.
* **The Fix:** Create a `DiscountStrategy` interface with a `calculate()` method. Create separate classes (`VipDiscount`, `RegularDiscount`) that implement it. When a new tier is added, you just write a new class. The original calculator code remains untouched.

## 3. Liskov Substitution Principle (LSP)

**Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.**

If Class B extends Class A, you should be able to drop Class B into anywhere Class A is used without the program crashing or behaving weirdly. Subclasses must honor the "contract" established by their parents.

* **The Violation:** You have a `Bird` parent class with a `fly()` method. You create a `Penguin` class that extends `Bird`. Since penguins can't fly, you write `throw new UnsupportedOperationException()` inside the penguin's `fly()` method. Any code looping through a list of `Bird` objects and calling `fly()` will now unexpectedly crash.
* **The Fix:** Segregate the hierarchy. Have a base `Bird` class, and then two sub-interfaces: `FlyingBird` and `FlightlessBird`. The penguin only implements the flightless one.

## 4. Interface Segregation Principle (ISP)

**No client should be forced to depend on methods it does not use.**

It is better to have many small, specific interfaces than one massive, general-purpose "fat" interface.

* **The Violation:** A `Worker` interface that demands `work()`, `eat()`, and `sleep()`. If you create a `RobotWorker` class that implements this interface, it is forced to write dummy code for `eat()` and `sleep()` because robots don't do those things.
* **The Fix:** Break the interface down into `Workable`, `Eatable`, and `Sleepable`. A `HumanWorker` can implement all three, while the `RobotWorker` strictly implements `Workable`.

## 5. Dependency Inversion Principle (DIP)

**High-level modules should not depend on low-level modules. Both should depend on abstractions.**

Your core business logic (high-level) shouldn't be hard-wired to specific tools or frameworks (low-level). Instead, your core logic should dictate an interface, and the tools should implement that interface.

* **The Violation:** A `ShoppingCart` class directly instantiates `new StripePaymentAPI()`. Your cart is now permanently glued to Stripe. If you want to switch to PayPal, you have to tear apart your cart logic.

* **The Fix:** Create a `PaymentProcessor` interface. The `ShoppingCart` depends only on this interface. You then write a `StripeAdapter` and a `PayPalAdapter` that implement it. You pass the desired adapter into the cart (usually via the constructor). This is called **Dependency Injection**.

*/
