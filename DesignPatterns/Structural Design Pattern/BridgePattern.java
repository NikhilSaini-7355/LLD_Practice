interface Engine{
    void start();
}

class Electric implements Engine{
    public void start()
    {
        System.out.println("Electric engine started");
    }
}

class Petrol implements Engine{
    public void start()
    {
        System.out.println("Petrol engine started");
    }
}
class Diesel implements Engine{
    public void start()
    {
        System.out.println("Diesel engine started");
    }
}

abstract class Car{
    protected Engine engine;
    abstract void drive();
    protected Car(Engine engine)
    {
        this.engine = engine;
    }
    public void setEngine(Engine engine)
    {
        this.engine = engine;
    }
}

class SUV extends Car{
    public SUV(Engine engine)
    {
        super(engine);
    }
    public void drive()
    {
        this.engine.start();
        System.out.println("Driving SUV");
    }
}

class Sedan extends Car{
    public Sedan(Engine engine)
    {
        super(engine);
    }
    public void drive()
    {
        this.engine.start();
        System.out.println("Driving Sedan");
    }
}

class Hatchback extends Car{
    public Hatchback(Engine engine)
    {
        super(engine);
    }
    public void drive()
    {
        this.engine.start();
        System.out.println("Driving Hatchback");
    }
}



public class Main{
    public static void main(String[] args)
    {
        Car suv = new SUV(new Electric());
        Car sedan = new Sedan(new Petrol());
        Car hatchback = new Hatchback(new Diesel());
        
        suv.setEngine(new Electric());
        suv.drive();
        suv.setEngine(new Petrol());
        suv.drive();
        suv.setEngine(new Diesel());
        suv.drive();
        
        System.out.println("========================================================");
        sedan.setEngine(new Electric());
        sedan.drive();
        sedan.setEngine(new Petrol());
        sedan.drive();
        sedan.setEngine(new Diesel());
        sedan.drive();
        
        System.out.println("========================================================");
        hatchback.setEngine(new Electric());
        hatchback.drive();
        hatchback.setEngine(new Petrol());
        hatchback.drive();
        hatchback.setEngine(new Diesel());
        hatchback.drive();
    }
}
