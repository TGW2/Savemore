package simpleServer.test;

import simpleServer.server.Server;

import java.io.IOException;
import java.net.*;

public class testServer extends Server {

    private final Object testLock = new Object();

    @Override
    public void writeInfo(String topic,String info,Socket socket) throws IOException {
        synchronized (testLock) {
            System.out.println("SERVER: start writing in topic 1");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            super.writeInfo("topic 1",info,socket);

            System.out.println("SERVER: end writing");
        }
    }

    @Override
    public String returnInfo(String topic,long from,long to) {
        synchronized (testLock) {
            System.out.println("SERVER: start reading");

            String result = super.returnInfo(topic,0,1);

            System.out.println("SERVER: end reading");
            return result;
        }
    }
}
