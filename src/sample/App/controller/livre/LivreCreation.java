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

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;


public class LivreCreation implements Initializable {

    @FXML
    private Label isbnlbl;

    @FXML
    private Label titrelbl;

    @FXML
    private Label auteurlbl;

    @FXML
    private TextField isbnfield;

    @FXML
    private TextField titrefield;

    @FXML
    private TextField auteurfield;

    @FXML
    private RadioButton policierbtn;
    @FXML
    private RadioButton romantiquebtn;
    @FXML
    private RadioButton scfictionbtn;
    @FXML
    private RadioButton autrebtn;

    @FXML
    private Label detectivelbl;
    @FXML
    private TextField detectivefield;

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

    private ArrayList<String> listeISBN ;

    private boolean isItYear;
    private boolean isbn;
    private boolean titre;
    private boolean auteur;

    public static boolean isInt(String string) {
        String regex = "\\d+";
        return string.matches(regex);
    }

    public static boolean isYear(String string) {
        String regex = "^(?:[0-9]+|B\\.C\\.|[0-9]+ B\\.C\\.)$\n";
        return string.matches(regex);
    }


    @FXML
    void verifIsbn(KeyEvent event) {
        String mat = isbnfield.getText();
        if (mat.isEmpty()) {
            isbnfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            isbnlbl.setStyle("-fx-text-fill: red");
            isbnlbl.setText("X Remplir ce champ");
            isbn = false;
        } else if (!isInt(mat)) {
            isbnfield.setStyle("-fx-text-box-border: red;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            isbnlbl.setStyle("-fx-text-fill: red");
            isbnlbl.setText("X Saisir Correctement le numéro ISBN");
            isbn = false;
        } else if (listeISBN.contains(mat)) {
            isbnfield.setStyle("-fx-text-box-border: orange;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            isbnlbl.setStyle("-fx-text-fill: orange");
            isbnfield.setStyle("-fx-background-color: orange,linear-gradient(to bottom, derive(orange,60%) 5%,derive(orange,90%) 40%);");
            isbnlbl.setText("! ISBN déjà existe !");
            isbn = true;
        } else {
            isbnfield.setStyle("-fx-text-box-border: #32CD32;  -fx-border-width: 2px  ;-fx-background-insets: 0, 0 0 3 0 ; -fx-background-radius: 0.7em ;");
            isbnlbl.setText("✓");
            isbn = true;
            isbnlbl.setStyle("-fx-text-fill: #32CD32");
        }
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
    void policier(ActionEvent event) {
        romantiquebtn.setSelected(false);
        scfictionbtn.setSelected(false);
        autrebtn.setSelected(false);
        detectivelbl.setVisible(true);
        detectivefield.setVisible(true);
        victimlbl.setVisible(true);
        victimefield.setVisible(true);
        descriptiffield.setVisible(true);
        historyfield.setVisible(false);
        personnageprincipallbl.setVisible(false);
        personnageprincipalfield.setVisible(false);
        anneelbl.setVisible(false);
        anneefield.setVisible(false);
        espacelbl.setVisible(false);
        Espacefield.setVisible(false);
        victimeHbox.toFront();// todo you need to find how to let the fields accessible
        descriptionHbox.toFront();
        detectiveHbox.toFront();
    }

    @FXML
    void romantique(ActionEvent event) {
        policierbtn.setSelected(false);
        scfictionbtn.setSelected(false);
        autrebtn.setSelected(false);
        detectivelbl.setVisible(false);
        detectivefield.setVisible(false);
        victimlbl.setVisible(false);
        victimefield.setVisible(false);
        descriptiffield.setVisible(false);
        historyfield.setVisible(true);
        personnageprincipallbl.setVisible(true);
        personnageprincipalfield.setVisible(true);
        anneelbl.setVisible(false);
        anneefield.setVisible(false);
        espacelbl.setVisible(false);
        Espacefield.setVisible(false);
        personnageHbox.toFront();
        historyHbox.toFront();
    }

    @FXML
    void sciencesFictions(ActionEvent event) {
        policierbtn.setSelected(false);
        romantiquebtn.setSelected(false);
        autrebtn.setSelected(false);
        detectivelbl.setVisible(false);
        detectivefield.setVisible(false);
        victimlbl.setVisible(false);
        victimefield.setVisible(false);
        descriptiffield.setVisible(false);
        historyfield.setVisible(false);
        personnageprincipallbl.setVisible(false);
        personnageprincipalfield.setVisible(false);
        anneelbl.setVisible(true);
        anneefield.setVisible(true);
        espacelbl.setVisible(true);
        Espacefield.setVisible(true);
        anneeHbox.toFront();
        espaceHbox.toFront();
    }

    @FXML
    void autre(ActionEvent event) {
        policierbtn.setSelected(false);
        scfictionbtn.setSelected(false);
        romantiquebtn.setSelected(false);
        detectivelbl.setVisible(false);
        detectivefield.setVisible(false);
        victimlbl.setVisible(false);
        victimefield.setVisible(false);
        descriptiffield.setVisible(false);
        historyfield.setVisible(false);
        personnageprincipallbl.setVisible(false);
        personnageprincipalfield.setVisible(false);
        anneelbl.setVisible(false);
        anneefield.setVisible(false);
        espacelbl.setVisible(false);
        Espacefield.setVisible(false);
    }

    @FXML
    void confirmerButton(ActionEvent event) throws URISyntaxException {
        try {
            String isbns = isbnfield.getText();
            String titres = titrefield.getText();
            String auteurs = auteurfield.getText();

            if (!isbn || !titre || !auteur) {

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
                        String query = "insert into Livre values (null,?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, titres);
                        statement.setString(2, auteurs);
                        statement.setString(3, isbns);
                        statement.executeUpdate();
                        // Retrieve the generated key
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        int codeunique = -1;
                        if (generatedKeys.next()) {
                            codeunique = generatedKeys.getInt(1);
                        }

                        if (policierbtn.isSelected()) {
                            String detective = detectivefield.getText();
                            String victime = victimefield.getText();
                            String descriptif = descriptiffield.getText();
                            String lecteurQuery = "INSERT INTO Police  VALUES (?, ?, ?, ?)";
                            statement = connection.prepareStatement(lecteurQuery);

                            statement.setInt(1, codeunique);
                            statement.setString(2, descriptif);
                            statement.setString(3, detective);
                            statement.setString(4, victime);
                            statement.executeUpdate();
                        }

                        if (romantiquebtn.isSelected()) {
                            String history = historyfield.getText();
                            String personnage = personnageprincipalfield.getText();
                            String lecteurQuery = "INSERT INTO Romantique  VALUES (?, ?, ?)";
                            statement = connection.prepareStatement(lecteurQuery);

                            statement.setInt(1, codeunique);
                            statement.setString(2, history);
                            statement.setString(3, personnage);
                            statement.executeUpdate();
                        }

                        if (scfictionbtn.isSelected()) {
                            String annee = anneefield.getText();
                            String espace = Espacefield.getText();
                            String lecteurQuery = "INSERT INTO SciencesFicition  VALUES (?, ?, ?)";
                            statement = connection.prepareStatement(lecteurQuery);

                            statement.setInt(1, codeunique);
                            statement.setString(2, annee);
                            statement.setString(3, espace);
                            statement.executeUpdate();
                        }


                        generatedKeys.close();
                        statement.close();


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
        isbnfield.setText("");
        titrefield.setText("");
        auteurfield.setText("");
        descriptiffield.setText("");
        victimefield.setText("");
        detectivefield.setText("");
        historyfield.setText("");
        anneefield.setText("");
        personnageprincipalfield.setText("");
        Espacefield.setText("");
        isbnfield.setStyle("-fx-background-color:white;");
        titrefield.setStyle("-fx-background-color:white;");
        auteurfield.setStyle("-fx-background-color:white;");
        descriptiffield.setStyle("-fx-background-color:white;");
        victimefield.setStyle("-fx-background-color:white;");
        detectivefield.setStyle("-fx-background-color:white;");
        historyfield.setStyle("-fx-background-color:white;");
        anneefield.setStyle("-fx-background-color:white;");
        personnageprincipalfield.setStyle("-fx-background-color:white;");
        Espacefield.setStyle("-fx-background-color:white;");
        isbnlbl.setText("");
        titrelbl.setText("");
        auteurlbl.setText("");
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
        listeISBN = new ArrayList<>();
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery(
                    "select isbn from Livre"
            );
            while (rs.next()) {
                listeISBN.add(String.valueOf(rs.getLong("isbn")));
            }
            rs.close();
            ObservableList list1 = FXCollections.observableArrayList();
            list1.removeAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}