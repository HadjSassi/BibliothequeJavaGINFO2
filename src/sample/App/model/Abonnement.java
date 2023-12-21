package sample.App.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Abonnement {

    private int idAbonnement;
    private static double frais = 20;
    private LocalDate creationDate;
    private ArrayList<Details_Emprunts> listeLivres;

    public Abonnement(ArrayList<Details_Emprunts> listeLivres,LocalDate creationDate) {
        this.creationDate = creationDate;
        this.listeLivres = listeLivres;
    }

    public Abonnement(ArrayList<Details_Emprunts> listeLivres) {
        this.creationDate = LocalDate.now();
        this.listeLivres = listeLivres;
    }

    public Abonnement() {
        this.creationDate = LocalDate.now();
        this.listeLivres = new ArrayList<>();
    }

    public Abonnement(int idAbonnement, LocalDate creationDate, ArrayList<Details_Emprunts> listeLivres) {
        this.idAbonnement = idAbonnement;
        this.creationDate = creationDate;
        this.listeLivres = listeLivres;
    }

    public Abonnement(int idAbonnement, LocalDate creationDate) {
        this.idAbonnement = idAbonnement;
        this.creationDate = creationDate;
        this.listeLivres = new ArrayList<>();
    }

    public int getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public static double getFrais() {
        return frais;
    }

    public static void setFrais(double frais) {
        Abonnement.frais = frais;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public ArrayList<Details_Emprunts> getListeLivres() {
        return listeLivres;
    }

    public void setListeLivres(ArrayList<Details_Emprunts> listeLivres) {
        this.listeLivres = listeLivres;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "idAbonnement=" + idAbonnement +
                ", creationDate=" + creationDate +
                ", listeLivres=" + listeLivres +
                '}';
    }
}
