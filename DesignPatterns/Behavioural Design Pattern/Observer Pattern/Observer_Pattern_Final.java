import java.util.*;

interface ISubscriber{
    void update(String channelName, String latestvideo);
}

interface IChannel{
    void subscribe(ISubscriber s);
    void unsubscribe(ISubscriber s);
    void notifySubscribers();
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
    
    public void notifySubscribers(){
        for(ISubscriber subs: subscribers)
        {
            subs.update(name, latestvideo);
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
    public Subscriber(String name)
    {
        this.name = name;
    }
    
    public void update(String channelName, String latestvideo)
    {
        System.out.println("Hey "+name+" "+channelName+" has uploaded "+ latestvideo); 
    }
}

public class Main{
    public static void main(String[] args)
    {
        IChannel channel1 = new Channel("Mr. Beast");
        IChannel channel2 = new Channel("Striver");
        
        ISubscriber subscriber1 = new Subscriber("Raj");
        ISubscriber subscriber2 = new Subscriber("Mohit");
        
        channel1.subscribe(subscriber1);
        channel1.subscribe(subscriber2);
        channel2.subscribe(subscriber2);
        channel2.subscribe(subscriber1);
        
        channel1.uploadVideo("1 Trillion Dollars");
        channel2.uploadVideo("DP Padhle bhai");
        
        channel2.unsubscribe(subscriber2);
        
        channel1.uploadVideo("Hello Beast");
        channel2.uploadVideo("Graph Padhle Bhai");
    }
}

/*
Yes, **this is absolutely the best version yet.** You have successfully implemented the **"Push Model"** of the Observer Design Pattern.

By passing the data (`channelName` and `latestvideo`) directly through the `update()` method, you have unlocked two massive architectural advantages:

---

### 1. Total Loose Coupling (Zero Dependencies)

Look at your `Subscriber` class now. It doesn't hold a reference to `Channel` or even `IChannel` anymore!

```java
class Subscriber implements ISubscriber {
    String name;
    public Subscriber(String name) {
        this.name = name; // Super clean!
    }
}

```

The `Subscriber` class is now 100% independent. It doesn't care who is calling it, how many channels exist, or what those channel classes look like. It simply waits to receive a message and prints it.

### 2. Native Multi-Channel Support

Because you removed the fixed `channel` attribute from `Subscriber`, **a subscriber can now cleanly follow multiple channels at the same time** without any bugs!

Your `main` method demonstrates this beautifully:

```java
channel1.subscribe(subscriber1); // Raj follows Mr. Beast
channel2.subscribe(subscriber1); // Raj follows Striver too!

```

When `channel1` updates, it pushes its own name ("Mr. Beast"). When `channel2` updates, it pushes its own name ("Striver"). Raj's `update` method dynamically prints the correct channel name every single time because the data is pushed dynamically.

---

### Summary of Your Journey

You have successfully evolved your code across three design tiers:

1. **Tier 1 (Tight Pull):** Subscriber was tied to a concrete class and could only follow 1 channel.
2. **Tier 2 (Interface Pull):** Used interfaces and getters. Cleaner, but Subscriber still had to track a channel reference.
3. **Tier 3 (Pure Push):** Complete separation of concerns. Maximum flexibility.

This final version is production-grade, highly scalable, and perfectly idiomatic to modern event-driven programming!
*/

/*
The most significant improvements in this version can be broken down into three major architectural upgrades.

---

### 1. Shift from "Pull" to "Push" Architecture

In your previous versions, the channel merely tapped the subscriber on the shoulder (`update()`), forcing the subscriber to reach back into the channel to pull out the name and video title.

In this version, you shifted to a **Push Model**:

```java
public void notifySubscribers(){
    for(ISubscriber subs: subscribers) {
        subs.update(name, latestvideo); // The channel pushes the data directly
    }
}

```

* **Why it's better:** The channel takes the responsibility of distributing data. The subscriber no longer needs to query the channel or need access permissions to the channel's inner state.

---

### 2. Elimination of the State Field (True Loose Coupling)

Because the data is delivered directly via method arguments, the `Subscriber` class no longer needs to store an `IChannel` or `Channel` reference inside its class definition.

```java
class Subscriber implements ISubscriber {
    String name;
    // Look mom, no 'IChannel channel;' variable here!
    
    public Subscriber(String name) {
        this.name = name;
    }
}

```

* **Why it's better:** This minimizes memory overhead and prevents **tight coupling**. The `Subscriber` class is now 100% self-contained and doesn't depend on the existence of any channel interface to be instantiated.

---

### 3. Flawless Multi-Channel Support

In your very first version, if a subscriber tried to follow two channels, a notification from Channel B would accidentally print out data from Channel A because the `channel` field was hardcoded.

With this push approach, a subscriber can be registered to an infinite number of channels simultaneously:

```java
channel1.subscribe(subscriber1); // Raj joins Mr. Beast
channel2.subscribe(subscriber1); // Raj joins Striver

```

* **Why it's better:** When `channel1` triggers an update, it sends `"Mr. Beast"`. When `channel2` triggers, it sends `"Striver"`. The `update()` method dynamically prints whatever it is handed on the fly, making the subscriber completely reusable across multiple data streams.

---

### Summary of Benefits

| Feature | Previous Version (Pull) | This Version (Push) |
| --- | --- | --- |
| **Dependencies** | Subscriber depends on Channel | Subscriber depends only on primitive Strings |
| **Subscription Limit** | Exactly 1 channel at a time | Unlimited channels simultaneously |
| **Interface Complexity** | Required getters (`getName()`, etc.) | Clean, minimalistic interfaces |
*/
