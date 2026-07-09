import java.util.*;

interface ISubscriber{
    void update();
}

interface IChannel{
    void subscribe(ISubscriber s);
    void unsubscribe(ISubscriber s);
    void notifySubscribers();
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
         // SYSTEM ERROR WILL OCCUR HERE
         // name = subscriber.name; 
         
         // COMMENT COMPLETION: 
         // ...here it will not be able to find name attribute because the parameter type is 'ISubscriber' (the interface). 
         // Java only allows access to methods/variables declared inside the interface itself. The 'ISubscriber' interface 
         // only declares the update() method, not the 'name' field. The compiler does not know that the underlying object is a 'Subscriber' class.
         
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
             // SYSTEM ERROR WILL OCCUR HERE
             // name = subs.name; 
             
             // COMMENT COMPLETION: 
             // ...here it will not be able to find name attribute because 'subs' is retrieved as an 'ISubscriber' reference type. 
             // Just like in the subscribe method, the interface hides the specific fields of the concrete 'Subscriber' class.
             
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
    Channel channel;
    public Subscriber(String name, Channel channel)
    {
        this.name = name;
        this.channel = channel;
    }
    
    public void update()
    {
        // THIS COMPILES PERFECTLY
        System.out.println("Hey "+name+" "+channel.name+" has uploaded "+ channel.latestvideo); 
        // COMMENT COMPLETION: 
        // ...here it is able to find name because we are currently inside the 'Subscriber' class scope. 
        // The execution environment is directly working with the concrete fields ('this.name' and 'this.channel') 
        // belonging to this specific object instance, meaning interface restrictions no longer mask these variables.
    }
}

public class Main{
    public static void main(String[] args)
    {
        Channel channel1 = new Channel("Mr. Beast");
        Channel channel2 = new Channel("Striver");
        
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
