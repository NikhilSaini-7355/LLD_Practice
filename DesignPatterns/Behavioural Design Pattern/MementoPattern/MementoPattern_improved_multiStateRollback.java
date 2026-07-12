import java.util.*;

class DatabaseMemento{
    private Map<String, String> database;
    public DatabaseMemento(Map<String, String> dbData)
    {
        database = new HashMap<>(dbData);
    }
    
    public Map<String,String> getState()
    {
        return Collections.unmodifiableMap(this.database);
    }
}

class DatabaseOriginator{
    private Map<String,String> database;
    public DatabaseOriginator()
    {
        database = new HashMap<>();
    }
    protected void create(String key, String value)
    {
        database.put(key,value);
    }
    
    protected void update(String key, String value)
    {
        database.put(key,value);
    }
    
    protected void delete(String key)
    {
        database.remove(key);
    }
    
    protected String read(String key)
    {
        return database.get(key);
    }
    
    protected DatabaseMemento createMemento()
    {
        DatabaseMemento memento = new DatabaseMemento(database);
        return memento;
    }
    
    protected void restore(DatabaseMemento memento)
    {
        database.clear(); // Empty the original bucket
        database.putAll(memento.getState()); // Fill it back up using the wrapper
    }
    
    protected void display()
    {
        for(Map.Entry<String,String> entry: database.entrySet())
        {
            System.out.println(entry.getKey() +" -> "+ entry.getValue());
        }
    }
}

class DatabaseCaretaker{
    Deque<DatabaseMemento> mementos; 
    protected DatabaseCaretaker()
    {
        mementos = new ArrayDeque<>();
    }
    
    protected void beginTransaction(DatabaseOriginator db)
    {
        mementos.push(db.createMemento());
    }
    
    protected void commit()
    {
        if(!mementos.isEmpty())
        {
            mementos.pop();
        }
    }
    
    protected void rollback(DatabaseOriginator db)
    {
        if(!mementos.isEmpty())
        { db.restore(mementos.pop()); }
        else
        {
            System.out.println("No Backup Available");
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        DatabaseOriginator db = new DatabaseOriginator();
        DatabaseCaretaker caretaker = new DatabaseCaretaker();

        System.out.println("=====================================================================");
        System.out.println("🧪 TEST 1: Initial State & Baseline Setup");
        System.out.println("=====================================================================");
        db.create("User_1", "Alice");
        db.create("User_2", "Bob");
        System.out.println("Active Database State:");
        db.display();

        System.out.println("\n=====================================================================");
        System.out.println("🧪 TEST 2: Deep Nested Transactions (Three Levels Deep)");
        System.out.println("=====================================================================");
        
        // --- LEVEL 1 TRANSACTION ---
        System.out.println("👉 Starting Transaction Level 1...");
        caretaker.beginTransaction(db);
        db.create("User_3", "Charlie");
        db.update("User_1", "Alice_Modified_L1");
        System.out.println("\n[State at Level 1]:");
        db.display();

        // --- LEVEL 2 TRANSACTION ---
        System.out.println("\n👉 Starting Nested Transaction Level 2...");
        caretaker.beginTransaction(db);
        db.create("User_4", "David");
        db.delete("User_2"); // Bob is gone in L2
        System.out.println("\n[State at Level 2]:");
        db.display();

        // --- LEVEL 3 TRANSACTION ---
        System.out.println("\n👉 Starting Deepest Nested Transaction Level 3...");
        caretaker.beginTransaction(db);
        db.create("User_5", "Ethan");
        db.update("User_4", "David_Modified_L3");
        System.out.println("\n[State at Level 3 (Deepest Preview)]:");
        db.display();

        System.out.println("\n=====================================================================");
        System.out.println("🧪 TEST 3: Executing Sequential Stack Rollbacks");
        System.out.println("=====================================================================");
        
        // Rollback 1: Should revert Level 3 back to Level 2
        System.out.println("↩️ Triggering Rollback #1 (Should discard Level 3 changes)...");
        caretaker.rollback(db);
        System.out.println("\n[Database State after Rollback #1 - Expect Level 2 State]:");
        db.display(); // User_5 should vanish, User_4 should go back to "David", User_2 still deleted

        // Rollback 2: Should revert Level 2 back to Level 1
        System.out.println("\n↩️ Triggering Rollback #2 (Should discard Level 2 changes)...");
        caretaker.rollback(db);
        System.out.println("\n[Database State after Rollback #2 - Expect Level 1 State]:");
        db.display(); // User_4 should vanish, User_2 (Bob) should reappear!

        System.out.println("\n=====================================================================");
        System.out.println("🧪 TEST 4: Committing a Surviving Transaction Block");
        System.out.println("=====================================================================");
        
        // Commit: Finalize Level 1 changes permanently
        System.out.println("💾 Committing Level 1 changes permanently...");
        caretaker.commit();
        System.out.println("\n[Database State after Committing Level 1]:");
        db.display();

        System.out.println("\n=====================================================================");
        System.out.println("🧪 TEST 5: Edge Case - Rolling Back an Empty Stack");
        System.out.println("=====================================================================");
        
        // The stack is now empty because we rolled back 2 levels and committed 1.
        System.out.println("↩️ Attempting an illegal rollback on an empty transaction history...");
        caretaker.rollback(db); 
        
        System.out.println("\n Final Verified Database State:");
        db.display();
    }
}
