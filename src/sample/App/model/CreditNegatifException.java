package sample.App.model;

public class CreditNegatifException extends Exception{
    CreditNegatifException(double credit){
        super("Le Crédit : "+credit+"est négatif");
    }
}
