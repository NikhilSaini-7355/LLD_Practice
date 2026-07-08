interface Burger{
    void prepare();
}

class BasicBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("preparing basic burger");
    }
}

class StandardBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("preparing standard burger");
    }
}

class PremiumBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("preparing premium burger");
    }
}

class BasicWheatBurger implements Burger{
    @Override
    public void prepare()
    {
        System.out.println("preparing basic wheat burger");
    }
}

class StandardWheatBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("preparing standard wheat burger");
    }
}

class PremiumWheatBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("preparing premium wheat burger");
    }
}

interface GarlicBread{
    void prepare();
}

class BasicGarliBread implements GarlicBread{
    @Override
    public void prepare(){
        System.out.println("preparing basic garlic bread");
    }
}

class StandardGarlicBread implements GarlicBread {
    @Override
    public void prepare(){
        System.out.println("preparing standard garlic bread");
    }
}

class PremiumGarlicBread implements GarlicBread{
    @Override
    public void prepare(){
        System.out.println("preparing premium garlic bread");
    }
}

class BasicWheatGarlicBread implements GarlicBread{
    @Override
    public void prepare(){
        System.out.println("preparing basic wheat garlic bread");
    }
}

class StandardWheatGarlicBread implements GarlicBread{
    @Override
    public void prepare(){
        System.out.println("preparing standard wheat garlic bread");
    }
}

class PremiumWheatGarlicBread implements GarlicBread{
    @Override
    public void prepare(){
        System.out.println("preparing premium wheat garlic bread");
    }
}

interface Factory{
    Burger createBurger(String type);
    GarlicBread createGarlicBread(String type);
}

class SinghFactory implements Factory{
    public Burger createBurger(String type){
        if(type.equalsIgnoreCase("basic"))
        {
            return new BasicBurger();
        }
        else if(type.equalsIgnoreCase("standard"))
        {
            return new StandardBurger();
        }
        else if(type.equalsIgnoreCase("premium"))
        {
            return new PremiumBurger();
        }
        return null;
    }
    
    public GarlicBread createGarlicBread(String type){
        if(type.equalsIgnoreCase("basic"))
        {
            return new BasicGarliBread();
        }
        else if(type.equalsIgnoreCase("standard"))
        {
            return new StandardGarlicBread();
        }
        else if(type.equalsIgnoreCase("premium"))
        {
            return new PremiumGarlicBread();
        }
        return null;
    }
}

class KingFactory implements Factory{
    public Burger createBurger(String type){
        if(type.equalsIgnoreCase("basic"))
        {
            return new BasicWheatBurger();
        }
        else if(type.equalsIgnoreCase("standard"))
        {
            return new StandardWheatBurger();
        }
        else if(type.equalsIgnoreCase("premium"))
        {
            return new PremiumWheatBurger();
        }
        return null;
    }
    
    public GarlicBread createGarlicBread(String type){
        if(type.equalsIgnoreCase("basic"))
        {
            return new BasicWheatGarlicBread();
        }
        else if(type.equalsIgnoreCase("standard"))
        {
            return new StandardWheatGarlicBread();
        }
        else if(type.equalsIgnoreCase("premium"))
        {
            return new PremiumWheatGarlicBread();
        }
        return null;
    }
}

class Main{
    public static void main(String[] args){
        Factory KingDishes = new KingFactory();
        Factory SinghDishes = new SinghFactory();
        
        Burger kingburger1 = KingDishes.createBurger("basic");
        Burger kingburger2 = KingDishes.createBurger("standard");
        Burger kingburger3 = KingDishes.createBurger("premium");
        
        Burger singhburger1 = SinghDishes.createBurger("basic");
        Burger singhburger2 = SinghDishes.createBurger("standard");
        Burger singhburger3 = SinghDishes.createBurger("premium");
        
        GarlicBread kinggarlicbread1 = KingDishes.createGarlicBread("basic");
        GarlicBread kinggarlicbread2 = KingDishes.createGarlicBread("standard");
        GarlicBread kinggarlicbread3 = KingDishes.createGarlicBread("premium");
        
        GarlicBread singhgarlicbread1 = SinghDishes.createGarlicBread("basic");
        GarlicBread singhgarlicbread2 = SinghDishes.createGarlicBread("standard");
        GarlicBread singhgarlicbread3 = SinghDishes.createGarlicBread("premium");
        
        kingburger1.prepare();
        kingburger2.prepare();
        kingburger3.prepare();
        
        singhburger1.prepare();
        singhburger2.prepare();
        singhburger3.prepare();
        
        kinggarlicbread1.prepare();
        kinggarlicbread2.prepare();
        kinggarlicbread3.prepare();
        
        singhgarlicbread1.prepare();
        singhgarlicbread2.prepare();
        singhgarlicbread3.prepare();
    }
}
