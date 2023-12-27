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

public class Restituer {


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


    public void restituer(ActionEvent event) {
        int codeUnique = 0;
        try {
            codeUnique = Integer.parseInt(this.codeUniqueField.getText());
            Abonnement a = new Abonnement();
            Connection connection = getOracleConnection();
            String foundQuery = "select count(*) , returned from DetailsEmprunts where  codeunique = " + codeUnique + " group by returned order by returned ;";
            System.out.println(foundQuery);
            ResultSet rs = connection.createStatement().executeQuery(foundQuery);
            if (rs.next()) {
                if (rs.getBoolean("returned")) {
                    throw new LivreEmpruntedException();
                }
                String query = "update DetailsEmprunts set returned = true where codeunique = ?;";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, codeUnique);
                statement.executeUpdate();
                statement.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setHeaderText(null);
                alert.setContentText("Restitution avec succés");
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/approved.png").toURI().toString()));
                alert.showAndWait();
                this.codeUniqueField.setText("");
            } else {
                throw new LivreNotFoundException();
            }
            rs.close();

        } catch (LivreNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Le Livre avec le code unique = " + codeUnique + " non emprunté ou non trouvé!");
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
            alert.setContentText("Le livre avec le code unique = " + codeUnique + " est déjà restitué!");
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
