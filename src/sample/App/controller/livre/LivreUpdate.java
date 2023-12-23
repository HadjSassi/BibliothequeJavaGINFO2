package sample.App.controller.livre;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.App.model.LivreCount;
import sample.App.model.Type;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;


public class LivreUpdate implements Initializable {

    private String titred;
    private String authored;
    private String victimed;
    private String detectived;
    private String descriptifed;
    private String personed;
    private String historied;
    private int anneed;
    private String espaced;


    @FXML
    private Label typelbl;
    @FXML
    private Label nbLbl;

    @FXML
    private Label isbnlbl;

    @FXML
    private Label titrelbl;

    @FXML
    private Label auteurlbl;

    @FXML
    private TextField titrefield;

    @FXML
    private TextField auteurfield;

    @FXML
    private Label detectivelbl;

    @FXML
    private TextField detectivefield;

    @FXML
    private TextField nbField;

    @FXML
    private Label victimlbl;

    @FXML
    private TextField victimefield;

    @FXML
    private TextField descriptiffield;

    @FXML
    private TextField historyfield;

    @FXML
    private Label personnageprincipallbl;

    @FXML
    private TextField personnageprincipalfield;

    @FXML
    private Label anneelbl;

    @FXML
    private TextField anneefield;

    @FXML
    private Label espacelbl;

    @FXML
    private TextField Espacefield;


    @FXML
    private Button buttonConfirmer;

    @FXML
    private Button buttonFermer;

    @FXML
    private HBox detectiveHbox;

    @FXML
    private HBox victimeHbox;

    @FXML
    private HBox descriptionHbox;

    @FXML
    private HBox historyHbox;

    @FXML
    private HBox personnageHbox;

    @FXML
    private HBox anneeHbox;

    @FXML
    private HBox espaceHbox;

    String type;

    private boolean titre = true;
    private boolean nbLivre = true;
    private boolean auteur = true;
    private boolean isItYear = true;

    public static boolean isInt(String string) {
        String regex = "\\d+";
        return string.matches(regex);
    }

    public static boolean isYear(String string) {
        String regex = "^(?:[0-9]+|B\\.C\\.|[0-9]+ B\\.C\\.)$\n";
        return string.matches(regex);
    }

    @FXML
    void verifNom(KeyEvent event) {
        String mat = titrefield.getText();
        if (!mat.isEmpty()) {
            titrefield.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            titrelbl.setText("✓");
            titre = true;
            titrelbl.setStyle("-fx-text-fill: #32CD32");
        } else {
            titrefield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            titrelbl.setStyle("-fx-text-fill: red");
            titrelbl.setText("X Remplir ce champ");
            titre = false;
        }
    }

    @FXML
    void verifAuteur(KeyEvent event) {
        String mat = auteurfield.getText();
        if (!mat.isEmpty()) {
            auteurfield.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            auteurlbl.setText("✓");
            auteur = true;
            auteurlbl.setStyle("-fx-text-fill: #32CD32");
        } else {
            auteurfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            auteurlbl.setStyle("-fx-text-fill: red");
            auteurlbl.setText("X Remplir ce champ");
            auteur = false;
        }
    }

    @FXML
    void verifYear(KeyEvent event) {
        if (!isYear(anneefield.getText())) {
            isItYear = false;
            anneefield.setStyle("-fx-text-fill: red");
            anneefield.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
        } else {
            isItYear = true;
            anneefield.setStyle("-fx-background-color:white;");
        }
    }

    @FXML
    public void verifNb(KeyEvent keyEvent) {
        String mat = nbField.getText();
        if (isInt(mat)) {
            nbField.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            nbLivre = true;
        } else {
            nbField.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            nbLivre = false;
        }
    }

