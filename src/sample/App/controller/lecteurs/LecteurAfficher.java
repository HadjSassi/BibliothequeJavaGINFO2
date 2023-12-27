package sample.App.controller.lecteurs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.App.model.Abonnement;
import sample.App.model.ClientFidele;
import sample.App.model.CreditNegatifException;
import sample.App.model.Lecteur;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;

public class LecteurAfficher {


    @FXML
    private Label typelbl;


    @FXML
    private Label cinlbl;


    @FXML
    private Label nomlbl;

    @FXML
    private Label prenomlbl;


    @FXML
    private Label creditlbl;


    @FXML
    private Label emaillbl;

    @FXML
    private Label preflbl;

    @FXML
    private Label emaillblee;

    @FXML
    private Label preflblee;


    @FXML
    private Label abondate;


    @FXML
    private Label renouvdate;

    @FXML
    private Label fraisabonne;


    @FXML
    private Label nblivre;


    @FXML
    private Button buttonFermer1;


        public void setTextField(String cin) {
//            ClientFidele lecteur = new ClientFidele();
            ClientFidele lecteur = new ClientFidele(0,"test","test","test",null);
            int abonnementId = -1;
            int nblivres = 0;
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
                            abonnementId = rsLecteur.getInt("idAbonnement");
                        }
                    }
                }

                // Query to find if the client is fidele
                boolean isFidele = false;
                String fideleQuery = "SELECT * FROM ClientFidele WHERE cin = ?";
                try (PreparedStatement fideleStatement = connection.prepareStatement(fideleQuery)) {
                    fideleStatement.setString(1, cin);
                    try (ResultSet rsFidele = fideleStatement.executeQuery()) {
                        if(rsFidele.next()){
                            isFidele = true;
                            lecteur.setEmail(rsFidele.getString("email"));
                            prefs = rsFidele.getString("preferences");
                        }
                    }
                }

                // Query to find the abonnement and the number of the emprunted livre
                if (abonnementId != -1) {
                    String abonnementQuery = "SELECT * FROM Abonnement WHERE idAbonnement = ?";
                    try (PreparedStatement abonnementStatement = connection.prepareStatement(abonnementQuery)) {
                        abonnementStatement.setInt(1, abonnementId);
                        try (ResultSet rsAbonnement = abonnementStatement.executeQuery()) {
                            if (rsAbonnement.next()) {
                                lecteur.setAbonnement(new Abonnement(abonnementId, rsAbonnement.getDate("creationDate").toLocalDate()));
                            }
                        }
                    }

                    String empruntQuery = "SELECT COUNT(*) AS nbLivres, idAbonnement FROM DetailsEmprunts, Emprunts WHERE idAbonnement = ? and Emprunts.idDetails = DetailsEmprunts.idDetail";
                    try (PreparedStatement empruntStatement = connection.prepareStatement(empruntQuery)) {
                        empruntStatement.setInt(1, abonnementId);
                        try (ResultSet rsEmprunt = empruntStatement.executeQuery()) {
                            if (rsEmprunt.next()) {
                                nblivres = rsEmprunt.getInt("nbLivres");
                            }
                        }
                    }
                }
                cinlbl.setText(String.valueOf(lecteur.getCin()));
                nomlbl.setText(lecteur.getNom());
                prenomlbl.setText(lecteur.getPrenom());
                creditlbl.setText(String.valueOf(lecteur.getCredit()));
                abondate.setText(String.valueOf(lecteur.getAbonnement().getCreationDate()));
                renouvdate.setText(String.valueOf(lecteur.getAbonnement().getCreationDate().plusYears(1)));
                fraisabonne.setText(String.valueOf(Abonnement.getFrais()));
                nblivre.setText(String.valueOf(nblivres));
                nomlbl.setText(lecteur.getNom());
                emaillblee.setVisible(false);
                preflblee.setVisible(false);
                emaillbl.setText("");
                preflbl.setText("");
                if(isFidele){
                    emaillblee.setVisible(true);
                    preflblee.setVisible(true);
                    typelbl.setText("Lecteur Fidéle");
                    emaillbl.setText(lecteur.getEmail());
                    preflbl.setText(prefs);
                }

            } catch (SQLException| CreditNegatifException e ) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setHeaderText(null);
                alert.setContentText("Il y'a un probléme rencontré!\n" + e);
                try {
                    alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                alert.showAndWait();
            }
        }


    public void fermerButton(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) buttonFermer1.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

}
