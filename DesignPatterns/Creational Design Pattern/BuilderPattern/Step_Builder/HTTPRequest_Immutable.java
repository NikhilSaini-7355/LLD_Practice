import java.util.*;
class HTTPRequest{
    // Trying to incorporate Perfect Immutability this time
    private  final String url;
    private  final String body;
    private  final String method;
    private  final Map<String,String> headers;
    private  final Map<String,String> queryParameters;
    private  final int timeout;
    
    private HTTPRequest(HTTPStepBuilder builder)
    {
        this.url = builder.url;
        this.body = builder.body;
        this.method = builder.method;
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.queryParameters = Collections.unmodifiableMap(new HashMap<>(builder.queryParameters));
        this.timeout = builder.timeout;
    }
    
    interface OptionalStep{
        OptionalStep withBody(String body);
        OptionalStep withQueryParameters(String key, String value);
        OptionalStep withTimeout(int timeout);
        HTTPRequest build();
    }
    
    interface HeaderStep{
        OptionalStep withHeader(String key, String value);
    }
    
    interface MethodStep{
        HeaderStep withMethod(String method);
    }
    
    interface URLStep{
        MethodStep withURL(String url);
    }

    static class HTTPStepBuilder implements URLStep,MethodStep,HeaderStep,OptionalStep{
        private   String url;
        private   String body;
        private   String method;
        private   Map<String,String> headers;
        private   Map<String,String> queryParameters;
        private   int timeout;
        
        public HTTPStepBuilder()
        {
            url = "";
            body = "";
            method = "";
            headers = new HashMap<>();
            queryParameters = new HashMap<>();
            timeout = 0;
        }
        
        public MethodStep withURL(String url)
        {
            this.url = url;
            return this;
        }
        
        public HeaderStep withMethod(String method)
        {
            this.method = method;
            return this;
        }
        
        public OptionalStep withHeader(String key, String value) 
        {
            this.headers.put(key, value);
            return this;
        }
        
        public OptionalStep withTimeout(int timeout)
        {
            this.timeout = timeout;
            return this;
        }
        
        public OptionalStep withQueryParameters(String key, String value)
        {
            this.queryParameters.put(key,value);
            return this;
        }
        
        public OptionalStep withBody(String body)
        {
            this.body = body;
            return this;
        }
        
        public HTTPRequest build()
        {
            // we can add Validation Logic Here, if any
            
            if(this.url==null || this.method==null || this.url.equals("") || this.method.equals(""))
            {
                throw new RuntimeException("invalid paramters");
            }
            
            return new HTTPRequest(this);
        }
    }
    
    static URLStep getBuilder()
    {
        return new HTTPStepBuilder();
    }
    
    public void execute()
    {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParameters.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParameters.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }

        System.out.println("Headers:");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println("  " + header.getKey() + ": " + header.getValue());
        }

        if (!body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }
}

/*
Question:- I had one doubt.
in withURL() , the object will be of type URLStep. as far as I know this interface will let the object access only those which are written inside the interface. then how the object is able to access this.url

Answer:-
That is a fantastic observation. You have hit on a core concept in Java regarding **Reference Types vs. Instance Types**.

You are 100% correct that an interface restricts what the *outside world* can see. But the restriction only applies to the **Caller**, not to the **Object itself** when it is executing its own internal code.

Here is the breakdown of why `this.url` works perfectly inside the method:

### 1. The Outside View (The Caller)

When a user calls `HTTPRequest.getBuilder()`, the reference returned is of type `URLStep`.
Because of this, the compiler restricts the user. The outside developer **cannot** do this:

```java
URLStep step = HTTPRequest.getBuilder();
step.url = "https://..."; // ERROR! The URLStep interface doesn't have a 'url' field.

```

From the outside, the object is wearing a "URLStep mask". The outside world only sees the `withURL()` method.

### 2. The Inside View (The Execution)

When the user calls `step.withURL("https://...")`, the execution jumps *inside* the `HTTPStepBuilder` class.

Even though the object is wearing a `URLStep` mask on the outside, **the actual object sitting in memory is still an `HTTPStepBuilder**`.

When you are inside the method:

```java
public MethodStep withURL(String url) {
    this.url = url; 
    return this;
}

```

The keyword `this` does not mean "the URLStep interface." The keyword `this` means "the actual, concrete object currently running this code." And since the actual object is an `HTTPStepBuilder`, it has full access to all of its own private fields (`this.url`, `this.method`, etc.).

### Summary Analogy

Imagine you are an employee at a company, and you wear a badge that says **"Cashier"** (the interface).

* To the customers (the outside world), you can only do Cashier things: `takeOrder()`, `giveChange()`. They aren't allowed to ask you about your private life.
* But when you are executing `takeOrder()`, **you are still you**. You still have access to your own private thoughts, memories, and pockets (`this.wallet`, `this.memories`), because you know who you really are, regardless of the badge the customer sees.

Java interfaces restrict what the **reference** can do from the outside, but they never erase the underlying fields of the **actual object** on the inside.

*/



















