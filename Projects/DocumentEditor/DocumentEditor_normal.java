import java.util.*;

interface DocumentElement{
    void render();
    String getAttribute();
}

class TextElement implements DocumentElement{
    String text;
    public TextElement(String text)
    {
        this.text = text;
    }
    public void render()
    {
        System.out.println("Rendering text = "+this.text);
    }
    public String getAttribute()
    {
        return text;
    }
}

class ImageElement implements DocumentElement{
    String path;
    public ImageElement(String path)
    {
        this.path = path;
    }
    public void render()
    {
        System.out.println("Rendering Image with Path = "+this.path);
    }
    public String getAttribute()
    {
        return path;
    }
}

// This is the Domain Model
class Document{
    List<DocumentElement> elements;
    public Document()
    {
        this.elements = new ArrayList<>();
    }
    public void addElement(DocumentElement element)
    {
        elements.add(element);
    }
    public List<DocumentElement> getElements()
    {
        return Collections.unmodifiableList(elements);
    }
}

interface Persistence{
    void save(Document doc);
}

class FileStorage implements Persistence{
    public void save(Document doc)
    {
        List<DocumentElement> elements = doc.getElements();
        for(DocumentElement element : elements)
        {
            System.out.println("Saved to File Stoarge = "+element.getAttribute());
        }
    }
}

class DBStorage implements Persistence{
    public void save(Document doc)
    {
        List<DocumentElement> elements = doc.getElements();
        for(DocumentElement element : elements)
        {
            System.out.println("Saved to File Stoarge = "+element.getAttribute());
        }
    }
}

class DocumentRenderer{
    Document doc;
    public void setDoc(Document doc)
    {
        this.doc = doc;
    }
    public void render()
    {
        List<DocumentElement> elements = doc.getElements();
        for(DocumentElement element: elements)
        {
            element.render();
        }
    }
}

class DocumentEditor{
    Document doc;
    Persistence storage;
    public DocumentEditor(Document doc) {
        this.doc = doc;
    }
    public void setDoc(Document doc)
    {
        this.doc = doc;
    }
    public void setStorage(Persistence storage)
    {
        this.storage = storage;
    }
    public void addText(String text)
    {
        doc.addElement(new TextElement(text));
    }
    
    public void addImage(String path)
    {
        doc.addElement(new ImageElement(path));
    }
    
    public void save()
    {
        if(this.storage == null)
        {
            System.out.println("Storage not set");
            return;
        }
        this.storage.save(this.doc);
    }
}

class Main{
    public static void main(String[] args)
    {
        Document doc = new Document();
        DocumentEditor editor = new DocumentEditor(doc);
        DocumentRenderer renderer = new DocumentRenderer();
        editor.setDoc(doc);
        editor.addImage("image1.png");
        editor.addText("This is a text element");
        renderer.setDoc(doc);
        renderer.render();
        editor.setStorage(new FileStorage());
        editor.save();
        editor.setStorage(new DBStorage());
        editor.save();
    }
}

/*

In Low-Level Design (LLD), a **Domain Model** is a conceptual blueprint that represents the real-world entities, their data, business rules, and relationships within a specific problem space.

It acts as the critical bridge between raw business requirements and executable code. Before writing classes or database tables, a domain model maps out *what* objects exist in the system and *how* they interact.

---

## Key Building Blocks

Every domain model is constructed using four core elements:

1. **Entities (Nouns):** The core objects in your problem domain (e.g., `User`, `Order`, `Vehicle`, `Account`).
2. **Attributes:** The properties that define the state of an entity (e.g., `Order.totalAmount`, `User.email`).
3. **Behaviors (Methods):** The actions an entity can perform or undergo (e.g., `Order.cancel()`, `Payment.refund()`).
4. **Relationships:** How entities interact or connect with one another:
* **Association:** A general connection between entities (e.g., a `Driver` delivers an `Order`).
* **Aggregation:** A "has-a" relationship where the child entity can exist independently (e.g., a `Library` has `Book` items; if the library closes, the books still exist).
* **Composition:** A strict "has-a" relationship where the child cannot exist without the parent (e.g., an `Order` owns `OrderItems`; deleting the order destroys the item entries).
* **Inheritance / Generalization:** An "is-a" relationship (e.g., `CreditCardPayment` is a `PaymentMethod`).



---

## Quick Example: Parking Lot System

To design a Parking Lot in an LLD interview or real-world project, your domain model identifies the core entities and their structural bindings:

* **`ParkingLot`** $\rightarrow$ *Composes* $\rightarrow$ **`ParkingFloor`** *(A floor cannot exist without a parking lot)*
* **`ParkingFloor`** $\rightarrow$ *Aggregates* $\rightarrow$ **`ParkingSpot`** *(Spots are grouped into floors)*
* **`Ticket`** $\rightarrow$ *Associates with* $\rightarrow$ **`Vehicle`** and **`ParkingSpot`** *(Tracks which vehicle is parked where)*
* **`Vehicle`** $\rightarrow$ *Generalized into* $\rightarrow$ **`Car`**, **`Bike`**, **`Truck`** *(Inheritance based on size requirements)*

---

## Domain Model vs. Class Diagram vs. DB Schema

It's common to confuse a Domain Model with other LLD artifacts. Here is how they differ across stages of design:

| Aspect | Domain Model | Class Diagram | Database Schema (ERD) |
| --- | --- | --- | --- |
| **Primary Focus** | Conceptual business entities & rules | Code structure, types, and design patterns | Physical data storage & relational integrity |
| **Technical Detail** | High-level (attributes & behaviors) | High (visibility `+`/`-`, exact data types, interfaces) | Database-specific (foreign keys, indexes, normal forms) |
| **Language Dependency** | Agnostic | Object-Oriented (Java, C++, Go, Python) | SQL / NoSQL schemas |

---


*/
