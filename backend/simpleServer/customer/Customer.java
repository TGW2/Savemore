package simpleServer.customer;

import com.google.gson.Gson;
import simpleServer.content.Message;

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
            pr = new PrintWriter(connection.getOutputStream(),true);
        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException m){
            m.printStackTrace();
        }
    }

    /**
     * calling server and request reserved data(content) from server
     */
    public void infoWriting(String info){
        pr.println(info);
        if (pr.checkError()) {
            System.out.println("Message sending failed");
        }
    }
    public void infoWriting(String from, String to){
        pr.println(from);
        pr.println(to);
        if (pr.checkError()) {
            System.out.println("Message sending failed");
        }
    }

    /**
     * writing back received info
     */
    public void infoOutput(){
        try{
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            line = br.readLine();

            Gson gson = new Gson();
            Message[] messages = gson.fromJson(line, Message[].class);

            for (Message msg : messages) {
                System.out.println(gson.toJson(msg));
            }

        }catch(IOException e){
            System.out.println("printing error");
            e.printStackTrace();
        }
    }

    /**
     * close connection and pr once terminated
     */
    public void close(){
        if(connection!=null){
           try{
               connection.close();
           }catch(IOException e){
               e.printStackTrace();
           }
        }

        if(pr!=null){
            pr.close();
            pr=null;
        }
    }
}
