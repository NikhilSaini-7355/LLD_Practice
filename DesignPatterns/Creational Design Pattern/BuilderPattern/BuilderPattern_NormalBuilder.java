import java.util.*;

class HTTPRequest{
    private String url;
    private String method;
    private Map<String,String> headers;
    private Map<String, String> queryParameters;
    private String body;
    private int timeout;
    
    private HTTPRequest(){}
    
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
        
        public HTTPRequestBuilder withHeader(Map<String, String> headers)
        {
            req.headers = headers;
            return this;
        }
        
        public HTTPRequestBuilder withQueryParameters(Map<String, String> queryParameters)
        {
            req.queryParameters = queryParameters;
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
            if(req.url.length()==0)
            {
                return null;
            }
            
            if(req.method.length()==0)
            {
                return null;
            }
            
            if(req.timeout == -1)
            {
                return null;
            }
            
            // If all validation passes, then finally return the formed object
            return req;
        }
    }
    
    public void execute()
    {
        System.out.println("Executing HTTPRequest");
        System.out.println(this.url);
        System.out.println(this.method);
        System.out.println(this.body);
        System.out.println(this.timeout);
        System.out.println(this.headers);
        System.out.println(this.queryParameters);
    }
}

public class Main{
    public static void main(String[] args)
    {
        HTTPRequest request = new HTTPRequest.HTTPRequestBuilder().withURL("hello.com").withTimeout(20).withBody("hello world").withMethod("POST").build();
        request.execute();
    }
}

// TYPE OF NESTED CLASS USED PIN BUILDER PATTERN
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


/*
The **Builder Design Pattern** is a creational pattern designed to solve the problems associated with creating complex objects.

When an object requires a lot of fields, nested objects, or heavy configuration to initialize, standard object creation methods (like constructors) quickly break down.

Here are the primary issues that the Builder pattern solves:

---

## 1. The "Telescoping Constructor" Anti-Pattern

Without the Builder pattern, if a class has optional parameters, developers often write multiple constructors with increasing numbers of arguments (telescoping).

### The Issue:

```java
// Hard to read, easy to mix up parameters of the same type
Computer comp = new Computer("Intel i7", "16GB", "1TB SSD", true, false, true);

```

If you want to create a computer with a CPU, RAM, and a graphics card, but *no* extra storage or WiFi card, you are forced to pass `null` or `false` values into a massive constructor. It becomes unreadable, error-prone, and a nightmare to maintain.

### The Builder Solution:

The Builder pattern allows you to write clean, readable code using method chaining. You only call the methods for the parameters you actually care about.

```java
Computer comp = new ComputerBuilder()
                    .setCPU("Intel i7")
                    .setRAM("16GB")
                    .setGraphicsCard(true)
                    .build(); // Everything else defaults cleanly

```

---

## 2. Preventing "Immutability" Breakdown (The Setter Problem)

To avoid telescoping constructors, a common alternative is to use a default no-argument constructor and then call a long list of setter methods.

### The Issue:

```java
Computer comp = new Computer();
comp.setCPU("Intel i7");
comp.setRAM("16GB");
// ... Object is in a temporary "mutated" or incomplete state here
comp.setStorage("1TB");

```

This introduces two massive problems:

* **Incomplete Objects:** The object exists in an invalid, half-baked state while setters are being called. If another thread accesses it mid-setup, it will crash.
* **Loss of Immutability:** Because you have to expose public `setX()` methods, anyone can alter the object's properties *after* it has been created.

### The Builder Solution:

The Builder collects all the configuration data first. The target object itself remains **immutable** (with `final` fields and no setters). The actual object is only instantiated at the very end when `.build()` is called, ensuring it is 100% valid from its first second of life.

---

## 3. Violating the Single Responsibility Principle (SRP)

When a class is responsible for both its **core logic** and the **complex logic of how to build itself**, it takes on too much responsibility.

### The Issue:

If a `User` profile requires parsing complex strings, validating phone numbers, or fetching default profile images during creation, stuffing all that validation and parsing logic inside the `User` class pollutes it.

### The Builder Solution:

The Builder pattern isolates the construction logic into a completely separate class (`UserBuilder`). The core class focuses strictly on what it's supposed to do *after* it is born, while the Builder handles the messy mechanics of birth.

---

## 4. Building Different Representations of the Same Product

Sometimes you need to create different variations of the same complex object using the same construction steps.

### The Issue:

If you are building a `Document` object, you might want to build an HTML version, a PDF version, or a Markdown version. Hardcoding this inside a single class results in an explosion of conditional `if/else` statements.

### The Builder Solution:

You can introduce a **Director** class. The Director defines the steps to build something (e.g., `buildHeader()`, `buildBody()`, `buildFooter()`), but you can pass different Builders to it.

* Pass an `HTMLBuilder` $\rightarrow$ get an HTML page.
* Pass a `PDFBuilder` $\rightarrow$ get a PDF document.

---

### Summary Checklist: When should you use it?

* [x] Your class has more than 4 or 5 optional constructor parameters.
* [x] You want to ensure the resulting object is immutable (`read-only`).
* [x] The creation process involves complex validation or parsing.
* [x] You need to create different representations of the same object structure.

Would you like to see a complete code implementation of a thread-safe Builder pattern to see how the inner class structure looks?
*/
