package sample.App.model;

import java.time.LocalDate;

public class Details_Emprunts {

    private int idDetail;
    private Livre livre;
    private LocalDate dateEmrpunt;
    private boolean returned;


    public Details_Emprunts(Livre livre) {
        this.idDetail = 0;
        this.livre = livre;
        this.dateEmrpunt = LocalDate.now();
        this.returned = false;
    }

    public Details_Emprunts(Livre livre, LocalDate dateEmrpunt) {
        this.idDetail = 0 ;
        this.livre = livre;
        this.dateEmrpunt = dateEmrpunt;
        this.returned = false;
    }

    public Details_Emprunts(int idDetail, Livre livre) {
        this.idDetail = idDetail;
        this.livre = livre;
        this.dateEmrpunt = LocalDate.now();
        this.returned = false;
    }

    public Details_Emprunts(int idDetail, Livre livre, LocalDate dateEmrpunt) {
        this.idDetail = idDetail ;
        this.livre = livre;
        this.dateEmrpunt = dateEmrpunt;
        this.returned = false;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public LocalDate getDateEmrpunt() {
        return dateEmrpunt;
    }

    public void setDateEmrpunt(LocalDate dateEmrpunt) {
        this.dateEmrpunt = dateEmrpunt;
    }

    public boolean getReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public LocalDate getDateRetour(){
        return this.dateEmrpunt.plusWeeks(1);
    }
    @Override
    public String toString() {
        return "Details_Emprunts{" +
                "idDetail=" + idDetail +
                ", livre=" + livre +
                ", dateEmrpunt=" + dateEmrpunt +
                ", dateRetour=" + getDateRetour() +
                ", returned=" + returned +
                '}';
    }
}

