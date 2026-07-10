/*
While all three belong to the **Proxy Design Pattern** family and share the exact same structural layout (a Client, an Interface, a Proxy, and a Real Subject), they are used for completely different engineering purposes.

The core difference lies in **what the proxy is controlling access to**.

---

### The Core Differences

| Proxy Type | Core Purpose | When is the Real Object Created? |
| --- | --- | --- |
| **Virtual Proxy** | **Optimizes performance** by delaying the creation of a resource-heavy object. | Only when a method is called that *requires* the real object (Lazy Loading). |
| **Protection Proxy** | **Enforces security** by checking a user's role/permissions before granting access. | Usually during instantiation, or passed into the proxy's constructor. |
| **Remote Proxy** | **Hides network complexity** by making an object on a different machine look local. | It exists on a remote server; the proxy just holds a network connection to it. |

---

### Standard Code Implementations

To see how they differ structurally, let's use a shared context: accessing a secure **Document**.

#### 1. The Common Setup (The Interface)

Every proxy pattern variant requires a shared interface so the client can treat the proxy and the real object uniformly.

```java
interface Document {
    void display();
}

class RealDocument implements Document {
    private String filename;

    public RealDocument(String filename) {
        this.filename = filename;
        System.out.println("Loading heavy document from disk: " + filename);
    }

    @Override
    public void display() {
        System.out.println("Displaying document content: " + filename);
    }
}

```

---

### 2. Virtual Proxy (Performance Focus)

This variant focuses on **lazy loading**. It prevents the expensive disk/memory operation of loading the `RealDocument` until the client actually calls `display()`.

```java
class VirtualProxy implements Document {
    private String filename;
    private RealDocument realDocument; // Kept null until needed

    public VirtualProxy(String filename) {
        this.filename = filename; 
        // Notice: RealDocument is NOT created here!
    }

    @Override
    public void display() {
        if (realDocument == null) {
            realDocument = new RealDocument(filename); // Lazy initialization
        }
        realDocument.display();
    }
}

```

---

### 3. Protection Proxy (Security Focus)

This variant focuses on **authorization**. It checks the actor's credentials before delegating the call to the real underlying object.

```java
class User {
    private String role;
    public User(String role) { this.role = role; }
    public String getRole() { return role; }
}

class ProtectionProxy implements Document {
    private RealDocument realDocument;
    private User user;

    public ProtectionProxy(String filename, User user) {
        // The real object can be created instantly, but access is guarded
        this.realDocument = new RealDocument(filename);
        this.user = user;
    }

    @Override
    public void display() {
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            realDocument.display(); // Access granted
        } else {
            System.out.println("ACCESS DENIED: You do not have permissions to view this document.");
        }
    }
}

```

---

### 4. Remote Proxy (Network Focus)

This variant focuses on **location transparency**. The actual object lives on a separate server. The proxy handles the messy network serialization, sockets, and protocols under the hood so the client thinks it's working with a local object.

*(Note: In modern Java, this is typically handled via RMI, gRPC, or REST clients, but structurally it looks like this pseudo-code)*

```java
class RemoteProxy implements Document {
    private String filename;
    private NetworkClient networkClient; // Handles sockets/HTTP

    public RemoteProxy(String filename) {
        this.filename = filename;
        this.networkClient = new NetworkClient("https://api.documentserver.com");
    }

    @Override
    public void display() {
        // The proxy packages the request and ships it over the wire
        String response = networkClient.sendRequest("/getDocument?name=" + filename);
        System.out.println("Remote Data Received: " + response);
    }
}

```

---

### How They Look to the Client (`Main`)

Notice how from the client's perspective, the invocation syntax is identical, but the architectural outcome is vastly different:

```java
public class Main {
    public static void main(String[] args) {
        // 1. Virtual Proxy: Fast startup, delays heavy disk loading
        Document virtualDoc = new VirtualProxy("massive_report.pdf");
        System.out.println("Proxy created. No disk activity yet.");
        virtualDoc.display(); // Disk loading happens right here!

        System.out.println("------------------------------------");

        // 2. Protection Proxy: Blocks non-admins
        User guest = new User("GUEST");
        Document securedDoc = new ProtectionProxy("confidential.pdf", guest);
        securedDoc.display(); // Prints: ACCESS DENIED
        
        System.out.println("------------------------------------");

        // 3. Remote Proxy: Talks to a server over the internet
        Document remoteDoc = new RemoteProxy("cloud_file.pdf");
        remoteDoc.display(); // Sends network request seamlessly
    }
}

```

*/


class User {
	private String name;
	private boolean isPremium;
	public User(String name, boolean isPremium)
	{
		this.name = name;
		this.isPremium = isPremium;
	}
	public boolean isPremiumUser()
	{
	    return isPremium;
	}
}

abstract class IDocReader {
	abstract void unlockPDF(String file, String password);
	public void setUser(User user){
	    
	}
}

class RealDocReader extends IDocReader {
	public void unlockPDF(String file, String password) {
		System.out.println("File = "+file+" unlocked with "+password);
	}
}



