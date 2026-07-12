import java.util.*;
interface IColleagues{
     public void sendAll(String message);
     public void sendTo(String To, String message);
     public void receive(String from, String message);
     public String getUsername();
     public void blockColleague(String To);
}

class User implements IColleagues{
    private IMediator mediator;
    private String username;
    public User(IMediator mediator, String username)
    {
        this.mediator = mediator;
        this.username = username;
        this.mediator.register(this);
    }
    
    public void sendAll(String message)
    {
        this.mediator.sendAll(this.username, message);
    }
    
    public void sendTo(String To, String message)
    {
        this.mediator.sendTo(this.username, To, message);
    }
    
    public void receive(String from, String message)
    {
        System.out.println(this.username+" received = "+"'"+message+"'"+" from "+ from);
    }
    
    public void blockColleague(String To)
    {
        this.mediator.blockColleague(this.username, To);
    }
    public String getUsername()
    {
        return this.username;
    }
}

interface IMediator{
    public void sendAll(String from, String message);
    public void sendTo(String from, String To, String message);
    public void remove(IColleagues colleague);
    public void register(IColleagues colleague);
    public void blockColleague(String from, String To);
}

class ChatMediator implements IMediator{
    private List<IColleagues> colleagues;
    private Map<String,List<String>> muted;
    public ChatMediator()
    {
        colleagues = new ArrayList<>();
        muted = new HashMap<>();
    }
    
    public void sendAll(String from, String message)
    {
        for(IColleagues colleague : colleagues)
        {
            if(colleague.getUsername().equals(from) || (muted.containsKey(colleague.getUsername()) && muted.get(colleague.getUsername()).contains(from))){
                continue;
            }
            colleague.receive(from, message);
        }
    }
    
    public void sendTo(String from, String To, String message)
    {
        if(muted.containsKey(To) && muted.get(To).contains(from))
        {
            return;
        }
        for(IColleagues colleague : colleagues)
        {
            if(colleague.getUsername().equals(To))
            {
                colleague.receive(from, message);
            }
        }
    }
    
    public void remove(IColleagues colleague)
    {
        if(colleagues.contains(colleague))
        {
            colleagues.remove(colleague);
        }
    }
    
    public void register(IColleagues colleague)
    {
        if(!colleagues.contains(colleague))
        {
            colleagues.add(colleague);
            muted.put(colleague.getUsername(),new ArrayList<>());
        }
    }
    
    public void blockColleague(String from, String To)
    {
        if(muted.containsKey(from))
        {
                if(!(muted.get(from).contains(To)))
            {
                muted.get(from).add(To);
            }
        }
    }
}

public class Main{
    public static void main(String[] args)
    {
        IMediator mediator = new ChatMediator();
        IColleagues user1 = new User(mediator,"user1");
        IColleagues user2 = new User(mediator,"user2");
        IColleagues user3 = new User(mediator,"user3");
        IColleagues user4 = new User(mediator,"user4");
        IColleagues user5 = new User(mediator,"user5");
        user4.blockColleague("user1");
        user1.sendAll("Hello World!!");
        user5.blockColleague("user2");
        user2.sendAll("Life is Hard and Unfair");
        
        user3.sendTo("user4","Life is hard man and really really unfair");
        user4.sendTo("user5","user4 said life is hard and unfair. I think so too.");
        user5.sendTo("user3","I agree with you. Life is really hard and unfair.");
    }
}
