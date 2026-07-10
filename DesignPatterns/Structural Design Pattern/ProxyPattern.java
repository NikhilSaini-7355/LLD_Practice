interface IDisplay{
    public void display();
}

class ImageDisplay implements IDisplay{
    String path;
    protected void load(String path)
    {
        System.out.println("Loading Image from path = "+ path);
    }
    
    protected void compress()
    {
        System.out.println("compressing image");
    }
    
    protected void filter(){
        System.out.println("filtering image");
    }
    
    protected ImageDisplay(String path)
    {
        this.path = path;
        
        // doing expensive operations
        load(this.path);
        compress();
        filter();
    }
    
    public void display()
    {
        System.out.println("Displaying image with the path = "+this.path);
    }
}

class ImageProxy implements IDisplay{
    ImageDisplay imageDis;
    String path;
    protected ImageProxy(String path){
        imageDis = null;
        this.path = path;
    }
    public void display()
    {
        if(imageDis == null)
        {
            imageDis = new ImageDisplay(this.path);
        }
        imageDis.display();
    }
}

public class Main{
    public static void main(String[] args)
    {
        IDisplay imageproxy = new ImageProxy("/images/image1.jpg");
        imageproxy.display();
    }
}

/*
This code is a perfect implementation of the **Proxy Design Pattern** (specifically, a **Virtual Proxy**).

The Proxy Pattern provides a surrogate or placeholder for another object to control access to it. In your code, `ImageProxy` acts as a stand-in wrapper for the real, resource-heavy `ImageDisplay` object.

---

### What This Design Pattern is Doing (How it Works)

The core mechanism at play here is called **Lazy Initialization** (or On-Demand Loading).

1. **The Placeholder Phase:** When you execute `new ImageProxy("/images/image1.jpg")` in your `main` method, **no image is loaded or processed yet**. The proxy object is lightweight; it simply stores the string path and leaves its `imageDis` reference pointing to `null`.
2. **The Interception:** When the client calls `imageproxy.display()`, the proxy intercepts the request.
3. **On-Demand Instantiation:** Inside the proxy's `display()` method, it checks if the real image object exists (`if(imageDis == null)`). Since it doesn't, it finally instantiates `new ImageDisplay(this.path)`.
4. **The Heavy Lifting:** Only *at this exact moment* do the expensive methods (`load()`, `compress()`, and `filter()`) run.
5. **Delegation:** Once the real object is safely built, the proxy delegates the actual work to it by calling `imageDis.display()`.

---

### How It Is Helpful (The Real-World Benefits)

Why go through all this trouble instead of just creating `ImageDisplay` directly?

#### 1. Performance and Memory Optimization

Imagine you are building a photo gallery application or a social media feed that needs to display a list of 1,000 images. If you instantiate 1,000 `ImageDisplay` objects directly, your application will attempt to load, compress, and filter all 1,000 high-resolution images *simultaneously* into the computer's RAM during startup. This will cause the application to freeze or crash due to an `OutOfMemoryError`.

By using the `ImageProxy`, you can safely instantiate 1,000 lightweight proxies instantly. The heavy CPU/RAM costs are deferred until the user actually scrolls down and needs to see that specific image.

#### 2. Separation of Concerns

The `ImageDisplay` class only needs to care about one job: how to load and process an image. It doesn't need complex logic managing *when* it should be created or caching states. The proxy handles the scheduling and access control rules, keeping both classes focused on a single responsibility.

#### 3. Complete Transparency to the Client

Because both `ImageProxy` and `ImageDisplay` implement the exact same `IDisplay` interface, the client (`Main`) has no idea it is interacting with a middleman.

```java
IDisplay imageproxy = new ImageProxy("/images/image1.jpg");
imageproxy.display();

```

If you decide later to remove the proxy and use the real object directly, you only change the constructor call. The rest of your business logic remains completely untouched.
*/