    @FXML
    void confirmerButton(ActionEvent event) throws URISyntaxException {
        try {
            String titres = titrefield.getText();
            String auteurs = auteurfield.getText();
            long isbn = Long.parseLong(isbnlbl.getText());

            if (!titre || !auteur || !nbLivre) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.TRANSPARENT);
                alert.setHeaderText(null);
                alert.setContentText("Un des champs n'est pas correctement inserer");
                alert.setGraphic(new ImageView(getClass().getResource("../../../images/errorinsert.png").toURI().toString()));
                alert.showAndWait();

            } else {
                Connection connection = null;
                try {
                    connection = getOracleConnection();
                    try {
                        String query = "update Livre set titre = ?, auteur = ? where isbn = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setString(1, titres);
                        statement.setString(2, auteurs);
                        statement.setLong(3, isbn);
                        statement.executeUpdate();

                        String lecteurQuery = "SELECT codeunique FROM Livre WHERE isbn = ?";
                        try (PreparedStatement lecteurStatement = connection.prepareStatement(lecteurQuery)) {
                            lecteurStatement.setLong(1, isbn);
                            try (ResultSet rs = lecteurStatement.executeQuery()) {
                                while (rs.next()) {
                                    if (type.equals("Policiers")) {
                                        String detective = detectivefield.getText();
                                        String victime = victimefield.getText();
                                        String descriptif = descriptiffield.getText();
                                        query = "update Police set detective = ?, victime = ?, descriptif = ? where codeunique = ?";
                                        statement = connection.prepareStatement(query);

                                        statement.setString(1, detective);
                                        statement.setString(2, victime);
                                        statement.setString(3, descriptif);
                                        statement.setLong(4, rs.getLong("codeunique"));
                                        statement.executeUpdate();
                                    }

                                    if (type.equals("Romantique")) {
                                        String history = historyfield.getText();
                                        String personnage = personnageprincipalfield.getText();
                                        query = "update Romantique set personnageprincipal = ?, history = ? where codeunique = ?";
                                        statement = connection.prepareStatement(query);
                                        statement.setString(1, personnage);
                                        statement.setString(2, history);
                                        statement.setLong(3, rs.getLong("codeunique"));
                                        statement.executeUpdate();
                                    }

                                    if (type.equals("SciencesFictions")) {
                                        String annee = anneefield.getText();
                                        String espace = Espacefield.getText();
                                        query = "update SciencesFicition set espace = ? , annee = ? where codeunique = ?";
                                        statement = connection.prepareStatement(query);

                                        statement.setString(1, espace);
                                        statement.setString(2, annee);
                                        statement.setLong(3, rs.getLong("codeunique"));
                                        statement.executeUpdate();
                                    }

                                }
                            }
                        }

                        statement.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initStyle(StageStyle.TRANSPARENT);
                        alert.setHeaderText(null);
                        alert.setContentText("Modification avec succés");
                        alert.setGraphic(new ImageView(getClass().getResource("../../../images/approved2.png").toURI().toString()));
                        alert.showAndWait();
                        Stage stage = (Stage) buttonConfirmer.getScene().getWindow();
                        stage.close();
                    } catch (NumberFormatException e) {

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
    void plusButton(ActionEvent event) throws URISyntaxException {
        try {
            int nbe = Integer.parseInt(nbField.getText());
            Connection connection = null;
            for (int i = 0; i < nbe; i++) {
                try {
                    connection = getOracleConnection();
                    try {
                        String query = "insert into Livre values (null,?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, titred);
                        statement.setString(2, authored);
                        statement.setLong(3, Long.parseLong(isbnlbl.getText()));
                        statement.executeUpdate();
                        // Retrieve the generated key
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        int codeunique = -1;
                        if (generatedKeys.next()) {
                            codeunique = generatedKeys.getInt(1);
                        }

                        if (type.equals("Policiers")) {
                            String lecteurQuery = "INSERT INTO Police  VALUES (?, ?, ?, ?)";
                            statement = connection.prepareStatement(lecteurQuery);
                            statement.setInt(1, codeunique);
                            statement.setString(2, descriptifed);
                            statement.setString(3, detectived);
                            statement.setString(4, victimed);
                            statement.executeUpdate();
                        }

                        if (type.equals("Romantique")) {
                            String lecteurQuery = "INSERT INTO Romantique  VALUES (?, ?, ?)";
                            statement = connection.prepareStatement(lecteurQuery);

                            statement.setInt(1, codeunique);
                            statement.setString(2, historied);
                            statement.setString(3, personed);
                            statement.executeUpdate();
                        }

                        if (type.equals("SciencesFictions")) {
                            String lecteurQuery = "INSERT INTO SciencesFicition  VALUES (?, ?, ?)";
                            statement = connection.prepareStatement(lecteurQuery);

                            statement.setInt(1, codeunique);
                            statement.setInt(2, anneed);
                            statement.setString(3, espaced);
                            statement.executeUpdate();
                        }


                        generatedKeys.close();
                        statement.close();

                    } catch (NumberFormatException e) {

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Ajout avec succés");
            alert.setGraphic(new ImageView(getClass().getResource("../../../images/approved.png").toURI().toString()));
            alert.showAndWait();
            Stage stage = (Stage) buttonConfirmer.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setHeaderText(null);
            alert.setContentText("Saisi erroné");
            alert.setGraphic(new ImageView(getClass().getResource("../../../images/nooo.png").toURI().toString()));
            alert.showAndWait();
        }
    }

    public void setTextField(Long isbn, int nb, String type) {
        String query;
        this.type = type;
        this.nbLbl.setText("[" + nb + "]");
        this.typelbl.setText(this.typelbl.getText() + " " + type);
        this.isbnlbl.setText(isbn.toString());
        long codeUnique = 0;
        try (Connection connection = getOracleConnection()) {
            String lecteurQuery = "SELECT * FROM Livre WHERE isbn = ?";
            try (PreparedStatement lecteurStatement = connection.prepareStatement(lecteurQuery)) {
                lecteurStatement.setLong(1, isbn);
                try (ResultSet rs = lecteurStatement.executeQuery()) {
                    if (rs.next()) {
                        titrefield.setText(rs.getString("titre"));
                        this.titred = rs.getString("titre");
                        auteurfield.setText(rs.getString("auteur"));
                        this.authored = rs.getString("auteur");
                        codeUnique = rs.getLong("codeunique");
                    }
                }
            }

            switch (type) {
                case "Romantique":
                    typelbl.setText("Mise à Jour Romantique");
                    detectiveHbox.setVisible(false);
                    victimeHbox.setVisible(false);
                    anneeHbox.setVisible(false);
                    espaceHbox.setVisible(false);
                    personnageHbox.setVisible(true);
                    query = "SELECT * FROM Romantique WHERE codeunique = ? ";
                    try (PreparedStatement lecteurStatement = connection.prepareStatement(query)) {
                        lecteurStatement.setLong(1, codeUnique);
                        try (ResultSet rs = lecteurStatement.executeQuery()) {
                            if (rs.next()) {
                                personnageprincipalfield.setText(rs.getString("personnageprincipal"));
                                this.personed = rs.getString("personnageprincipal");
                                historyfield.setText(rs.getString("history"));
                                this.historied = rs.getString("history");
                            }
                        }
                    }
                    break;
                case "SciencesFictions":
                    typelbl.setText("Mise à Jour Sciences Fictions");
                    detectiveHbox.setVisible(false);
                    victimeHbox.setVisible(false);
                    anneeHbox.setVisible(true);
                    espaceHbox.setVisible(true);
                    personnageHbox.setVisible(false);
                    query = "SELECT * FROM SciencesFicition WHERE codeunique = ? ";
                    try (PreparedStatement lecteurStatement = connection.prepareStatement(query)) {
                        lecteurStatement.setLong(1, codeUnique);
                        try (ResultSet rs = lecteurStatement.executeQuery()) {
                            if (rs.next()) {
                                anneefield.setText(String.valueOf(rs.getInt("annee")));
                                this.anneed = rs.getInt("annee");
                                Espacefield.setText(rs.getString("espace"));
                                this.espaced = rs.getString("espace");
                            }
                        }
                    }
                    break;
                case "Policiers":
                    typelbl.setText("Mise à Jour Policier");
                    detectiveHbox.setVisible(true);
                    victimeHbox.setVisible(true);
                    anneeHbox.setVisible(false);
                    espaceHbox.setVisible(false);
                    personnageHbox.setVisible(false);
                    query = "SELECT * FROM Police WHERE codeunique = ? ";
                    try (PreparedStatement lecteurStatement = connection.prepareStatement(query)) {
                        lecteurStatement.setLong(1, codeUnique);
                        try (ResultSet rs = lecteurStatement.executeQuery()) {
                            if (rs.next()) {
                                victimefield.setText(String.valueOf(rs.getString("victime")));
                                this.victimed = rs.getString("victime");
                                detectivefield.setText(rs.getString("detective"));
                                this.detectived = rs.getString("detective");
                                descriptiffield.setText(rs.getString("descriptif"));
                                this.descriptifed = rs.getString("descriptif");
                            }
                        }
                    }
                    break;
                default:
                    detectiveHbox.setVisible(false);
                    victimeHbox.setVisible(false);
                    anneeHbox.setVisible(false);
                    espaceHbox.setVisible(false);
                    personnageHbox.setVisible(false);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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


}