import com.google.gson.Gson;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class App {
    // int variabile = 10;

    public OkHttpClient client;

    // Costruttore
    public App() {
        client = new OkHttpClient();
    }

    public void doGet(){
        Request request = new Request.Builder()
                .url("https://crudcrud.com/api/c0a5b8df007243db866f25f462ea6e56/Pizze")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            Gson gson = new Gson();
            Pizza[] pizze = gson.fromJson(response.body().string(), Pizza[].class);

            for(Pizza pizza : pizze){
                System.out.println(pizza);
            }

            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        try{
            doGet();
        }
        catch(Exception e){
            throw e;
        };

        System.out.println("Ciao");
        // System.out.println(variabile);
    }
}
