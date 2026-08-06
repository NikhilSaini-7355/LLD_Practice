package Projects.Cricket_Dashboard.models;

import Projects.Cricket_Dashboard.enums.DeliveryType;

public class Delivery {
    private int runs;
    private DeliveryType type;
    public Delivery(String delivery)
    {
        this.runs = 0;
        if(delivery.equals("W"))
        {
            this.type = DeliveryType.WICKET;
        }
        else if(delivery.equals("Wd"))
        {
            this.type = DeliveryType.WIDE;
        }
        else
        {
            this.type = DeliveryType.NORMAL;
            this.runs = (delivery.charAt(0)-'0');
        }
    }

    public int getRuns()
    {
        return this.runs;
    }
    public DeliveryType getType()
    {
        return this.type;
    }
}
