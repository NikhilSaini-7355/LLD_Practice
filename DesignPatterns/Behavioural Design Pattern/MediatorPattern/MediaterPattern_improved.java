import java.util.*;
interface IColleagues{
     public void sendAll(String message);
     public void sendTo(String To, String message);
     public void receive(String from, String message);
     public String getUsername();
     public void blockColleague(String To);
}

class User implements IColleagues{
    private IMediator mediator;
    private String username;
    public User(IMediator mediator, String username)
    {
        this.mediator = mediator;
        this.username = username;
        this.mediator.register(this);
    }
    
    public void sendAll(String message)
    {
        this.mediator.sendAll(this.username, message);
    }
    
    public void sendTo(String To, String message)
    {
        this.mediator.sendTo(this.username, To, message);
    }
    
    public void receive(String from, String message)
    {
        System.out.println(this.username+" received = "+"'"+message+"'"+" from "+ from);
    }
    
    public void blockColleague(String To)
    {
        this.mediator.blockColleague(this.username, To);
    }
    public String getUsername()
    {
        return this.username;
    }
}

interface IMediator{
    public void sendAll(String from, String message);
    public void sendTo(String from, String To, String message);
    public void remove(IColleagues colleague);
    public void register(IColleagues colleague);
    public void blockColleague(String from, String To);
}

class ChatMediator implements IMediator{
    private List<IColleagues> colleagues;
    private Map<String,List<String>> muted;
    public ChatMediator()
    {
        colleagues = new ArrayList<>();
        muted = new HashMap<>();
    }
    
    public void sendAll(String from, String message)
    {
        for(IColleagues colleague : colleagues)
        {
            if(colleague.getUsername().equals(from) || (muted.containsKey(colleague.getUsername()) && muted.get(colleague.getUsername()).contains(from))){
                continue;
            }
            colleague.receive(from, message);
        }
    }
    
    public void sendTo(String from, String To, String message)
    {
        if(muted.containsKey(To) && muted.get(To).contains(from))
        {
            return;
        }
        for(IColleagues colleague : colleagues)
        {
            if(colleague.getUsername().equals(To))
            {
                colleague.receive(from, message);
            }
        }
    }
    
    public void remove(IColleagues colleague)
    {
        if(colleagues.contains(colleague))
        {
            colleagues.remove(colleague);
        }
    }
    
    public void register(IColleagues colleague)
    {
        if(!colleagues.contains(colleague))
        {
            colleagues.add(colleague);
            muted.put(colleague.getUsername(),new ArrayList<>());
        }
    }
    
