import com.google.gson.annotations.Expose;

public class Pizza {
    @Expose(serialize = false, deserialize = true)
    private String _id;

    @Expose
    public String Nome;
    @Expose
    public String[] Ingredienti;
    @Expose
    public double Prezzo;

    public Pizza(String Nome, String[] Ingredienti, double Prezzo) {
        this.Nome = Nome;
        this.Ingredienti = Ingredienti;
        this.Prezzo = Prezzo;
    }

    public String getId() { return _id; }
    public String getNome() { return Nome; }
    public String[] getIngredienti() { return Ingredienti; }
    public double getPrezzo() { return Prezzo; }

    @Override
    public String toString(){ return "\nNome:" + getNome() + "\n\t- ID: " + getId() + "\n\t- Ingredienti" + String.join(", ", getIngredienti()) + "\n\t- Prezzo: " + getPrezzo();}
}