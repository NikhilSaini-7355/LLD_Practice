interface Burger{
    void prepare();
}

class BasicBurger implements Burger{
    @Override
    public void prepare(){
        System.out.println("Preparing Basic Burger");
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
    public void prepare(){
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

interface BurgerFactory{
    Burger createBurger(String type);
}

class SinghBurger implements BurgerFactory{
    @Override
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
}


class KingBurger implements BurgerFactory{
    @Override
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
}

class Main{
    public static void main(String[] args)
    {
        BurgerFactory Kingfactory = new KingBurger();
        BurgerFactory Singhfactory = new SinghBurger();
        Burger kingburger1 = Kingfactory.createBurger("basic");
        Burger kingburger2 = Kingfactory.createBurger("standard");
        Burger kingburger3 = Kingfactory.createBurger("premium");
        
        Burger singhburger1 = Singhfactory.createBurger("basic");
        Burger singhburger2 = Singhfactory.createBurger("standard");
        Burger singhburger3 = Singhfactory.createBurger("premium");
        
        kingburger1.prepare();
        kingburger2.prepare();
        kingburger3.prepare();
        
        singhburger1.prepare();
        singhburger2.prepare();
        singhburger3.prepare();
    }
}
