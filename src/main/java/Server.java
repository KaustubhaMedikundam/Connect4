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
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
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


        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void updateClients(String message) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(message);
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

            updateClients("new client on server: client #"+count);

            if(count == 2){
                while(true) {
                    try {
//                    String data = in.readObject().toString();
//                    callback.accept("client: " + count + " sent: " + data);
//                    updateClients("client #"+count+" said: "+data);

                    }
                    catch(Exception e) {
                        callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                        updateClients("Client #"+count+" has left the server!");
                        clients.remove(this);
                        break;
                    }
                }
            }
            else{
                try {
                    out.writeObject("only has one player;");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }//end of run


    }//end of client thread
}
