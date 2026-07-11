import java.util.*;

class HTTPRequest{
    private String url;
    private String method;
    private Map<String,String> headers;
    private Map<String,String> queryParameters;
    private String body;
    private int timeout;
    
    private HTTPRequest(){
        url = "";
        method = "";
        headers = new HashMap<>();
        queryParameters = new HashMap<>();
        body = "";
        timeout = -1;
    }
    
    static class HTTPRequestBuilder
    {
        private HTTPRequest req;
        
        public HTTPRequestBuilder(){
            req = new HTTPRequest();
        }
        public HTTPRequestBuilder withURL(String url)
        {
            req.url = url;
            return this;
        }
        
        public HTTPRequestBuilder withMethod(String method)
        {
            req.method = method;
            return this;
        }
        
        public HTTPRequestBuilder withHeader(String key, String value)
        {
            req.headers.put(key,value);
            return this;
        }
        
        public HTTPRequestBuilder withQueryParameters(String key, String value)
        {
            req.queryParameters.put(key,value);
            return this;
        }
        
        public HTTPRequestBuilder withBody(String body)
        {
            req.body = body;
            return this;
        }
        
        public HTTPRequestBuilder withTimeout(int timeout)
        {
            req.timeout = timeout;
            return this;
        }
        
        public HTTPRequest build()
        {
            // Do Validation Logic here if any
            if(req.url != null && req.url.length()==0)
            {
                throw new RuntimeException("URL cannot be empty");
            }
            
            if(req.method.length()==0)
            {
                throw new RuntimeException("method cannot be empty");
            }
            
            if(req.timeout == -1)
            {
                throw new RuntimeException("timeout should be given");
            }
            
            // If all validation passes, then finally return the formed object
            return req;
        }
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
        
        if (!headers.isEmpty()) {
        System.out.println("Headers:");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println("  " + header.getKey() + ": " + header.getValue());
         }
        }
        
        if (body != null && !body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }
}

public class Main{
    public static void main(String[] args)
    {
        HTTPRequest request = new HTTPRequest.HTTPRequestBuilder().withURL("hello.com").withTimeout(20).withBody("hello world").withMethod("POST").build();
        request.execute();
    }
}


// But the object attributes are mutable. To follow one of the requirement of Builder Pattern, the attributes of object should be IMMUTABLE. To Ensure IMMUTABILITY the best code is:-
import java.util.*;

public class HTTPRequest {
    // 1. All fields are final to guarantee immutability
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final Map<String, String> queryParameters;
    private final String body;
    private final int timeout;
    
    // 2. Private constructor maps the builder's state to the object
    private HTTPRequest(HTTPRequestBuilder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.timeout = builder.timeout;
        // Make the maps unmodifiable so they can't be changed after creation
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.queryParameters = Collections.unmodifiableMap(new HashMap<>(builder.queryParameters));
    }
    
    // 3. Builder class holds its OWN state
    public static class HTTPRequestBuilder {
        private String url;
        private String method;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParameters = new HashMap<>();
        private String body = "";
        private int timeout = -1;
        
        public HTTPRequestBuilder withURL(String url) {
            this.url = url;
            return this;
        }
        
        public HTTPRequestBuilder withMethod(String method) {
            this.method = method;
            return this;
        }
        
        public HTTPRequestBuilder withHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }
        
        public HTTPRequestBuilder withQueryParameter(String key, String value) {
            this.queryParameters.put(key, value);
            return this;
        }
        
        public HTTPRequestBuilder withBody(String body) {
            this.body = body;
            return this;
        }
        
        public HTTPRequestBuilder withTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public HTTPRequest build() {
            // 4. Fixed Validation Logic
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalStateException("URL cannot be empty or null");
            }
            if (method == null || method.trim().isEmpty()) {
                throw new IllegalStateException("Method cannot be empty or null");
            }
            if (timeout <= 0) {
                throw new IllegalStateException("Timeout must be greater than 0");
            }
            
