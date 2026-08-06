package Projects.Cricket_Dashboard.models;
import java.util.*;
import Projects.Cricket_Dashboard.enums.*;

public class Over {
    private List<Delivery> deliveries;
    private Player bowler;
    private int cnt = 0;
    public Over(Player bowler)
    {
        this.bowler = bowler;
        deliveries = new ArrayList<>();
    }
    public void addDelivery(Delivery d)
    {
        if(d.getType()==DeliveryType.NORMAL && cnt<6)
        {
            deliveries.add(d);
            cnt++;
        }
        else if(d.getType()!=DeliveryType.NORMAL){
            deliveries.add(d);
        }
    }
    public List<Delivery> getDeliveries()
    {
        return Collections.unmodifiableList(deliveries);
    }

    public Player getBowler()
    {
        return bowler;
    }

}
