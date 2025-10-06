import java.util.Arrays;
import java.util.Scanner;

public class Main {
    // COLORI
    public static final String DEFAULT = "\u001B[0m";
    public static final String ROSSO = "\u001B[31m";
    public static final String GIALLO = "\u001B[33m";

    // OVERLOAD del metodo stampa
    public static void stampa(String str){
        stampa(str, DEFAULT);
    }

    public static void stampa(String str, String color){
        System.out.println(color + str);
    }

    public static void main(String[] args) {
        App app = new App();

        Scanner scanner = new Scanner(System.in);

        stampa("Opzioni:", GIALLO);
        stampa("\tOPTION - Visualizza le opzioni");
        stampa("\tEXIT - ESCI");
        stampa("\tGET - Visualizza pizze (GET)");
        stampa("\tPOST - Aggiungi una pizza (POST)");

        boolean continua = true;
        while(continua){
            System.out.print(DEFAULT + "Opzione: ");
            String scelta = scanner.nextLine().toUpperCase();

            switch (scelta) {
                case "OPTION":
                    {
                        stampa("\nOpzioni:", GIALLO);
                        stampa("\tOPTION - Visualizza le opzioni");
                        stampa("\tEXIT - ESCI");
                        stampa("\tGET - Visualizza pizze (GET)");
                        stampa("\tPOST - Aggiungi una pizza (POST)");
                    }
                    break;
                case "EXIT":
                    continua = false;
                    stampa("EXIT", ROSSO);
                    break;
                case "GET":
                    app.doGet();
                    break;
                case "POST":
                    {
                        // Nome pizza
                        System.out.print("\t- Nome pizza: ");
                        String nome = scanner.nextLine();
                        // Ingredienti
                        System.out.print("\t- Ingredienti (separati da virgola): ");
                        String[] ingredienti = scanner.nextLine().split(",");
                        for(String ing : ingredienti){
                            ing = ing.trim();
                        }
                        // Oridna in odine alfabetico l'array ingredienti
                        Arrays.sort(ingredienti);
                        // Prezzo
                        System.out.print("\t- Prezzo: ");
                        double prezzo = scanner.nextDouble();
                        scanner.nextLine();

                        app.doPost(new Pizza(nome, ingredienti, prezzo));
                        break;
                    }

                default:
                    stampa("Scelta non valida.\n", ROSSO);
            }
        }

        scanner.close();
    }
}