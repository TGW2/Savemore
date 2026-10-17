package simpleServer.server;

import java.io.*;
import java.net.*;
public class Server {
    ServerSocket server;
    Socket client;
    BufferedReader br;
    /**
     * start listening port 9901
     */
    public void startListeningPort(){
        try{
            server = new ServerSocket(9901);
            System.out.println("port is listening...");

            Thread work = new Thread(()->{
                try{
                    client = server.accept();
                    if(client!=null){
                        readInfo();

                    }
                }catch(IOException e){
                    System.out.println("IOException");
                }
            });
            work.start();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void readInfo() throws IOException{
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        while(true){
            String info = br.readLine();
            if(info==null) break;
            if(info.equals("close")) break;
            System.out.println("from server: "+info);
        }

    }

}