    public void blockColleague(String from, String To)
    {
        if(muted.containsKey(from))
        {
                if(!(muted.get(from).contains(To)))
            {
                muted.get(from).add(To);
            }
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        IMediator mediator = new ChatMediator();
        IColleagues user1 = new User(mediator,"user1");
        IColleagues user2 = new User(mediator,"user2");
        IColleagues user3 = new User(mediator,"user3");
        IColleagues user4 = new User(mediator,"user4");
        IColleagues user5 = new User(mediator,"user5");
        user4.blockColleague("user1");
        user1.sendAll("Hello World!!");
        user5.blockColleague("user2");
        user2.sendAll("Life is Hard and Unfair");
        
        user3.sendTo("user4","Life is hard man and really really unfair");
        user4.sendTo("user5","user4 said life is hard and unfair. I think so too.");
        user5.sendTo("user3","I agree with you. Life is really hard and unfair.");
    }
}


/*

This is a very common problem in object-oriented design. When an interface forces a class to implement methods it doesn't actually need, it violates the **Interface Segregation Principle (ISP)**—the "I" in SOLID design.

There are three main ways to handle this effectively depending on your needs.

---

## Approach 1: Interface Segregation (The Cleanest Way)

Instead of one bloated interface, break it down into smaller, highly focused interfaces based on roles. A class can then implement exactly what it needs.

### The Code Setup:

```java
interface CanDo1 { void method1(); }
interface CanDo2 { void method2(); }
interface CanDo3 { void method3(); }

// Class B needs all three
class B implements CanDo1, CanDo2, CanDo3 {
    public void method1() {  }
    public void method2() {  }
    public void method3() {  }
}

// Class C only needs 1 and 2
class C implements CanDo1, CanDo2 {
    public void method1() {  }
    public void method2() {  }
}

// Class D only needs 1
class D implements CanDo1 {
    public void method1() {  }
}

```

* **Best for:** Strict type safety, clean architecture, and decoupled code.

---

## Approach 2: Java 8+ Default Methods (The Easiest Way)

If you are using Java 8 or newer, you can provide `default` empty or baseline implementations directly inside interface `A`. Concrete classes are then only required to override the methods they actually want to change.

### The Code Setup:

```java
interface A {
    void method1(); // Still abstract, everyone MUST implement this
    
    default void method2() {
        // Default empty implementation or placeholder behavior
    }
    
    default void method3() {
        // Default empty implementation or placeholder behavior
    }
}

class B implements A {
    public void method1() {  }
    public void method2() {  }
    public void method3() { }
}

class C implements A {
    public void method1() { }
    public void method2() {  }
    // method3 is automatically inherited as an empty shell
}

class D implements A {
    public void method1() {  }
    // method2 and method3 are inherited automatically
}

```

* **Best for:** Refactoring legacy code where you cannot easily split the interface without breaking existing systems.

---

## Approach 3: The Adapter Pattern (The Traditional Way)

If you are working with older Java versions or want a rigid class hierarchy, you can introduce an abstract "Adapter" class between the interface and your concrete classes. The adapter provides default empty bodies.

### The Code Setup:

```java
interface A {
    void method1();
    void method2();
    void method3();
}

// Intermediate base adapter
abstract class BaseAdapter implements A {
    public void method1() {}
    public void method2() {}
    public void method3() {}
}

// B overrides everything
class B extends BaseAdapter {
    public void method1() {  }
    public void method2() {  }
    public void method3() {  }
}

// C only overrides 1 and 2
class C extends BaseAdapter {
    public void method1() {  }
    public void method2() {  }
}

```

* **Best for:** Older environments, or when the base adapter needs to manage some shared internal states/variables alongside those empty methods.

---

## Summary Recommendation

* Choose **Approach 1 (Interface Segregation)** if you want the best architectural design. It keeps your code modular and prevents unrelated components from knowing about methods they don't care about.
* Choose **Approach 2 (Default Methods)** if you want the fastest, cleanest code layout without introducing multiple new files.

*/



























/*

## The Mediator Design Pattern

The **Mediator Design Pattern** is a behavioral design pattern that reduces chaotic dependencies between objects. It restricts direct communications between the objects and forces them to collaborate only via a mediator object.

Instead of dozens of objects talking directly to each other (creating a tangled "spaghetti" of dependencies), they all talk to a single central mediator, which routes the messages correctly.

---

## Key Uses of the Mediator Pattern

* **Simplifying Complex GUIs:** The most classic use case. Think of a complex dialog box. When a user clicks a checkbox, a text field might need to be disabled, a button might need to change its color, and a dropdown might need to refresh its data. Instead of the checkbox knowing about the text field, button, and dropdown, it just tells the mediator: *"Hey, I was checked."* The mediator handles the rest.
* **Air Traffic Control (The Classic Analogy):** Pilots of airplanes approaching an airport don’t talk directly to each other to coordinate landings. That would be chaotic. Instead, they talk to the Air Traffic Control (ATC) tower (the Mediator), which coordinates who lands when.
* **Enterprise Service Bus (ESB) in Architecture:** In distributed systems, instead of microservices communicating directly with every other microservice, they often route messages through a central message broker or ESB.

---

## Mediator vs. Observer: What's the Difference?

These two patterns are frequently confused because they both handle communication between objects, and they are often used *together*. However, their intent and structural goals are fundamentally different.

| Feature | Mediator Pattern | Observer Pattern |
| --- | --- | --- |
| **Primary Intent** | **Decouple a set of interacting objects.** It centralizes and manages complex communication flows between a specific group of peers. | **Define a one-to-many dependency.** When one object changes state, all its dependents are notified automatically. |
| **Communication Flow** | **Many-to-Many** (or Hub-and-Spoke). Multiple objects talk to the mediator, and the mediator talks back to multiple objects. | **One-to-Many**. A single "Subject" or "Publisher" broadcasts state changes to multiple "Observers" or "Subscribers." |
| **Direction** | **Bi-directional**. Objects send data *to* the mediator and receive data *from* the mediator. | **Uni-directional (mostly)**. The publisher pushes updates down to the subscribers. Subscribers rarely talk back to the publisher. |
| **Awareness** | Objects only know about the Mediator. They have no idea who the other participating objects are. | The Publisher knows it has a list of subscribers (but treats them abstractly). Subscribers know exactly which Publisher they are watching. |

### How They Work Together

In practice, developers often use the **Observer pattern to implement the Mediator pattern**.

The components can act as *Publishers*, raising events when their state changes. The Mediator acts as the *Subscriber*, listening to those events and deciding how other components should react.

Are you looking to implement one of these patterns in a specific project or programming language right now?
*/
























/*
No, they are **not masked** from the perspective of how the program actually runs.

When you use an interface to hold an object, Java uses a mechanism called **Polymorphism**. Let’s break down exactly what is happening to the `colleagues` and `muted` structures under the hood.

---

### Understanding the "Split" View

When you write `IMediator mediator = new ChatMediator();`, the Java compiler splits your object into two conceptual layers:

1. **The Contract View (`IMediator`):** This dictates what methods you are *allowed* to call directly on the `mediator` variable (e.g., `sendAll`, `sendTo`, `register`).
2. **The Actual Object Layer (`ChatMediator`):** This is the living, breathing object inside your computer's memory. It contains the actual implementations of those methods, along with the `colleagues` list and the `muted` map.

---

### Why the Code Works Seamlessly

When `user1` calls `sendAll`, it delegates the work to the interface:

```java
this.mediator.sendAll(this.username, message);

```

Even though the `mediator` variable is declared as the interface type `IMediator`, Java knows at runtime that the underlying object is actually a `ChatMediator`.

Therefore, it jumps execution directly into the `ChatMediator.sendAll()` method. Because that method lives inside the `ChatMediator` class, **it has full, native access to its own `colleagues` list and `muted` map.** ---

### When *Would* They Be Masked?

They would only be considered "masked" if you tried to access them **directly from the `Main` class** using the `mediator` variable, like this:

```java
public static void main(String[] args) {
    IMediator mediator = new ChatMediator();

    // ❌ COMPILER ERROR! 
    // The compiler checks the IMediator interface contract, sees no "colleagues" field, and blocks it.
    int size = mediator.colleagues.size(); 
}

```

### Summary

Because the `colleagues` list and `muted` map are internal private structures meant only for the `ChatMediator` to manage its routing business, **you don't want the rest of the program to see them anyway.** This hiding of implementation details is a core principle of Object-Oriented Programming called **Encapsulation**. Your interface cleanly hides the "how" (the data structures) while exposing the "what" (the routing methods).

*/
