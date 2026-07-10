interface IATMHandler{
    void handleReq(int num);
}

class TwoThousandHandler implements IATMHandler{
    IATMHandler nextHandler;
    public TwoThousandHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/2000>0)
        {
            System.out.println("No. of 2000 Notes = "+(num/2000));
            num = num%2000;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class ThousandHandler implements IATMHandler{
    IATMHandler nextHandler;
    public ThousandHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/1000>0)
        {
            System.out.println("No. of 1000 Notes = "+(num/1000));
            num = num%1000;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class FiveHundredHandler implements IATMHandler{
    IATMHandler nextHandler;
    public FiveHundredHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/500>0)
        {
            System.out.println("No. of 500 Notes = "+(num/500));
            num = num%500;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class HundredHandler implements IATMHandler{
    IATMHandler nextHandler;
    public HundredHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/100>0)
        {
            System.out.println("No. of 100 Notes = "+(num/100));
            num = num%100;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class FiftyHandler implements IATMHandler{
    IATMHandler nextHandler;
    public FiftyHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/50>0)
        {
            System.out.println("No. of 50 Notes = "+(num/50));
            num = num%50;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class TwentyHandler implements IATMHandler{
    IATMHandler nextHandler;
    public TwentyHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/20>0)
        {
            System.out.println("No. of 20 Notes = "+(num/20));
            num = num%20;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}



class TenHandler implements IATMHandler{
    IATMHandler nextHandler;
    public TenHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/10>0)
        {
            System.out.println("No. of 10 Notes = "+(num/10));
            num = num%10;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class FiveHandler implements IATMHandler{
    IATMHandler nextHandler;
    public FiveHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/5>0)
        {
            System.out.println("No. of 5 Notes = "+(num/5));
            num = num%5;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class TwoHandler implements IATMHandler{
    IATMHandler nextHandler;
    public TwoHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/2>0)
        {
            System.out.println("No. of 2 Notes = "+(num/2));
            num = num%2;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class OneHandler implements IATMHandler{
    IATMHandler nextHandler;
    public OneHandler(IATMHandler next)
    {
        nextHandler = next;
    }
    public void handleReq(int num)
    {
        if(num/1>0)
        {
            System.out.println("No. of 2 Notes = "+(num/1));
            num = num%1;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        IATMHandler one = new OneHandler(null);
        IATMHandler two = new TwoHandler(one);
        IATMHandler five = new FiveHandler(two);
        IATMHandler ten = new TenHandler(five);
        IATMHandler twenty = new TwentyHandler(five);
        IATMHandler fifty = new FiftyHandler(ten);
        IATMHandler hundred = new HundredHandler(fifty);
        IATMHandler fiveHundred = new FiveHundredHandler(hundred);
        IATMHandler thousand = new ThousandHandler(fiveHundred);
        IATMHandler twoThousand = new TwoThousandHandler(two);
        
        twoThousand.handleReq(578929);
    }
}
