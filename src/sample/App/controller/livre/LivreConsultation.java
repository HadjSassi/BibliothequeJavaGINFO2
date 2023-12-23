package sample.App.controller.livre;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.App.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;


public class LivreConsultation implements Initializable {


    @FXML
    private Label nbPolicierLbl;

    @FXML
    private Label nbScFictionLbl;

    @FXML
    private Label nbRomantiqueLbl;


    @FXML
    private TableView<LivreCount> tableView;

    @FXML
    private TableColumn<LivreCount, String> typeCol;
    @FXML
    private TableColumn<LivreCount, String> isbnCol;
    @FXML
    private TableColumn<LivreCount, String> titreCol;
    @FXML
    private TableColumn<LivreCount, String> auteurCol;
    @FXML
    private TableColumn<LivreCount, String> nbLivreCol;
    @FXML
    private TableColumn<LivreCount, String> modifierCol;


    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refresh;


    @FXML
    private CheckBox check_selAll;

    @FXML
    private TextField filterField;


    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private LivreCount livrecount = null;

    private ObservableList<LivreCount> items;
    private ObservableList<LivreCount> oblist;

    private ArrayList<LivreCount> Livres;
    private ArrayList<LivrePolice> policiers;
    private ArrayList<LivreRomantique> romantiques;
    private ArrayList<LivreSciencesFiction> sciencesFictions;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Livres = new ArrayList<>();
        policiers = new ArrayList<>();
        romantiques = new ArrayList<>();
        sciencesFictions = new ArrayList<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initTable();
        loadData();
        filter();
        stats();
    }

    private void stats() {
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery("select count(*) nb from Police");
            while (rs.next()) {
                nbPolicierLbl.setText(rs.getString("nb"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery("select count(*) nb from Romantique ");
            while (rs.next()) {
                nbRomantiqueLbl.setText(rs.getString("nb"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery("select count(*) nb from SciencesFicition ");
            while (rs.next()) {
                nbScFictionLbl.setText(rs.getString("nb"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void filter() {
        FilteredList<LivreCount> filteredData = new FilteredList<>(oblist, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Livre -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Livre.getAuteur().toLowerCase().contains(lowerCaseFilter)) {
                    return true;//filter cin
                } else if (Livre.getTitre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;//filter nom
                } else if (Livre.getCodeUnique().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;//filter nom
                } else return Livre.getISBN().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<LivreCount> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }


    private void initTable() {
        initCols();
    }


    private boolean isPoliciers(Livre livre) {
        for (LivrePolice p : policiers) {
            if (p.getCodeUnique().equals(livre.getCodeUnique()))
                return true;
        }
        return false;
    }

    private boolean isSciencesFictions(Livre livre) {
        for (LivreSciencesFiction p : sciencesFictions) {
            if (p.getCodeUnique().equals(livre.getCodeUnique()))
                return true;
        }
        return false;
    }

    private boolean isRomantique(Livre livre) {
        //todo there is a problem
        for (LivreRomantique p : romantiques) {
            if (p.getCodeUnique().equals(livre.getCodeUnique()))
                return true;
        }
        return false;
    }


    private void initCols() {

        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("ISBN")
        );
        titreCol.setCellValueFactory(
                new PropertyValueFactory<>("titre")
        );
        auteurCol.setCellValueFactory(
                new PropertyValueFactory<>("auteur")
        );
        nbLivreCol.setCellValueFactory(
                new PropertyValueFactory<>("nbLivre")
        );
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );

        Callback<TableColumn<LivreCount, String>, TableCell<LivreCount, String>> cellFoctory = (TableColumn<LivreCount, String> param) -> {
            // make cell containing buttons
            final TableCell<LivreCount, String> cell = new TableCell<LivreCount, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView viewIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        viewIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:linear-gradient(#0288D1 17%, #e7e5e5 100%);"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:linear-gradient(#4E342E 17%, #e7e5e5 100%);"
                        );
                        viewIcon.setOnMouseClicked((MouseEvent event) -> {

                            livrecount = tableView.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../../view/livre/LivreAfficher.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(LivreConsultation.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            LivreAfficher addLivreController = loader.getController();
                            addLivreController.setTextField(livrecount.getISBN(),livrecount.getNbLivre(),livrecount.getType());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.showAndWait();

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            livrecount = tableView.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../../view/livre/LivreUpdate.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(LivreConsultation.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            LivreUpdate addLivreController = loader.getController();
                            addLivreController.setTextField(livrecount.getISBN(),livrecount.getNbLivre(),livrecount.getType());
                            Parent parent = loader.getRoot();

                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.showAndWait();
                            refresh();

                        });

                        HBox managebtn = new HBox(editIcon, viewIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(viewIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };
            return cell;
        };
        modifierCol.setCellFactory(cellFoctory);


    }


    private void loadData() {
        stats();
        if (!Livres.isEmpty())
            oblist.clear();
        oblist = FXCollections.observableArrayList();
        Livres.clear();
        try {
            Connection connection = getOracleConnection();

            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Police");
            while (rs.next()) {
                this.policiers.add(new LivrePolice(rs.getLong("codeunique"), rs.getString("descriptif"), rs.getString("detective"), rs.getString("victime")));
            }
            rs.close();


            rs = connection.createStatement().executeQuery("SELECT * FROM Romantique");
            while (rs.next()) {
                this.romantiques.add(new LivreRomantique(rs.getLong("codeunique"), rs.getString("history"), rs.getString("personnageprincipal")));
            }
            rs.close();


            rs = connection.createStatement().executeQuery("SELECT * FROM SciencesFicition");
            while (rs.next()) {
                this.sciencesFictions.add(new LivreSciencesFiction(rs.getLong("codeunique"), rs.getInt("annee"), rs.getString("espace")));
            }
            rs.close();

            rs = connection.createStatement().executeQuery("SELECT * FROM Livre");
            while (rs.next()) {
                LivreCount livre = new LivreCount(rs.getLong("codeunique"), rs.getString("titre"), rs.getString("auteur"), rs.getLong("isbn"));
                if (isRomantique(livre)) {
                    livre.setType("Romantique");
                } else if (isSciencesFictions(livre)) {
                    livre.setType("SciencesFictions");
                } else if (isPoliciers(livre)) {
                    livre.setType("Policiers");
                } else {
                    livre.setType("simple");
                }
                Livres.add(livre);
            }
            rs.close();


            rs = connection.createStatement().executeQuery(
                    "select titre,auteur,isbn,count(codeunique) as nb ,MIN(codeunique) AS codeunique from Livre group by titre, auteur, isbn;"
            );
            while (rs.next()) {
                LivreCount livre = new LivreCount(rs.getLong("codeunique"), rs.getString("titre"), rs.getString("auteur"), rs.getLong("isbn"));
                if (isRomantique(livre)) {
                    livre.setType("Romantique");
                } else if (isSciencesFictions(livre)) {
                    livre.setType("SciencesFictions");
                } else if (isPoliciers(livre)) {
                    livre.setType("Policiers");
                } else {
                    livre.setType("simple");
                }
                livre.setNbLivre(rs.getInt("nb"));
                oblist.add(livre);
            }
            rs.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tableView.setItems(oblist);
    }


    @FXML
    public void ajouter(ActionEvent event) {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/sample/App/view/livre/LivreCreation.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Bibliothéque");
        assert root != null;
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.showAndWait();
        refresh();
    }

    @FXML
    public void refresh(ActionEvent event) {
        loadData();
        filter();
    }

    @FXML
    public void refresh() {
        loadData();
        filter();
    }

    @FXML
    void supprimer(ActionEvent event) throws URISyntaxException {
        String s = "";

        ObservableList<LivreCount> ob = tableView.getSelectionModel().getSelectedItems();
        if (ob.toArray().length != 0) {
            for (LivreCount per : ob) {
                s += per.getISBN() + "[" + per.getNbLivre() + "]" + "///";
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText(null);
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.setContentText("Etes-vous sure de supprimer l'Livre  de n°: ///" + s);
            alert.setGraphic(new ImageView(getClass().getResource("../../../images/delete.png").toURI().toString()));
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    for (LivreCount per : ob) {
                        try {
                            Connection connection = getOracleConnection();


                            query = "select codeunique from Livre where isbn = ?";

                            try (PreparedStatement statement = connection.prepareStatement(query)) {
                                statement.setLong(1, per.getISBN());
                                try (ResultSet rs = statement.executeQuery()) {
                                    while (rs.next()) {
                                        switch (per.getType()) {
                                            case "Romantique":
                                                query = "delete from Romantique where codeunique = ?";
                                                try (PreparedStatement statement2 = connection.prepareStatement(query)) {
                                                    statement2.setLong(1, rs.getLong("codeunique"));
                                                    statement2.executeUpdate();
                                                }
                                                break;
                                            case "SciencesFictions":

                                                query = "delete from SciencesFicition where codeunique = ?";
                                                try (PreparedStatement statement2 = connection.prepareStatement(query)) {
                                                    statement2.setLong(1, rs.getLong("codeunique"));
                                                    statement2.executeUpdate();
                                                }
                                                break;
                                            case "Policiers":

                                                query = "delete from Police where codeunique = ?";
                                                try (PreparedStatement statement2 = connection.prepareStatement(query)) {
                                                    statement2.setLong(1, rs.getLong("codeunique"));
                                                    statement2.executeUpdate();
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                            query = "delete from Livre where isbn = ?";
                            try (PreparedStatement statement = connection.prepareStatement(query)) {
                                statement.setString(1, String.valueOf(per.getISBN()));
                                statement.executeUpdate();

                                connection.close();
                            }

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }

                }
            });
        }
        refresh();
    }


}

