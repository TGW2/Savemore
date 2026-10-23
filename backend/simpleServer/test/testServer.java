package simpleServer.test;

import simpleServer.server.Server;

import java.io.IOException;

public class testServer extends Server {

    private final Object testLock = new Object();

    @Override
    public void writeInfo(String info) throws IOException {
        synchronized (testLock) {
            System.out.println("SERVER: start writing");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            super.writeInfo(info);

            System.out.println("SERVER: end writing");
        }
    }

    @Override
    public String returnInfo() {
        synchronized (testLock) {
            System.out.println("SERVER: start reading");

            String result = super.returnInfo();

            System.out.println("SERVER: end reading");
            return result;
        }
    }
}
