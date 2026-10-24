package simpleServer.test;

import simpleServer.client.Client;
import simpleServer.customer.Customer;


public class synchronizeTest {
    public static void main(String[] args) {
        testServer server = new testServer();
        Client client = new Client();
        Customer customer = new Customer();

        server.startListeningPort();

        client.makePortConnection();
        customer.makePortConnection();

        Thread test1 = new Thread(()->{
            client.infoWriting("p");
            client.infoWriting("topic 1");
            client.infoWriting("info");
        });

        Thread test2 = new Thread(()->{
            customer.infoWriting("f");
            customer.infoWriting("topic 1");
            customer.infoOutput();
        });

        try{
            test1.start();
            Thread.sleep(1000);
            test2.start();
            test1.join();
            test2.join();
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        server.close();
        client.close();
        customer.close();
    }
}
