package Lab03SRC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.*;

public class Controller {

    @FXML private TableView<Location> table1;
    @FXML private TableColumn<Location, Integer> id;
    @FXML private TableColumn<Location, String> name;
    @FXML private TableColumn<Location, String> region;
    @FXML private TableColumn<Location, Double> latitude;
    @FXML private TableColumn<Location, Double> longitude;

    @FXML private TextField textName;
    @FXML private TextField textRegion;
    @FXML private TextField textLatitude;
    @FXML private TextField textLongitude;
    @FXML private TextField textUrl;

    @FXML private Button buttonAdd;
    @FXML private Button buttonUnselect;
    @FXML private Button buttonUpdate;
    @FXML private Button buttonDelete;

    @FXML private ImageView image1;

    private ObservableList<Location> locationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        region.setCellValueFactory(new PropertyValueFactory<>("region"));
        latitude.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitude.setCellValueFactory(new PropertyValueFactory<>("longitude"));

        loadData();

        table1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textName.setText(newSelection.getName());
                textRegion.setText(newSelection.getRegion());
                textLatitude.setText(String.valueOf(newSelection.getLatitude()));
                textLongitude.setText(String.valueOf(newSelection.getLongitude()));
                
                String url = newSelection.getImageUrl();
                if (url != null && !url.trim().isEmpty()) {
                    textUrl.setText(url);
                    try {
                        image1.setImage(new Image(url, true));
                    } catch (Exception e) {
                        image1.setImage(null);
                    }
                } else {
                    textUrl.clear();
                    image1.setImage(null);
                }
            }
        });

        textUrl.textProperty().addListener((obs, oldText, newText) -> {
             if (newText != null && !newText.trim().isEmpty()) {
                 try {
                     image1.setImage(new Image(newText, true));
                 } catch (Exception e) {
                     image1.setImage(null);
                 }
             } else {
                 image1.setImage(null);
             }
        });
    }

    private void loadData() {
        locationList.clear();
        String query = "SELECT * FROM locations";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                locationList.add(new Location(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Region"),
                        rs.getDouble("Latitude"),
                        rs.getDouble("Longitude"),
                        rs.getString("ImageUrl")
                ));
            }
            table1.setItems(locationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonAdd() {
        String query = "INSERT INTO locations (Name, Region, Latitude, Longitude, ImageUrl) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, textName.getText());
            pstmt.setString(2, textRegion.getText());
            pstmt.setDouble(3, Double.parseDouble(textLatitude.getText()));
            pstmt.setDouble(4, Double.parseDouble(textLongitude.getText()));
            pstmt.setString(5, textUrl.getText());
            pstmt.executeUpdate();
            
            loadData();
            handleButtonUnselect();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonUpdate() {
        Location selected = table1.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String query = "UPDATE locations SET Name = ?, Region = ?, Latitude = ?, Longitude = ?, ImageUrl = ? WHERE ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, textName.getText());
            pstmt.setString(2, textRegion.getText());
            pstmt.setDouble(3, Double.parseDouble(textLatitude.getText()));
            pstmt.setDouble(4, Double.parseDouble(textLongitude.getText()));
            pstmt.setString(5, textUrl.getText());
            pstmt.setInt(6, selected.getId());
            pstmt.executeUpdate();
            
            loadData();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonDelete() {
        Location selected = table1.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String query = "DELETE FROM locations WHERE ID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, selected.getId());
            pstmt.executeUpdate();
            
            loadData();
            handleButtonUnselect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonUnselect() {
        table1.getSelectionModel().clearSelection();
        textName.clear();
        textRegion.clear();
        textLatitude.clear();
        textLongitude.clear();
        textUrl.clear();
        image1.setImage(null);
    }
}
