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
    
    protected void display()
    {
        System.out.println("Length = "+ this.flyweight.getLength());
        System.out.println("Width = "+ this.flyweight.getWidth());
        System.out.println("Weight = "+ this.flyweight.getWeight());
        System.out.println("Color = "+ this.flyweight.getColor());
        System.out.println("Texture = "+ this.flyweight.getTexture());
        System.out.println("posX = "+ this.posX);
        System.out.println("posY = "+ this.posY);
        System.out.println("velX = "+ this.velX);
        System.out.println("velY = "+ this.velY);
    }
}

class AsteroidFlyweightFactory{
    Map<String, AsteroidFlyweight> asteroidPool;
    protected AsteroidFlyweightFactory()
    {
        asteroidPool = new HashMap<>();
    }
    
    protected AsteroidContext getAsteroidWithFlyweight(int length, int width, int weight, String color, String texture, int posX, int posY, int velX, int velY)
    {
        String key = length + " | " + width + " | "+weight + " | "+color+" | "+texture;
        if(asteroidPool.containsKey(key))
        {
            return new AsteroidContext(asteroidPool.get(key),posX, posY, velX, velY);
        }
        else
        {
            asteroidPool.put(key, new AsteroidFlyweight(length,width,weight,color,texture));
            return new AsteroidContext(asteroidPool.get(key),posX, posY, velX, velY);
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        AsteroidFlyweightFactory factory = new AsteroidFlyweightFactory();
        AsteroidContext asteroid1 = factory.getAsteroidWithFlyweight(1,1,1,"red","coarse",1,1,1,1);
        asteroid1.display();
        
        AsteroidContext asteroid2 = factory.getAsteroidWithFlyweight(1,2,1,"blue","coarse",1,1,12,1);
        asteroid2.display();
        
        AsteroidContext asteroid3 = factory.getAsteroidWithFlyweight(1,2,1,"red","coarse",1,1,12,1);
        asteroid3.display();
    }
}
