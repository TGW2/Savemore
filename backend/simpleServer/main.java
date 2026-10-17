package simpleServer;

import simpleServer.client.Client;
import simpleServer.server.Server;


import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args){
        Server server = new Server();
        Client client = new Client();

        server.startListeningPort();

        client.makePortConnection();

        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("any info: ");
            String info = sc.nextLine();
            client.infoGetting(info);
            if(info.equals("close")) break;
            try{Thread.sleep(100);}catch(InterruptedException e){}
            System.out.println();
        }
    }
}
