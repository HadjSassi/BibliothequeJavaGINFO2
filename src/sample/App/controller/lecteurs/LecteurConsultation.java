package sample.App.controller.lecteurs;

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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.mysql_connection.MySqlConnection.getOracleConnection;


public class LecteurConsultation implements Initializable {


    @FXML
    private Label labelNbLecteurs;

    @FXML
    private Label labelNbClientFidele;

    @FXML
    private Label labelNbAbonnementEpuises;


    @FXML
    private TableView<Lecteur> tableView;

    @FXML
    private TableColumn<Lecteur, String> fidelCol;
    @FXML
    private TableColumn<Lecteur, String> cinCol;
    @FXML
    private TableColumn<Lecteur, String> nomCol;
    @FXML
    private TableColumn<Lecteur, String> prenomCol;
    @FXML
    private TableColumn<Lecteur, String> creditCol;
    @FXML
    private TableColumn<Lecteur, String> modifierCol;


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
    private Lecteur Lecteur = null;

    private ObservableList<Lecteur> items;
    private ObservableList<Lecteur> oblist;
    public ArrayList<Lecteur> lecteurs;
    public ArrayList<Lecteur> lecteursFidele;

    public ArrayList<Lecteur> Abonnements_epuises() {
        ArrayList<Lecteur> lista = new ArrayList<>();
        for (Lecteur l : lecteurs)
            if (l.getAbonnement().getCreationDate().plusYears(1).isAfter(LocalDate.now()))
                lista.add(l);
        return lista;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lecteurs = new ArrayList<>();
        lecteursFidele = new ArrayList<>();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initTable();
        loadData();
        filter();
        stats();
    }

