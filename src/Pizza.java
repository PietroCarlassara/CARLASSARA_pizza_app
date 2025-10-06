import java.util.Arrays;

public class Pizza {
    // Con transient _id non viene incluso nel JSON, nella serializzazione
    public transient String _id;
    public String Nome;
    public String[] Ingredienti;
    public double Prezzo;

    // Costruttore
    public Pizza(String Nome, String[] Ingredienti, double Prezzo) {
        this.Nome = Nome;
        this.Ingredienti = Ingredienti;
        this.Prezzo = Prezzo;
    }

    @Override
    public String toString(){
        return "\tPizza: " + this.Nome + "\n\t\tIngredienti: " + Arrays.toString(this.Ingredienti) + "\n\t\tPrezzo: " + this.Prezzo + "\n\t\tID: " + this._id + "\n";
    }
}