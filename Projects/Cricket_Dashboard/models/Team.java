package Projects.Cricket_Dashboard.models;
import java.util.*;

public class Team {
    private List<Player> players;
    private String name;
    public Team(String name)
    {
        this.name = name;
        this.players = new ArrayList<>();
    }

    public String getname()
    {
        return this.name;
    }

    public List<Player> getPlayers()
    {
        return Collections.unmodifiableList(this.players);
    }

    public void addPlayer(Player player)
    {
        this.players.add(player);
    }

    public void removePlayer(Player player)
    {
        this.players.remove(player);
    }

    
}
