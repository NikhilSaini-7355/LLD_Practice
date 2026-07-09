interface ICharacter{
    String getAbilities();
}

class Mario implements ICharacter{
    public String getAbilities(){
        return "Mario ";
    }
}

abstract class Decorator implements ICharacter{
    protected ICharacter character;
    public Decorator(ICharacter c){
        this.character = c;
    }
}

class GunPowderDecorator extends Decorator{
    public GunPowderDecorator(ICharacter c)
    {
        super(c);
    }
    
    public String getAbilities()
    {
        return this.character.getAbilities() + "|| GunPowderAdded ";
    }
}

class HeightUPDecorator extends Decorator{
    public HeightUPDecorator(ICharacter c)
    {
        super(c);
    }
    
    public String getAbilities()
    {
        return this.character.getAbilities() + "|| HeightUPAdded ";
    }
}

class StarPowerUPDecorator extends Decorator{
    public StarPowerUPDecorator(ICharacter c)
    {
        super(c);
    }
    
    public String getAbilities()
    {
        return this.character.getAbilities() + "|| StarPowerUPAdded ";
    }
}

public class Main{
    public static void main(String[] args)
    {
        ICharacter MarioObj = new StarPowerUPDecorator(new HeightUPDecorator(new GunPowderDecorator(new Mario())));
        String ability = MarioObj.getAbilities();
        System.out.println(ability);
    
        ICharacter MarioObj2 = new HeightUPDecorator(new StarPowerUPDecorator(new GunPowderDecorator(new Mario())));
        String ability2 = MarioObj2.getAbilities();
        System.out.println(ability2);
    }
}
