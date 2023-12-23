package sample.App.controller.livre;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.App.model.Abonnement;
import sample.App.model.ClientFidele;
import sample.App.model.CreditNegatifException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;

public class LivreAfficher {


    @FXML
    private Label typelbl;


    @FXML
    private Label isbnlbl;


    @FXML
    private Label titrelbl;

    @FXML
    private Label auteurlbl;


    @FXML
    private Label nblbl;


    @FXML
    private Label aneelble;

    @FXML
    private Label aneelbl;

    @FXML
    private HBox anneehbox;

    @FXML
    private Label espacelbl;

    @FXML
    private Label espacee;


    @FXML
    private HBox espaceHbox;


    @FXML
    private Label personlbl;

    @FXML
    private Label person;


    @FXML
    private HBox persone;

    @FXML
    private Label victimelbl;

    @FXML
    private Label detectivelbl;


    @FXML
    private HBox detectiveBox;

    @FXML
    private HBox victimebox;


    @FXML
    private Label histoireDescriptionslbl;


    @FXML
    private Button buttonFermer1;


    public void setTextField(Long isbn, int nb, String type) {
        String query;
        this.nblbl.setText(nb + " Livre");
        this.isbnlbl.setText(isbn.toString());
        long codeUnique = 0;
        try (Connection connection = getOracleConnection()) {
            String lecteurQuery = "SELECT * FROM Livre WHERE isbn = ? limit 1";
            try (PreparedStatement lecteurStatement = connection.prepareStatement(lecteurQuery)) {
                lecteurStatement.setLong(1, isbn);
                try (ResultSet rs = lecteurStatement.executeQuery()) {
                    if (rs.next()) {
                        titrelbl.setText(rs.getString("titre"));
                        auteurlbl.setText(rs.getString("auteur"));
                        codeUnique = rs.getLong("codeunique");
                    }
                }
            }

            switch (type) {
                case "Romantique":
                    typelbl.setText("Livre Romantiques");
                    detectiveBox.setVisible(false);
                    victimebox.setVisible(false);
                    anneehbox.setVisible(false);
                    espaceHbox.setVisible(false);
                    persone.setVisible(true);
                    query = "SELECT * FROM Romantique WHERE codeunique = ? ";
                    try (PreparedStatement lecteurStatement = connection.prepareStatement(query)) {
                        lecteurStatement.setLong(1, codeUnique);
                        try (ResultSet rs = lecteurStatement.executeQuery()) {
                            if (rs.next()) {
                                personlbl.setText(rs.getString("personnageprincipal"));
                                histoireDescriptionslbl.setText(rs.getString("history"));
                            }
                        }
                    }
                    break;
                case "SciencesFictions":
                    typelbl.setText("Livre Sciences Fictions");
                    detectiveBox.setVisible(false);
                    victimebox.setVisible(false);
                    anneehbox.setVisible(true);
                    espaceHbox.setVisible(true);
                    persone.setVisible(false);
                    query = "SELECT * FROM SciencesFicition WHERE codeunique = ? ";
                    try (PreparedStatement lecteurStatement = connection.prepareStatement(query)) {
                        lecteurStatement.setLong(1, codeUnique);
                        try (ResultSet rs = lecteurStatement.executeQuery()) {
                            if (rs.next()) {
                                aneelbl.setText(String.valueOf(rs.getInt("annee")));
                                espacelbl.setText(rs.getString("espace"));
                            }
                        }
                    }
                    break;
                case "Policiers":
                    typelbl.setText("Livre Policier");
                    detectiveBox.setVisible(true);
                    victimebox.setVisible(true);
                    anneehbox.setVisible(false);
                    espaceHbox.setVisible(false);
                    persone.setVisible(false);
                    query = "SELECT * FROM Police WHERE codeunique = ? ";
                    try (PreparedStatement lecteurStatement = connection.prepareStatement(query)) {
                        lecteurStatement.setLong(1, codeUnique);
                        try (ResultSet rs = lecteurStatement.executeQuery()) {
                            if (rs.next()) {
                                victimelbl.setText(String.valueOf(rs.getString("victime")));
                                detectivelbl.setText(rs.getString("detective"));
                                histoireDescriptionslbl.setText(rs.getString("descriptif"));
                            }
                        }
                    }
                    break;
                default:
                    detectiveBox.setVisible(false);
                    victimebox.setVisible(false);
                    anneehbox.setVisible(false);
                    espaceHbox.setVisible(false);
                    persone.setVisible(false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void fermerButton(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) buttonFermer1.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

}
