import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.vandermeer.asciitable.AsciiTable;
import okhttp3.*;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;

public class App {
    // int variabile = 10;

    // Costanti
    final static String URL = "https://crudcrud.com/api/";
    final static String API_KEY = "f15bcfc15682424f9a60ec5ab0376cf8";
    final static String ENDPOINT = "/Pizze";

    static String url = URL + API_KEY + ENDPOINT;

    public OkHttpClient client;

    // Costruttore
    public App() {
        client = new OkHttpClient();
    }

    private Scanner sc = new Scanner(System.in);

    public void menu(){
        while(true){
            System.out.println("\nDigita l'operazione da svolgere: ");
            System.out.println("0 - EXIT");
            System.out.println("1 - Leggi tutto");
            System.out.println("2 - Crea Pizza");
            System.out.println("3 - Aggiorna Pizza");
            System.out.println("4 - Elimina Pizza");

            int operation = -1;

            try {
                // nextInt() legge solo il numero ma lascia il carattere di nuova riga nella coda di input
                operation = sc.nextInt();
                sc.nextLine();  // CONSUME il newline rimasto
            } catch (InputMismatchException e){
                System.out.println("Digita un numero per favore");
                sc.nextLine();
                continue;
            }

            if(operation < 0 || operation > 4){
                // System.out.println("Hai selezionato " + operation);
                continue;
            }

            switch (operation){
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    try{
                        // Recupero pizze
                        Pizza[] pizze = getAllPizze();
                        // Inizializzazione della tabella
                        AsciiTable asciiTable = new AsciiTable();
                        // Aggiunge un riga orizzontale
                        asciiTable.addRule();
                        // Aggiugne OGNI pizza alla tabella
                        for(Pizza pizza : pizze){
                            asciiTable.addRow(pizza.getId(), pizza.getNome(), pizza.getPrezzo(), String.join(",", pizza.getIngredienti()));
                            asciiTable.addRule();
                        }
                        // Stampa della tabella
                        // render() genera la stringa formattata con la tabella ASCII
                        System.out.println(asciiTable.render());
                    } catch (Exception e) {
                        // Stampa messaggio di errore
                        System.out.println("Errore" + e.getClass().getSimpleName());
                    }
                    break;
                case 2:
                    {
                        System.out.println("Inserisci il nome della pizza:");
                        String nome = sc.nextLine();
                        if(NomePizzaEsiste(nome)){
                            System.out.println("Nome pizza già esistente");
                            break;
                        }

                        System.out.println("Inserisci gli ingredienti ( separati da virgola ):");
                        String[] ingredienti = sc.nextLine().split(",");
                        for (int i = 0; i < ingredienti.length; i++) {
                            ingredienti[i] = ingredienti[i].trim();
                        }
                        // Oridna in odine alfabetico l'array ingredienti
                        Arrays.sort(ingredienti);

                        System.out.println("Inserisci il prezzo:");
                        double prezzo;

                        while (true) {
                            try {
                                prezzo = Double.parseDouble(sc.nextLine());
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Prezzo non valido!");
                            }
                        }

                        Pizza pizza = new Pizza(nome, ingredienti, prezzo);
                        createPizza(pizza);
                    }
                    break;
                case 3:
                    System.out.println("Inserisci l'ID della pizza da aggiornare:");
                    String idPizzaU = sc.nextLine();

                    // Possibile controllo
                    // if(NomePizzaEsiste(idPizza)){
                    //     System.out.println("ID pizza inesistente");
                    //     break;
                    // }

                    System.out.println("Inserisci il nuovo nome:");
                    String nome = sc.nextLine();

                    System.out.println("Inserisci i nuovi ingredienti ( separati da virgola):");
                    String[] ingredienti = sc.nextLine().split(",");
                    for (int i = 0; i < ingredienti.length; i++) {
                        ingredienti[i] = ingredienti[i].trim();
                    }
                    // Oridna in odine alfabetico l'array ingredienti
                    Arrays.sort(ingredienti);

                    System.out.println("Inserisci il nuovo prezzo:");
                    double prezzo;

                    while (true) {
                        try {
                            prezzo = Double.parseDouble(sc.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Prezzo non valido!");
                        }
                    }

                    Pizza pizza = new Pizza(nome, ingredienti, prezzo);
                    updatePizza(idPizzaU, pizza);
                    break;
                case 4:
                    System.out.println("Inserisci l'ID della pizza da cancellare:");
                    String idPizzaD = sc.nextLine();
                    deletePizza(idPizzaD);
                    break;
                default:
                    continue;
            }
        }
    }

    // Metodo GET, all
    // Recupera e restituisce tutte le pizze come array dell'oggetto Pizza
    public Pizza[] getAllPizze(){
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Esegue la richiesta HTTP con client.newCall(request).execute()
        try (Response response = client.newCall(request).execute()) {
            // Se la risposta NON ha avuto successo
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Stampa tutti gli header HTTP della risposta
            /*
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            */

            Gson gson = new Gson();
            // Converte la risposta JSON in un array di oggetti Pizza
            // response.body().string() ottiene il corpo della risposta come stringa JSON
            // gson.fromJson(..., Pizza[].class) deserializza
            Pizza[] pizze = gson.fromJson(response.body().string(), Pizza[].class);
            return pizze;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo ...
    private boolean NomePizzaEsiste(String nome) {
        Pizza[] pizze = getAllPizze();
        for (Pizza pizza : pizze) {
            if (pizza.getNome().toUpperCase().equals(nome.trim().toUpperCase())) {
                return true; // nome già esistente
            }
        }
        return false; // nome disponibile
    }

    // Metodo POST
    public void createPizza(Pizza pizza){
        // Tipo di contenuto JSON
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        Gson  gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        // Converte l'oggetto Pizza in una stringa JSON
        String json = gson.toJson(pizza);
        // Crea il corpo della richiesta POST
        RequestBody body = RequestBody.create(json, JSON);

        // Costruisce la richiesta HTTP POST
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // Esegue la richiesta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            Pizza pizzaCreata = gson.fromJson(response.body().string(), Pizza.class);
            System.out.println("Pizza creata: " + pizzaCreata.toString());
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    // Metodo di aggiornamento, PUT
    public void updatePizza(String id, Pizza pizza){
        // Tipo di contenuto JSON
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // new GsonBuilder() crea un builder per configurare un oggetto Gson ( libreria per convertire oggetti java e JSON )
        Gson  gson = new GsonBuilder()
                // excludeFieldsWithoutExposeAnnotation() dice a Gson di serializzare/deserializzare solo i campo con annotazione @Expose
                .excludeFieldsWithoutExposeAnnotation()
                // Costruisce un istanza di Gson con questa configurazione
                .create();

        String json = gson.toJson(pizza);
        RequestBody body = RequestBody.create(json, JSON);

        String newUrl = url + "/" + id;

        // Richiesta HTTP PUT
        // Pattern Builder per la configurazione
        // Viene creata la richiesta
        Request request = new Request.Builder()
                // Url di destinaizione della richiesta
                .url(newUrl)
                // Imposta il metodo HTTP della richiesta come PUT e allega un corpo(body) alla richiesta
                .put(body)
                // Costruisce l'oggetto request
                .build();

        // Esegue la richiesta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            // La response di una richiesta PUT non é deserializzabile in un oggetto Pizza, contiene lo status code
            // Pizza pizzaAggiornata = gson.fromJson(response.body().string(), Pizza.class);
            // System.out.println("Pizza aggiornata: " + pizzaAggiornata.toString());

            System.out.println("Pizza aggiornata con successo");
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    // Metodo di eliminazione, DELETE
    public void deletePizza(String id){
        String newUrl = url + "/" + id;

        Request request = new Request.Builder()
                .url(newUrl)
                .delete()
                .build();

        // Esegue la richiesta
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            System.out.println("Pizza eliminata con successo");
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void run(){
         menu();
    }
}
