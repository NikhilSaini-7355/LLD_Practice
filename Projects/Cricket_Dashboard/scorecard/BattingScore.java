package Projects.Cricket_Dashboard.scorecard;
import Projects.Cricket_Dashboard.models.Player;

public class BattingScore {
    private Player player;
    private int sixes;
    private int fours;
    private int runs;
    private int balls;
    public BattingScore(Player player)
    {
        this.player = player;
        this.sixes = 0;
        this.fours = 0;
        this.runs = 0;
        this.balls = 0;
    }
    public void addSix()
    {
        this.sixes++;
    }
    public void addFour()
    {
        this.fours++;
    }
    public void addBall()
    {
        this.balls++;
    }
    public void addRuns(int runs)
    {
        this.addBall();
        if(runs==4)
        {
            this.runs += runs;
            this.addFour();
        }
        else if(runs==6)
        {
            this.runs += runs;
            this.addSix();
        }
        else
        {
            this.runs += runs;
        }
    }
    public Player getPlayer()
    {
        return this.player;
    }
    public void showScore()
    {
        System.out.println(this.player.getName()+" Scored = Runs:"+this.runs+ " ,Sixes:" + this.sixes + " ,Fours:"+this.fours + " ,Balls Faced:"+this.balls);
    }
}
