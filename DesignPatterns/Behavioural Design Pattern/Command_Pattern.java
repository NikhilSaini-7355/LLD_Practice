import java.util.*;

interface ICommand{
    void execute();
    void undo();
}

class LightReceiver{
    void on(){
        System.out.println("Light is ON");
    }
    
    void off(){
        System.out.println("Light is OFF");
    }
}

class FanReceiver {
    void on(){
        System.out.println("Fan is ON");
    }
    
    void off(){
        System.out.println("Fan is OFF");
    }
}

class LightCommand implements ICommand{
    private LightReceiver light;
    public LightCommand(LightReceiver l)
    {
        light = l;
    }
    public void execute(){
        light.on();
    }
    
    public void undo(){
        light.off();
    }
}

class FanCommand implements ICommand{
    private FanReceiver fan;
    public FanCommand(FanReceiver f)
    {
        fan = f;
    }
    public void execute(){
       fan.on();   
    }
    
    public void undo(){
        fan.off();
    }
}

class RemoteControl{
    List<ICommand> buttonCommands;
    List<Boolean> buttonPressed;
    public RemoteControl(){
        buttonCommands = new ArrayList<>();
        buttonPressed = new ArrayList<>();
    }
    
    public void setCommand(ICommand command,int i)
    {
        if(buttonCommands.size()>i)
        {
            buttonCommands.set(i,command);
            buttonPressed.set(i,false);
        }
        else{
            buttonCommands.add(command);
            buttonPressed.add(false);
        }
    }
    
    public void pressButton(int i)
    {
        if(buttonPressed.get(i)==true)
        {
            buttonPressed.set(i,false);
            buttonCommands.get(i).undo();
        }
        else{
            buttonPressed.set(i,true);
            buttonCommands.get(i).execute();
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        RemoteControl remote = new RemoteControl();
        LightReceiver light = new LightReceiver();
        FanReceiver fan = new FanReceiver();
        ICommand fanCommand = new FanCommand(fan);
        ICommand lightCommand = new LightCommand(light);
        remote.setCommand(fanCommand,0);
        remote.setCommand(lightCommand,1);
        
        remote.pressButton(0);
        remote.pressButton(1);
        remote.pressButton(0);
        remote.pressButton(1);
        remote.pressButton(0);
        remote.pressButton(1);
    }
}