            // 5. Create the immutable object
            return new HTTPRequest(this);
        }
    }
    
    public void execute() {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParameters.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParameters.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }
        
        if (!headers.isEmpty()) {
            System.out.println("Headers:");
            for (Map.Entry<String, String> header : headers.entrySet()) {
                System.out.println("  " + header.getKey() + ": " + header.getValue());
            }
        }
        
        if (body != null && !body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }
}

class Main {
    public static void main(String[] args) {
        HTTPRequest request = new HTTPRequest.HTTPRequestBuilder()
            .withURL("https://api.hello.com")
            .withMethod("POST")
            .withHeader("Content-Type", "application/json")
            .withQueryParameter("user", "123")
            .withBody("{ \"message\": \"hello world\" }")
            .withTimeout(20)
            .build();
            
        request.execute();
    }
}

// TYPE OF NESTED CLASS USED IN BUILDER PATTERN
/*
In the Builder pattern, a **Static Nested Class** is almost universally used.

Here is why: the entire purpose of a Builder is to construct an instance of the outer class.

If the Builder were a non-static inner class, it would be tied to an *existing* instance of the outer class. You would have a chicken-and-egg problem: you would need an instance of the outer class to create the Builder, but you need the Builder to create the outer class!

Because a static nested class is independent of any outer instance, you can instantiate the Builder first, configure it, and then have it generate the outer class for you.

## The Builder Pattern in Action

Here is how a static nested class is typically structured to create an immutable `User` object:

```java
public class User {
    // 1. Fields are typically final (immutable)
    private final String name; 
    private final String email;
    private final int age;     

    // 2. Private constructor: Only the Builder can create a User
    private User(UserBuilder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }

    // Getters omitted for brevity...

    // 3. The Static Nested Class Builder
    public static class UserBuilder {
        private String name;
        private String email;
        private int age;

        // 4. Constructor for mandatory fields
        public UserBuilder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // 5. Methods for optional fields (returning 'this' for method chaining)
        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        // 6. The build method that finally creates the outer class instance
        public User build() {
            return new User(this);
        }
    }
}

```

### How to use it:

You instantiate the static nested builder directly using `new OuterClass.Builder()`, chain your optional configurations together, and call `build()` at the very end.

```java
// We don't need a User instance to start building!
User myUser = new User.UserBuilder("Alice", "alice@example.com")
                      .age(28)
                      .build();

```
*/





// INFO ABOUT NESTED CLASSES IN JAVA

/*
In Java, nested classes are classes defined within another class. They provide a way to logically group classes that are only used in one place, increase encapsulation, and improve code readability.

It is important to clarify the terminology: **An "Inner Class" is a specific type of "Non-static Nested Class."**

---

### 1. Static Nested Class

A static nested class is essentially a top-level class that has been nested within another class for packaging convenience. It does not have access to the instance members (fields or methods) of the outer class; it can only access static members of the outer class.

* **Key characteristic:** Does not require an instance of the outer class to be instantiated.

#### Code Example

```java
public class OuterClass {
    private static String staticMessage = "Hello from Outer";

    public static class StaticNestedClass {
        public void display() {
            // Can access static member
            System.out.println("Accessing: " + staticMessage);
        }
    }
}

// Usage:
// OuterClass.StaticNestedClass nested = new OuterClass.StaticNestedClass();
// nested.display();

```

---

### 2. Non-Static Nested Class (Inner Class)

A non-static nested class is associated with an instance of the outer class. Because of this association, an inner class has direct access to all members of the outer class, including private instance variables and methods.

* **Key characteristic:** Requires an instance of the outer class to be created first.

#### Code Example

```java
public class OuterClass {
    private String instanceMessage = "Hello from Instance";

    public class InnerClass {
        public void display() {
            // Can access instance member
            System.out.println("Accessing: " + instanceMessage);
        }
    }
}

// Usage:
// OuterClass outer = new OuterClass();
// OuterClass.InnerClass inner = outer.new InnerClass();
// inner.display();

```

---

### Summary Comparison

| Feature | Static Nested Class | Inner Class (Non-static) |
| --- | --- | --- |
| **Relationship** | Independent of outer instance. | Dependent on outer instance. |
| **Access** | Only to static members of outer class. | Access to all members (static & instance). |
| **Instantiation** | `new Outer.Nested()` | `outerInstance.new Inner()` |
| **Memory** | Lower overhead. | Higher overhead (holds reference to outer). |

---

### Important Nuance: Variations of Inner Classes

While the term "Inner Class" technically refers to the non-static member class above, Java also includes two special types of inner classes:

1. **Local Inner Class:** A class defined inside a method body. It is only accessible within that method.
2. **Anonymous Inner Class:** A class defined without a name, usually used to instantiate an object with certain "extras" (like overriding a method) on the fly. This is very common in GUI programming or when implementing interfaces like `Runnable`.

#### Quick Example of Anonymous Inner Class

```java
// Implementing an interface on the fly
Runnable myRunnable = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running in an anonymous inner class!");
    }
};

```

Would you like to see a more complex example involving how these classes handle scope and "this" references in nested environments?
*/

