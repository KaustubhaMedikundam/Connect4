import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    int port;
    TheServer server;
    int count = 1;
    //keeps count of number of players
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    //client thread...have not figure out yet
    private Consumer<Serializable> callback;
    Server(Consumer<Serializable> call, int portNum){
        callback = call;
        server = new TheServer();
        server.start();
        port = portNum;
    }

    public class TheServer extends Thread{

        public void run() {

            try(ServerSocket mysocket = new ServerSocket(port);){
                System.out.println("Server is waiting for a client!");

                while(true) {
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start();
                    count++;

                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }

    class ClientThread extends Thread{
        CFourInfo game = new CFourInfo();
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void updateClients() {
            for(int i = 0; i <clients.size();i++){
                System.out.println("server:"+game.hasTwoPlayers);
                ClientThread p1 = clients.get(i);
                try {
                    game.numPlayers = clients.size();
                    p1.out.reset();
                    p1.out.writeObject(game);
//                    game.whoseTurn = !game.whoseTurn;
                   game.turn = !game.turn;
                }
                catch(Exception e) {}
            }
        }

        public void run(){
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }
                while(true) {
                    if(clients.size()<2) {
                        game.hasTwoPlayers = false;
                        updateClients();
                    }
                       game.hasTwoPlayers = true;
//                    System.out.println("boo");
                    if(game.rowMove==10){
                        updateClients();
                        System.out.println("10");
                    }
                        try {
                            CFourInfo data = (CFourInfo) in.readObject();
                            int cM = game.columnMove + 1;
                            int rM = game.rowMove + 1;
                            String sent = "client: " + count + " sent: "+ cM+ " " + rM;
                            callback.accept((Serializable) sent);
                            System.out.println(data.columnMove + " " + data.rowMove);
                            //callback.accept("client: " + count + " sent: " + data.rowMove + ", " + data.columnMove);
                            game=data;
                            game.whoseTurn=!game.whoseTurn;
//                            System.out.println(game.turn);
                            game.hasTwoPlayers = true;
                            if(game.whoseTurn){
                                game.turn=true;
                             }
                            else {
                                game.turn=false;
                            }
                            updateClients();
                        } catch (Exception e) {
                           callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                            game.hasTwoPlayers = false;
                            game.numPlayers --;
                            clients.remove(this);
                            updateClients();
                            break;
                        }
                    }

        }//end of run


    }//end of client thread
}
