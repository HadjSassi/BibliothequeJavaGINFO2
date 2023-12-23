package sample.App.model;

public class LivrePolice extends Livre{

    private String descriptif;
    private String detective;
    private String victime;

    public LivrePolice(Long codeUnique, String descriptif, String detective, String victime) {
        super(codeUnique,null,null,null);
        this.descriptif = descriptif;
        this.detective = detective;
        this.victime = victime;
    }

    public LivrePolice(String descriptif, String detective, String victime) {
        this.descriptif = descriptif;
        this.detective = detective;
        this.victime = victime;
    }

    public LivrePolice(Long codeUnique, String titre, String auteur, Long ISBN, String descriptif, String detective, String victime) {
        super(codeUnique, titre, auteur, ISBN);
        this.descriptif = descriptif;
        this.detective = detective;
        this.victime = victime;
    }

    public LivrePolice(String titre, String auteur, Long ISBN, String descriptif, String detective, String victime) {
        super(titre, auteur, ISBN);
        this.descriptif = descriptif;
        this.detective = detective;
        this.victime = victime;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public String getDetective() {
        return detective;
    }

    public void setDetective(String detective) {
        this.detective = detective;
    }

    public String getVictime() {
        return victime;
    }

    public void setVictime(String victime) {
        this.victime = victime;
    }

    @Override
    public String toString() {
        return "Policier{" +
                "descriptif='" + descriptif + '\'' +
                ", detective='" + detective + '\'' +
                ", victime='" + victime + '\'' +
                "} " + super.toString();
    }
}
