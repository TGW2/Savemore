package simpleServer.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import simpleServer.content.Message;

public class Server {
    ServerSocket server;
    String line;
    //<topic,offsets>
    private final Map<String,Long> getOffsets = new HashMap<>();
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
                        String fromInput = br.readLine();
                        String toInput = br.readLine();

                        long from_l = parseOffsetInput(fromInput,0l);
                        long to_l = parseOffsetInput(toInput, Long.MAX_VALUE);
                        pr.println(returnInfo(topic,from_l,to_l));
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
     * topic2.jsonl file
     * @throws IOException
     */
    public void writeInfo(String topic, String info,Socket socket) throws IOException{
        PrintWriter pr = new PrintWriter(socket.getOutputStream(),true);
        try{
                if("1".equals(topic) || "topic 1".equalsIgnoreCase(topic)){
                    writeInfoByHashMap(info,"topic 1","simpleServer/content/topic1.jsonl");
                    pr.println("info stored");
                }else if("2".equals(topic) || "topic 2".equalsIgnoreCase(topic)){
                    writeInfoByHashMap(info,"topic 2","simpleServer/content/topic2.jsonl");
                    pr.println("info stored");
                }else{
                    pr.println("Invalid topic");
                }
        }catch(IOException e){
            pr.println("storage failed");
            e.printStackTrace();
        }

    }

    /**
     * use hashmap to implement offset(gives each message a label)
     * @param info
     * @param fileAddress
     */
    private void writeInfoByHashMap(String info,String topic,String fileAddress) throws IOException{
        synchronized(filelock){
            try(FileWriter fw = new FileWriter(fileAddress,true)){
                long offset = getOffsets.getOrDefault(topic,0L);
                Message msg = new Message(offset,topic,info);
                Gson gson = new Gson();
                String json = gson.toJson(msg);
                fw.write(json+System.lineSeparator());
            }

            long nextOffset = getOffsets.getOrDefault(topic,0L);
            getOffsets.put(topic,nextOffset+1);

        }

    }

    /**
     * first read all content fron storage then written into json
     * and return back to client let client make connection
     * and send to customer back
     * @return
     */
    public String returnInfo(String topic,long from,long to){
        if (from < 0 || to < from) {
            return "Invalid offset range";
        }
        try{

            synchronized(filelock){
                Gson gson = new Gson();
                BufferedReader br ;
                if("1".equals(topic) || "topic 1".equalsIgnoreCase(topic)){
                    br = new BufferedReader(new FileReader("simpleServer/content/topic1.jsonl"));
                }else if("2".equals(topic) || "topic 2".equalsIgnoreCase(topic)){
                    br = new BufferedReader(new FileReader("simpleServer/content/topic2.jsonl"));
                }else{
                    return "Wrong topic";
                }
                List<Message> msgLists = new ArrayList<>();
                String line;
                while((line = br.readLine())!=null){
                    Message msg = gson.fromJson(line, Message.class);
                    long currentOffset = msg.getOffset();
                    if(from<=currentOffset&&currentOffset<=to){
                        msgLists.add(msg);
                    }
                    if(currentOffset>to){
                        break;
                    }
                }
                return gson.toJson(msgLists);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return "File not found exception";
        }catch (IOException e){
            e.printStackTrace();
            return "IOException";
        }
    }

    /**
     * identify the range customer want by given value
     * @param input
     * @param defaultValue
     * @return
     */
    private long parseOffsetInput(String input, long defaultValue) {
        if (input == null) {
            throw new IllegalArgumentException("Connection closed");
        }

        if (input.isBlank()) {
            return defaultValue;
        }

        return Long.parseLong(input.trim());
    }
    public void close(){
        try{
            if(server!=null){server.close();}
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

