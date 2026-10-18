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

        Scanner sc = new Scanner(System.in);

        //start listing port 9901(all the time)
        server.startListeningPort();
        //always getting connected to server port 9901
        client.makePortConnection();

        while(true) {
            Thread.sleep(100);
//           System.out.println(
//                   "1: 生产者发送消息到服务端\n"+
//                   "2: 消费者接受所有的生产者发向服务端的消息\n"+
//                   "3: break");
            System.out.println("Operation take(p for publish, f for fetch,, q for quit): ");
           String choice = sc.next();

//           if(choice.equalsIgnoreCase("p")){
//               sc.nextLine();
//               System.out.println("info client send to server:...");
//               String info = sc.nextLine();
//               client.infoGetting(info);
//           }else if(choice.equalsIgnoreCase("f")){
//               customer.getInfo();
//           }else if(choice.equalsIgnoreCase("q")){
//               client.close();
//               break;
//           }
        }
    }
}
