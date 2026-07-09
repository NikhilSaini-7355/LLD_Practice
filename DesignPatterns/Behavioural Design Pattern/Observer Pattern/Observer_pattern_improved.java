import java.util.*;

interface ISubscriber{
    void update();
}

interface IChannel{
    void subscribe(ISubscriber s);
    void unsubscribe(ISubscriber s);
    void notifySubscribers();
    String getName();
    String getLatestvideo();
    void uploadVideo(String title);
}

class Channel implements IChannel {
    List<ISubscriber> subscribers;
    String latestvideo;
    String name;
    public Channel(String name)
    {
        this.name = name;
        latestvideo = "";
        this.subscribers = new ArrayList<>();
    }
    
    public void subscribe(ISubscriber subscriber)
    {
        if(!subscribers.contains(subscriber))
        {
            subscribers.add(subscriber);
        }
    }
    
    public void unsubscribe(ISubscriber subscriber){
        if(subscribers.contains(subscriber))
        {
            subscribers.remove(subscriber);
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getLatestvideo()
    {
        return latestvideo;
    }
    
    public void notifySubscribers(){
        for(ISubscriber subs: subscribers)
        {
            subs.update();
        }
    }
    
    public void uploadVideo(String title)
    {
        latestvideo = title;
        notifySubscribers();
    }
}

class Subscriber implements ISubscriber{
    String name;
    IChannel channel;
    public Subscriber(String name, IChannel channel)
    {
        this.name = name;
        this.channel = channel;
    }
    
    public void update()
    {
        System.out.println("Hey "+name+" "+channel.getName()+" has uploaded "+ channel.getLatestvideo()); 
    }
}

public class Main{
    public static void main(String[] args)
    {
        IChannel channel1 = new Channel("Mr. Beast");
        IChannel channel2 = new Channel("Striver");
        
        ISubscriber subscriber1 = new Subscriber("Raj",channel1);
        ISubscriber subscriber2 = new Subscriber("Mohit",channel2);
        
        channel1.subscribe(subscriber1);
        channel2.subscribe(subscriber2);
        
        channel1.uploadVideo("1 Trillion Dollars");
        channel2.uploadVideo("DP Padhle bhai");
        
        channel2.unsubscribe(subscriber2);
        
        channel1.uploadVideo("Hello Beast");
        channel2.uploadVideo("Graph Padhle Bhai");
    }
}


/*
Yes, **this code is significantly better!** In fact, it is excellent.

By adding `getName()`, `getLatestvideo()`, and `uploadVideo(String title)` directly into the `IChannel` interface, you have successfully decoupled your system. Now, your `Subscriber` class and your `main` method can interact entirely through interfaces (`IChannel` and `ISubscriber`) without needing to know anything about the concrete `Channel` implementation.

---

### The Improved Parts of the Code & Explanation

The major improvements happen in three key areas:

#### 1. The `IChannel` Interface Upgrade

```java
interface IChannel {
    void subscribe(ISubscriber s);
    void unsubscribe(ISubscriber s);
    void notifySubscribers();
    String getName();            // <-- New addition
    String getLatestvideo();     // <-- New addition
    void uploadVideo(String title); // <-- New addition
}

```

* **Explanation:** Previously, your interface only defined how to attach/detach listeners. By adding these three methods, you have defined a complete **contract** for what a channel is supposed to do. Any class that wants to be a channel in the future (like a `TwitchChannel` or a `NewsChannel`) is now forced to implement these methods.

#### 2. Clean Execution inside `Subscriber`

```java
class Subscriber implements ISubscriber {
    String name;
    IChannel channel; // Safely remains an Interface type

    // ... Constructor ...

    public void update() {
        // Safe, clean abstraction using interface methods!
        System.out.println("Hey "+name+" "+channel.getName()+" has uploaded "+ channel.getLatestvideo()); 
    }
}

```

* **Explanation:** Because `getName()` and `getLatestvideo()` are now explicitly declared in the `IChannel` contract, Java allows the `Subscriber` to call them smoothly. You no longer have compilation errors, and you don't break encapsulation rules by trying to access fields directly.

#### 3. No Type-Casting in `Main`

```java
public static void main(String[] args) {
    IChannel channel1 = new Channel("Mr. Beast");
    // ...
    channel1.uploadVideo("1 Trillion Dollars"); // Works perfectly without casting!
}

```

* **Explanation:** In your previous code, if `channel1` was initialized as an `IChannel`, you had to write `((Channel) channel1).uploadVideo(...)` to trigger an upload. Now, because `uploadVideo` belongs to the interface contract, you can call it directly on your `IChannel` reference.

---

### Why this follows the true Observer Design Pattern

This is a textbook implementation of **Program to an interface, not an implementation**.

If you wanted to add a completely new type of channel later—say, an `InstagramLive` class—you would only need to write `class InstagramLive implements IChannel`. Your `Subscriber` class wouldn't need a single line of code changed to support it because it only cares about the `IChannel` interface, not the concrete class. That is the definition of clean, scalable architecture!
*/
