interface Engine{
    void start();
}

class Electric implements Engine{
    public void start()
    {
        System.out.println("Electric engine started");
    }
}

class Petrol implements Engine{
    public void start()
    {
        System.out.println("Petrol engine started");
    }
}
class Diesel implements Engine{
    public void start()
    {
        System.out.println("Diesel engine started");
    }
}

abstract class Car{
    protected Engine engine;
    abstract void drive();
    protected Car(Engine engine)
    {
        this.engine = engine;
    }
    public void setEngine(Engine engine)
    {
        this.engine = engine;
    }
}

class SUV extends Car{
    public SUV(Engine engine)
    {
        super(engine);
    }
    public void drive()
    {
        this.engine.start();
        System.out.println("Driving SUV");
    }
}

class Sedan extends Car{
    public Sedan(Engine engine)
    {
        super(engine);
    }
    public void drive()
    {
        this.engine.start();
        System.out.println("Driving Sedan");
    }
}

class Hatchback extends Car{
    public Hatchback(Engine engine)
    {
        super(engine);
    }
    public void drive()
    {
        this.engine.start();
        System.out.println("Driving Hatchback");
    }
}



public class Main{
    public static void main(String[] args)
    {
        Car suv = new SUV(new Electric());
        Car sedan = new Sedan(new Petrol());
        Car hatchback = new Hatchback(new Diesel());
        
        suv.setEngine(new Electric());
        suv.drive();
        suv.setEngine(new Petrol());
        suv.drive();
        suv.setEngine(new Diesel());
        suv.drive();
        
        System.out.println("========================================================");
        sedan.setEngine(new Electric());
        sedan.drive();
        sedan.setEngine(new Petrol());
        sedan.drive();
        sedan.setEngine(new Diesel());
        sedan.drive();
        
        System.out.println("========================================================");
        hatchback.setEngine(new Electric());
        hatchback.drive();
        hatchback.setEngine(new Petrol());
        hatchback.drive();
        hatchback.setEngine(new Diesel());
        hatchback.drive();
    }
}