    private void stats() {
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery("select count(*) nb from Lecteur");
            while (rs.next()) {
                labelNbLecteurs.setText(rs.getString("nb"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery("select count(*) nb from ClientFidele ");
            while (rs.next()) {
                labelNbClientFidele.setText(rs.getString("nb"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        try {

            labelNbAbonnementEpuises.setText(String.valueOf(Abonnements_epuises().size()));

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }


    private void filter() {
        FilteredList<Lecteur> filteredData = new FilteredList<>(oblist, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Lecteur -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (Lecteur.getCin().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;//filter cin
                } else //doesn't match
                    if (Lecteur.getNom().toLowerCase().contains(lowerCaseFilter)) {
                        return true;//filter nom
                    } else return Lecteur.getPrenom().toLowerCase().contains(lowerCaseFilter);//filter prenom
            });
        });
        SortedList<Lecteur> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }


    private void initTable() {
        initCols();
    }


    private void initCols() {

        cinCol.setCellValueFactory(
                new PropertyValueFactory<>("cin")
        );
        nomCol.setCellValueFactory(
                new PropertyValueFactory<>("nom")
        );
        prenomCol.setCellValueFactory(
                new PropertyValueFactory<>("prenom")
        );
        creditCol.setCellValueFactory(
                new PropertyValueFactory<>("credit")
        );

        //add cell of button edit
        Callback<TableColumn<Lecteur, String>, TableCell<Lecteur, String>> cellFoctory = (TableColumn<Lecteur, String> param) -> {
            // make cell containing buttons
            final TableCell<Lecteur, String> cell = new TableCell<Lecteur, String>() {
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

                            Lecteur = tableView.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../../view/lecteur/LecteurAfficher.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(LecteurConsultation.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            LecteurAfficher addLecteurController = loader.getController();
                            addLecteurController.setTextField(Lecteur.getCin().toString());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.showAndWait();

                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {

                            Lecteur = tableView.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../../view/lecteur/LecteurUpdate.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(LecteurConsultation.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            LecteurUpdate addLecteurController = loader.getController();
                            addLecteurController.setTextField(Lecteur.getCin().toString());
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


        //add cell of button edit
        Callback<TableColumn<Lecteur, String>, TableCell<Lecteur, String>> cellFoctory2 = (TableColumn<Lecteur, String> param) -> {
            // make cell containing buttons
            final TableCell<Lecteur, String> cell2 = new TableCell<Lecteur, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        if (lecteursFidele.contains(getTableView().getItems().get(getIndex())))
                            setText("Client Fidele");
                        else
                            setText("Client Simple");

                    }
                }

            };
            return cell2;
        };
        fidelCol.setCellFactory(cellFoctory2);


    }


    private void loadData() {
        stats();
        if (!lecteurs.isEmpty())
            oblist.clear();
        oblist = FXCollections.observableArrayList();
        lecteurs.clear();
        try {
            Connection connection = getOracleConnection();
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT Lecteur.*, Abonnement.creationDate " +
                            "FROM Lecteur " +
                            "LEFT JOIN Abonnement ON Abonnement.idAbonnement = Lecteur.idAbonnement"
            );
            while (rs.next()) {
                int cin = rs.getInt("cin");
                oblist.add(new Lecteur(cin, rs.getString("nom"), rs.getString("prenom"), rs.getDouble("credit")));
                LocalDate creationDate = rs.getDate("creationDate") != null ? rs.getDate("creationDate").toLocalDate() : null;
                Abonnement ab = new Abonnement(rs.getInt("idAbonnement"), creationDate);
                lecteurs.add(new Lecteur(cin, rs.getString("nom"), rs.getString("prenom"), ab, rs.getDouble("credit")));
            }
            rs.close();
            rs = connection.createStatement().executeQuery("SELECT * FROM ClientFidele");
            while (rs.next()) {
                this.lecteursFidele.add(new ClientFidele(rs.getInt("cin"), rs.getString("email"), rs.getString("preferences")));
            }
            rs.close();
        } catch (SQLException | CreditNegatifException throwables) {
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
            root = FXMLLoader.load(getClass().getResource("/sample/App/view/lecteur/LecteurCreation.fxml"));
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

        ObservableList<Lecteur> ob = tableView.getSelectionModel().getSelectedItems();
        if (ob.toArray().length != 0) {
            for (Lecteur per : ob) {
                s += per.getCin() + "///";
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText(null);
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.setContentText("Etes-vous sure de supprimer l'Lecteur  de n°: ///" + s);
            alert.setGraphic(new ImageView(getClass().getResource("../../../images/delete.png").toURI().toString()));
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    for (Lecteur per : ob) {
                        try {
                            Connection connection = getOracleConnection();


                            String fideleQuery = "SELECT * FROM ClientFidele WHERE cin = ?";
                            try (PreparedStatement fideleStatement = connection.prepareStatement(fideleQuery)) {
                                fideleStatement.setString(1, String.valueOf(per.getCin()));
                                try (ResultSet rsFidele = fideleStatement.executeQuery()) {
                                    if(rsFidele.next()){
                                        query = "DELETE FROM ClientFidele WHERE cin = ?";
                                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                            preparedStatement.setLong(1, per.getCin());
                                            preparedStatement.executeUpdate();
                                        }
                                    }
                                }
                            }



                            query = "SELECT * FROM Lecteur WHERE cin = ?";
                            try (PreparedStatement fideleStatement = connection.prepareStatement(query)) {
                                fideleStatement.setString(1, String.valueOf(per.getCin()));
                                try (ResultSet resultSet = fideleStatement.executeQuery()) {
                                    if(resultSet.next()){
                                        int abonid = resultSet.getInt("idAbonnement");
                                        query = "DELETE FROM Lecteur WHERE cin = ?";

                                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                            preparedStatement.setLong(1, per.getCin());
                                            preparedStatement.executeUpdate();
                                        }
                                        query = "DELETE FROM Abonnement WHERE idAbonnement = ?";

                                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                                            preparedStatement.setInt(1,abonid);
                                            preparedStatement.executeUpdate();
                                        }
                                    }
                                }
                            }

                            connection.close();
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