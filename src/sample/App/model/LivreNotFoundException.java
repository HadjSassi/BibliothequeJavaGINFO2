package sample.App.model;

public class LivreNotFoundException extends Exception{
    public LivreNotFoundException(){
        super("Livre non trouvé !");
    }
}
