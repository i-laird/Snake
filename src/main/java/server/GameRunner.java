package server;

import communication.message.Start;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GameRunner extends Thread{

    Set<Player> players;

    public void addPlayer(Player p){
        this.players.add(p);
    }

    private int port;

    private AsynchronousServerSocketChannel server;

    public void run(){

        // set up the async server for this game
        try {
            server = AsynchronousServerSocketChannel.open();
            server.bind(null);
            port = ((InetSocketAddress)server.getLocalAddress()).getPort();
        } catch (IOException e) {

            //error unable to set up the async socket
        }

        // send start to each client
        this.players.forEach(x -> {
            try {
                x.getMessageHandler().sendMessage(new Start(port));
            } catch (IOException e) {}
        });

        // open a stream of communication for each client

        // send start to each client

        // wait until all ACK have been received

        // until game is over
        // collect all players moves and then forward to all other players
    }

    public void openConnections() throws ExecutionException, InterruptedException {
        List<Future<AsynchronousSocketChannel>> connectionFutures = new ArrayList<>(players.size());
        List<AsynchronousSocketChannel> sockets = new ArrayList<>(players.size());

        for(int i = 0; i < players.size(); i++){
            Future<AsynchronousSocketChannel> future = server.accept();
            connectionFutures.add(future);
        }

        for(Future<AsynchronousSocketChannel> f : connectionFutures){
            sockets.add(f.get());
        }

        for(AsynchronousSocketChannel socket: sockets){
            //socket.
        }

        // get the start messages from the clients
    }
}
