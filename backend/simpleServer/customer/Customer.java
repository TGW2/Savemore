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

    String groupId;
    String consumerId;
//    public Customer(String groupId, String consumerId){
//        this.groupId = groupId;
//        this.consumerId = consumerId;
//    }

    public void makePortConnection(){
        try{
            connection = new Socket("127.0.0.1",9901);
            pr = new PrintWriter(connection.getOutputStream(),true);
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
    public void infoWriting(String groupId, String consumerId, String topic, String length){
        pr.println(groupId);
        pr.println(consumerId);
        pr.println(topic);
        pr.println(length);
        if (pr.checkError()) {
            System.out.println("Message sending failed");
        }
    }

    /**
     * writing back received info
     */
    public void infoOutput(){
        try{
            line = br.readLine();
            if (line == null) {
                System.out.println("服务器已断开连接");
                return;
            }

            if ("Consumer already active".equals(line)) {
                System.out.println("这个组的该 Topic 已有活跃消费者");
                return;
            }
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
