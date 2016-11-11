
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Client {

    public static void main(String[] args) {
        int orderNumber = extractOrder(args,0,args.length);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = null;

        //1 for posting parcel
        //2 for getting parcel trail
        //3 for getting station stop count
        if(orderNumber == 1){
            CloseableHttpAsyncClient ac = HttpAsyncClients.createDefault();ac.start();
            ac.start();
            CountDownLatch latch = new CountDownLatch(1000);
            String parcelID = extractParcelID(args,2,args.length);
            String stationID = extractStationID(args,4,args.length);
            for(int i = 0; i < 1000; i++){
                HttpPost req = new HttpPost("http://127.0.0.1:9090/events");
                req.setHeader("stationId", String.valueOf(i));
                req.setHeader("parcelId", parcelID);
                req.setHeader("timestamp",String.valueOf(System.currentTimeMillis()));
                try{

                    ac.execute(req, new FutureCallback<HttpResponse>() {
                        @Override
                        public void completed(HttpResponse httpResponse) {latch.countDown();}
                        @Override
                        public void failed(Exception e) {latch.countDown();}
                        @Override
                        public void cancelled() {latch.countDown();}
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error while sending request");
                    System.exit(0);
                }

            }
            try{
                latch.await();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                ac.close();
            }catch (Exception e){

            }



        }
        else if(orderNumber == 2) {
            String parcelID = extractParcelID(args,2,args.length);
            System.out.println(parcelID);
            HttpGet req = new HttpGet("http://127.0.0.1:9090/trail/" + parcelID);
//            req.setHeader("parcelId",parcelID);
            try{
                response = client.execute(req);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Error while sending request");
            }
        }
        else if(orderNumber == 3){
            String stationID = extractStationID(args,2,args.length);
            HttpGet req = new HttpGet("http://127.0.0.1:9090/stopCount/" + stationID);
//            req.setHeader("stationId",stationID);
            try{
                response = client.execute(req);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Error while sending request");
            }
        }
        else{
            System.out.println("Incorrect input");
            System.exit(0);
        }


        if (response != null){printAll(response,orderNumber);}
    }
    public static void printAll(HttpResponse response, int order){
        Header[] h = response.getAllHeaders();
        System.out.println(response.getStatusLine());
        for(Header a : h){
            System.out.println(a);
        }
        if(order != 1){
            try{
                System.out.println(EntityUtils.toString(response.getEntity()));
            }catch (Exception e){
                System.out.println("Error: Not valid response entity");
            }
        }
    }


    public static String extractParcelID(String[] args,int start, int end){
        if(end - start < 2){
            System.out.println("Incorrect input");
            System.exit(0);
        }
        if(args[start].equals("-p")){
            try{
                return args[start+1];
            }
            catch(Exception e){
                System.out.println("Incorrect input");
                System.exit(0);
            }
        }
        else{
            System.out.println("Incorrect input");
            System.exit(0);
        }
        return "";
    }
    public static String extractStationID(String[] args, int start, int end){
        if(end - start < 2){
            System.out.println("Incorrect input");
            System.exit(0);
        }
        if(args[start].equals("-s")){
            try{
                return args[start+1];
            }
            catch(Exception e){
                System.out.println("Incorrect input");
                System.exit(0);
            }
        }
        else{
            System.out.println("Incorrect input");
            System.exit(0);
        }
        return "";
    }
    public static int extractOrder(String[] args, int start, int end){
        if(end - start < 2){
            System.out.println("Incorrect input");
            System.exit(0);
        }
        if(args[start].equals("-o")){
            try{
                return Integer.parseInt(args[start+1]);
            }
            catch(Exception e){
                System.out.println("Incorrect input");
                System.exit(0);
            }
        }
        else{
            System.out.println("Incorrect input");
            System.exit(0);
        }
        return 0;
    }
}
