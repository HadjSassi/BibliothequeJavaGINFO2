package sample.App.model;

public class LivreRomantique extends Livre{
    private String history;
    private String personnagePrincipal;

    public LivreRomantique(String history, String personnagePrincipal) {
        this.history = history;
        this.personnagePrincipal = personnagePrincipal;
    }

    public LivreRomantique(Long codeUnique, String titre, String auteur, Long ISBN, String history, String personnagePrincipal) {
        super(codeUnique, titre, auteur, ISBN);
        this.history = history;
        this.personnagePrincipal = personnagePrincipal;
    }

    public LivreRomantique(String titre, String auteur, Long ISBN, String history, String personnagePrincipal) {
        super(titre, auteur, ISBN);
        this.history = history;
        this.personnagePrincipal = personnagePrincipal;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getPersonnagePrincipal() {
        return personnagePrincipal;
    }

    public void setPersonnagePrincipal(String personnagePrincipal) {
        this.personnagePrincipal = personnagePrincipal;
    }

    @Override
    public String toString() {
        return "Romance{" +
                "history='" + history + '\'' +
                ", personnagePrincipal='" + personnagePrincipal + '\'' +
                "} " + super.toString();
    }
}
