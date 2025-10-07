public class Pizza {
    public String Nome;
    public String[] Ingredienti;
    public double Prezzo;

    public Pizza(String Nome, String[] Ingredienti, double Prezzo) {
        this.Nome = Nome;
        this.Ingredienti = Ingredienti;
        this.Prezzo = Prezzo;
    }

    @Override
    public String toString(){
        return "";
    }
}