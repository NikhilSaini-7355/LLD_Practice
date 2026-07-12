class VendingMachine{
    private VendingMachineState currentState;
    private int itemCount;
    private int itemPrice;
    private int insertedCoins;
    
    private VendingMachineState noCoinState;
    private VendingMachineState hasCoinState;
    private VendingMachineState dispenseState;
    private VendingMachineState soldOutState;
    
    protected VendingMachine(int itemCount, int itemPrice)
    {
        this.noCoinState = new NoCoinState();
        this.hasCoinState = new HasCoinState();
        this.dispenseState = new DispenseState();
        this.soldOutState = new SoldOutState();
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.insertedCoins = 0;
        
        if(this.itemCount>0)
        {
            this.currentState = this.noCoinState;
        }
        else
        {
            this.currentState = this.soldOutState;
        }
    }
    
    protected void setCurrentState(String state)
    {
        if(state.equals("NoCoinState"))
        {
            this.currentState = this.noCoinState;
        }
        else if(state.equals("HasCoinState"))
        {
            this.currentState = this.hasCoinState;
        }
        else if(state.equals("DispenseState"))
        {
            this.currentState = this.dispenseState;
        }
        else if(state.equals("SoldOutState"))
        {
            this.currentState = this.soldOutState;
        }
    }
    protected void addCoins(int coins)
    {
        insertedCoins += coins;
    }
    
    protected void addCount(int quantity)
    {
        itemCount += quantity;
    }
    
    protected void reduceCount(int quantity)
    {
        itemCount -= quantity;
    }
    
    protected void reduceCoins(int coins)
    {
        insertedCoins -= coins;
    }
    
    protected int getItemCount()
    {
        return itemCount;
    }
    
    protected int getItemPrice()
    {
        return itemPrice;
    }
    
    protected int getInsertedCoins()
    {
        return insertedCoins;
    }
    
    public void insertCoin(int coins)
    {
        this.currentState.insertCoin(this,coins);
    }
    
    public void selectItem()
    {
        this.currentState.selectItem(this);
    }
    
    public void dispense()
    {
        this.currentState.dispense(this);
    }
    
    public void returnCoin()
    {
        this.currentState.returnCoin(this);
    }
    
    public void refill(int quantity)
    {
        this.currentState.refill(this,quantity);
    }

    public void printStatus() {
        System.out.println("\n--- Vending Machine Status ---");
        System.out.println("Items remaining: " + itemCount);
        System.out.println("Inserted coin: Rs " + insertedCoins);
        System.out.println("Current state: " + currentState.getStateName() + "\n");
    }
}

abstract class VendingMachineState{
    void insertCoin(VendingMachine machine, int coins)
    {
        machine.setCurrentState(this.getStateName());
    }
    void selectItem(VendingMachine machine)
    {
        machine.setCurrentState(this.getStateName());
    }
    void dispense(VendingMachine machine)
    {
        machine.setCurrentState(this.getStateName());
    }
    void returnCoin(VendingMachine machine)
    {
        machine.setCurrentState(this.getStateName());
    }
    void refill(VendingMachine machine, int quantity)
    {
        machine.setCurrentState(this.getStateName());
    }
    abstract String getStateName();
}

class NoCoinState extends VendingMachineState{
    @Override
    public void insertCoin(VendingMachine machine, int coins)
    {
        machine.addCoins(coins);
        machine.setCurrentState("HasCoinState");
    }
    
    @Override
    public String getStateName()
    {
        return "NoCoinState";
    }
}

class HasCoinState extends VendingMachineState{
    @Override
    public void insertCoin(VendingMachine machine, int coins)
    {
        machine.addCoins(coins);
        machine.setCurrentState("HasCoinState");
    }
    public void selectItem(VendingMachine machine)
    {
        if(machine.getInsertedCoins()>=machine.getItemPrice())
        {
            machine.setCurrentState("DispenseState");
        }
        else
        {
            machine.setCurrentState("HasCoinState");
        }
    }
    
    @Override
    public void returnCoin(VendingMachine machine)
    {
        machine.reduceCoins(machine.getInsertedCoins());
        machine.setCurrentState("NoCoinState");
    }
    
    @Override
    public String getStateName()
    {
        return "HasCoinState";
    }
}

class DispenseState extends VendingMachineState{
    @Override
    public void dispense(VendingMachine machine)
    {
        int numberOfItems = Math.min(machine.getItemCount(),(machine.getInsertedCoins()/machine.getItemPrice()));
        machine.reduceCount(numberOfItems);
        machine.reduceCoins(numberOfItems*machine.getItemPrice());
        int change = machine.getInsertedCoins();
        System.out.println("Number of Items Dispensed = "+ numberOfItems);
        System.out.println("Change = "+change);
        machine.reduceCoins(change);
        if(machine.getItemCount()==0)
        {
            machine.setCurrentState("SoldOutState");
        }
        else
        {
            machine.setCurrentState("NoCoinState");
        }
    }
    
    @Override
    public String getStateName()
    {
        return "DispenseState";
    }
}

class SoldOutState extends VendingMachineState{
    @Override
    public void refill(VendingMachine machine, int quantity)
    {
        machine.addCount(quantity);
        machine.setCurrentState("NoCoinState");
    }
    
    @Override
    public String getStateName()
    {
        return "SoldOutState";
    }
}

public class Main{
    public static void main(String[] args)
    {
        System.out.println("=== Water Bottle VENDING MACHINE ===");
        
        int itemCount = 2;
        int itemPrice = 20;

        VendingMachine machine = new VendingMachine(itemCount, itemPrice);
        machine.printStatus();
        
        // Test scenarios - each operation potentially changes state
        System.out.println("1. Trying to select item without coin:");
        machine.selectItem();  // Should ask for coin, no state change
        machine.printStatus();
        
        System.out.println("2. Inserting coin:");
        machine.insertCoin(10);  // State changes to HAS_COIN
        machine.printStatus();
        
        System.out.println("3. Selecting item with insufficient funds:");
        machine.selectItem();  // Insufficient funds, stays in HAS_COIN
        machine.printStatus();
        
        System.out.println("4. Adding more coins:");
        machine.insertCoin(20);  // Add more money, stays in HAS_COIN
        machine.printStatus();
        
        System.out.println("5. Selecting item Now");
        machine.selectItem();  // State changes to SOLD
        machine.printStatus();
        
        System.out.println("6. Dispensing item:");
        machine.dispense(); // State changes to NO_COIN (items remaining)
        machine.printStatus();
        
        System.out.println("7. Buying last item:");
        machine.insertCoin(20);  // State changes to HAS_COIN
        machine.selectItem();  // State changes to SOLD
        machine.dispense(); // State changes to SOLD_OUT (no items left)
        machine.printStatus();
        
        System.out.println("8. Trying to use sold out machine:");
        machine.insertCoin(5);  // Coin returned, stays in SOLD_OUT
        machine.printStatus();
        
        System.out.println("9. Trying to use sold out machine:");
        machine.refill(2);
        machine.printStatus(); // State changes NO_COIN
    }
}
                                                                                                                                                                                                                                                                                                                                         
