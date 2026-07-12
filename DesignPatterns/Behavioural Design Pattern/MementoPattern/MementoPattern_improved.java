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
    DatabaseMemento backup;
    protected DatabaseCaretaker()
    {
        backup = null;
    }
    
    protected void beginTransaction(DatabaseOriginator db)
    {
        backup = db.createMemento();
    }
    
    protected void commit()
    {
        if(backup!=null)
        {
            backup = null;
        }
    }
    
    protected void rollback(DatabaseOriginator db)
    {
        if(backup!=null)
        { db.restore(backup); }
        else{
            System.out.println("No Backup Available");
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        DatabaseOriginator db = new DatabaseOriginator();
        DatabaseCaretaker caretaker = new DatabaseCaretaker();
        
        caretaker.beginTransaction(db);
        db.create("A","0");
        db.create("B","1");
        db.update("B","0");
        db.delete("B");
        db.create("C","2");
        System.out.println("C = "+db.read("C"));
        caretaker.commit();
        db.display();
        System.out.println("=====================================================================");
        
        caretaker.beginTransaction(db);
        db.create("D","3");
        db.create("E","5");
        caretaker.rollback(db);
        db.display();
        
        System.out.println("=====================================================================");
        caretaker.beginTransaction(db);
        db.create("E","5");
        caretaker.commit();
        db.display();
    }
}
