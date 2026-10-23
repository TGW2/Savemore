package simpleServer;

import simpleServer.client.Client;
import simpleServer.customer.Customer;
import simpleServer.server.Server;


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
        customer.makePortConnection();
        //always getting connected to server port 9901
        client.makePortConnection();

        while(true) {
//            Thread.sleep(100);
            System.out.println(
                   "1: 生产者发送消息到服务端\n"+
                   "2: 消费者接受所有的生产者发向服务端的消息\n"+
                   "3: break");
            int choice;

            try{
                choice = sc.nextInt();
            }catch(InputMismatchException e){
                System.out.println("Wrong info, pls try again:");
                sc.nextLine();
                continue;
            }
            sc.nextLine();
            if(choice==1){
                System.out.println("info publishing: ");
                String info = sc.nextLine();
                client.infoWriting("p");
                client.infoWriting(info);
            }else if(choice==2){
                customer.infoWriting("f");
                customer.infoOutput();
            }else if(choice==3){
                client.close();
                customer.close();
                server.close();
                break;
            }
        }
    }
}
