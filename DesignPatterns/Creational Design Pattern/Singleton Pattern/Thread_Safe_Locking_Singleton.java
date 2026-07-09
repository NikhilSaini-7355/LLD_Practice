class ThreadSafeLockingSingleton{
    private static ThreadSafeLockingSingleton instance = null;
    
    private ThreadSafeLockingSingleton(){
        System.out.println("Thread Safe Locking Singleton constructor called");
    }
    
    static public ThreadSafeLockingSingleton getInstance()
    {
        synchronized(ThreadSafeLockingSingleton.class)
        {
            if(instance == null)
            {
                instance = new ThreadSafeLockingSingleton();
            }
            return instance;
        }
        
    }
}

public class Main{
    public static void main(String[] args)
    {
        ThreadSafeLockingSingleton s1 = ThreadSafeLockingSingleton.getInstance();
        ThreadSafeLockingSingleton s2 = ThreadSafeLockingSingleton.getInstance();
        System.out.println((s1==s2));
    }
}

/*
The `synchronized` keyword is the foundation of thread safety in Java. It provides a straightforward way to prevent race conditions and ensure that only one thread can execute a specific section of code at a time.

To understand `synchronized` in detail, you need to understand the concept of the **Monitor Lock** (or Intrinsic Lock).

In Java, **every single object** you create automatically comes with a hidden, built-in lock. When you use the `synchronized` keyword, you are telling a thread to acquire the monitor lock of a specific object before it can proceed. If another thread already holds that lock, the new thread is blocked and goes to sleep until the lock is released.

Here are the three ways you can use `synchronized`, how they work, and what they are actually locking.

---

### 1. Synchronized Instance Methods

When you put `synchronized` on a standard (non-static) method, the thread must acquire the lock on the **current object instance** (the `this` reference).

```java
public class BankAccount {
    private int balance = 100;

    // Locks the specific BankAccount instance ('this')
    public synchronized void withdraw(int amount) {
        balance -= amount;
    }
    
    // Also locks 'this'
    public synchronized void deposit(int amount) {
        balance += amount;
    }
}

```

**What this means:** If Thread A is calling `withdraw()` on a `BankAccount` object, and Thread B wants to call `deposit()` on that **same** object, Thread B must wait. However, if Thread B is interacting with a completely *different* `BankAccount` object, it does not have to wait.

### 2. Synchronized Static Methods

When you put `synchronized` on a `static` method, there is no `this` instance because static methods belong to the class, not an individual object. Therefore, the thread acquires the lock on the **Class object itself** (e.g., `BankAccount.class`).

```java
public class DatabaseConnection {
    private static int activeConnections = 0;

    // Locks the DatabaseConnection.class object
    public static synchronized void incrementConnections() {
        activeConnections++;
    }
}

```

**What this means:** This is a global lock for that class. If Thread A is executing a static synchronized method, **no other thread** in the entire application can execute *any* static synchronized method in that class until Thread A is done.

### 3. Synchronized Blocks (The Best Practice)

Synchronizing entire methods is easy, but it's often a sledgehammer approach. If a method does 10 things, but only 1 thing actually modifies shared data, locking the whole method hurts performance.

Synchronized blocks allow you to lock only the specific lines of code that need protection. They also allow you to specify *exactly which object's lock* you want to use.

```java
public class WebServer {
    private int requestCount = 0;
    // A dedicated dummy object used purely as a lock
    private final Object lockObject = new Object(); 

    public void handleRequest() {
        System.out.println("Doing heavy processing..."); // Multiple threads can do this simultaneously
        
        // Only this specific block is locked
        synchronized(lockObject) { 
            requestCount++;
        }
        
        System.out.println("Sending response..."); // Multiple threads can do this simultaneously
    }
}

```

**Why use a dedicated `lockObject`?** It hides the lock from the outside world. If you use `synchronized(this)`, malicious or poorly written code elsewhere in your application could also lock on your object and cause a deadlock. Using a private, final dummy object prevents this.

---

### Two Crucial Superpowers of `synchronized`

1. **Reentrancy:** If a thread already holds a lock on an object, it can enter *other* synchronized methods or blocks that require the same lock without getting stuck. For example, if a `synchronized` method calls another `synchronized` method on the same object, the thread just walks right through. It doesn't deadlock itself.
2. **Memory Visibility (Happens-Before):** CPU caches can cause threads to see stale data. `synchronized` acts as a memory barrier. When a thread exits a synchronized block, it flushes its changes back to the main memory. When the next thread enters a synchronized block for the same lock, it is guaranteed to see the most up-to-date data.

---

Now that you know how threads lock resources, would you like to explore how threads can actually talk to each other inside these blocks using the `wait()` and `notify()` methods?
*/

