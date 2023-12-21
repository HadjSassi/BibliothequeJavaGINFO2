package sample.App.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main2 {
    public static void main(String[] args) {


        Bibliotheque bib = new Bibliotheque();

        //Constructeur:  public Livre(String titre, String auteur, long ISBN)

        Livre l1 = new Livre("Stupeur et tremblement", "Nathalie Nothomb", 12578945677L);
        bib.liste_livres.add(l1);
        Livre l2 = new LivreRomantique("Les mis�rables", "Victor Hugo", 1236547896541L, "histo", "Jean Valgean");
        bib.liste_livres.add(l2);
        Livre l3 = new LivrePolice("meurtre", "Hercule Poirot ", 7412589637418L, "Le crime de l'orient express", "Sherlock Holmes", "gatha Christie");
        bib.liste_livres.add(l3);
        Livre l4 = new LivrePolice("meurtre", "Hercule Poirot ", 7894561236547L, "Mort sur le Nil", "Conan Edigawa", "Agatha Christie");
        bib.liste_livres.add(l4);
        Livre l5 = new Livre("Crime et Chat�ment", "Fyodor Dostoevsky", 1234567891230L);
        bib.liste_livres.add(l5);
        Livre l6 = new LivreRomantique("Orgueuil et Pr�jug�s", "Jane Austen", 3216549871230L, "histoire", "Elizabeth");
        bib.liste_livres.add(l6);
        Livre l7 = new LivreRomantique("Emma", "Jane Austen", 1487956412347L, "longue histoire", "Emma");
        bib.liste_livres.add(l7);
        Livre l8 = new LivreRomantique("Orgueuil et Pr�jug�s", "Jane Austen", 3216549871230L, "histoire", "Elizabeth");
        bib.liste_livres.add(l8);
        Livre l9 = new LivreRomantique("Orgueuil et Pr�jug�s", "Jane Austen", 3216549871230L, "histoire", "Elizabeth");
        bib.liste_livres.add(l9);
        Livre l10 = new LivreSciencesFiction("La plan�te des singes", "Pierre Boulle", 2589631478520L, 2500, "Plan�te Soror");
        bib.liste_livres.add(l10);
        Livre l11 = new Livre("Stupeur et tremblement", "Nathalie Nothomb", 12578945677L);
        bib.liste_livres.add(l11);
        Livre l12 = new Livre("Le Mur", "Jean Paul Sartre", 7412589637418L);
        bib.liste_livres.add(l12);


        /*****  remplir maplivres en appelant it�rativement
         * la m�thode void ajouter_MapLivres( Livre L) � impl�menter *********/

        for (Livre livre : bib.liste_livres) {
            bib.ajouter_MapLivres(livre);
        }


        // Afficher Maplivres

        System.out.println(bib.mapLivres);
        // vous devez avoir ce r�sultat {7412589637418=2, 2589631478520=1, 7894561236547=1, 1234567891230=1, 12578945677=2, 1487956412347=1, 1236547896541=1, 3216549871230=3}

        /******************Liste de lecteurs***********************/

        // lecteur1
        ArrayList<Details_Emprunts> listeEmprunts1 = new ArrayList<>();
        listeEmprunts1.add(new Details_Emprunts(l1, LocalDate.of(2022, 2, 1)));
        listeEmprunts1.add(new Details_Emprunts(l2, LocalDate.of(2022, 2, 9)));
        listeEmprunts1.add(new Details_Emprunts(l3, LocalDate.of(2022, 2, 17)));
        listeEmprunts1.add(new Details_Emprunts(l4, LocalDate.of(2022, 3, 5)));
        Abonnement a1 = new Abonnement(listeEmprunts1, LocalDate.of(2023, 10, 25));
        Lecteur lecteur1 = null;
        try {
            lecteur1 = new Lecteur(782456789, "Ines", "Slim", a1,0);
        } catch (CreditNegatifException e) {
            System.out.println("Probléme crédit");
            lecteur1 = new Lecteur(782456789, "Ines", "Slim", a1);
        }
        bib.lecteurs.add(lecteur1);

        // lecteur2
        ArrayList<Details_Emprunts> listeEmprunts2 = new ArrayList<>();
        listeEmprunts2.add(new Details_Emprunts(l1, LocalDate.of(2022, 9, 1)));
        listeEmprunts2.add(new Details_Emprunts(l2, LocalDate.of(2022, 9, 9)));
        listeEmprunts2.add(new Details_Emprunts(l3, LocalDate.of(2022, 9, 17)));
        listeEmprunts2.add(new Details_Emprunts(l4, LocalDate.of(2022, 10, 5)));
        listeEmprunts2.add(new Details_Emprunts(l5, LocalDate.of(2022, 10, 11)));
        listeEmprunts2.add(new Details_Emprunts(l6, LocalDate.of(2022, 10, 18)));
        listeEmprunts2.add(new Details_Emprunts(l5, LocalDate.of(2022, 10, 25)));
        Abonnement a2 = new Abonnement(listeEmprunts2, LocalDate.of(2022, 9, 1));
        Lecteur lecteur2 = null;
        try {
            lecteur2 = new Lecteur(254567899, "Aymen", "Ben Salah", a2,-5);
        } catch (CreditNegatifException e) {
            System.out.println("Probléme crédit");
            lecteur2 = new Lecteur(254567899, "Aymen", "Ben Salah", a2);
        }
        bib.lecteurs.add(lecteur2);

        // lecteur3
        ArrayList<Details_Emprunts> listeEmprunts3 = new ArrayList<>();
        listeEmprunts3.add(new Details_Emprunts(l5, LocalDate.of(2022, 10, 1)));
        Abonnement a3 = new Abonnement(listeEmprunts3, LocalDate.of(2022, 10, 1));
        Lecteur lecteur3 = new Lecteur(254566899, "Imen", "Massoudi", a3);
        bib.lecteurs.add(lecteur3);

        // lecteur4
        ArrayList<Details_Emprunts> listeEmprunts4 = new ArrayList<>();
        listeEmprunts1.add(new Details_Emprunts(l4, LocalDate.of(2022, 3, 1)));
        Abonnement a4 = new Abonnement(listeEmprunts4, LocalDate.of(2022, 2, 1));
        Lecteur lecteur4 = new ClientFidele(264567899, "Selim", "Ben Aissa", a4, "ii@gmail.com", "Romantique");
        bib.lecteurs.add(lecteur4);


        try {
            bib.emprunter(782456789L, l1);// Ines Slim emprunte Le Mur
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(782456789L, l2);// Ines Slim emprunte Le Mur
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(782456789L, l3);// Ines Slim emprunte Le Mur
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(782456789L, l4);// Ines Slim emprunte Le Mur
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(782456789L, l5);// Ines Slim emprunte Le Mur
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(782456789L, l12);// Ines Slim emprunte Le Mur
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(884567899L, l6);// Amine Ben Youssef emprunte Orgueuil et pr jug s
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(264567899L, l8);// Selim Ben Aissa emprunte Orgueil et pr jug s
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        try {
            bib.emprunter(254566899L, l9);// Imen Massoudi emprunte Orgueil et pr jug s
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }

        try {
            bib.emprunter(254567899L, l9);// Aymen Ben Saleh emprunte Orgueil et pr jug s
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }
        //  dans ce cas, vous devrez avoir cet affichage: Le livre n'est pas disponible
        try {
            bib.emprunter(782456789L, l5);// Ines Slim emprunte Les Mis rables
        } catch (EmpruntInterdit e) {
            System.out.println("Emprunt annulé");
        }


        ArrayList<Lecteur> lesfideles = bib.lecteurs_fideles();
        for (Lecteur c : lesfideles)
            System.out.println(c);

        System.out.println(bib.categories_livres());
    }
}