/*
Question :- can we use strategy design pattern instead of bridge design pattern. I think we can replace bridge design pattern with strategy design pattern, only we need to have two families of strategies in our design pattern. We can select any one strategy from the first family and any one strategy from the second family?

Ans:-
You have actually hit on a very profound realization! Conceptually, you are absolutely right: if you look at the class diagrams, the **Bridge** pattern and the **Strategy** pattern look nearly identical. Both rely on composition instead of inheritance (favoring "has-a" over "is-a").

However, while you *can* technically implement a Bridge using two families of Strategies, the software engineering community separates them because **their intent, design goals, and how they evolve are completely different.**

Here is a breakdown of why they aren't completely interchangeable in practice, and why we don't just call a Bridge "two Strategies."

---

## 1. The Core Difference: Intent

* **Strategy Pattern:** Focuses on **behavior**. It allows you to swap out different algorithms or behaviors at runtime. The client usually knows exactly what strategy it is picking.
* **Bridge Pattern:** Focuses on **structural growth**. It is used *before* you write the code to prevent a "class explosion" when you have two independent dimensions of variance (Abstraction and Implementation). It hides the implementation from the client entirely.

---

## 2. Structural Comparison

Let's look at how your idea of "two families of strategies" compares to a standard Bridge pattern.

### The Bridge Approach

In a Bridge, the two sides have different roles: one is the **Abstraction** (the frontend/control part) and the other is the **Implementation** (the backend/platform part).

### Your "Dual Strategy" Approach

If you implement this using two Strategy families, your architecture looks like this:

```
[Context / Client]
       │
       ├───> [Strategy Family 1: Abstraction/GUI] (e.g., TouchUI, VoiceUI)
       │
       └───> [Strategy Family 2: Implementor/OS]  (e.g., LinuxAPI, WindowsAPI)

```

### Why the Bridge structure is usually preferred for this scenario:

1. **Tight Coupling in Strategy:** In your dual-strategy model, the Client or Context has to know about *both* families and manage how they interact. If `TouchUI` needs to call `LinuxAPI`, the client has to orchestrate that.
2. **Encapsulation in Bridge:** In a Bridge, the `Abstraction` (Family 1) *contains* the `Implementor` (Family 2). The client only talks to the Abstraction. The client doesn't need to know that the Implementor even exists.

---

## 3. When Your Idea Actually Makes Sense

Your idea of combining two Strategy families isn't wrong—it's just a different pattern variant often seen in **Policy-Based Design** or multi-dimensional configuration.

You *can* replace a Bridge with two Strategies if:

* The two families are completely independent and don't need to deeply collaborate.
* The client *wants* full control over mixing and matching both dimensions directly.

### Summary Comparison

| Feature | Bridge Pattern | Dual Strategy Approach |
| --- | --- | --- |
| **Primary Goal** | Separate interface from implementation so they can vary independently. | Allow swapping of two different types of behaviors at runtime. |
| **Client Awareness** | Client only knows about the Abstraction. | Client must manage and choose from both strategy families. |
| **Relationship** | Hierarchical: Abstraction *uses* Implementor. | Peer-like: Context *uses* Strategy A and Strategy B. |

So, while your structural intuition is 100% correct, we keep the **Bridge** pattern around because it structurally glues those two families together, shielding the client from the complexity of mixing and matching them manually.

---

Would you like to see a quick code example contrasting how a Client interacts with a Bridge versus interacting with a Dual-Strategy setup?

=================================================================================================================================================================================================================================================================================

Question:- how bridge interacts only with the abstraction?
Ans:-
To understand how the client interacts *only* with the abstraction, it helps to look at the structural hierarchy of the Bridge pattern.

In a Bridge pattern, the client does not orchestrate both sides. Instead, the **Abstraction** acts as a wrapper or a gateway that completely hides the **Implementor** from the client.

Here is exactly how that interaction works:

---

## The Structural Hierarchy

Notice the chain of dependency:


$$\text{Client} \longrightarrow \text{Abstraction} \longrightarrow \text{Implementor}$$

The client only ever holds a reference to the **Abstraction** interface. It has zero direct contact with the **Implementor** interface or any concrete implementations.

---

## Step-by-Step Interaction

Let's use a real-world analogy: **A Remote Control (Abstraction) and a TV (Implementor).**

### 1. Initialization (The Only Time the Client Sees Both)

When the application starts up (often in a main method or a dependency injection container), the concrete pieces are glued together.

* The client (or a factory) creates a `SonyTV` (Concrete Implementor).
* It passes that TV into the constructor of a `AdvancedRemote` (Refined Abstraction).

```java
// The client only keeps track of the Remote (Abstraction)
RemoteControl remote = new AdvancedRemote(new SonyTV());

```

### 2. The Client Triggers an Action

The user (client) wants to mute the TV. The client does not know how a Sony TV handles muting; it only knows that the remote has a `mute()` button.

```java
remote.mute(); // Client interacts ONLY with the abstraction

```

### 3. The Bridge Passes the Work Behind the Scenes

Inside the `AdvancedRemote` class, the `mute()` method is implemented by talking to the hidden TV object that was passed in during setup.

```java
public class AdvancedRemote extends RemoteControl {
    // The abstraction holds a hidden reference to the implementor
    public AdvancedRemote(TV tv) { super(tv); } 

    public void mute() {
        System.out.println("Remote: Mute button pressed.");
        this.tv.setVolume(0); // The Abstraction talks to the Implementor
    }
}

```

---

## Why This Setup is Powerful

Because the client interacts *only* with the abstraction, you get two massive benefits:

* **The Implementation can change at runtime:** The client can start by controlling a `SonyTV`. Mid-program, you can swap the internal TV reference to a `SamsungTV`. The client continues to call `remote.mute()` completely unaware that the underlying hardware just changed.
* **Clean Code Decoupling:** If you decide to completely rewrite how `SonyTV` handles volume control, you don't have to touch a single line of client code. The client's interface remains completely untouched.

=======================================================================================================================================================================================================================================================================================================

Question:- and how strategy need to interact with both the families?
Ans:-
In the **Strategy** pattern (or specifically, the "Dual Strategy" variation you proposed), the architectural dynamics change completely.

Unlike the Bridge pattern—where the Abstraction wraps and hides the Implementor—the Strategy pattern relies on a **Context** object. If you have two independent families of strategies, the Context (or the Client) must actively manage, hold references to, and coordinate the interaction between **both** families.

Here is how that interaction works structurally and behaviorally.

---

## 1. The Dual-Strategy Architecture

In this setup, the Context sits in the middle as the orchestrator. It does not hide one family from the other; instead, it acts as the meeting point for both.

```
             ┌─────────────────────────┐
             │         Context         │
             └────┬───────────────┬────┘
                  │               │
                  ▼               ▼
        ┌────────────────┐ ┌────────────────┐
        │  Strategy A    │ │  Strategy B    │
        │  (Interface)   │ │  (Interface)   │
        └────────────────┘ └────────────────┘

```

The Context has direct dependencies on **both** strategy interfaces. It is responsible for accepting them (usually via a constructor or setters) and invoking them when needed.

---

## 2. How They Interact (The Code Flow)

Let's ground this in a practical example. Imagine you are building a **Document Exporter** system.

* **Family 1 (Data Formatting):** JSONFormatter, XMLFormatter, CSVFormatter.
* **Family 2 (Delivery Method):** EmailDelivery, FileSystemDelivery, CloudStorageDelivery.

Here is how the Context interacts with both families to get the job done:

```java
// 1. Define the Strategy Interfaces
interface FormatterStrategy { String format(Data data); }
interface DeliveryStrategy  { void deliver(String formattedData); }

// 2. The Context interacts with BOTH families
class DocumentExporter {
    private FormatterStrategy formatter;
    private DeliveryStrategy delivery;

    // The client injects both strategies into the context
    public DocumentExporter(FormatterStrategy formatter, DeliveryStrategy delivery) {
        this.formatter = formatter;
        this.delivery = delivery;
    }

    // The Context orchestrates the interaction
    public void export(Data data) {
        // Step 1: Interact with Family A
        String result = formatter.format(data); 
        
        // Step 2: Pass the output of Family A into Family B
        delivery.deliver(result); 
    }
}

```

---

## 3. How the Client Sets It Up

Because these are two independent families of strategies, the **Client** has total freedom to mix and match them at runtime.

```java
// Client wants a JSON report sent via Email
DocumentExporter jsonEmailExporter = new DocumentExporter(
    new JSONFormatter(), 
    new EmailDelivery()
);
jsonEmailExporter.export(myData);

// Client instantly switches the behavior to XML saved to Disk
DocumentExporter xmlDiskExporter = new DocumentExporter(
    new XMLFormatter(), 
    new FileSystemDelivery()
);
xmlDiskExporter.export(myData);

```

---

## Summary of the Interaction Difference

* **In a Bridge:** The Client talks to `Family A` (Abstraction). `Family A` internally talks to `Family B` (Implementor). The client is oblivious to `Family B`.
* **In a Dual Strategy:** The Client creates both `Family A` and `Family B` and hands them to a `Context`. The `Context` acts as the middleman, calling `Family A` to get a result, and then passing that result to `Family B`.

This is why your idea works perfectly for **orthogonal behaviors** (like formatting vs. delivering), while the Bridge pattern is better suited for **structural encapsulation** (like a generic GUI system hiding platform-specific OS window APIs).
*/







