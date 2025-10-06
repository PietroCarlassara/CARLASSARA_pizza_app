import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOError;
import java.io.IOException;

public class App {
    public String api = "aede03a82a594385ade8f87cc53122df";
    public String url = "https://crudcrud.com/api/" + api + "/Pizze";
    public OkHttpClient client;

    // Costruttore
    public App() {
        client = new OkHttpClient();
    }

    // Richiesta POST
    public void doPost(Pizza pizza){
        Gson gson = new Gson();
        // Serializza l'oggetto pizza in JSON
        String json = gson.toJson(pizza);
        // Imposta il tipo
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        // Crea il corpo della richiesta
        RequestBody body = RequestBody.create(json, JSON);
        // Crea la richista POST
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        // Invia la richiesta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Richiesta GET
    public void doGet(){
       Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Header della risposta
            /*
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
             */

            String responseBody = response.body().string();

            Gson gson = new Gson();
            Pizza[] pizze = gson.fromJson(responseBody, Pizza[].class);

            System.out.println("Pizze: ");
            for(Pizza pizza : pizze){
                // Viene chiamato il metodo .toString() dell'oggetto Pizza
                System.out.println(pizza);
            }
            System.out.println(" ");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        try {
            doGet();
        } catch (IOError e) {
            throw new RuntimeException(e);
        }
    }
}
