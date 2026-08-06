package Projects.Cricket_Dashboard.scorecard;

import Projects.Cricket_Dashboard.enums.MatchStatus;
import Projects.Cricket_Dashboard.models.Inning;
import Projects.Cricket_Dashboard.models.Team;
import Projects.Cricket_Dashboard.models.Player;
public class ScoreBoard {
    private Team team1;
    private Team team2;
    private Inning inning1;
    private Inning inning2;
    private int score;
    public ScoreBoard(Team team1, Team team2)
    {
        this.team1 = team1;
        this.team2 = team2;
        inning1 = new Inning(team1, team2);
        inning2 = new Inning(team2, team1);
        this.score = 0;
    }
    public void addScore(int runs)
    {
        this.score += runs;
    }

    public void addPlayerScore(Player batter, int runs, MatchStatus status)
    {
        if(status==MatchStatus.FIRST_INNING)
        {
            inning1.addPlayerScore(batter, runs);
        }
        else{
            inning2.addPlayerScore(batter, runs);
        }
    }
    public void addPlayerWicket(Player bowler, MatchStatus status)
    {
        if(status==MatchStatus.FIRST_INNING)
        {
            inning1.addPlayerWicket(bowler);
        }
        else{
            inning2.addPlayerWicket(bowler);
        }
    }

    public void addPlayerOver(Player bowler, MatchStatus status)
    {
        if(status==MatchStatus.FIRST_INNING)
        {
            inning1.addPlayerOver(bowler);
        }
        else{
            inning2.addPlayerOver(bowler);
        }
    }
    public void addPlayerConcededRuns(Player bowler, int runs, MatchStatus status)
    {
        if(status==MatchStatus.FIRST_INNING)
        {
            inning1.addPlayerConcededRuns(bowler, runs);
        }
        else{
            inning2.addPlayerConcededRuns(bowler, runs);
        }
    }
    
    public void showScore()
    {
        System.out.println("======================== Showing Score Board ================================");
        System.out.println("TOTAL SCORE= "+this.score);
        System.out.println("===========INNING-1============== Batting= "+team1.getname()+" ========== Bowling= "+team2.getname());
        inning1.showScore();
        System.out.println("===========INNING-2============== Batting= "+team2.getname()+" ========== Bowling= "+team1.getname());
        inning2.showScore();
    }
}
