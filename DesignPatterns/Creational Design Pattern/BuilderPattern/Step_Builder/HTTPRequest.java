import java.util.*;
/* 
In Java, interfaces can declare attributes (fields), but they are always public, static, and final, even if you don't explicitly write those modifiers.

This means interface fields are constants, not instance variables.
*/

class HTTPRequest{
    // Trying to incorporate Perfect Immutability this time
    private  String url;
    private  String body;
    private  String method;
    private  Map<String,String> headers;
    private  Map<String,String> queryParameters;
    private  int timeout;
    
    private HTTPRequest()
    {
        url = "";
        body = "";
        method = "";
        headers = new HashMap<>();
        queryParameters = new HashMap<>();
        timeout = 0;
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
        private HTTPRequest request;
        public HTTPStepBuilder()
        {
            this.request = new HTTPRequest();
        }
        
        public MethodStep withURL(String url)
        {
            request.url = url;
            return this;
        }
        
        public HeaderStep withMethod(String method)
        {
            request.method = method;
            return this;
        }
        
        public OptionalStep withHeader(String key, String value) 
        {
            request.headers.put(key, value);
            return this;
        }
        
        public OptionalStep withTimeout(int timeout)
        {
            request.timeout = timeout;
            return this;
        }
        
        public OptionalStep withQueryParameters(String key, String value)
        {
            request.queryParameters.put(key,value);
            return this;
        }
        
        public OptionalStep withBody(String body)
        {
            request.body = body;
            return this;
        }
        
        public HTTPRequest build()
        {
            // we can add Validation Logic Here, if any
            
            if(request.url==null || request.method==null || request.url.equals("") || request.method.equals(""))
            {
                throw new RuntimeException("invalid paramters");
            }
            
            return request;
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


// MULTIPLE INHERITANCE IN JAVA IS NOT SUPPORTED THROUGH CLASSES BUT SUPPORTED THROUGH INTERFACES

/*  ### Does Java support multiple inheritance?

Java **does not support multiple inheritance of classes**, but it **does support multiple inheritance through interfaces**.

### Why doesn't Java allow multiple inheritance of classes?

The main reason is to avoid the **Diamond Problem**.

Consider:

```java
class A {
    void display() {
        System.out.println("A");
    }
}

class B extends A {
}

class C extends A {
}

// Not allowed in Java
class D extends B, C {
}
```

If both `B` and `C` override `display()`, then calling:

```java
D obj = new D();
obj.display();
```

would create ambiguity—which version should be called, `B`'s or `C`'s?

---

## Java supports multiple inheritance using interfaces

A class can implement multiple interfaces.

```java
interface Animal {
    void eat();
}

interface Pet {
    void play();
}

class Dog implements Animal, Pet {

    public void eat() {
        System.out.println("Dog is eating");
    }

    public void play() {
        System.out.println("Dog is playing");
    }
}

public class Main {
    public static void main(String[] args) {
        Dog d = new Dog();
        d.eat();
        d.play();
    }
}
```

**Output**

```
Dog is eating
Dog is playing
```

---

## What about default methods?

Since Java 8, interfaces can have `default` methods. If two interfaces provide the same default method, the implementing class must resolve the conflict.

```java
interface A {
    default void show() {
        System.out.println("A");
    }
}

interface B {
    default void show() {
        System.out.println("B");
    }
}

class Test implements A, B {

    @Override
    public void show() {
        A.super.show();    // or B.super.show()
    }
}

public class Main {
    public static void main(String[] args) {
        Test t = new Test();
        t.show();
    }
}
```

**Output**

```
A
```

---

## Summary

| Feature                                                   | Supported in Java?                               |
| --------------------------------------------------------- | ------------------------------------------------ |
| Multiple inheritance of classes (`extends A, B`)          | ❌ No                                             |
| Multiple inheritance using interfaces (`implements A, B`) | ✅ Yes                                            |
| Multiple interface inheritance with default methods       | ✅ Yes (conflicts must be resolved by overriding) |

**Key takeaway:** Java avoids multiple inheritance of classes to eliminate ambiguity (the Diamond Problem), while allowing multiple inheritance of behavior through interfaces.


*/
