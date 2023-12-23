package sample.App.model;

public class LivreCount extends Livre{
    private int nbLivre;
    private String type;

    public LivreCount(String titre, String auteur, Long isbn, int nb){
        super(titre,auteur,isbn);
        this.nbLivre = nb;
        this.type = "simple";
    }

    public LivreCount(Long codeUnique, String titre, String auteur, Long isbn){
        super(codeUnique,titre,auteur,isbn);
        this.nbLivre = 0;
        this.type = "simple";
    }

    public LivreCount(String titre, String auteur, Long isbn, int nb, String type){
        super(titre,auteur,isbn);
        this.nbLivre = nb;
        this.type = type;
    }

    public int getNbLivre() {
        return nbLivre;
    }

    public void setNbLivre(int nbLivre) {
        this.nbLivre = nbLivre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return super.toString()+"-*-"+type+"--"+nbLivre;
    }

}