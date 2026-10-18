package simpleServer.server;

import java.io.*;
import java.net.*;
public class Server {
    ServerSocket server;
    Socket client;
    BufferedReader br;
    FileWriter fw;
    String line;
    /**
     * start listening port 9901
     */
    public void startListeningPort(){
        try{
            server = new ServerSocket(9901);
            System.out.println("port is listening...");

            //同时新开一个线程进行accept的等待操作
            Thread work = new Thread(()->{
                try{
                    client = server.accept();
                    if(client!=null){
                        writeInfo();
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

    /**
     * server store the information client passed by through 'infoGetting' method and store it into
     * content.txt file
     * @throws IOException
     */
    public void writeInfo() throws IOException{
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try{
            fw = new FileWriter("simpleServer/content.txt",true);
            line = br.readLine();
            //因为socket全局只有一次调用，所以while的目的是为了可以进行不止一次的write
            while(line!=null){
                fw.write(line+ System.lineSeparator());
                fw.flush();
                System.out.println("info stored");
                line = br.readLine();
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

}

