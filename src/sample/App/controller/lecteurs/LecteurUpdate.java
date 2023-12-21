package sample.App.controller.lecteurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import sample.App.model.Abonnement;
import sample.App.model.ClientFidele;
import sample.App.model.CreditNegatifException;
import sample.App.model.Type;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;


public class LecteurUpdate implements Initializable {

    @FXML
    private Label cinlbl;

    @FXML
    private Label nomlbl;

    @FXML
    private Label prenomlbl;

    @FXML
    private Label creditlbl;

    @FXML
    private Label abonlbl;

    @FXML
    private Label fidelelbl;

    @FXML
    private Label emaillbl;

    @FXML
    private TextField nomfield;

    @FXML
    private TextField prenomfield;

    @FXML
    private TextField creditfield;

    @FXML
    private DatePicker abondate;

    @FXML
    private TextField emailfield;

    @FXML
    private TextField preferencesfield;

    @FXML
    private CheckBox fidelecheckbox;

    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonFermer;

    @FXML
    private Label emaillblee;

    @FXML
    private Label staremail;

    @FXML
    private Label preflblee;


    private boolean email = false;
    private boolean checked = false;
    private boolean alreadyfidel = false;
    private boolean nom = false;
    private boolean prenom = false;
    private boolean credit = false;
    private boolean abdate = false;

    private String cin;
    private boolean marque = true , verservice = true;

    private int idAbonnement ;


    public static boolean isInt(String string) {
        String regex = "\\d+";
        return  string.matches(regex);
    }

