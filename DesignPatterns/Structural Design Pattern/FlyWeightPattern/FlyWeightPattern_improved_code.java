import java.util.*;
class AsteroidFlyweight{
    final private int length;
    final private int width;
    final private int weight;
    final private String color;
    final private String texture;
    
    protected AsteroidFlyweight(int length, int width, int weight, String color, String texture){
        this.length = length;
        this.width = width;
        this.weight = weight;
        this.color = color;
        this.texture = texture;
    }
    
    protected int getLength()
    {
        return length;
    }
    
    protected int getWidth()
    {
        return width;
    }
    
    protected int getWeight()
    {
        return weight;
    }
    
    protected String getColor()
    {
        return color;
    }
    
    protected String getTexture()
    {
        return texture;
    }
    
    public void render(int posX, int posY, int velocityX, int velocityY) {
        System.out.println("Rendering " + color + ", " + texture + ", " 
            + " asteroid at (" + posX + "," + posY 
            + ") Size: " + length + "x" + width
            + " Velocity: (" + velocityX + ", " 
            + velocityY + ")");
    }

    public static long getMemoryUsage() {
        return Integer.BYTES * 3 + 40 * 3;                      
    }
}

class AsteroidContext{
    final private AsteroidFlyweight flyweight;
    private int posX;
    private int posY;
    private int velX;
    private int velY;
    
    protected AsteroidContext(AsteroidFlyweight flyweight, int posX, int posY, int velX, int velY)
    {
        this.flyweight = flyweight;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
    }
    
    protected int getPosX()
    {
        return posX;
    }
    
    protected int getPosY()
    {
        return posY;
    }
    
    protected int getvelX()
    {
        return velX;
    }
    
    protected int getvelY()
    {
        return velY;
    }
    
    protected void setVelX(int velX)
    {
        this.velX = velX;
    }
    
    protected void setPosX(int posX)
    {
        this.posX = posX;
    }
    
    protected void setPosY(int posY)
    {
        this.posY = posY;
    }
    
    protected void setVelY(int velY)
    {
        this.velY = velY;
    }
    
    public void render() {
        flyweight.render(posX, posY, velX, velY);
    }

    public static long getMemoryUsage() {
        return 8 + Integer.BYTES * 4; // approximate pointer + ints
    }
}

class AsteroidFlyweightFactory{
    private static Map<String, AsteroidFlyweight> asteroidPool = new HashMap<>();
    
    protected static AsteroidFlyweight getAsteroidWithFlyweight(int length, int width, int weight, String color, String texture)
    {
        String key = length + " | " + width + " | "+weight + " | "+color+" | "+texture;
        if(asteroidPool.containsKey(key))
        {
            return asteroidPool.get(key);
        }
        else
        {
            asteroidPool.put(key, new AsteroidFlyweight(length,width,weight,color,texture));
            return asteroidPool.get(key);
        }
    }
    
    public static int getFlyweightCount() {
        return asteroidPool.size();
    }

    public static long getTotalFlyweightMemory() {
        return asteroidPool.size() * AsteroidFlyweight.getMemoryUsage();
    }

    public static void cleanup() {
        asteroidPool.clear();
    }
}

class SpaceGameWithFlyweight {
    private List<AsteroidContext> asteroids = new ArrayList<>();

    public void spawnAsteroids(int count) {
        System.out.println("\n=== Spawning " + count + " asteroids ===");

        String[] colors = {"Red", "Blue", "Gray"};
        String[] textures = {"Rocky", "Metallic", "Icy"};
        int[] sizes = {25, 35, 45};

        for (int i = 0; i < count; i++) {
            int type = i % 3;

            AsteroidFlyweight flyweight= AsteroidFlyweightFactory.getAsteroidWithFlyweight(
                sizes[type], sizes[type], sizes[type] * 10,
                colors[type], textures[type]
            );
            
            asteroids.add(new AsteroidContext(
                flyweight,
                100 + i * 50, // Simple x: 100, 150, 200, 250...
                200 + i * 30, // Simple y: 200, 230, 260, 290...
                1, // All move right with velocity 1
                2  // All move down with velocity 2
            ));
        }

        System.out.println("Created " + asteroids.size() + " asteroid contexts");
        System.out.println("Total flyweight objects: " + AsteroidFlyweightFactory.getFlyweightCount());
    }

    public void renderAll() {
        System.out.println("\n--- Rendering first 5 asteroids ---");
        for (int i = 0; i < Math.min(5, asteroids.size()); i++) {
            asteroids.get(i).render();
        }
    }

    public long calculateMemoryUsage() {
        long contextMemory = asteroids.size() * AsteroidContext.getMemoryUsage();
        long flyweightMemory = AsteroidFlyweightFactory.getTotalFlyweightMemory();
        return contextMemory + flyweightMemory;
    }

    public int getAsteroidCount() {
        return asteroids.size();
    }
}

public class Main{
    public static void main(String[] args)
    {
        final int ASTEROID_COUNT = 1_000_000;

        System.out.println("\nTESTING WITH FLYWEIGHT PATTERN");
        SpaceGameWithFlyweight game = new SpaceGameWithFlyweight();

        game.spawnAsteroids(ASTEROID_COUNT);

        // Show first 5 asteroids to see the pattern
        game.renderAll();

        // Calculate and display memory usage
        long totalMemory = game.calculateMemoryUsage();

        System.out.println("\n=== MEMORY USAGE ===");
        System.out.println("Total asteroids: " + ASTEROID_COUNT);
        System.out.println("Memory per asteroid: " + AsteroidContext.getMemoryUsage() + " bytes");
        System.out.println("Total memory used: " + totalMemory + " bytes");
        System.out.println("Memory in MB: " + (totalMemory / (1024.0 * 1024.0)) + " MB");
    }
}
