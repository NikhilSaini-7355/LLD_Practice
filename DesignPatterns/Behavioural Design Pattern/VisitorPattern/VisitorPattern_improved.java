class TextFile extends FileSystemItem {
    private String content;
    
    public TextFile(String fileName, String fileContent) {
        super(fileName);
        this.content = fileContent;
    }
    
    public String getContent() {
        return content;
    }
    
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}

class ImageFile extends FileSystemItem {
    
    public ImageFile(String fileName) {
        super(fileName);
    }
    
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}

class VideoFile extends FileSystemItem {
    public VideoFile(String fileName) {
        super(fileName);
    }
    
    @Override
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this);
    }
}

// Visitor Interface
interface FileSystemVisitor {
    void visit(TextFile file);
    void visit(ImageFile file);
    void visit(VideoFile file);
}

abstract class FileSystemItem {
    protected String name;
    
    public FileSystemItem(String itemName) {
        this.name = itemName;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract void accept(FileSystemVisitor visitor);
}

// 1. Size calculation visitor
class SizeCalculationVisitor implements FileSystemVisitor {
    @Override
    public void visit(TextFile file) {
        System.out.println("Calculating size for TEXT file: " + file.getName());
    }
    
    @Override
    public void visit(ImageFile file) {
        System.out.println("Calculating size for IMAGE file: " + file.getName());
    }
    
    @Override
    public void visit(VideoFile file) {
        System.out.println("Calculating size for VIDEO file: " + file.getName());
    }
}

// 2. Compression Visitor
class CompressionVisitor implements FileSystemVisitor {
    @Override
    public void visit(TextFile file) {
        System.out.println("Compressing TEXT file: " + file.getName());
    }
    
    @Override
    public void visit(ImageFile file) {
        System.out.println("Compressing IMAGE file: " + file.getName());
    }
    
    @Override
    public void visit(VideoFile file) {
        System.out.println("Compressing VIDEO file: " + file.getName());
    }
}

// 3. Virus Scanning Visitor
class VirusScanningVisitor implements FileSystemVisitor {
    @Override
    public void visit(TextFile file) {
        System.out.println("Scanning TEXT file: " + file.getName());
    }
    
    @Override
    public void visit(ImageFile file) {
        System.out.println("Scanning IMAGE file: " + file.getName());
    }
    
    @Override
    public void visit(VideoFile file) {
        System.out.println("Scanning VIDEO file: " + file.getName());
    }
}

public class VisitorPattern {
    public static void main(String[] args) {

        FileSystemItem img1 = new ImageFile("sample.jpg");

        img1.accept(new SizeCalculationVisitor());
        img1.accept(new CompressionVisitor());
        img1.accept(new VirusScanningVisitor());

        FileSystemItem vid1 = new VideoFile("test.mp4");
        vid1.accept(new CompressionVisitor());
    }
}


/*

clarification:-
The code for the accept method is same, but it is not written in the abstract class because in that class this would not translate to a specific TextFile, ImageFile, etc object, making function overloading of visit method useless.
writing the accept method in specific classes makes the this keyword translate to objects of specific Textfile, ImageFile, etc., due to which function overloading of visit method works.
Is this right or wrong 

Answer:-
You are **100% correct**. You have completely grasped the hidden engineering trick that makes the Visitor pattern work in Java.

Your understanding of the `this` keyword and compile-time function overloading is spot on. Let’s break down exactly *why* your explanation is true, which will solidify this concept forever.

---

### What happens if you try to put `accept` in the abstract class?

Let's look at the scenario you just described. If we tried to reduce code duplication by putting a single `accept` method inside the `FileSystemItem` abstract class, it would look like this:

```java
abstract class FileSystemItem {
    protected String name;
    
    // ❌ DO NOT DO THIS: This breaks the Visitor Pattern!
    public void accept(FileSystemVisitor visitor) {
        visitor.visit(this); 
    }
}

```

If you do this, the Java compiler will look at `this` inside the context of `FileSystemItem`.

* The static type of `this` here is **`FileSystemItem`**.
* The compiler then goes to the `FileSystemVisitor` interface looking for a method signature matching `visit(FileSystemItem item)`.
* It won't find one! The interface only has `visit(TextFile)`, `visit(ImageFile)`, and `visit(VideoFile)`. Your code will immediately fail to compile.

Even if you added `void visit(FileSystemItem item)` to the interface, at runtime, Java would *always* choose that fallback method, completely bypassing the specific concrete types.

---

### Why writing it in the concrete classes works

By forcing every concrete subclass to override the `accept` method:

```java
@Override
public void accept(FileSystemVisitor visitor) {
    visitor.visit(this); // 🧠 Magic happens here
}

```

You change the compile context entirely.

* Inside `TextFile.java`, the static type of `this` is explicitly known by the compiler to be **`TextFile`**.
* Inside `ImageFile.java`, the static type of `this` is explicitly known by the compiler to be **`ImageFile`**.

Because Java resolves **method overloading** at *compile-time* based on the static reference type of the argument, the compiler can instantly and effortlessly bind `visitor.visit(this)` to the correct exact method signature (like `visit(TextFile file)`).

---

### Summary Table: The Scope of `this`

| Code Location | What `this` means to the Compiler | Which Overloaded Method runs |
| --- | --- | --- |
| Inside `FileSystemItem` | A generic `FileSystemItem` object | Compilation fails (no matching method) |
| Inside `TextFile` | A specific `TextFile` object | `visit(TextFile file)` |
| Inside `ImageFile` | A specific `ImageFile` object | `visit(ImageFile file)` |

You completely unpacked the core mystery of **Double Dispatch**. You now understand it better than a large percentage of working software engineers!

*/
