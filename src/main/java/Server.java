import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    TheServer server;
    int count = 1;
    //keeps count of number of players
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    //client thread...have not figure out yet
    private Consumer<Serializable> callback;
    Server(Consumer<Serializable> call){
        callback = call;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread{

        public void run() {

            try(ServerSocket mysocket = new ServerSocket(5555);){
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
//            for(int i = 0; i < clients.size(); i++) {
                ClientThread p1 = clients.get(0);
                try {
                    if(game.whoseTurn){
                        game.turn=true;
                    }
                    else {
                        game.turn=false;
                    }
                    p1.out.writeObject(game);
                    if(clients.size()>1){
                        p1.game.hasTwoPlayers=true;
                        ClientThread p2 = clients.get(1);
                        game.turn = !game.turn;
//                        game.whoseTurn= !game.whoseTurn;
//                        game.whoseTurn = false;
                        p2.out.writeObject(game);
                        System.out.println("update client says: " + game.turn);
//                        game.turn = !game.turn;
                    }

                }
                catch(Exception e) {}
//            }
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
                            callback.accept((Serializable) data);
                            System.out.println(data.columnMove + " " + data.rowMove);
                            //callback.accept("client: " + count + " sent: " + data.rowMove + ", " + data.columnMove);
                            game=data;
                            game.whoseTurn=!game.whoseTurn;
//                            System.out.println(game.turn);
                            game.hasTwoPlayers = true;
                            updateClients();
                        } catch (Exception e) {
                            callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                            game.hasTwoPlayers = false;
                            updateClients();
//                        clients.remove(this);
                            break;
                        }


                    }
//            }
//            else{
//                System.out.println("not working");
//                game.hasTwoPlayers=false;
//                updateClients(game);
//            }


        }//end of run


    }//end of client thread
}
