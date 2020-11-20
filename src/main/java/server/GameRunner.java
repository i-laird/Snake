package server;

import java.util.Set;

public class GameRunner extends Thread{

    public Set<Player> players;

    public void addPlayer(Player p){
        this.players.add(p);
    }
}