/*
This distinction—the separation between the **Reference Type** (the variable) and the **Instance Type** (the actual object in memory)—is the engine that drives object-oriented programming in Java. It is what makes Polymorphism possible.

To master this, you have to understand how Java splits responsibilities between the **Compiler** (which checks the rules before the code runs) and the **JVM** (which actually executes the code).

Here is everything you need to know about how Java handles this split.

## The Remote Control Analogy

The easiest way to visualize this is to think of the **Reference Type** as a remote control, and the **Instance Type** as the actual television.

```java
// Reference Type        // Instance Type
List<String> names = new ArrayList<>();

```

1. **The Reference Type (`List`) is the remote control.** It lives on the Stack. It dictates exactly which buttons are available to press. If a method isn't defined in the `List` interface, the compiler won't let you call it—even if the actual object supports it.
2. **The Instance Type (`ArrayList`) is the TV.** It lives on the Heap. It contains the actual circuits and logic. When you press a button on the remote (call a method), the TV decides *how* that action is actually performed.

## The Rules of Resolution

When you mix types, Java has strict rules about who gets the final say. This is where many developers get tripped up.

| Feature | Dictated By | Evaluated At |
| --- | --- | --- |
| **Available Methods** | Reference Type | Compile Time |
| **Method Execution** | Instance Type | Runtime (Dynamic Dispatch) |
| **Instance Fields** | Reference Type | Compile Time (Shadowing) |
| **Static Methods** | Reference Type | Compile Time (Hiding) |

### 1. Methods Follow the Object (Polymorphism)

If a method is overridden, Java waits until the exact moment the code runs (Runtime) to look at the actual object in memory and execute its specific version of the method. This is called **Dynamic Method Dispatch** or **Late Binding**.

### 2. Fields Follow the Reference (Shadowing)

This is a massive Java quirk: **Fields are not polymorphic.** If a parent class and a child class both define a variable with the same name, Java looks *only* at the Reference Type to decide which one to use.

```java
class Animal {
    String type = "Generic Animal";
    void speak() { System.out.println("Silence"); }
}

class Dog extends Animal {
    String type = "Canine"; // Shadows the parent field
    void speak() { System.out.println("Bark!"); } // Overrides the parent method
}

// In execution:
Animal myPet = new Dog();

System.out.println(myPet.type);  // Prints: "Generic Animal" (Follows Reference)
myPet.speak();                   // Prints: "Bark!" (Follows Instance)

```

## Moving Between Masks: Casting

Because objects can wear different masks, you can change the Reference Type to restrict or expand what you are allowed to do with the object.

### Upcasting (Implicit and Safe)

This is when you assign a specific object to a broader, more generic reference type (like a parent class or an interface). You are putting a simpler mask on a complex object. Java does this automatically because it is 100% safe.

```java
Dog myDog = new Dog();
Animal genericPet = myDog; // Upcasting. Automatic and safe.

```

### Downcasting (Explicit and Risky)

This is when you have a broad reference type, and you want to force it into a more specific reference type to access hidden methods. You are telling the compiler, *"Trust me, I know this Animal is actually a Dog."*

You must explicitly cast this, and if you are wrong, the JVM throws a `ClassCastException` and crashes your program.

```java
Animal myPet = new Dog(); 
// myPet.fetch(); // ERROR: The 'Animal' remote doesn't have a fetch button.

Dog realDog = (Dog) myPet; // Downcasting.
realDog.fetch(); // SUCCESS: Now we are using the 'Dog' remote.

```

## Safe Peeking: The `instanceof` Operator

To prevent your program from crashing during a downcast, you should always check what the actual Instance Type is before changing the Reference Type. You do this using `instanceof`.

```java
Animal unknownPet = getPetFromDatabase();

if (unknownPet instanceof Dog) {
    // Java 16+ Pattern Matching allows you to declare the new reference directly in the if-statement!
    Dog actualDog = (Dog) unknownPet;
    actualDog.fetch();
}

```
*/
