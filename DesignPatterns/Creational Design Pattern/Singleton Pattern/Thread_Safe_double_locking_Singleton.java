class ThreadSafeDoubleLockingSingleton{
    private static volatile ThreadSafeDoubleLockingSingleton instance = null;
    
    private ThreadSafeDoubleLockingSingleton(){
        System.out.println("Thread Safe Double Locking Singleton constructor called");
    }
    
    static public ThreadSafeDoubleLockingSingleton getInstance()
    {
        if(instance==null)
        {
            synchronized(ThreadSafeDoubleLockingSingleton.class){
                if(instance==null)
                {
                    instance = new ThreadSafeDoubleLockingSingleton();
                }
            }
        }
        return instance;
    }
}

public class Main{
    public static void main(String[] args)
    {
        ThreadSafeDoubleLockingSingleton s1 = ThreadSafeDoubleLockingSingleton.getInstance();
        ThreadSafeDoubleLockingSingleton s2 = ThreadSafeDoubleLockingSingleton.getInstance();
        System.out.println((s1==s2));
    }
}

/*
To understand why we need **two** instance checks, let's look at the distinct job each check performs.

If you only had one check, the code would either be incredibly slow or completely broken in a multithreaded environment.

Here is the breakdown of why both are absolutely necessary.

---

### 1. The First Check (Outside the Lock): Performance

The first check is there to **avoid synchronization overhead** once the instance has already been created.

Acquiring a lock (`synchronized`) is computationally expensive. In a real-world app, `getInstance()` might be called millions of times. Once the `instance` is created, we don't need a lock anymore; threads can just grab the instance and go.

* **Without the first check:** Every single thread would have to wait in line for the lock just to read the variable, destroying performance.

---

### 2. The Second Check (Inside the Lock): Thread Safety

The second check is there to handle the **initial race condition** when the very first threads arrive at the exact same time while `instance` is still `null`.

To see why it's necessary, let’s look at a timeline of what happens if you **remove** the second check.

#### The Nightmare Scenario (No Second Check)

Imagine **Thread A** and **Thread B** call `getInstance()` at the exact same moment for the first time:

1. **Thread A** checks `if (instance == null)` $\rightarrow$ It is true.
2. **Thread B** checks `if (instance == null)` $\rightarrow$ It is also true (because Thread A haven't created it yet).
3. **Thread A** reaches the `synchronized` block, acquires the lock, and enters.
4. **Thread B** reaches the `synchronized` block, but because Thread A holds the lock, **Thread B is blocked** and waits outside.
5. **Thread A** executes `instance = new ThreadSafeDoubleLockingSingleton();`
6. **Thread A** exits the synchronized block and releases the lock.
7. **Thread B** wakes up, acquires the lock, and enters the block.
8. **CRASH!** Because there is *no second check*, Thread B blindly executes `instance = new ThreadSafeDoubleLockingSingleton();` again.

Thread A and Thread B now have two completely different object references, entirely breaking the Singleton pattern.

#### How the Second Check Fixes This

With the second check in place, when **Thread B** enters the synchronized block at Step 7, it executes `if (instance == null)`. It sees that Thread A already initialized it, skips the creation line, and safely exits.

---

### ⚠️ A Critical Bug in Your Code: The Missing `volatile`

While your logic for the two checks is correct, this code is actually **not completely thread-safe yet** due to a quirk in how CPUs and Java optimize code.

You must declare your `instance` variable as **`volatile`**:

```java
private static volatile ThreadSafeDoubleDoubleLockingSingleton instance = null;

```

#### Why? (Instruction Reordering)

When the JVM executes `instance = new ThreadSafeDoubleLockingSingleton();`, it does three things behind the scenes:

1. **Memory Allocation:** Allocates raw memory for the object.
2. **Construction:** Runs the constructor code to initialize the object.
3. **Assignment:** Points the `instance` variable to that memory address.

To optimize speed, the CPU/compiler is allowed to reorder these steps to **1 $\rightarrow$ 3 $\rightarrow$ 2**.

If this reordering happens, **Thread A** will point `instance` to the memory address (Step 3) *before* the constructor actually runs (Step 2). If **Thread B** arrives at that exact microsecond and hits the **First Check**, it will see that `instance` is not null, grab the object, and try to use it. But because the constructor hasn't finished running, the object is a half-baked, corrupted mess!

Adding `volatile` forces the compiler and CPU to strictly follow the order and guarantees memory visibility across threads.
*/
