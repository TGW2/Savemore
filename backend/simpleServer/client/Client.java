package simpleServer.client;

import java.io.*;
import java.net.*;

public class Client {
    Socket connection;
    PrintWriter pr;
    /**
     * telling port 9901 i'm making connection
     */
    public void makePortConnection(){
        try{
            connection = new Socket("127.0.0.1",9901);
        }catch(UnknownHostException e){
            System.out.println("unknownhost exception");
        }catch(IOException m){
            System.out.println("IOexception");
        }
    }

    /**
     * send info through 'infoGetting' and pass the information to server
     * @param info
     */
    public void infoGetting(String info) {
        try{
            pr = new PrintWriter(connection.getOutputStream(),true);
            pr.println(info);
        }catch(IOException e){
            System.out.println("IOException");
        }

    }

    /**
     * 关闭当前线程的输入，关闭建立的连接
     */
    public void close(){
        try{
            connection.close();
        }catch(IOException e){
            System.out.println("IOException");
        }
    }
}
