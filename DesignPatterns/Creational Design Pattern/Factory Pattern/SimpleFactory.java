interface Burger{
    void prepare();
}

class BasicBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("Preparing basic burger");
    }
}

class StandardBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("Preparing standard burger");
    }
}

class PremiumBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("preparing premium burger");
    }
}

class BurgerFactory{
    public Burger createBurger(String type)
    {
        if(type=="basic")
        {
            return new BasicBurger();
        }
        else if(type=="standard")
        {
            return new StandardBurger();
        }
        else if(type=="premium")
        {
            return new PremiumBurger();
        }
        return null;
    }
}

class Main{
    public static void main(){
        BurgerFactory factory = new BurgerFactory();
        Burger burger1 = factory.createBurger("basic");
        Burger burger2 = factory.createBurger("standard");
        Burger burger3 = factory.createBurger("premium");
        burger1.prepare();
        burger2.prepare();
        burger3.prepare();
    }
}
