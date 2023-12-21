package sample.App.model;

import java.util.Objects;
import java.util.Scanner;

public class Lecteur implements Comparable {
    private Long cin ;
    private String nom;
    private String prenom;

    private double credit;

    private Abonnement abonnement;

    public Lecteur() {
        Scanner sc= new Scanner(System.in);
        this.nom = sc.next();
        this.prenom = sc.next();
        this.cin = Long.valueOf(sc.next());
    }

    public Lecteur(int cin) {
        this.cin = (long) cin;
    }

    public Lecteur(int cin, String nom, String prenom) {
        this.cin = (long) cin;
        this.nom = nom;
        this.prenom = prenom;
        credit = 0;
    }


    public Lecteur(int cin, String nom, String prenom,double credit) {
        this.cin = (long) cin;
        this.nom = nom;
        this.prenom = prenom;
        this.credit = credit;
    }

    public Lecteur(int cin, String nom, String prenom, Abonnement abonnement) {
        this.cin = (long) cin;
        this.nom = nom;
        this.prenom = prenom;
        this.abonnement = abonnement;
        credit = 0;
    }

    public Lecteur(int cin, String nom, String prenom, Abonnement abonnement, double credit) throws CreditNegatifException {
        this.cin = (long) cin;
        this.nom = nom;
        this.prenom = prenom;
        this.abonnement = abonnement;
        if (credit < 0)
            throw new CreditNegatifException(credit);
        this.credit = credit;
    }


    public void calculerCredit(double credi)throws CreditNegatifException{
        if (this.frais_Abonnement()-this.credit-credi < 0 )
            throw new CreditNegatifException(this.frais_Abonnement()-this.credit);
        this.credit = this.frais_Abonnement() - this.credit - credi;
    }

    public Long getCin() {
        return cin;
    }

    public void setCin(Long cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) throws CreditNegatifException {
        if (credit < 0)
            throw new CreditNegatifException(credit);
        this.credit = credit;
    }

    public Abonnement getAbonnement() {
        return abonnement;
    }

    public void setAbonnement(Abonnement abonnement) {
        this.abonnement = abonnement;
    }

    public double frais_Abonnement(){
        return Abonnement.getFrais();
    }



    @Override
    public String toString() {
        return "Lecteur{" +
                "cin=" + cin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", abonnement=" + abonnement +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Lecteur oo = (Lecteur) o;
        String ch1 = this.getNom()+ this.getPrenom();
        String ch2 = oo.getNom()+oo.getPrenom();
        return ch1.compareTo(ch2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecteur)) return false;
        Lecteur lecteur = (Lecteur) o;
        return Objects.equals(getCin(), lecteur.getCin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCin());
    }
}