class DocReaderProxy extends IDocReader {
	RealDocReader realDocReader;
	User user;
	private boolean isAuth(User user)
	{
		if(user==null)
		{
			return false;
		}
		if(user.isPremiumUser())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public DocReaderProxy()
	{
		this.user = null;
		realDocReader = null;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public void unlockPDF(String name, String password)
	{
		// first it checks authentication/authorization (Hence Protects the resources from unauthorized/unauthenticated users)
		if(isAuth(this.user))
		{
			if(realDocReader == null)
			{
				realDocReader = new RealDocReader();
			}
			realDocReader.unlockPDF(name, password);
		}
	}
}


public class Main {
	public static void main(String[] args)
	{
		User user1 = new User("Mohit",true);
		User user2 = new User("Sohit",false);
		IDocReader docReader = new DocReaderProxy();
		docReader.setUser(user1);
		docReader.unlockPDF("path1","pass1");
		docReader.setUser(user2);
		docReader.unlockPDF("path2","pass2");
	}
}

/*
The **first implementation** is structurally better and closer to how real-world **Protection Proxies** are implemented, even though the **second implementation** has cleaner, more standard Java syntax.

This is an interesting case where the code with slightly messier formatting actually follows a more robust architectural pattern. Here is the detailed breakdown comparing the two.

---

### Why the First Implementation Wins Architecturally

#### 1. Dynamic User State Handling vs. Hardcoded Proxies

In a real application, a single client session or a persistent service handler object stays alive while users log in, log out, or switch contexts.

* **In the first implementation:** The proxy allows you to dynamically swap the current user using `docReader.setUser(user)`. You don't have to throw away the proxy object just because a user changed. It also incorporates a **Virtual Proxy** optimization by lazy-loading the `RealDocReader` only when an authorized user actually needs it.
* **In the second implementation:** The user is tightly bound via the constructor (`new DocumentProxy(user1)`). If you have 50 users clicking a button, you are forced to instantiate 50 separate proxy objects. This defeats the purpose of managing a stable access control gateway.

#### 2. Graceful Access Control Failure

Look at how both snippets handle a blocked request:

* **First snippet:** If access is denied, it exits silently or returns (though adding an `else { println("Access Denied"); }` would make it perfect).
* **Second snippet:** It explicitly leaks system information across layers. If a condition fails, it bails out, but its `User` implementation completely breaks encapsulation by making its fields raw and `public` (`user.premiumMembership`). The first snippet correctly uses a secure getter method (`user.isPremiumUser()`).

---

### What the Second Implementation Got Right (Syntax & Safety)

While the first code wins on structural flexibility, it has a couple of clean-code issues that the second one solves:

* **Use of `interface` over `abstract class`:** The second snippet correctly uses an `interface` (`IDocumentReader`) to model the system behavior. The first snippet uses an `abstract class` (`IDocReader`) and is forced to declare a dummy `setUser()` method that the `RealDocReader` doesn't even need, solely to satisfy the proxy.
* **No Feedback on Failure:** In your first snippet, when Sohit (the non-premium user) tries to unlock the PDF, nothing happens on screen. The second snippet prints an explicit `[DocumentProxy] Access denied` warning message, which is far better practice.

---

### Comparison Summary

| Design Quality | First Implementation | Second Implementation | Winner |
| --- | --- | --- | --- |
| **Object Lifecycle Management** | Dynamic context shifting (`setUser()`) | Static context binding (Constructor only) | **First** |
| **Resource Efficiency** | Optimized lazy loading of the real service | Instantiates the real service eagerly | **First** |
| **Encapsulation & Security** | Safe private data fields with getters | Leaky `public` user state fields | **First** |
| **Abstractions Compliance** | Messy abstract class method bloat | Clean interface contracts | **Second** |

---

### Final Verdict

Stick with the **first approach's architectural pattern** (using a state-changing method like `setUser`), but clean up its data visibility and use `interfaces` instead of `abstract classes` as demonstrated in the second snippet.
*/



// -----------------------------------------------------------------------------------------------

// IDEAL APPROACH
// // 1. The Interface remains clean and pure
// interface IDocReader {
//     void unlockPDF(String file, String password);
// }

// // 2. A separate Security Context to hold the logged-in user session
// class SecurityContext {
//     private static User currentUser;
//     public static void login(User user) { currentUser = user; }
//     public static User getCurrentUser() { return currentUser; }
// }

// // 3. The Proxy depends ONLY on the interface, pulling user data from the context
// class DocReaderProxy implements IDocReader {
//     private RealDocReader realDocReader;

//     @Override
//     public void unlockPDF(String name, String password) {
//         User user = SecurityContext.getCurrentUser();
        
//         if (user == null || !user.isPremiumUser()) {
//             System.out.println("ACCESS DENIED: Premium access required.");
//             return;
//         }

//         if (realDocReader == null) {
//             realDocReader = new RealDocReader();
//         }
//         realDocReader.unlockPDF(name, password);
//     }
// }
// public class Main {
//     public static void main(String[] args) {
//         // Build your users
//         User premiumUser = new User("Mohit", true);
//         User freeUser = new User("Sohit", false);

//         // PERFECT IMPLEMENTATION: Polymorphic interface declaration!
//         IDocReader docReader = new DocReaderProxy();

//         // Simulate user login through the security gateway
//         SecurityContext.login(premiumUser);
//         docReader.unlockPDF("file1.pdf", "pass1"); // Allowed

//         // Switch user session globally
//         SecurityContext.login(freeUser);
//         docReader.unlockPDF("file2.pdf", "pass2"); // Blocked!
//     }
// }
