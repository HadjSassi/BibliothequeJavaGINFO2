package sample.App.model;

public class EmpruntInterdit extends Exception{
    public EmpruntInterdit(){
        super("abonnement est epuisé");
    }
}
