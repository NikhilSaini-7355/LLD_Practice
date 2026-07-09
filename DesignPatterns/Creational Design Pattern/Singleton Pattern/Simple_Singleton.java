    class SimpleSingleton{
    private static SimpleSingleton instance = null;
    
    private SimpleSingleton(){
        System.out.println("Simple Singleton constructor called");
    }
    
    static public SimpleSingleton getInstance()
    {
        if(instance == null)
        {
            instance = new SimpleSingleton();
        }
        return instance;
    }
}

public class Main{
    public static void main(String[] args)
    {
        SimpleSingleton s1 = SimpleSingleton.getInstance();
        SimpleSingleton s2 = SimpleSingleton.getInstance();
        System.out.println((s1==s2));
    }
}
