package Projects.Cricket_Dashboard.scorecard;

import Projects.Cricket_Dashboard.models.Player;

public class BowlingScore {
    private Player player;
    private int overs;
    private int runs;
    private int wickets;
    public BowlingScore(Player player)
    {
        this.player = player;
        this.overs = 0;
        this.runs = 0;
        this.wickets = 0;
    }
    public Player getPlayer()
    {
        return this.player;
    }
    public void addRuns(int runs)
    {
        this.runs += runs;
    }

    public void addOver()
    {
        this.overs++;
    }

    public void addWicket()
    {
        this.wickets++;
    }

    public void showScore()
    {
        System.out.println(this.player.getName()+" bowled = overs:"+this.overs+ " ,Runs Conceded:" + this.runs + " ,Wickets taken:"+this.wickets);
    }
}
