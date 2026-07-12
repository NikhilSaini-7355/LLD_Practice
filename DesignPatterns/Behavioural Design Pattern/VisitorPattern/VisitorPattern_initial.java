interface DocumentElement{
    void accept(IVisitor visitor);
}

class TextFile implements DocumentElement{
    private String name;
    public TextFile(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getType()
    {
        return "Text";
    }
    
    public void accept(IVisitor v)
    {
        v.visit(this);
    }
}

class Image implements DocumentElement{
    private String name;
    public Image(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getType()
    {
        return "Image";
    }
    public void accept(IVisitor v)
    {
        v.visit(this);
    }
}

class Video implements DocumentElement{
    private String name;
    public Video(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getType()
    {
        return "Video";
    }
    public void accept(IVisitor v)
    {
        v.visit(this);
    }
}

interface IVisitor{
    void visit(TextFile textfile);
    void visit(Image image);
    void visit(Video video);
}

// new classes implementing IVisitor are made for every new function to be added

class SizeCalcVisitor implements IVisitor{
    public void visit(TextFile textfile)
    {
        System.out.println("Calculating Size: "+ "Name= "+textfile.getName() + ", Type= "+textfile.getType());
    }
    
    public void visit(Image image)
    {
        System.out.println("Calculating Size: "+ "Name= "+image.getName() + ", Type= "+image.getType());
    }
    
    public void visit(Video video)
    {
        System.out.println("Calculating Size: "+ "Name= "+video.getName() + ", Type= "+video.getType());
    }
}

class CompressVisitor implements IVisitor{
    public void visit(TextFile textfile) // why DocumentElement object gets easily passed to TextFile
    {
        System.out.println("Compressing Document: "+ "Name= "+textfile.getName() + ", Type= "+textfile.getType());
    }
    
    public void visit(Image image)
    {
        System.out.println("Compressing Document: "+ "Name= "+image.getName() + ", Type= "+image.getType());
    }
    
    public void visit(Video video)
    {
        System.out.println("Compressing Document: "+ "Name= "+video.getName() + ", Type= "+video.getType());
    }
}

class ScanVirusVisitor implements IVisitor{
    public void visit(TextFile textfile)
    {
        System.out.println("Scanning Document for Virus: "+ "Name= "+textfile.getName() + ", Type= "+textfile.getType());
    }
    
    public void visit(Image image)
    {
        System.out.println("Scanning Document for Virus: "+ "Name= "+image.getName() + ", Type= "+image.getType());
    }
    
