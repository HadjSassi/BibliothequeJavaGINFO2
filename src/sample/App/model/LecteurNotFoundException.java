package sample.App.model;

public class LecteurNotFoundException extends Exception{
    public LecteurNotFoundException (){
        super("Lecteur non trouvé !");
    }
}
