abstract class IATMHandler{
    IATMHandler nextHandler;
    int numNotes;
    public abstract void handleReq(int num);
    public void setNextHandler(IATMHandler handler)
    {
        nextHandler = handler;
    }
}

class TwoThousandHandler extends IATMHandler{
    public TwoThousandHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/2000;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 2000 Notes = "+spentNotes);
            num -= spentNotes*2000;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class ThousandHandler extends IATMHandler{
    public ThousandHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/1000;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 1000 Notes = "+spentNotes);
            num -= spentNotes*1000;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class FiveHundredHandler extends IATMHandler{
    public FiveHundredHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/500;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 500 Notes = "+spentNotes);
            num -= spentNotes*500;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class HundredHandler extends IATMHandler{
    public HundredHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/100;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 100 Notes = "+spentNotes);
            num -= spentNotes*100;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class FiftyHandler extends IATMHandler{
    public FiftyHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/50;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 50 Notes = "+spentNotes);
            num -= spentNotes*50;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class TwentyHandler extends IATMHandler{
    public TwentyHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/20;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 20 Notes = "+spentNotes);
            num -= spentNotes*20;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}



class TenHandler extends IATMHandler{
    public TenHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/10;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 10 Notes = "+spentNotes);
            num -= spentNotes*10;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class FiveHandler extends IATMHandler{
    public FiveHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/5;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 5 Notes = "+spentNotes);
            num -= spentNotes*5;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class TwoHandler extends IATMHandler{
    public TwoHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/2;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 2 Notes = "+spentNotes);
            num -= spentNotes*2;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            nextHandler.handleReq(num);
        }
    }
}

class OneHandler extends IATMHandler{
    public OneHandler(int num)
    {
        numNotes = num;
    }
    public void handleReq(int num)
    {
        int requiredNotes = num/1;
        if(requiredNotes>0)
        {
            int spentNotes = Math.min(numNotes,requiredNotes);
            System.out.println("No. of 1 Notes = "+spentNotes);
            num -= spentNotes*1;
            numNotes -= spentNotes;
        }
        if(num>0)
        {
            System.out.println("Funds Insufficient: "+num+" money left");
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        IATMHandler one = new OneHandler(6);
        IATMHandler two = new TwoHandler(5);
        IATMHandler five = new FiveHandler(10);
        IATMHandler ten = new TenHandler(23);
        IATMHandler twenty = new TwentyHandler(21);
        IATMHandler fifty = new FiftyHandler(23);
        IATMHandler hundred = new HundredHandler(34);
        IATMHandler fiveHundred = new FiveHundredHandler(67);
        IATMHandler thousand = new ThousandHandler(3);
        IATMHandler twoThousand = new TwoThousandHandler(2);
        
        twoThousand.setNextHandler(thousand);
        thousand.setNextHandler(fiveHundred);
        fiveHundred.setNextHandler(hundred);
        hundred.setNextHandler(fifty);
        fifty.setNextHandler(twenty);
        twenty.setNextHandler(ten);
        ten.setNextHandler(five);
        five.setNextHandler(two);
        two.setNextHandler(one);
        
        twoThousand.handleReq(578929);
    }
}


==========================================================================================================================================
  // Above implementation violates DRY Principle
  // With DRY Principle:-
  abstract class IATMHandler {
    protected IATMHandler nextHandler;
    protected int numNotes;
    protected int denomination; // Store the note value here

    public IATMHandler(int denomination, int numNotes) {
        this.denomination = denomination;
        this.numNotes = numNotes;
    }

    public void setNextHandler(IATMHandler handler) {
        this.nextHandler = handler;
    }

    // Write the logic ONCE in the parent class
    public void handleReq(int num) {
        int requiredNotes = num / denomination;
        
        if (requiredNotes > 0) {
            int spentNotes = Math.min(numNotes, requiredNotes);
            if (spentNotes > 0) {
                System.out.println("No. of " + denomination + " Notes = " + spentNotes);
                num -= spentNotes * denomination;
                numNotes -= spentNotes;
            }
        }
        
        // Pass to next handler if money is still owed
        if (num > 0) {
            if (nextHandler != null) {
                nextHandler.handleReq(num);
            } else {
                // If there is no next handler, the ATM is out of money
                System.out.println("Funds Insufficient: " + num + " money left unfulfilled");
            }
        }
    }
}

// Now, look at how beautiful and simple your subclasses become!
class TwoThousandHandler extends IATMHandler {
    public TwoThousandHandler(int num) { super(2000, num); }
}

class ThousandHandler extends IATMHandler {
    public ThousandHandler(int num) { super(1000, num); }
}

class FiveHundredHandler extends IATMHandler {
    public FiveHundredHandler(int num) { super(500, num); }
}

class HundredHandler extends IATMHandler {
    public HundredHandler(int num) { super(100, num); }
}

class FiftyHandler extends IATMHandler {
    public FiftyHandler(int num) { super(50, num); }
}

class TwentyHandler extends IATMHandler {
    public TwentyHandler(int num) { super(20, num); }
}

class TenHandler extends IATMHandler {
    public TenHandler(int num) { super(10, num); }
}

class FiveHandler extends IATMHandler {
    public FiveHandler(int num) { super(5, num); }
}

class TwoHandler extends IATMHandler {
    public TwoHandler(int num) { super(2, num); }
}

class OneHandler extends IATMHandler {
    public OneHandler(int num) { super(1, num); }
}

public class Main {
    public static void main(String[] args) {
        IATMHandler twoThousand = new TwoThousandHandler(2);
        IATMHandler thousand = new ThousandHandler(3);
        IATMHandler fiveHundred = new FiveHundredHandler(67);
        IATMHandler hundred = new HundredHandler(34);
        IATMHandler fifty = new FiftyHandler(23);
        IATMHandler twenty = new TwentyHandler(21);
        IATMHandler ten = new TenHandler(23);
        IATMHandler five = new FiveHandler(10);
        IATMHandler two = new TwoHandler(5);
        IATMHandler one = new OneHandler(6);
        
        twoThousand.setNextHandler(thousand);
        thousand.setNextHandler(fiveHundred);
        fiveHundred.setNextHandler(hundred);
        hundred.setNextHandler(fifty);
        fifty.setNextHandler(twenty);
        twenty.setNextHandler(ten);
        ten.setNextHandler(five);
        five.setNextHandler(two);
        two.setNextHandler(one);
        
        System.out.println("Processing Withdrawal for: 578929\n---------------------------------");
        twoThousand.handleReq(578929);
    }
}