    public static boolean isMail(String string) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return  string.matches(regex);
    }


    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            if (string.length() >= 3) {

                try {
                    String[] p = string.split("\\.");
                    if (p[0].length() <= 6 && p[1].length() <= 3)
                        return true;
                    else {
                        return false;
                    }
                } catch (IndexOutOfBoundsException e) {
                    return true;
                }
            } else
                return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }



    @FXML
    void verifNom(KeyEvent event) {
        String mat = nomfield.getText();
        if (!mat.isEmpty()) {
            nomfield.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            nomlbl.setText("✓");
            nom = true;
            nomlbl.setStyle("-fx-text-fill: #32CD32");
        } else {
            nomfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            nomlbl.setStyle("-fx-text-fill: red");
            nomlbl.setText("X Remplir ce champ");
            nom = false;
        }
    }

    @FXML
    void verifPrenom(KeyEvent event) {
        String mat = prenomfield.getText();
        if (!mat.isEmpty()) {
            prenomfield.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            prenomlbl.setText("✓");
            prenom = true;
            prenomlbl.setStyle("-fx-text-fill: #32CD32");
        } else {
            prenomfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            prenomlbl.setStyle("-fx-text-fill: red");
            prenomlbl.setText("X Remplir ce champ");
            prenom = false;
        }
    }

    @FXML
    void verifCredit(KeyEvent event) {
        if (creditfield.getText().isEmpty()) {
            creditlbl.setText("X Saisir le credit");
            credit = false;
            creditlbl.setStyle("-fx-text-fill: red");
            creditfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
        }  if (!isFloat(creditfield.getText())) {
            creditlbl.setText("X Saisir Correctement le credit");
            credit = false;
            creditlbl.setStyle("-fx-text-fill: red");
            creditfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
        } else {
            creditlbl.setStyle("-fx-text-fill: #32CD32");
            creditlbl.setText("✓");
            credit = true;
            creditfield.setStyle("-fx-background-color:white;");
        }
    }


    @FXML
    void verifEmail(KeyEvent event) {
        if (!isMail(emailfield.getText())) {
            emaillbl.setText("X Saisir Correctement le credit");
            email = false;
            emaillbl.setStyle("-fx-text-fill: red");
            emailfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
        } else {
            emaillbl.setStyle("-fx-text-fill: #32CD32");
            emaillbl.setText("✓");
            email = true;
            emailfield.setStyle("-fx-background-color:white;");
        }
    }




    @FXML
    void confirmerButton(ActionEvent event) throws URISyntaxException {
        try {
            String abondateeee = abondate.getValue().toString();
            String nome = nomfield.getText();
            String prenome = prenomfield.getText();
            String credite = creditfield.getText();
            String emaile = emailfield.getText();
            if (!abdate || !nom || !prenom || !credit || (checked && !email)  || (checked && !isMail(emaile)) ) {
                if (!abdate) {
                    abonlbl.setText("X Choisir une date correct!");
                    abonlbl.setStyle("-fx-text-fill: red");
                    abondate.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
                }

                if (!nom) {
                    nomlbl.setText("X Saisir le nom");
                    nomlbl.setStyle("-fx-text-fill: red");
                    nomfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                }

                if (!prenom) {
                    prenomlbl.setText("X Saisir le prenom");
                    prenomlbl.setStyle("-fx-text-fill: red");
                    prenomfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                }

                if (!credit || !isFloat(credite)) {
                    creditlbl.setText("X Saisir le credit");
                    creditlbl.setStyle("-fx-text-fill: red");
                    creditfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                }

                if (checked){
                    if (!email){
                        emaillbl.setText("X Saisir l'email");
                        emaillbl.setStyle("-fx-text-fill: red");
                        emailfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                    }
                    else if (!isMail(emaile)){
                        emaillbl.setText("X Saisir Correctement l'email");
                        emaillbl.setStyle("-fx-text-fill: red");
                        emailfield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                    }
                }


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setHeaderText(null);
                alert.setContentText("Un des champs n'est pas correctement inserer");
                alert.setGraphic(new ImageView(getClass().getResource("../../images/errorinsert.png").toURI().toString()));
                alert.showAndWait();
            } else {
                Connection connection = null;
                try {
                    connection = getOracleConnection();
                    try {
                        String query = "update Abonnement set creationDate = ? where idAbonnement = ?";
                        PreparedStatement abonnementStatement = connection.prepareStatement(query);
                        abonnementStatement.setDate(1, Date.valueOf(abondate.getValue()));
                        abonnementStatement.setInt(2, idAbonnement);
                        abonnementStatement.executeUpdate();

                        String lecteurQuery = "update Lecteur set nom = ?, prenom=?, credit= ? where cin = ?";
                        PreparedStatement lecteurStatement = connection.prepareStatement(lecteurQuery);

                        lecteurStatement.setString(1, nome);
                        lecteurStatement.setString(2, prenome);
                        lecteurStatement.setDouble(3, Double.parseDouble(credite));
                        lecteurStatement.setInt(4, Integer.parseInt(cin));
                        lecteurStatement.executeUpdate();


                        if (alreadyfidel && checked){
                            String fidelQuerye = "update ClientFidele set email = ?, preferences=? where cin = ?";
                            PreparedStatement fidelQuery = connection.prepareStatement(fidelQuerye);

                            fidelQuery.setString(1, emaile);
                            fidelQuery.setString(2, preferencesfield.getText());
                            fidelQuery.setInt(3, Integer.parseInt(cin));
                            fidelQuery.executeUpdate();
                            fidelQuery.close();
                        }
                        else if (!alreadyfidel && checked){
                            String fidelQuerye = "insert into ClientFidele values (?,?,?)";
                            PreparedStatement fidelQuery = connection.prepareStatement(fidelQuerye);

                            fidelQuery.setInt(1, Integer.parseInt(cin));
                            fidelQuery.setString(2, emaile);
                            fidelQuery.setString(3, preferencesfield.getText());
                            fidelQuery.executeUpdate();
                            fidelQuery.close();
                        }

                        abonnementStatement.close();
                        lecteurStatement.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.TRANSPARENT);
                        alert.setHeaderText(null);
                        alert.setContentText("Modification avec succés");
                        alert.setGraphic(new ImageView(getClass().getResource("../../../images/approved2.png").toURI().toString()));
                        alert.showAndWait();
                        Stage stage = (Stage) buttonConfirmer.getScene().getWindow();
                        stage.close();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException | URISyntaxException throwables) {
                    throwables.printStackTrace();
                }

            }
        } catch (NullPointerException e) {
            //System.out.println("you have an error go check please mr Mahdi");
            e.printStackTrace();
        }
    }

    @FXML
    void fidelCheck(){
        checked = !checked;
        if(checked){
            emailfield.setVisible(true);
            preferencesfield.setVisible(true);
            emaillbl.setVisible(true);
            emaillblee.setVisible(true);
            staremail.setVisible(true);
            preflblee.setVisible(true);
            fidelelbl.setText("Si tu click ceci,lecteur restera fidéle si vous ne le supprimer pas.");
        }else{
            fidelelbl.setText("Si tu clique sur le checkbox de fidilité, alors vous devez ecrire l'email");
            emailfield.setVisible(false);
            preferencesfield.setVisible(false);
            emaillbl.setVisible(false);
            emaillblee.setVisible(false);
            staremail.setVisible(false);
            preflblee.setVisible(false);
        }
    }

    @FXML
    public void fermerButton(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) buttonFermer.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList list1 = FXCollections.observableArrayList();
        list1.removeAll();
        list1.addAll(Type.Oui.toString(), Type.Non.toString());
    }

    public void setTextField(String id) {
        this.cin = id;
        this.idAbonnement = -1;
        ClientFidele lecteur = new ClientFidele(0,"test","test","test",null);
        String prefs = "";

        try (Connection connection = getOracleConnection()) {
            // Query to find the client itself
            String lecteurQuery = "SELECT * FROM Lecteur WHERE cin = ?";
            try (PreparedStatement lecteurStatement = connection.prepareStatement(lecteurQuery)) {
                lecteurStatement.setString(1, cin);
                try (ResultSet rsLecteur = lecteurStatement.executeQuery()) {
                    if (rsLecteur.next()) {
                        lecteur.setCin(Long.valueOf(cin));
                        lecteur.setNom(rsLecteur.getString("nom"));
                        lecteur.setPrenom(rsLecteur.getString("prenom"));
                        lecteur.setCredit(Double.parseDouble(rsLecteur.getString("credit")));
                        this.idAbonnement = rsLecteur.getInt("idAbonnement");
                    }
                }
            }

            // Query to find if the client is fidele
            String fideleQuery = "SELECT * FROM ClientFidele WHERE cin = ?";
            try (PreparedStatement fideleStatement = connection.prepareStatement(fideleQuery)) {
                fideleStatement.setString(1, cin);
                try (ResultSet rsFidele = fideleStatement.executeQuery()) {
                    if(rsFidele.next()){
                        alreadyfidel = true;
                        lecteur.setEmail(rsFidele.getString("email"));
                        prefs = rsFidele.getString("preferences");
                    }
                }
            }

            // Query to find the abonnement and the number of the emprunted livre
            if (idAbonnement != -1) {
                String abonnementQuery = "SELECT * FROM Abonnement WHERE idAbonnement = ?";
                try (PreparedStatement abonnementStatement = connection.prepareStatement(abonnementQuery)) {
                    abonnementStatement.setInt(1, idAbonnement);
                    try (ResultSet rsAbonnement = abonnementStatement.executeQuery()) {
                        if (rsAbonnement.next()) {
                            lecteur.setAbonnement(new Abonnement(idAbonnement, rsAbonnement.getDate("creationDate").toLocalDate()));
                        }
                    }
                }

            }
            nom = true;
            prenom = true;
            credit = true;
            abdate = true;
            cinlbl.setText(String.valueOf(lecteur.getCin()));
            nomfield.setText(lecteur.getNom());
            prenomfield.setText(lecteur.getPrenom());
            creditfield.setText(String.valueOf(lecteur.getCredit()));
            abondate.setValue(lecteur.getAbonnement().getCreationDate());
            emaillblee.setVisible(false);
            staremail.setVisible(false);
            preflblee.setVisible(false);
            emaillbl.setVisible(false);
            preferencesfield.setVisible(false);
            emailfield.setVisible(false);
            checked = false;
            if(alreadyfidel){
                checked = true;
                email = true;
                this.fidelecheckbox.setSelected(true);
                emaillblee.setVisible(true);
                staremail.setVisible(true);
                preflblee.setVisible(true);
                emaillbl.setVisible(true);
                preferencesfield.setVisible(true);
                emailfield.setVisible(true);
                emailfield.setText(lecteur.getEmail());
                preferencesfield.setText(prefs);
                fidelelbl.setText("Si tu click ceci,lecteur restera fidéle si vous ne le supprimer pas.");
            }

        } catch (SQLException | CreditNegatifException e ) {
            e.printStackTrace();
        }
    }

}