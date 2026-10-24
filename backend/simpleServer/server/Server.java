package simpleServer.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class Server {
    ServerSocket server;
//    Socket socket;
    FileWriter fw;
    String line;
    List<String> list;

    Object filelock = new Object();

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
                    while(true){
                        Socket socket = server.accept();
                        threadhandler(socket);
                    }
                }catch(IOException e){
                    if(!server.isClosed()){
                        e.printStackTrace();
                    }

                }
            });
            work.start();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * handle client and customer by separate thread
     * @param socket (identify socket->client or socket->customer)
     */
    public void threadhandler(Socket socket){
        Thread work = new Thread(()->{
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(true){
                    String response = br.readLine();
                    if(response == null) {return;}
                    else if("p".equals(response) && socket!=null){
                        String topic = br.readLine();
                        String info = br.readLine();
                        writeInfo(topic,info,socket);
                    }else if("f".equals(response) && socket!=null){
                        PrintWriter pr = new PrintWriter(socket.getOutputStream(),true);
                        String topic = br.readLine();
                        pr.println(returnInfo(topic));
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        work.start();
    }

    /**
     * server store the information client passed by through 'infoGetting' method and store it into
     * topic2.txt file
     * @throws IOException
     */
    public void writeInfo(String topic, String info,Socket socket) throws IOException{
        PrintWriter pr = new PrintWriter(socket.getOutputStream(),true);
        try{
            synchronized(filelock){
                if("1".equals(topic) || "topic 1".equalsIgnoreCase(topic)){
                    fw = new FileWriter("simpleServer/content/topic1.txt",true);
                    fw.write(info + System.lineSeparator());
                    fw.flush();
                    pr.println("info stored");
                }else if("2".equals(topic) || "topic 2".equalsIgnoreCase(topic)){
                    fw = new FileWriter("simpleServer/content/topic2.txt",true);
                    fw.write(info + System.lineSeparator());
                    fw.flush();
                    pr.println("info stored");
                }else{
                    pr.println("Invalid topic");
                }

            }
        }catch(IOException e){
            pr.println("storage failed");
            e.printStackTrace();
        }

    }

    /**
     * first read all content fron storage then written into json
     * and return back to client let client make connection
     * and send to customer back
     * @return
     */
    public String returnInfo(String topic){
        String json;
        try{
            synchronized(filelock){
                BufferedReader br ;
                if("1".equals(topic) || "topic 1".equalsIgnoreCase(topic)){
                    br = new BufferedReader(new FileReader("simpleServer/content/topic1.txt"));
                }else if("2".equals(topic) || "topic 2".equalsIgnoreCase(topic)){
                    br = new BufferedReader(new FileReader("simpleServer/content/topic2.txt"));
                }else{
                    return "Wrong topic";
                }

                list = new ArrayList<>();
                while((line = br.readLine())!=null){
                    list.add(line);
                }
                json = new Gson().toJson(list);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return "File not found exception";
        }catch (IOException e){
            e.printStackTrace();
            return "IOException";
        }
        return json;
    }

    public void close(){
        try{
            if(fw!=null){fw.close();}
//            if(socket!=null){socket.close();}
            if(server!=null){server.close();}
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

