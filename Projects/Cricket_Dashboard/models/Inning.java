package Projects.Cricket_Dashboard.models;
import java.util.*;

import Projects.Cricket_Dashboard.scorecard.BattingScore;
import Projects.Cricket_Dashboard.scorecard.BowlingScore;

public class Inning {
    private List<BattingScore> battingScores;
    private List<BowlingScore> bowlingScores;
    private Team battingTeam;
    private Team bowlingTeam;
    public Inning(Team battingteam, Team bowlingteam)
    {
        this.battingTeam = battingteam;
        this.bowlingTeam = bowlingteam;

        this.battingScores = new ArrayList<>();
        this.bowlingScores = new ArrayList<>();
        for(Player player : this.battingTeam.getPlayers())
        {
            battingScores.add(new BattingScore(player));
        }

        for(Player player: this.bowlingTeam.getPlayers())
        {
            bowlingScores.add(new BowlingScore(player));
        }
    }

    public void addPlayerScore(Player player, int runs)
    {
        for(BattingScore score: battingScores)
        {
            if(score.getPlayer().equals(player))
            {
                score.addRuns(runs);
                break;
            }
        }
    }

    public void addPlayerConcededRuns(Player player, int runs)
    {
        for(BowlingScore score: bowlingScores)
        {
            if(score.getPlayer().equals(player))
            {
                score.addRuns(runs);
                break;
            }
        }
    }

    public void addPlayerWicket(Player player)
    {
        for(BowlingScore score: bowlingScores)
        {
            if(score.getPlayer().equals(player))
            {
                score.addWicket();
                break;
            }
        }
    }

    public void addPlayerOver(Player player)
    {
        for(BowlingScore score: bowlingScores)
        {
            if(score.getPlayer().equals(player))
            {
                score.addOver();
                break;
            }
        }
    }
    
    public void showScore()
    {
        System.out.println("======================== Showing Score for this Innings ===================================");
        System.out.println("Showing Batting Score for Team= "+battingTeam.getname());
        for(BattingScore score : this.battingScores)
        {
            score.showScore();
        }
        System.out.println("Showing Bowling Score for Team= "+bowlingTeam.getname());
        for(BowlingScore score : this.bowlingScores)
        {
            score.showScore();
        }
    }
}
