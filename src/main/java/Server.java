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

        public void updateClients(CFourInfo message) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread p1 = clients.get(0);
                try {
                    message.whoseTurn = true;
                    p1.out.writeObject(message);
                    if(clients.size()>1){
                        ClientThread p2 = clients.get(1);
                        if(message.turn){
                            message.turn=false;
                        }
                        else{
                            message.turn=true;
                        }
                        message.whoseTurn = false;
                        p2.out.writeObject(message);
                    }
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
                    while(count<1) {
                        game.hasTwoPlayers = false;
                        updateClients(game);
                    }
                        game.hasTwoPlayers = true;
                        System.out.println("he");
                        updateClients(game);
                        try {
                            CFourInfo data = (CFourInfo) in.readObject();
                            callback.accept((Serializable) data);
                            System.out.println(data.columnMove + " " + data.rowMove);
                            //callback.accept("client: " + count + " sent: " + data.rowMove + ", " + data.columnMove);
                            if (game.turn) {
                                game.turn = false;
                            } else {
                                game.turn = true;
                            }
                            game.hasTwoPlayers = true;
                            updateClients(game);
                        } catch (Exception e) {
                            callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                            game.hasTwoPlayers = false;
                            updateClients(game);
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
