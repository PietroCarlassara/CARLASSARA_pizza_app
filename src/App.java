import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.vandermeer.asciitable.AsciiTable;
import okhttp3.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    // int variabile = 10;

    public OkHttpClient client;

    // Costruttore
    public App() {
        client = new OkHttpClient();
    }

    Scanner sc = new Scanner(System.in);

    public void menu(){
        while(true){
            System.out.println("Digita l'operazione da svolgere: ");
            System.out.println("1 - Leggi tutto");
            System.out.println("2 - Crea Pizza");
            System.out.println("3 - Aggiorna Pizza");
            System.out.println("4 - Elimina Pizza");

            int operation = -1;

            try {
                operation = sc.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Digita un numero per favore");
                sc.nextLine();
                continue;
            }

            if(operation > 0 && operation < 4){
                System.out.println("Hai selezionato " + operation);
                continue;
            }

            switch (operation){
                case 1:
                    try{
                        Pizza[] pizze = getAllPizze();
                        AsciiTable asciiTable = new AsciiTable();
                        asciiTable.addRule();
                        for(Pizza pizza : pizze){
                            asciiTable.addRow(pizza.Nome, pizza.Prezzo, pizza.Ingredienti);
                            asciiTable.addRule();
                        }
                        System.out.println(asciiTable.render());
                    } catch (Exception e) {
                        System.out.println("E' avvenuto un errore" + e.getClass().getSimpleName());
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    continue;
            }
        }
    }

    public void createPizza(){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        Gson  gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

    }

    public Pizza[] getAllPizze(){
        Request request = new Request.Builder()
                .url("https://crudcrud.com/api/eb1fe1413aa5436b899cb06f072ee146/Pizze")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            Gson gson = new Gson();
            Pizza[] pizze = gson.fromJson(response.body().string(), Pizza[].class);
            return pizze;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
         menu();
    }
}
