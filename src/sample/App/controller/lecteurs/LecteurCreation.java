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
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import sample.App.model.Type;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;


public class LecteurCreation implements Initializable {

    @FXML
    private Label cinlbl;

    @FXML
    private Label nomlbl;

    @FXML
    private Label prenomlbl;

    @FXML
    private Label creditlbl;

    @FXML
    private TextField cinfield;

    @FXML
    private TextField nomfield;

    @FXML
    private TextField prenomfield;

    @FXML
    private TextField creditfield;

    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonFermer;


    private boolean cin = false;
    private boolean nom = false;
    private boolean prenom = false;
    private boolean credit = false;

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
            return false;
        }
    }

    public static boolean isInt(String string) {
        String regex = "\\d+";
        return  string.matches(regex);
    }


    @FXML
    void verifCin(KeyEvent event) {
        String mat = cinfield.getText();
        if (mat.isEmpty()) {
            cinfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            cinlbl.setStyle("-fx-text-fill: red");
            cinlbl.setText("X Remplir ce champ");
            cin = false;
        } else if (!isInt(mat)) {
            cinfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            cinlbl.setStyle("-fx-text-fill: red");
            cinlbl.setText("X Saisir Correctement le numéro CIN");
            cin = false;
        } else {
            cinfield.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            cinlbl.setText("✓");
            cin = true;
            cinlbl.setStyle("-fx-text-fill: #32CD32");
        }
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
    void confirmerButton(ActionEvent event) throws URISyntaxException {
        try {
            String cine = cinfield.getText();
            String nome = nomfield.getText();
            String prenome = prenomfield.getText();
            String credite = creditfield.getText();
            if (!cin || !nom || !prenom || !credit) {
                if (!cin) {
                    cinlbl.setText("X Saisir le numéro CIN");
                    cinlbl.setStyle("-fx-text-fill: red");
                    cinfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
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
                        String abon = LocalDate.now().getYear()+"-"+LocalDate.now().getDayOfMonth()+"-"+LocalDate.now().getMonthValue();
                        String query = "insert into Abonnement values (null, ?);";
                        PreparedStatement abonnementStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        abonnementStatement.setDate(1, Date.valueOf(LocalDate.now()));
                        abonnementStatement.executeUpdate();
                        // Retrieve the generated key
                        ResultSet generatedKeys = abonnementStatement.getGeneratedKeys();
                        int idAbonnement = -1;
                        if (generatedKeys.next()) {
                            idAbonnement = generatedKeys.getInt(1);
                        }

                        String lecteurQuery = "INSERT INTO Lecteur  VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement lecteurStatement = connection.prepareStatement(lecteurQuery);

                        lecteurStatement.setInt(1, Integer.parseInt(cine));
                        lecteurStatement.setString(2, nome);
                        lecteurStatement.setString(3, prenome);
                        lecteurStatement.setDouble(4, Double.parseDouble(credite));
                        lecteurStatement.setInt(5, idAbonnement);

                        lecteurStatement.executeUpdate();

                        generatedKeys.close();
                        abonnementStatement.close();
                        lecteurStatement.close();

                        refresh();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.TRANSPARENT);
                        alert.setHeaderText(null);
                        alert.setContentText("Ajout avec succés");
                        alert.setGraphic(new ImageView(getClass().getResource("../../../images/approved.png").toURI().toString()));
                        alert.showAndWait();
                    } catch (NumberFormatException e) {
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
                } catch (SQLException | URISyntaxException throwables) {
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
        } catch (NullPointerException e) {
            //System.out.println("you have an error go check please mr Mahdi");
            e.printStackTrace();
        }
    }


    private void refresh() {
        cinfield.setText("");
        nomfield.setText("");
        prenomfield.setText("");
        creditfield.setText("");
        cinlbl.setText("");
        nomlbl.setText("");
        prenomlbl.setText("");
        creditlbl.setText("");
        cinfield.setStyle("-fx-background-color:white;");
        nomfield.setStyle("-fx-background-color:white;");
        prenomfield.setStyle("-fx-background-color:white;");
        creditfield.setStyle("-fx-background-color:white;");
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
    }


    @FXML
    public void verifDispo(ActionEvent event) {

    }
}