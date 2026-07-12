import java.util.*;

class VendingMachine {
    private VendingMachineState currentState;
    private int itemCount;
    private int itemPrice;
    private int insertedCoins;
    
    private final VendingMachineState noCoinState;
    private final VendingMachineState hasCoinState;
    private final VendingMachineState dispenseState;
    private final VendingMachineState soldOutState;
    
    protected VendingMachine(int itemCount, int itemPrice) {
        this.noCoinState = new NoCoinState();
        this.hasCoinState = new HasCoinState();
        this.dispenseState = new DispenseState();
        this.soldOutState = new SoldOutState();
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.insertedCoins = 0;
        
        if (this.itemCount > 0) {
            this.currentState = this.noCoinState;
        } else {
            this.currentState = this.soldOutState;
        }
    }

    protected VendingMachineState getSoldOutState() { return this.soldOutState; }
    protected VendingMachineState getNoCoinState() { return this.noCoinState; }
    protected VendingMachineState getHasCoinState() { return this.hasCoinState; }
    protected VendingMachineState getDispenseState() { return this.dispenseState; }
    
    protected void setCurrentState(VendingMachineState state) {
        this.currentState = state;
    }
    
    protected void addCoins(int coins) { insertedCoins += coins; }
    protected void addCount(int quantity) { itemCount += quantity; }
    protected void reduceCount(int quantity) { itemCount -= quantity; }
    protected void reduceCoins(int coins) { insertedCoins -= coins; }
    
    protected int getItemCount() { return itemCount; }
    protected int getItemPrice() { return itemPrice; }
    protected int getInsertedCoins() { return insertedCoins; }
    
    public void insertCoin(int coins) { this.currentState.insertCoin(this, coins); }
    public void selectItem() { this.currentState.selectItem(this); }
    public void dispense() { this.currentState.dispense(this); }
    public void returnCoin() { this.currentState.returnCoin(this); }
    public void refill(int quantity) { this.currentState.refill(this, quantity); }

    public void printStatus() {
        System.out.println("\n--- Vending Machine Status ---");
        System.out.println("Items remaining: " + itemCount);
        System.out.println("Inserted coin: Rs " + insertedCoins);
        System.out.println("Current state: " + currentState.getStateName() + "\n");
    }
}

abstract class VendingMachineState {
    void insertCoin(VendingMachine machine, int coins) {
        System.out.println("Inserting coins is not supported in " + getStateName());
        System.out.println("Coins returned: Rs " + coins);
    }
    void selectItem(VendingMachine machine) {
        System.out.println("Selecting an item is not supported in " + getStateName());
    }
    void dispense(VendingMachine machine) {
        System.out.println("Dispensing is not supported in " + getStateName());
    }
    void returnCoin(VendingMachine machine) {
        System.out.println("Returning coins is not supported in " + getStateName());
    }
    void refill(VendingMachine machine, int quantity) {
        System.out.println("Refilling is not supported in " + getStateName());
    }
    abstract String getStateName();
}

class NoCoinState extends VendingMachineState {
    @Override
    public void insertCoin(VendingMachine machine, int coins) {
        machine.addCoins(coins);
        machine.setCurrentState(machine.getHasCoinState());
    }
    
    @Override
    public String getStateName() { return "NoCoinState"; }
}

class HasCoinState extends VendingMachineState {
    @Override
    public void insertCoin(VendingMachine machine, int coins) {
        machine.addCoins(coins);
        System.out.println("Added Rs " + coins + ". Total balance: Rs " + machine.getInsertedCoins());
    }

    @Override
    public void selectItem(VendingMachine machine) {
        if (machine.getInsertedCoins() >= machine.getItemPrice()) {
            machine.setCurrentState(machine.getDispenseState());
        } else {
            System.out.println("Insufficient funds. Item costs Rs " + machine.getItemPrice());
        }
    }
    
    @Override
    public void returnCoin(VendingMachine machine) {
        int coinsToReturn = machine.getInsertedCoins();
        machine.reduceCoins(coinsToReturn);
        System.out.println("Returned coins: Rs " + coinsToReturn);
        machine.setCurrentState(machine.getNoCoinState());
    }
    
    @Override
    public String getStateName() { return "HasCoinState"; }
}

class DispenseState extends VendingMachineState {
    @Override
    public void dispense(VendingMachine machine) {
        int numberOfItems = Math.min(machine.getItemCount(), (machine.getInsertedCoins() / machine.getItemPrice()));
        machine.reduceCount(numberOfItems);
        machine.reduceCoins(numberOfItems * machine.getItemPrice());
        
        int change = machine.getInsertedCoins();
        System.out.println("Number of Items Dispensed = " + numberOfItems);
        System.out.println("Change = " + change);
        machine.reduceCoins(change);
        
        if (machine.getItemCount() == 0) {
            machine.setCurrentState(machine.getSoldOutState());
        } else {
            machine.setCurrentState(machine.getNoCoinState());
        }
    }
    
    @Override
    public String getStateName() { return "DispenseState"; }
}

class SoldOutState extends VendingMachineState {
    @Override
    public void insertCoin(VendingMachine machine, int coins) {
        System.out.println("Sold Out!!! Cannot accept currency.");
        System.out.println("Coins returned = " + coins);
    }
    
    @Override
    public void refill(VendingMachine machine, int quantity) {
        machine.addCount(quantity);
        System.out.println("Machine refilled with " + quantity + " items.");
        machine.setCurrentState(machine.getNoCoinState());
    }
    
    @Override
    public String getStateName() { return "SoldOutState"; }
}
