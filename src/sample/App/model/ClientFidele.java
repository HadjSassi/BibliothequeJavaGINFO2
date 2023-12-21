package sample.App.model;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientFidele extends Lecteur{
    private String email;
    private ArrayList<String> preferences ;

    public ClientFidele(){
        super();
        this.email= "";
        this.preferences=new ArrayList<>();
    }

    public ClientFidele(int cin, String email, String preferences) {
        super(cin);
        this.email = email;
//        this.preferences = (ArrayList<String>) Arrays.asList(preferences.split(","));
    }

    public ClientFidele(int cin, String nom, String prenom, String email, ArrayList<String> preferences) {
        super(cin, nom, prenom);
        this.email = email;
        this.preferences = preferences;
    }

    public ClientFidele(int cin, String nom, String prenom, Abonnement abonnement, String email, ArrayList<String> preferences) {
        super(cin, nom, prenom, abonnement);
        this.email = email;
        this.preferences = preferences;
    }

    public ClientFidele(int cin, String nom, String prenom, Abonnement abonnement,double credit, String email, String  preferences) {
        super(cin, nom, prenom, abonnement);
        this.email = email;
        this.preferences = (ArrayList<String>) Arrays.asList(preferences.split(","));
    }

    public ClientFidele(int cin, String nom, String prenom,double credit, String email, String pref) {
        super(cin,nom,prenom,credit);
        this.email = email;
        this.preferences = (ArrayList<String>) Arrays.asList(pref.split(","));
    }


    public ClientFidele(int cin, String nom, String prenom, Abonnement abonnement, String email, String preferences) {
        super(cin, nom, prenom, abonnement);
        this.email = email;
        this.preferences = new ArrayList<>();
        this.preferences.add(preferences);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<String> preferences) {
        this.preferences = preferences;
    }

    @Override
    public double frais_Abonnement(){
        return Abonnement.getFrais()*0.5;
    }

    @Override
    public String toString() {
        return "LecteurFidele{" +
                "email='" + email + '\'' +
                ", preferences=" + preferences +
                "} " + super.toString();
    }
}
