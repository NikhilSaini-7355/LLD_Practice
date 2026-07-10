interface IDataService{
    String fetchData();
}

class DataService implements IDataService{
    public DataService()
    {
        System.out.println("[DataService] Initialized (simulating remote setup)");
    }
    
    @Override
    public String fetchData()
    {
        return "[DataService] Data from server";
    }
}

class DataProxy implements IDataService{
    DataService dataService;
    public DataProxy()
    {
        dataService = new DataService();
    }
    @Override
    public String fetchData()
    {
      System.out.println("[DataProxy] Connecting to remote service...");
      return dataService.fetchData();
    }
}

public class Main{
    public static void main(String[] args)
    {
        IDataService dataProxy = new DataProxy();
        System.out.println(dataProxy.fetchData());
    }
}



/*
class DataProxy implements IDataService{
    private DataService dataService;
    
    public DataProxy() {
        dataService = null;
    }
    
    @Override
    public synchronized String fetchData() { // Thread-safe lazy initialization
        if(dataService == null) {
            System.out.println("[DataProxy] Connecting to remote service...");
            dataService = new DataService();
        }
        return dataService.fetchData();
    }
}
*/

/*
Strictly speaking, the **second snippet** is closer to the intent of a **Remote Proxy**.

Here is the breakdown of why, and the distinction between the two patterns:

### 1. The First Snippet = Virtual Proxy

As mentioned earlier, the first snippet is a textbook **Virtual Proxy**. Its entire purpose is to delay the creation of an expensive local object (lazy initialization) until the exact moment it is needed to save memory and CPU.

### 2. The Second Snippet = Remote Proxy (In Spirit)

The second snippet is closer to a **Remote Proxy**.

In distributed systems, a Remote Proxy acts as a local stub for an object or service that lives in a completely different address space (like another node, a microservice, or behind a message broker). Its primary job is to hide the network boundary from the client.

* **Why Snippet 2 aligns better:** When building an RPC (Remote Procedure Call) client or a network stub, it is common to initialize the network channels or connection pools eagerly when the proxy is created. The proxy needs to be fully wired and ready to marshal/unmarshal data over the network the moment a method is called. Snippet 2's eager initialization (`realService = new RealDataService();`) mimics this behavior of setting up the communication channel upfront.

### The Catch: Why neither is a *true* Remote Proxy

While the second snippet has the right *intent*, both snippets fail to implement a true Remote Proxy mechanically because of one specific line:

```java
realService = new RealDataService(); 

```

A genuine Remote Proxy **never** instantiates the real object using the `new` keyword, because the real object doesn't exist on the same machine. Instead, a true Remote Proxy constructor would do something like this:

1. Look up a remote reference (like using Java RMI's `Naming.lookup()`).
2. Initialize an HTTP/gRPC client to send payloads to a remote endpoint.
3. Establish a connection to a message queue to dispatch tasks to a consumer.
**The Verdict:** If you are asked this in an interview or an exam, the **second snippet** is the intended answer for a Remote Proxy, while the **first snippet** is the definitive answer for a Virtual Proxy.

  */
