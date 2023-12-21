package sample.App.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class Bibliotheque {
    public ArrayList<Livre> liste_livres;
    public ArrayList<Lecteur> lecteurs;
    public Map<Long, ArrayList<Details_Emprunts>> Map_emprunts;
    public Map<Long, Integer> mapLivres;


    public Bibliotheque() {
        liste_livres = new ArrayList<>();
        lecteurs = new ArrayList<>();
        Map_emprunts = new HashMap<>();
        mapLivres = new HashMap<>();
    }

    public ArrayList<Livre> getListe_livres() {
        return liste_livres;
    }

    public void setListe_livres(ArrayList<Livre> liste_livres) {
        this.liste_livres = liste_livres;
    }

    public ArrayList<Lecteur> getLecteurs() {
        return lecteurs;
    }

    public void setLecteurs(ArrayList<Lecteur> lecteurs) {
        this.lecteurs = lecteurs;
    }

    public Map<Long, ArrayList<Details_Emprunts>> getMap_emprunts() {
        return Map_emprunts;
    }

    public void setMap_emprunts(Map<Long, ArrayList<Details_Emprunts>> map_emprunts) {
        Map_emprunts = map_emprunts;
    }

    public Map<Long, Integer> getMapLivres() {
        return mapLivres;
    }

    public void setMapLivres(Map<Long, Integer> mapLivres) {
        this.mapLivres = mapLivres;
    }

    public boolean isLivreExistInMap(Livre l) {
        return this.mapLivres.containsKey(l.getISBN());
    }

    public int indexLivreList(Livre l) {
        for (int i = 0; i < this.liste_livres.size(); i++) {
            if (l.getCodeUnique().equals(liste_livres.get(i).getCodeUnique()))
                return i;
        }
        return -1;
    }

    public int nombreIsbnEmprunte(Long isbn) {
        int x = 0;
        for (Map.Entry<Long, ArrayList<Details_Emprunts>> entry : Map_emprunts.entrySet()) {
            for (Details_Emprunts d : entry.getValue()) {
                if (d.getLivre().getISBN().equals(isbn)) {
                    x++;
                }
            }
        }
        return x;
    }

    public boolean stillIsbnExist(Long isbn) {
        if (this.isIsbnExistInList(isbn) == -1)
            return false;
//        System.out.printf("%d - %d = %d ", mapLivres.get(isbn), nombreIsbnEmprunte(isbn), this.mapLivres.get(isbn) - nombreIsbnEmprunte(isbn));
        return this.mapLivres.get(isbn) - nombreIsbnEmprunte(isbn) > 0;
    }

    public int isIsbnExistInList(Long isbn) {
        for (int i = 0; i < this.liste_livres.size(); i++) {
            if (isbn.equals(liste_livres.get(i).getISBN()))
                return i;
        }
        return -1;
    }

    public int indexLecteurList(Long l) {
        for (int i = 0; i < this.lecteurs.size(); i++) {
            if (l.equals(lecteurs.get(i).getCin()))
                return i;
        }
        return -1;
    }

    public void ajouter_Livres(Livre l) {
        if (indexLivreList(l) == -1) {
            this.liste_livres.add(l);
            if (isLivreExistInMap(l)) {
                mapLivres.put(l.getISBN(), mapLivres.get(l.getISBN()) + 1);
            } else {
                mapLivres.put(l.getISBN(), 1);
            }
        } else {
            if (isLivreExistInMap(l)) {
                mapLivres.put(l.getISBN(), mapLivres.get(l.getISBN()) + 1);
            } else {
                mapLivres.put(l.getISBN(), 1);
            }
        }
    }

    public boolean isBookTaken(Livre l) {
        for (Map.Entry<Long, ArrayList<Details_Emprunts>> entry : this.Map_emprunts.entrySet()) {
            for (Details_Emprunts d : entry.getValue())
                if (d.getLivre().getCodeUnique().equals(l.getCodeUnique()))
                    return true;
        }
        return false;
    }

    public boolean lecteurbusy(Long cin) {
        return this.Map_emprunts.get(cin) != null;
    }

    public Lecteur getLecteurByCin(Long cin) {
        for (Lecteur l : this.lecteurs)
            if (l.getCin().equals(cin))
                return l;
        return null;
    }

    public void emprunter(Long cin, Livre l) throws EmpruntInterdit {
        if (stillIsbnExist(l.getISBN()))
            if (!isBookTaken(l))
                if (indexLecteurList(cin) != -1)
//                    if (!lecteurbusy(cin))
                {
                    Lecteur ll = this.getLecteurByCin(cin);
                    if (ll.getAbonnement().getCreationDate().plusYears(1).isAfter(LocalDate.now())) {
                       throw new EmpruntInterdit();
                    } else {
                        try {
                            ArrayList<Details_Emprunts> ld = this.Map_emprunts.get(cin);
                            ld.add(new Details_Emprunts(l));
                            this.Map_emprunts.put(cin, ld);
                        } catch (NullPointerException e) {
                            ArrayList<Details_Emprunts> ld = new ArrayList<>();
                            ld.add(new Details_Emprunts(l));
                            this.Map_emprunts.put(cin, ld);
                        }
                    }
                }
//                    else
//                        System.out.println("Le lecteur a déjà emprunté un livre");
                else
                    System.out.println("Lecteur n'est pas existant!");
            else
                System.out.println("Le livré est emprunté");
        else
            System.out.println("Le livre n'est pas disponible");
    }

    /*public Livre retourner_livre(Lecteur lecteur) {
        Livre livre = this.Map_emprunts.get(lecteur.getCin()).getLivre();
        this.Map_emprunts.remove(lecteur.getCin());
        return livre;
    }*/

    public int nombre_livres_empruntes() {
        return this.Map_emprunts.size();
    }

    public int nombre_livre_retours() {
        int delayedTime = 0;
        LocalDate target = LocalDate.now().plusDays(7);
        for (Map.Entry<Long, ArrayList<Details_Emprunts>> entry : Map_emprunts.entrySet()) {
            for (Details_Emprunts d : entry.getValue())
                if (d.getDateRetour().isBefore(target))
                    delayedTime++;
        }
        return delayedTime;
    }


    public void ajouter_MapLivres(Livre livre) {
        this.ajouter_Livres(livre);
    }


    public ArrayList<Lecteur> lecteurs_fideles() {
        ArrayList<Lecteur> lista = new ArrayList<>();
        for (Lecteur l : this.lecteurs) {
            Period period = Period.between(l.getAbonnement().getCreationDate(), LocalDate.now());
            double avgTime = period.getYears() * 12 + period.getMonths();
            int avgLivr = 0;
            for (Map.Entry<Long, ArrayList<Details_Emprunts>> entry : this.Map_emprunts.entrySet()) {
                if (l.getCin().equals(entry.getKey()))
                    avgLivr += entry.getValue().size();
            }
            if (avgLivr / avgTime >= 2)
                lista.add(l);
        }
        return lista;
    }

    public Map<String, Integer> categories_livres() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Policier", 0);
        map.put("Romantique", 0);
        map.put("SciencesFiction", 0);
        for (Livre l : this.liste_livres)   
            if (l instanceof LivrePolice)
                map.put("Policier", map.get("Policier") + 1);
            else if (l instanceof LivreRomantique)
                map.put("Romantique", map.get("Romantique") + 1);
            else if (l instanceof LivreSciencesFiction)
                map.put("SciencesFiction", map.get("SciencesFiction") + 1);
        return map;
    }

    public ArrayList<Lecteur> Abonnements_epuises() {
        ArrayList<Lecteur> lista = new ArrayList<>();
        for (Lecteur l : lecteurs)
            if (l.getAbonnement().getCreationDate().plusYears(1).isAfter(LocalDate.now()))
                lista.add(l);
        return lista;
    }

    public ArrayList<Lecteur> chercher_lecteurs(String l){
        ArrayList<Lecteur > lista = new ArrayList<>();
        for (Lecteur i: this.lecteurs){
            if(i.getNom().equalsIgnoreCase(l))
                lista.add(i);
        }
        return lista;
    }

    public ArrayList<Lecteur> trier_Lectuer(){
        ArrayList<Lecteur> lista = this.lecteurs;
        Collections.sort(lista);
        return lista;
    }

    public long nbLivreByStream(){
        return this.liste_livres.stream()
                .filter(p -> p.getAuteur().equalsIgnoreCase("Victor Hugo"))
                .filter(p -> p.getTitre().toUpperCase().startsWith("L"))
                .count();   
    }

    public List<Lecteur> trierWithStream(){

        this.lecteurs.stream().sorted(
                new Comparator<Lecteur>() {
                    @Override
                    public int compare(Lecteur lecteur, Lecteur t1) {
                        return lecteur.getCin().compareTo(t1.getCin());
                    }
                }.thenComparing(
                        new Comparator<Lecteur>() {
                            @Override
                            public int compare(Lecteur lecteur, Lecteur t1) {
                                return Integer.parseInt(lecteur.getPrenom()+lecteur.getNom().toLowerCase().compareTo(
                                        t1.getPrenom()+t1.getNom().toLowerCase()));
                            }
                        }
                )
        );

        return this.lecteurs.stream()
                .sorted(Comparator.comparing(Lecteur::getCin))
                .sorted(Comparator.comparing(Lecteur::getNom))
                .collect(Collectors.toList())
        ;
    }

    public static void main(String[] args) {


        Bibliotheque bib = new Bibliotheque();

        //Constructeur:  public Livre(String titre, String auteur, long ISBN)

        Livre l1 = new Livre("Stupeur et tremblement", "Nathalie Nothomb", 12578945677L);
        bib.liste_livres.add(l1);
        Livre l2 = new Livre("Les misérables", "Victor Hugo", 1236547896541L);
        bib.liste_livres.add(l2);
        Livre l3 = new Livre("Le Mur", "Jean Paul Sartre", 7412589637418L);
        bib.liste_livres.add(l3);
        Livre l4 = new Livre("Notre dame de Paris", "Victor Hugo", 7894561236547L);
        bib.liste_livres.add(l4);
        Livre l5 = new Livre("Crime et Chatiment", "Fyodor Dostoevsky", 1234567891230L);
        bib.liste_livres.add(l5);
        Livre l6 = new Livre("Orgueuil et Préjugés", "Jane Austen", 3216549871230L);
        bib.liste_livres.add(l6);
        Livre l7 = new Livre("Emma", "Jane Austen", 1487956412347L);
        bib.liste_livres.add(l7);
        Livre l8 = new Livre("Orgueuil et Préjugés", "Jane Austen", 3216549871230L);
        bib.liste_livres.add(l8);
        Livre l9 = new Livre("Orgueuil et Préjugés", "Jane Austen", 3216549871230L);
        bib.liste_livres.add(l9);
        Livre l10 = new Livre("que serais je sans toi", "Guillaume Musso", 2589631478520L);
        bib.liste_livres.add(l10);
        Livre l11 = new Livre("Stupeur et tremblement", "Nathalie Nothomb", 12578945677L);
        bib.liste_livres.add(l11);
        Livre l12 = new Livre("Le Mur", "Jean Paul Sartre", 7412589637418L);
        bib.liste_livres.add(l12);

        /*****  remplir maplivres en appelant it rativement
         * la m thode void ajouter_MapLivres( Livre L)   impl menter *********/

        for (Livre livre : bib.liste_livres) {
            bib.ajouter_Livres(livre);
        }


        // Afficher Maplivres

        System.out.println(bib.mapLivres);
        // vous devez avoir ce résultat {7412589637418=2, 2589631478520=1, 7894561236547=1, 1234567891230=1, 12578945677=2, 1487956412347=1, 1236547896541=1, 3216549871230=3}

//	   /******************Liste de lecteurs***********************/
//
        bib.lecteurs.add(new Lecteur(782456789, "Ines", "Slim"));
        bib.lecteurs.add(new Lecteur(254567899, "Aymen", "Ben Salah"));
        bib.lecteurs.add(new Lecteur(254566899, "Imen", "Massoudi"));
        bib.lecteurs.add(new Lecteur(264567899, "Selim", "Ben Aissa"));
        bib.lecteurs.add(new Lecteur(884567899, "Amine", "Ben youssef"));

        /****************** Ajouter un emprunt à la map d'emprunts**************************/
        /******implémenter la méthode void emprunter_livre(long cin, Livre livre) ***/
        /** la méthode ajoute l'emprunt si le livre existe dans la liste des livres de la bibliothèque
         *  et si le lecteur n'existe pas déjà dans la map d'emprunts(veut dire qu'il n'a pas déjà un livre emprunt )
         *  n'oublier pas de décrémenter de 1 le nombre de copies de livres dans maplivres*****/

        //boolean ajouterEmprunt(long cin, long ISBN, LocalDate date)   impl menter
        System.out.println(bib.lecteurs);
        System.out.println("trieee");
        System.out.println(bib.trier_Lectuer());


       /* try {
            bib.emprunter(782456789L, l3);// Ines Slim emprunte Le Mur
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
        }*/
        //  dans ce cas, vous devrez avoir cet affichage: Le lecteur a d j  emprunt  un livre

//		   /********************************Affichage du  nombre total de livres*******************/
//
//	      ///  System.out.println("Nombre toatal de livres: "+bib.nombre_total_livres());
//	       // Nombre toatal de livres: 12
//
//	        /********************************Affichage du  nombre de livres emprunt s*******************/
//
//        System.out.println("Nombre toatal de livres emprunt s: " + bib.nombre_livres_empruntes());
//	        //Nombre toatal de livres emprunt s: 4
//
//	        /********************************Affichage du  nombre total de livres de retour dans les sept jours qui suivent j*******************/
//
//        System.out.println("Nombre  de livres de retour dans les 7 jours suivants: " + bib.nombre_livre_retours());
//               // Nombre  de livres de retour dans les 7 jours suivants: 0

        // faites les modifications nécessaires pour afficher 4
//
        /*for (Map.Entry<Long, Details_Emprunts> entry: bib.Map_emprunts.entrySet()){
            Details_Emprunts aux = entry.getValue();
            aux.setDateRetour(aux.getDateRetour().minusDays(5));
            entry.setValue(aux);
        }*/
//        System.out.println("Nombre  de livres de retour dans les 7 jours suivants: " + bib.nombre_livre_retours());


    }


}
