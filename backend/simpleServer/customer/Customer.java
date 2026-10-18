package simpleServer.customer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Customer {
    BufferedReader br;
    String line;
    public void getInfo(){
        try{
            br = new BufferedReader(new FileReader("simpleServer/content.txt"));
            while((line = br.readLine()) != null){
                System.out.println(line+'\n');
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
