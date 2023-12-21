package sample.App.model;

public class Livre {
    private static long nombreLivre = 0;
    private Long codeUnique;
    private String titre;
    private String auteur;
    private Long ISBN;


    public Livre() {
        this.codeUnique = 0L;
        this.ISBN = 0L;
        this.auteur = "";
        this.titre = "";
        Livre.nombreLivre++;
    }


    public Livre(Long codeUnique, String titre, String auteur, Long ISBN) {
        this.codeUnique = codeUnique;
        this.titre = titre;
        this.auteur = auteur;
        this.ISBN = ISBN;
        Livre.nombreLivre++;
    }


    public Livre(String titre, String auteur, Long ISBN) {
        this.titre = titre;
        this.auteur = auteur;
        this.ISBN = ISBN;
        Livre.nombreLivre++;
        this.codeUnique = Livre.nombreLivre;
    }

    public Long getCodeUnique() {
        return codeUnique;
    }

    public void setCodeUnique(Long codeUnique) {
        this.codeUnique = codeUnique;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    @Override
    public String toString() {
        return "Livre{" +
                "codeUnique=" + codeUnique +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", ISBN=" + ISBN +
                '}';
    }
}
