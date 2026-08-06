package Projects.Cricket_Dashboard.models;
import Projects.Cricket_Dashboard.engine.*;
import Projects.Cricket_Dashboard.scorecard.ScoreBoard;
import java.util.*;
public class Match {
    private Team team1;
    private Team team2;
    private MatchEngine engine;
    private ScoreBoard scoreboard;
    private int numPlayers;
    private int overs;
    public Match(int numPlayers, int overs, Team team1, Team team2)
    {
        this.team1 = team1;
        this.team2 = team2;
        this.numPlayers = numPlayers;
        this.overs = overs;
        this.engine = new MatchEngine(this.team1, this.team2);
        
    }

    public void addPlayers(List<Player> addedplayers)
    {
        int cnt = 1;
        for(Player player: addedplayers)
        {
            if(cnt<=numPlayers)
            {
                team1.addPlayer(player);
            }
            else
            {
                team2.addPlayer(player);
            }
            cnt++;
        }
        this.scoreboard = new ScoreBoard(this.team1, this.team2);
    }

    public void addOvers(List<Over> addedovers)
    {
        this.engine.processOvers(addedovers,this.overs,this.scoreboard);
    }

    public void showScore()
    {
        this.scoreboard.showScore();
    }
}
