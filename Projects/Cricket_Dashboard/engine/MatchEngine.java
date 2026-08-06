package Projects.Cricket_Dashboard.engine;
import java.util.*;
import Projects.Cricket_Dashboard.models.*;
import Projects.Cricket_Dashboard.scorecard.ScoreBoard;
import Projects.Cricket_Dashboard.*;
import Projects.Cricket_Dashboard.enums.DeliveryType;
import Projects.Cricket_Dashboard.enums.MatchStatus;

public class MatchEngine {
    private int cnt;
    private Team team1;
    private Team team2;
    public MatchEngine(Team team1, Team team2)
    {
        this.team1 = team1;
        this.team2 = team2;
        cnt = 1;
    }

    public void processOvers(List<Over> addedOvers, int overs, ScoreBoard scoreboard)
    {
        MatchStatus status = MatchStatus.SECOND_INNING;
        Team batting;
        Team bowling;
        Player batter = null;
        Player striker = null;
        Deque<Player> dq = new ArrayDeque<>();
        for(Over over: addedOvers)
        {
            if(cnt<=overs)
            {
                if(status!= MatchStatus.FIRST_INNING)
                {
                    batting = this.team1;
                    bowling = this.team2;
                    for(Player player: batting.getPlayers())
                    {
                        dq.addLast(player);
                    }
                    batter = dq.pollFirst();
                    striker = dq.pollFirst();
                }
                status = MatchStatus.FIRST_INNING;
            }
            else{
                if(status!= MatchStatus.SECOND_INNING)
                {
                    batting = this.team2;
                    bowling = this.team1;
                    for(Player player: batting.getPlayers())
                    {
                        dq.addLast(player);
                    }
                    batter = dq.pollFirst();
                    striker = dq.pollFirst();
                }
                status = MatchStatus.SECOND_INNING;
            }
            cnt++;
            int ballsdone=0;
            for(Delivery delivery: over.getDeliveries())
            {
                if(delivery.getType()==DeliveryType.NORMAL)
                {
                    ballsdone++;
                }
                if(delivery.getType()==DeliveryType.NORMAL)
                {
                    scoreboard.addScore(delivery.getRuns());
                    scoreboard.addPlayerScore(batter, delivery.getRuns(),status);
                    scoreboard.addPlayerConcededRuns(over.getBowler(),delivery.getRuns(),status);
                }
                else if(delivery.getType()==DeliveryType.WICKET)
                {
                    if(dq.size()>0)
                    {
                        batter = dq.pollFirst();
                    }
                    scoreboard.addPlayerWicket(over.getBowler(),status);
                }
                else if(delivery.getType()==DeliveryType.WIDE)
                {
                    scoreboard.addScore(1);
                    scoreboard.addPlayerConcededRuns(over.getBowler(),1,status);
                }
                if(ballsdone==6)
                {
                    scoreboard.addPlayerOver(over.getBowler(),status);
                    Player temp = batter;
                    batter = striker;
                    striker = batter;
                }
            }
        }
    }
}
