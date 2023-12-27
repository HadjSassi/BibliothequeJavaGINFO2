package sample.App.controller.emprunt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.App.model.*;

import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;

public class Emprunter {


    @FXML
    private TextField cinField;


    @FXML
    private TextField codeUniqueField;


    @FXML
    private Button buttonFermer;

    public void fermerButton(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) buttonFermer.getScene().getWindow();
        // do what you have to do
        stage.close();
    }


    public void emprunter(ActionEvent event) {
        String cin = "";
        int codeUnique = 0;
        try {
            //get the values
            cin = this.cinField.getText();
            codeUnique = Integer.parseInt(this.codeUniqueField.getText());
            // connect to databse and get the person
            Lecteur l = new Lecteur();
            Abonnement a = new Abonnement();
            boolean found = false;

            Connection connection = getOracleConnection();
            String foundQuery = "select * from Lecteur, Abonnement where cin = '" + cin + "' and  Lecteur.idAbonnement = Abonnement.idAbonnement";
            ResultSet rs = connection.createStatement().executeQuery(foundQuery);
            if (rs.next()) {
                found = true;
                l.setCin(Long.parseLong(cin));
                l.setNom(rs.getString("nom"));
                l.setPrenom(rs.getString("prenom"));
                l.setCredit(rs.getDouble("credit"));
                a.setIdAbonnement(rs.getInt("idAbonnement"));
                a.setCreationDate(rs.getDate("creationDate").toLocalDate());
                l.setAbonnement(a);
                if (a.getCreationDate().plusYears(1).isBefore(LocalDate.now())) {
                    throw new EmpruntInterdit();
                }
                foundQuery = "select * from Livre where codeunique = " + codeUnique + " ;";
                rs = connection.createStatement().executeQuery(foundQuery);
                if (!rs.next()) {
                    throw new LivreNotFoundException();
                }
                foundQuery = "select * from DetailsEmprunts where returned = false and codeunique = " + codeUnique + " ;";
                System.out.println(foundQuery);
                rs = connection.createStatement().executeQuery(foundQuery);
                if (rs.next()) {
                    throw new LivreEmpruntedException();
                }
                String query = "insert into DetailsEmprunts values (null, ?,?,?);";
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, codeUnique);
                statement.setDate(2, Date.valueOf(LocalDate.now()));
                statement.setBoolean(3, false);
                statement.executeUpdate();
                // Retrieve the generated key
                ResultSet generatedKeys = statement.getGeneratedKeys();
                int idDetail = -1;
                if (generatedKeys.next()) {
                    idDetail = generatedKeys.getInt(1);
                }

                query = "INSERT INTO Emprunts  VALUES (?, ?)";
                statement = connection.prepareStatement(query);

                statement.setInt(1, a.getIdAbonnement());
                statement.setInt(2, idDetail);

                statement.executeUpdate();

                generatedKeys.close();
                statement.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setHeaderText(null);
                alert.setContentText("Emprunt avec succés");
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/approved.png").toURI().toString()));
                alert.showAndWait();
                this.codeUniqueField.setText("");
            }
            rs.close();
            if (!found) {
                throw new LecteurNotFoundException();
            }

        } catch (CreditNegatifException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Le crédit du lecteur est négatif");
            try {
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            alert.showAndWait();
        } catch (EmpruntInterdit e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Emprunt Interdit: Abonnement épuisés");
            try {
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            alert.showAndWait();
        } catch (LecteurNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Lecteur avec cin = " + cin + " non trouvé!");
            try {
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            alert.showAndWait();
        } catch (LivreNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Le Livre avec le code unique = " + codeUnique + " non trouvé!");
            try {
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            alert.showAndWait();
        } catch (LivreEmpruntedException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Le livre avec le code unique = " + codeUnique + " est déjà emprunté!");
            try {
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            alert.showAndWait();
        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }catch (Exception throwables) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Il y'a un probléme rencontré!\n" + throwables);
            try {
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/close-window-64.png").toURI().toString()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            alert.showAndWait();
        }
    }

}