/*
In Java, a class defined within another class is called a **nested class**. They exist to logically group classes that are only used in one place, increase encapsulation, and keep code closer to where it is used.

Before diving into the code, it is helpful to clarify the terminology, as it often causes confusion: **"Inner class" is simply Java's official term for a non-static nested class.**

Here is the breakdown of how they work and how to use them.

## 1. Static Nested Class

A static nested class is essentially a standard, top-level class that has been tucked inside another class purely for packaging convenience.

* **Access:** It can only access the `static` members (variables and methods) of its outer class. It **cannot** directly access instance variables or methods.
* **Instantiation:** Because it is static, you do not need an instance of the outer class to create an instance of the nested class.

```java
public class OuterStatic {
    private static String staticSecret = "Static Outer Secret";
    private String instanceSecret = "Instance Outer Secret";

    // Static Nested Class
    public static class StaticNested {
        public void display() {
            // ✅ Can access static members of the outer class
            System.out.println("Accessing: " + staticSecret);
            
            // ❌ Cannot access instance members. This would cause a compiler error:
            // System.out.println(instanceSecret); 
        }
    }
}

class Main {
    public static void main(String[] args) {
        // Instantiating a static nested class does NOT require an outer object
        OuterStatic.StaticNested nested = new OuterStatic.StaticNested();
        nested.display();
    }
}

```

## 2. Non-Static Nested Class (Inner Class)

An inner class is associated directly with an *instance* of its outer class. You can think of it as another instance member, just like a field or a method.

* **Access:** It has unrestricted access to all members of the outer class, including `private` instance variables and methods.
* **Instantiation:** You **must** create an instance of the outer class before you can instantiate the inner class.

```java
public class OuterInstance {
    private String secret = "Outer's Private Secret";

    // Non-Static Nested Class (Inner Class)
    public class Inner {
        public void display() {
            // ✅ Can directly access any instance member of the outer class
            System.out.println("Accessing: " + secret);
        }
    }
}

class Main {
    public static void main(String[] args) {
        // 1. First, create the outer object
        OuterInstance outer = new OuterInstance();
        
        // 2. Use the outer object to instantiate the inner class
        OuterInstance.Inner inner = outer.new Inner();
        inner.display();
    }
}

```

---

## The "Other" Inner Classes

While the example above shows a **Member Inner Class** (defined at the class level), Java also has two specialized types of inner classes:

1. **Local Inner Class:** Defined entirely within a method body. It is only visible inside that specific method and is often used to encapsulate logic that is completely isolated to a single function.
2. **Anonymous Inner Class:** An inner class without a name, declared and instantiated in a single expression. These are extremely common when implementing interfaces on the fly (like `Runnable` or UI event listeners) without wanting to create a whole new named file.

## Summary Comparison

| Feature | Static Nested Class | Inner Class (Non-static) |
| --- | --- | --- |
| **Association** | Bound to the outer *class*. | Bound to an outer *instance*. |
| **Member Access** | `static` members only. | All members (`static` and instance). |
| **Creation Syntax** | `new Outer.Nested()` | `outerObj.new Inner()` |
| **Memory Implication** | Independent footprint. | Holds a hidden reference to the outer object. |
*/