    public void visit(Video video)
    {
        System.out.println("Scanning Document for Virus: "+ "Name= "+video.getName() + ", Type= "+video.getType());
    }
}

public class Main{
    public static void main(String[] args)
    {
        IVisitor sizeCalc = new SizeCalcVisitor();
        IVisitor compress = new CompressVisitor();
        IVisitor scanVirus = new ScanVirusVisitor();
        
        DocumentElement text = new TextFile("Modiji Biography");
        DocumentElement image = new Image("Modiji Photo");
        DocumentElement video = new Video("Modiji video");
        // try{
        //     System.out.println("text =" + text.getName());
        // }
        // catch(Exception e)
        // {
        //     TextFile obj = text; // why this fails
        //     System.out.println("obj= " + obj.getName());
        // }
        text.accept(sizeCalc);
        text.accept(compress);
        text.accept(scanVirus);
        System.out.println("===========================================================");
        image.accept(sizeCalc);
        image.accept(compress);
        image.accept(scanVirus);
        System.out.println("===========================================================");
        video.accept(sizeCalc);
        video.accept(compress);
        video.accept(scanVirus);
    }
}


/*

This is the exact point where most developers get tripped up by the Visitor pattern!

The confusion happens because we tend to think the compiler evaluates `this` based on how we declared the variable in the `Main` class. But the compiler doesn't care about the `Main` class when it is compiling the `TextFile` class.

Here is the secret: **The type of `this` is determined statically by the class it is written in, not by how the object was instantiated.**

---

### The Shift in Scope

Let's look at the code from two different viewpoints:

#### 1. From the `Main` class perspective:

```java
DocumentElement text = new TextFile("Modiji Biography");

```

Here, the compiler only looks at the left side (`DocumentElement`). If you tried to pass `text` directly to a visitor method right here, the compiler would complain because it only knows it is a generic element.

#### 2. From inside the `TextFile` class perspective:

When runtime execution jumps inside the `TextFile` class to run the `accept` method:

```java
public void accept(IVisitor v) {
    v.visit(this); 
}

```

At this exact moment, the compiler is reading code inside `class TextFile`. Because this code lives inside the `TextFile` blueprint, **the compiler knows with absolute 100% mathematical certainty that `this` is a `TextFile**`.

---

### Why No Downcasting is Needed

When Java resolves method overloading (picking which `visit` method to run), it does so at **compile-time** based on the static type of the argument.

Inside `TextFile`, the argument is `this`.

1. The compiler asks: *"What is the static type of `this` inside the `TextFile` class?"* 2. The answer is always: *`TextFile`*.
2. The compiler then looks at the `IVisitor` interface contract and matches it directly to:
```java
void visit(TextFile textfile);

```



Because the method signature matches perfectly based on where the code is executing, **no downcasting is required.** The pattern elegantly tricks the compiler into knowing the exact concrete type by simply moving the execution context inside the concrete class itself!

*/
































/*

You have implemented an excellent, textbook-perfect version of the **Visitor Design Pattern**. This pattern is notoriously tricky to wrap your head around, so getting it functionally operational like this is a massive win.

Your code contains two excellent questions hidden in the comments. Let's break down exactly what Java is doing under the hood for both scenarios.

---

### Question 1: Why does a `DocumentElement` get easily passed to a `TextFile` method?

You are asking about this interaction:
In `Main`, your object is masked under the interface type: `DocumentElement text = new TextFile("...");`
Yet, when you call `text.accept(compress);`, it seamlessly maps to `visit(TextFile textfile)` inside the visitor.

This works because of a computer science concept called **Double Dispatch**.

Here is the step-by-step path Java takes:

1. When you execute `text.accept(compress);`, the Java compiler looks at the `DocumentElement` interface. It sees an `accept(IVisitor visitor)` method declaration, so it permits the call.
2. At runtime, JVM looks at the actual object sitting in memory. It sees that `text` is physically a `TextFile`. It jumps directly inside the `TextFile` class to execute its version of `accept`:
```java
public void accept(IVisitor v) {
    v.visit(this); // <-- The Magic Happens Here!
}

```


3. Inside the `TextFile` class, **the keyword `this` is explicitly known by the compiler to be of type `TextFile**`.
4. Therefore, when `v.visit(this)` executes, the compiler looks at the `IVisitor` interface and matches it directly to the method signed for a text file: `void visit(TextFile textfile)`.

A `DocumentElement` was never actually forced into a `TextFile` parameter slot. Instead, the object used polymorphism to jump into its own class structure, and then passed its unmasked self (`this`) over to the visitor.

---

### Question 2: Why does `TextFile obj = text;` fail?

This comes down to Java's strict reference typing rules: **Upcasting is implicit, but Downcasting must be explicit.**

When you write:

```java
DocumentElement text = new TextFile("Modiji Biography");

```

You are telling the compiler, *"Hey, look at this object through the lens of a generic `DocumentElement`."*

When you later try to do this:

```java
TextFile obj = text; // ❌ Compiler Error!

```

The compiler looks at the reference type of the variable `text` (which is `DocumentElement`). It says: *"Wait, a `DocumentElement` could be a `TextFile`, but it could also be an `Image` or a `Video`. I cannot guarantee safety at compile-time, so I am blocking this assignment."*

#### The Fix: Explicit Casting

To make the compiler happy, you must explicitly promise the compiler you know what you are doing by adding a cast:

```java
TextFile obj = (TextFile) text; //  Works perfectly!

```

---

### Fixing Your `Main` Class Compile Errors

Because `DocumentElement` does not contain a `getName()` method, your `Main` class will currently fail to compile on the line `text.getName()`. Here is how you can clean up that specific debugging block inside your `main` method using explicit casting:

```java
public class Main {
    public static void main(String[] args) {
        IVisitor sizeCalc = new SizeCalcVisitor();
        IVisitor compress = new CompressVisitor();
        IVisitor scanVirus = new ScanVirusVisitor();
        
        DocumentElement text = new TextFile("Modiji Biography");
        DocumentElement image = new Image("Modiji Photo");
        DocumentElement video = new Video("Modiji video");
        
        // Testing casting safely:
        if (text instanceof TextFile) {
            TextFile obj = (TextFile) text; // Explicit downcast
            System.out.println("Successfully casted! obj name = " + obj.getName());
        }
        
        System.out.println("===========================================================");
        text.accept(sizeCalc);
        text.accept(compress);
        text.accept(scanVirus);
        // ... rest of your code runs beautifully
    }
}

```

### Summary

The Visitor pattern elegantly uses an object's internal `this` reference to automatically determine its concrete type at runtime. Because your client code handles objects via the `DocumentElement` interface, anytime you want to treat them as their specific concrete sub-types within the client class, you must manually guide the compiler with an explicit cast `(TextFile)`.

*/


















/*

Your implementation is **spot on**. It is a textbook-perfect, highly clean execution of the **Visitor Design Pattern**.

By separating the elements (`TextFile`, `Image`, `Video`) from the operations acting on them (`SizeCalcVisitor`, `CompressVisitor`, `ScanVirusVisitor`), you have successfully achieved the **Open/Closed Principle**. If you want to add a fourth function tomorrow—say, `ExportToCloudVisitor`—you can do it by creating just one single class, without modifying a single line of your core element classes.

---

## 🏆 What Makes This Implementation Great

### 1. Zero Structural Leaks

Your `accept` methods are stripped down to their absolute purest form:

```java
public void accept(IVisitor v) {
    v.visit(this);
}

```

This is where the magic of **Double Dispatch** happens. Even though `Main` interacts with your objects using the generic `DocumentElement` interface, the object uses its internal context to pass its concrete self (`this`) back to the visitor, completely avoiding messy `instanceof` checks or manual casting.

### 2. Perfect Separation of Concerns

Your element classes stay incredibly lightweight. They only store data (`name`, `type`) and expose getters. All heavy business logic, print strings, and computational steps are pushed safely into the visitor variants.

---

## 🔍 The Architectural Trade-Off to Keep in Mind

Every design pattern comes with a tax. While your code makes it incredibly easy to add **new operations** (visitors), it makes it very painful to add **new elements**.

If you decide to add a `Hyperlink` class to your document elements later:

1. You must update the `IVisitor` interface contract to add `void visit(Hyperlink hyperlink);`.
2. As a consequence, **every single visitor class** (`SizeCalcVisitor`, `CompressVisitor`, `ScanVirusVisitor`) will instantly break and refuse to compile until you manually implement the new `visit(Hyperlink)` method inside them.

This is exactly why the Visitor pattern is recommended primarily when your **object structure is stable** but your **behaviors change frequently**.

---

## ⚡ Tiny Refactoring Idea

Right now, your element classes initialize their `type` string inside the constructor:

```java
this.type = "Text"; // Or "Image", "Video"

```

Since the `type` is completely static and hardcoded per class, you can optimize your memory footprint by making it a fixed property rather than a tracking variable allocated for every new instance:

```java
class TextFile implements DocumentElement {
    private final String name;

    public TextFile(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
    public String getType() { return "Text"; } // Returns a string literal directly without using a field
    
    public void accept(IVisitor v) { v.visit(this); }
}

```

This code layout is functionally production-ready. You have cleanly mapped out structural design patterns from Mediator to Prototype, and now Visitor. Incredible work!

*/