/*
Using `synchronized(this)` locks the **entire object instance**, but it **only blocks other threads from executing code that *also* asks for that same lock.**

It does not magically lock down all the code inside the class. Instead, it acts as a gatekeeper for the object's single "key."

Here is exactly how it affects different types of methods on that same object while a thread is inside your `synchronized(this)` block:

---

### 1. Plain (Non-Synchronized) Methods $\rightarrow$ **NOT LOCKED**

Any method that does not have the `synchronized` keyword can be executed by other threads at the exact same time. They completely ignore the lock.

### 2. Other Synchronized Methods/Blocks $\rightarrow$ **LOCKED**

If another method is marked as `synchronized`, or contains another `synchronized(this)` block, it requires the exact same key to enter. Since the first thread holds that key, any other thread trying to call these methods will be blocked and forced to wait.

---

### A Concrete Example

Look at this class layout to see how threads interact with it simultaneously:

```java
public class MyObject {

    public void methodA() {
        // Line 1, 2, 3...
        synchronized(this) { 
            // Thread 1 is in here holding the 'this' lock.
            System.out.println("Inside synchronized block");
        }
    }

    // CASE 1: Completely unaffected
    public void methodB() {
        System.out.println("Thread 2 can run this anytime!"); 
    }

    // CASE 2: BLOCKED! 
    // This implicitly requires the 'this' lock, so Thread 3 must wait.
    public synchronized void methodC() {
        System.out.println("Thread 3 is blocked until Thread 1 leaves methodA's block");
    }

    // CASE 3: BLOCKED!
    // This explicitly requires the 'this' lock, so Thread 4 must wait.
    public void methodD() {
        synchronized(this) {
            System.out.println("Thread 4 is also blocked");
        }
    }
}

```

### Summary

* Your block does **not** restrict access to the method it is written in.
* It acquires the lock for the **object (`this`)**.
* Only other code blocks that explicitly ask for `synchronized(this)` or methods marked `synchronized` will care that the lock is taken.
*/

/*
To truly understand how `synchronized(this)` works, we have to peel back the layers of Java and look at what happens under the hood inside the **JVM (Java Virtual Machine)** and the **computer's memory**.

Let's break down the deep mechanics of what happens when a thread hits a `synchronized(this)` block.

---

### 1. The Blueprint of a Lock: The JVM Monitor

In the JVM, every object is paired with an internal structure called a **Monitor** (often implemented as an `ObjectMonitor` in C++ under the hood). You can think of the Monitor as a building with three distinct rooms:

* **The Owner:** A room that can hold exactly *one* thread. This is the thread currently executing the synchronized code.
* **The Entry Set (Blocked Queue):** The waiting room. If Thread A is in the Owner room, and Threads B and C try to enter a `synchronized(this)` block, they are shoved into the Entry Set and paused (put into the `BLOCKED` state).
* **The Wait Set:** A separate lounge for threads that explicitly called `this.wait()`. They are resting here until someone calls `this.notify()`.

When you write `synchronized(this)`, you are telling the thread: *"Go to the monitor of the `this` object. If the Owner room is empty, step inside. If it's full, go wait in the Entry Set."*

---

### 2. The Bytecode Level: `monitorenter` and `monitorexit`

What does the Java compiler actually do with your `synchronized(this)` block? It translates it into specific low-level bytecode instructions.

If you have this Java code:

```java
public void myMethod() {
    synchronized(this) {
        System.out.println("Locked!");
    }
}

```

The compiled bytecode looks something like this behind the scenes:

```text
0: aload_0                  // Load 'this' onto the operand stack
1: dup                      // Duplicate it
2: astore_1                 // Store it in a local variable
3: monitorenter             // <-- ACQUIRE THE LOCK HERE
4: getstatic     #2         // System.out
7: ldc           #3         // "Locked!"
9: invokevirtual #4         // println()
12: aload_1
13: monitorexit             // <-- RELEASE THE LOCK (Normal Exit)
14: goto          22
17: astore_2                // Catch block (if an exception occurs)
18: aload_1
19: monitorexit             // <-- RELEASE THE LOCK (Exception Exit)
20: aload_2
21: athrow
22: return

```

> **Notice something interesting?** There is *one* `monitorenter`, but *two* `monitorexit` instructions. The JVM automatically generates a hidden `try-finally` block. This guarantees that even if your code crashes with a terrible runtime exception inside the synchronized block, the lock is **always** released. Otherwise, a crash would lock up your application forever.

---

### 3. The Memory Level: The Object Header (Mark Word)

Where does the object actually keep track of who owns the lock? It doesn’t use a hidden Java variable. It uses raw memory inside the **Object Header**.

Every object in Java memory consists of a header and its actual data fields. The header contains a specific piece of metadata called the **Mark Word** (usually 32 bits or 64 bits depending on your architecture).

The JVM rewrites the bits of the Mark Word in real-time to represent the lock state:

| Lock State | Mark Word Bits Layout / Meaning |
| --- | --- |
| **Unlocked** | Contains standard hashcode, age, and a tag indicating it's unlocked. |
| **Lightweight Locked** | Pointer to the executing thread's execution stack lock record. (Used if there's no real competition). |
| **Heavyweight Locked** | Pointer to the actual OS-level native mutex/monitor structure. (Used when multiple threads are actively fighting for the lock). |

#### The Lock Upgrade Lifecycle

To save performance, the JVM is incredibly smart about how it handles this Mark Word. It doesn't instantly ask the Operating System for a heavy, expensive lock. It escalates it as needed:

1. **Lightweight Locking:** If Thread A approaches `synchronized(this)` and no one else is using it, the JVM performs a lightning-fast CPU operation (called CAS - Compare-And-Swap) to stamp Thread A's ID directly onto the object's Mark Word. No heavy OS threads are disturbed.
2. **Inflation to Heavyweight:** If Thread B arrives while Thread A is still holding it, a collision occurs. The JVM realizes there is actual contention. It **inflates** the lock. It creates a native OS monitor structure, updates the Mark Word to point to this monitor, and forces Thread B into a true OS-level thread sleep (`BLOCKED`).

---

### 4. What exactly happens to the CPU?

When Thread B hits the Entry Set and becomes `BLOCKED`:

* The OS kernel stops allocating CPU cycles to Thread B.
* A **context switch** occurs, meaning the CPU saves Thread B's current progress, benches it, and brings in another thread to do work.
* When Thread A hits `monitorexit`, the JVM signals the OS to wake up a thread from the Entry Set. The CPU performs *another* context switch to spin Thread B back up.

Because context switches take time, you should always design your synchronized blocks to be as short and fast as possible!
*/
