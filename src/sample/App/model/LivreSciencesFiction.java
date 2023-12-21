package sample.App.model;

public class LivreSciencesFiction extends Livre{
    private int annee;
    private String espace;

    public LivreSciencesFiction(int annee, String espace) {
        this.annee = annee;
        this.espace = espace;
    }

    public LivreSciencesFiction(Long codeUnique, String titre, String auteur, Long ISBN, int annee, String espace) {
        super(codeUnique, titre, auteur, ISBN);
        this.annee = annee;
        this.espace = espace;
    }

    public LivreSciencesFiction(String titre, String auteur, Long ISBN, int annee, String espace) {
        super(titre, auteur, ISBN);
        this.annee = annee;
        this.espace = espace;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getEspace() {
        return espace;
    }

    public void setEspace(String espace) {
        this.espace = espace;
    }

    @Override
    public String toString() {
        return "SciencesFictions{" +
                "annee=" + annee +
                ", espace='" + espace + '\'' +
                "} " + super.toString();
    }
}
