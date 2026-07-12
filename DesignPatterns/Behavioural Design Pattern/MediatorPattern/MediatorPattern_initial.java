import java.util.*;
abstract class IColleagues{
    IMediator mediator;
    String username;
    protected IColleagues(IMediator mediator, String username)
    {
        this.mediator = mediator;
        this.username = username;
    }
    abstract protected void sendAll(String message);
    abstract protected void sendTo(String To, String message);
    abstract protected void receive(String from, String message);
}

class User extends IColleagues{
    protected User(IMediator mediator, String username)
    {
        super(mediator,username);
    }
    
    protected void sendAll(String message)
    {
        this.mediator.sendAll(this.username, message);
    }
    
    protected void sendTo(String To, String message)
    {
        this.mediator.sendTo(this.username, To, message);
    }
    
    protected void receive(String from, String message)
    {
        System.out.println(this.username+" received = "+"'"+message+"'"+" from "+ from);
    }
}

abstract class IMediator{
    List<IColleagues> colleagues;
    Map<String,List<String>> muted;
    protected IMediator()
    {
        colleagues = new ArrayList<>();
        muted = new HashMap<>();
    }
    abstract protected void sendAll(String from, String message);
    abstract protected void sendTo(String from, String To, String message);
    protected void remove(IColleagues colleague)
    {
        if(colleagues.contains(colleague))
        {
            colleagues.remove(colleague);
        }
    }
    
    protected void register(IColleagues colleague)
    {
        if(!colleagues.contains(colleague))
        {
            colleagues.add(colleague);
        }
    }
}

class ChatMediator extends IMediator{
    protected ChatMediator()
    {
        super();
    }
    protected void sendAll(String from, String message)
    {
        for(IColleagues colleague : colleagues)
        {
            if(colleague.username.equals(from)){
                continue;
            }
            colleague.receive(from, message);
        }
    }
    
    protected void sendTo(String from, String To, String message)
    {
        for(IColleagues colleague : colleagues)
        {
            if(colleague.username.equals(To))
            {
                colleague.receive(from, message);
            }
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        IMediator mediator = new ChatMediator();
        User user1 = new User(mediator,"user1");
        User user2 = new User(mediator,"user2");
        User user3 = new User(mediator,"user3");
        User user4 = new User(mediator,"user4");
        User user5 = new User(mediator,"user5");
        
        mediator.register(user1);
        mediator.register(user2);
        mediator.register(user3);
        mediator.register(user4);
        mediator.register(user5);
        
        user1.sendAll("Hello World!!");
        user2.sendAll("Life is Hard and Unfair");
        
        user3.sendTo("user4","Life is hard man and really really unfair");
        user4.sendTo("user5","user4 said life is hard and unfair. I think so too.");
        user5.sendTo("user3","I agree with you. Life is really hard and unfair.");
    }
}
