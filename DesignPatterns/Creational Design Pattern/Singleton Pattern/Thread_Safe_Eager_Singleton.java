class ThreadSafeEagerSingleton{
    private static ThreadSafeEagerSingleton instance = new ThreadSafeEagerSingleton();
    
    private ThreadSafeEagerSingleton(){
        System.out.println("Thread Safe Eager Singleton constructor called");
    }
    
    static public ThreadSafeEagerSingleton getInstance()
    {
        return instance;
    }
}

public class Main{
    public static void main(String[] args)
    {
        ThreadSafeEagerSingleton s1 = ThreadSafeEagerSingleton.getInstance();
        ThreadSafeEagerSingleton s2 = ThreadSafeEagerSingleton.getInstance();
        System.out.println((s1==s2));
    }
}
