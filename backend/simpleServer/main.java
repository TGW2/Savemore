package simpleServer;

import simpleServer.client.Client;
import simpleServer.customer.Customer;
import simpleServer.server.Server;


import java.io.*;
import java.util.*;

/**
 * from client to server, server reserve the information, customer dragging all the logger
 */
public class main {
    public static void main(String[] args)throws InterruptedException{
        Server server = new Server();
        Client client = new Client();
        Customer customer = new Customer();

        try (FileWriter fw = new FileWriter("simpleServer/content.txt", false)) {
            // 只清空文件，不写入内容
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        //start listing port 9901(all the time)
        server.startListeningPort();
        //always getting connected to server port 9901
        client.makePortConnection();

        while(true) {
            Thread.sleep(100);
           System.out.println(
                   "1: 生产者发送消息到服务端\n"+
                   "2: 消费者接受所有的生产者发向服务端的消息\n"+
                   "3: break");
           int choice = sc.nextInt();
           if(choice==1){
               sc.nextLine();
               System.out.println("info client send to server:...");
               String info = sc.nextLine();
               client.infoGetting(info);
           }else if(choice==2){
               customer.getInfo();
           }else if(choice==3){
               client.close();
               break;
           }
        }
    }
}
