package simpleServer.customer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Customer {
    BufferedReader br;
    String line;
    Socket connection;
    PrintWriter pr;

    public void makePortConnection(){
        try{
            connection = new Socket("127.0.0.1",9901);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException m){
            m.printStackTrace();
        }
    }

    /**
     * calling server and request reserved data(content) from server
     */
    public void infoGetting(String info){
        try{
            pr = new PrintWriter(connection.getOutputStream(),true);
            pr.println(info);
        }catch(IOException e){
            System.out.println("IOException");
        }
    }

    /**
     * writing back received info
     */
    public void infoOutput(){
        try{
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            line = br.readLine();
            System.out.println(line);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
