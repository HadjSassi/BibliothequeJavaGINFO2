package sample.App.model;

public class EmpruntInterdit extends Exception{
    EmpruntInterdit(){
        super("abonnement est epuisé");
    }
}
