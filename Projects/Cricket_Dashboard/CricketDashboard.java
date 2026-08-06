package Projects.Cricket_Dashboard;
import java.util.ArrayList;
import java.util.List;

import Projects.Cricket_Dashboard.models.*;
public class CricketDashboard {
    public static void main(String[] args)
    {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Cricket Dashboard<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Team team1 = new Team("CSK");
        Team team2 = new Team("KXP");
        List<Player> players = new ArrayList<>();
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        Player p4 = new Player("P4");
        Player p5 = new Player("P5");

        Player q1 = new Player("Q1");
        Player q2 = new Player("Q2");
        Player q3 = new Player("Q3");
        Player q4 = new Player("Q4");
        Player q5 = new Player("Q5");

        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);

        players.add(q1);
        players.add(q2);
        players.add(q3);
        players.add(q4);
        players.add(q5);

        
        Match match = new Match(5,2,team1,team2);
        match.addPlayers(players);

        List<Over> overs = new ArrayList<>();
        Over over1 = new Over(q1);
        Over over2 = new Over(q2);

        Over over3 = new Over(p1);
        Over over4 = new Over(p2);

        over1.addDelivery(new Delivery("1"));
        over1.addDelivery(new Delivery("1"));
        over1.addDelivery(new Delivery("1"));
        over1.addDelivery(new Delivery("1"));
        over1.addDelivery(new Delivery("1"));
        over1.addDelivery(new Delivery("2"));

        over2.addDelivery(new Delivery("W"));
        over2.addDelivery(new Delivery("4"));
        over2.addDelivery(new Delivery("4"));
        over2.addDelivery(new Delivery("Wd"));
        over2.addDelivery(new Delivery("W"));
        over2.addDelivery(new Delivery("1"));
        over2.addDelivery(new Delivery("6"));

        over3.addDelivery(new Delivery("4"));
        over3.addDelivery(new Delivery("6"));
        over3.addDelivery(new Delivery("W"));
        over3.addDelivery(new Delivery("W"));
        over3.addDelivery(new Delivery("1"));
        over3.addDelivery(new Delivery("1"));

        over4.addDelivery(new Delivery("6"));
        over4.addDelivery(new Delivery("1"));
        over4.addDelivery(new Delivery("1"));
        over4.addDelivery(new Delivery("1"));
        over4.addDelivery(new Delivery("1"));
        over4.addDelivery(new Delivery("2"));

        overs.add(over1);
        overs.add(over2);
        overs.add(over3);
        overs.add(over4);
        
        match.addOvers(overs);
        match.showScore();

    }
}
