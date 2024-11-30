package com.dija.lecturehomework;

import com.dija.lecturehomework.model.*;
import com.dija.lecturehomework.service.MNBArfolyamServiceSoap;
import com.dija.lecturehomework.service.MNBArfolyamServiceSoapImpl;
import com.oanda.v20.order.*;
import com.oanda.v20.position.PositionCloseRequest;
import com.oanda.v20.position.PositionCloseResponse;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeCloseResponse;
import com.oanda.v20.trade.TradeSpecifier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.util.List;
import javafx.stage.Stage;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.oanda.v20.Context;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountSummary;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import com.oanda.v20.position.PositionGetResponse;

import com.oanda.v20.pricing.PricingGetResponse;



import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TableView<Pizza> pizzaTableView;
    @FXML
    private TableView<ResultJoin> resultJoinTableView; 
    @FXML
    private TableView<Category> categoryTableView; 
    @FXML

    private TableView<Orders> ordersTableView;

    @FXML
    private ComboBox<String> pizzaComboBox; 
    @FXML
    private ComboBox<String> categoryComboBox; 
    @FXML
    private TextField pnameTextField; 
    @FXML
    private TextField cnameTextField; 
    @FXML
    private TextField priceTextField; 
    @FXML
    private Button downloadButton;
    @FXML
    private ComboBox<String> currencyComboBox;
    @FXML
    private TextField startDateTextField;
    @FXML
    private TextField endDateTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private LineChart<String, Number> realTimeChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private XYChart.Series<String, Number> series;
    @FXML
    private TableView<MarketPosition> positionTableView; 
    @FXML
    private TableColumn<MarketPosition, String> currencyColumn; 
    @FXML
    private TableColumn<MarketPosition, Integer> quantityColumn; 
    @FXML
    private TableColumn<MarketPosition, String> statusColumn; 

    
    private Context ctx = new Context(Config.URL, Config.TOKEN);




    // SOAP Cli

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Database Menu
    @FXML
    public void readDatabaseAction(ActionEvent actionEvent) {
        // Cria uma nova janela (Stage)
        Stage stage = new Stage();
        stage.setTitle("Read");

        // Cria um VBox para organizar os componentes
        VBox vbox = new VBox();

        // Cria a TableView para exibir os dados
        TableView<ResultJoin> tableView = new TableView<>();

        // Cria as colunas da TableView
        TableColumn<ResultJoin, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("pname"));

        TableColumn<ResultJoin, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryname"));

        TableColumn<ResultJoin, Boolean> vegetarianCol = new TableColumn<>("Vegetarian");
        vegetarianCol.setCellValueFactory(new PropertyValueFactory<>("vegetarian"));

        TableColumn<ResultJoin, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<ResultJoin, Timestamp> takenCol = new TableColumn<>("Taken");
        takenCol.setCellValueFactory(new PropertyValueFactory<>("taken"));

        // Criação correta da coluna 'Dispatched'
        TableColumn<ResultJoin, Timestamp> dispatchedCol = new TableColumn<>("Dispatched");
        dispatchedCol.setCellValueFactory(new PropertyValueFactory<>("dispatched"));

        // Adiciona todas as colunas na TableView
        tableView.getColumns().addAll(nameCol, categoryCol, vegetarianCol, amountCol, takenCol, dispatchedCol);

        // Carregar dados do banco de dados
        List<ResultJoin> resultJoin = loadPizzasFromDatabase();
        tableView.getItems().setAll(resultJoin);

        // Adiciona a TableView no VBox
        vbox.getChildren().add(tableView);

        // Configura a cena da janela
        Scene scene = new Scene(vbox, 620, 540);
        stage.setScene(scene);
        stage.show();
    }

    private List<ResultJoin> loadPizzasFromDatabase() {
        List<ResultJoin> pizzas = new ArrayList<>();
        String query = "SELECT p.pname, c.cname, p.vegetarian, o.amount, o.taken, o.dispatched " +
                "FROM pizza p " +
                "JOIN category c ON p.categoryname = c.cname " +
                "JOIN orders o ON p.pname = o.pizzaname";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ResultJoin resultJoin = new ResultJoin();
                resultJoin.setPname(rs.getString("pname"));
                resultJoin.setCategoryname(rs.getString("cname"));
                resultJoin.setVegetarian(rs.getBoolean("vegetarian"));
                resultJoin.setAmount(rs.getInt("amount"));
                resultJoin.setTaken(rs.getTimestamp("taken"));
                resultJoin.setDispatched(rs.getTimestamp("dispatched"));
                pizzas.add(resultJoin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pizzas;
    }
    private List<String> getCategoryNamesFromDatabase() {
        List<String> categoryNames = new ArrayList<>();

        String query = "SELECT cname FROM category";  

        try (Connection connection = DatabaseHelper.connect();  // Conecta com o banco
             Statement stmt = connection.createStatement();  // Cria um statement
             ResultSet rs = stmt.executeQuery(query)) {  // Executa a consulta SQL

            while (rs.next()) {
                categoryNames.add(rs.getString("cname"));  // Adiciona o nome da categoria na lista
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Em caso de erro, exibe a stack trace
        }

        return categoryNames;  // Retorna a lista de categorias
    }

    public void readDatabaseFilteredAction(ActionEvent actionEvent) {
        // Cria uma nova janela (Stage)
        Stage stage = new Stage();
        stage.setTitle("Read2 - Filter Data");

        // Cria uma VBox para organizar os componentes
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // Cria a TableView para exibir os dados
        TableView<ResultJoin> tableView = new TableView<>();

        // Cria as colunas da TableView
        TableColumn<ResultJoin, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("pname"));

        TableColumn<ResultJoin, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryname"));

        TableColumn<ResultJoin, Boolean> vegetarianCol = new TableColumn<>("Vegetarian");
        vegetarianCol.setCellValueFactory(new PropertyValueFactory<>("vegetarian"));

        TableColumn<ResultJoin, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<ResultJoin, Timestamp> takenCol = new TableColumn<>("Taken");
        takenCol.setCellValueFactory(new PropertyValueFactory<>("taken"));

        TableColumn<ResultJoin, Timestamp> dispatchedCol = new TableColumn<>("Dispatched");
        dispatchedCol.setCellValueFactory(new PropertyValueFactory<>("dispatched"));

        // Adiciona as colunas à TableView
        tableView.getColumns().addAll(nameCol, categoryCol, vegetarianCol, amountCol, takenCol, dispatchedCol);

        // Formulário de Filtro
        HBox filterHBox = new HBox(15);
        filterHBox.setPadding(new Insets(10));
        filterHBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 10;");

        // Filtros
        VBox filterVBox = new VBox(10);
        filterVBox.setPrefWidth(300);

        // Name Filter
        Label pnameLabel = new Label("Name:");
        TextField pnameTextField = new TextField();
        pnameTextField.setPromptText("Enter pizza name");

        // Category Filter
        Label categoryLabel = new Label("Category:");
        ComboBox<String> pizzaCategoryComboBox = new ComboBox<>();
        pizzaCategoryComboBox.getItems().addAll(getCategoryNamesFromDatabase());

        // Vegetarian Filter
        Label vegetarianLabel = new Label("Vegetarian:");
        ToggleGroup vegetarianGroup = new ToggleGroup();
        RadioButton vegetarianRadioButton = new RadioButton("Yes");
        vegetarianRadioButton.setToggleGroup(vegetarianGroup);
        RadioButton nonVegetarianRadioButton = new RadioButton("No");
        nonVegetarianRadioButton.setToggleGroup(vegetarianGroup);

        // Amount Filter
        Label amountLabel = new Label("Amount:");
        TextField amountTextField = new TextField();

        // Taken Date Filter
        Label takenLabel = new Label("Taken Date:");
        DatePicker takenDatePicker = new DatePicker();

        // Dispatched Date Filter
        Label dispatchedLabel = new Label("Dispatched Date:");
        DatePicker dispatchedDatePicker = new DatePicker();

        // Organiza os campos de filtro no VBox
        filterVBox.getChildren().addAll(pnameLabel, pnameTextField, categoryLabel, pizzaCategoryComboBox, vegetarianLabel,
                vegetarianRadioButton, nonVegetarianRadioButton, amountLabel, amountTextField, takenLabel, takenDatePicker,
                dispatchedLabel, dispatchedDatePicker);

        // Botão para aplicar o filtro
        Button applyFilterButton = new Button("Apply Filter");
        applyFilterButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        applyFilterButton.setOnAction(e -> {
            List<ResultJoin> filteredResults = applyFilter(pnameTextField, pizzaCategoryComboBox, vegetarianGroup,
                    amountTextField, takenDatePicker, dispatchedDatePicker);
            tableView.getItems().setAll(filteredResults);  // Atualiza os resultados na TableView
        });

        // Adiciona o filtro e o botão ao HBox
        filterHBox.getChildren().add(filterVBox);

        // Adiciona o filtro e a TableView à VBox principal
        vbox.getChildren().addAll(filterHBox, tableView, applyFilterButton);

        // Configura a cena da janela
        Scene scene = new Scene(vbox, 620, 540);
        stage.setScene(scene);
        stage.show();
    }
    private List<ResultJoin> applyFilter(TextField pnameTextField, ComboBox<String> pizzaCategoryComboBox,
                                         ToggleGroup vegetarianGroup, TextField amountTextField, DatePicker takenDatePicker, DatePicker dispatchedDatePicker) {
        List<ResultJoin> filteredResults = new ArrayList<>();

        String pname = pnameTextField.getText();
        String category = pizzaCategoryComboBox.getValue();
        Boolean vegetarian = null;

        if (vegetarianGroup.getSelectedToggle() != null) {
            RadioButton selectedRadioButton = (RadioButton) vegetarianGroup.getSelectedToggle();
            vegetarian = selectedRadioButton.getText().equals("Yes");
        }

        Integer amount = amountTextField.getText().isEmpty() ? null : Integer.parseInt(amountTextField.getText());
        Timestamp taken = takenDatePicker.getValue() == null ? null : Timestamp.valueOf(takenDatePicker.getValue().atStartOfDay());
        Timestamp dispatched = dispatchedDatePicker.getValue() == null ? null : Timestamp.valueOf(dispatchedDatePicker.getValue().atStartOfDay());

        // Carregar os dados do banco com o filtro aplicado
        try (Connection connection = DatabaseHelper.connect()) {
            String query = "SELECT p.pname, c.cname, p.vegetarian, o.amount, o.taken, o.dispatched " +
                    "FROM pizza p " +
                    "JOIN category c ON p.categoryname = c.cname " +
                    "JOIN orders o ON p.pname = o.pizzaname WHERE 1=1 ";

            if (pname != null && !pname.isEmpty()) {
                query += "AND p.pname LIKE ? ";
            }
            if (category != null && !category.isEmpty()) {
                query += "AND c.cname = ? ";
            }
            if (vegetarian != null) {
                query += "AND p.vegetarian = ? ";
            }
            if (amount != null) {
                query += "AND o.amount = ? ";
            }
            if (taken != null) {
                query += "AND o.taken = ? ";
            }
            if (dispatched != null) {
                query += "AND o.dispatched = ? ";
            }

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                int index = 1;

                if (pname != null && !pname.isEmpty()) {
                    stmt.setString(index++, "%" + pname + "%");
                }
                if (category != null && !category.isEmpty()) {
                    stmt.setString(index++, category);
                }
                if (vegetarian != null) {
                    stmt.setBoolean(index++, vegetarian);
                }
                if (amount != null) {
                    stmt.setInt(index++, amount);
                }
                if (taken != null) {
                    stmt.setTimestamp(index++, taken);
                }
                if (dispatched != null) {
                    stmt.setTimestamp(index++, dispatched);
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ResultJoin resultJoin = new ResultJoin();
                    resultJoin.setPname(rs.getString("pname"));
                    resultJoin.setCategoryname(rs.getString("cname"));
                    resultJoin.setVegetarian(rs.getBoolean("vegetarian"));
                    resultJoin.setAmount(rs.getInt("amount"));
                    resultJoin.setTaken(rs.getTimestamp("taken"));
                    resultJoin.setDispatched(rs.getTimestamp("dispatched"));
                    filteredResults.add(resultJoin);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return filteredResults;
    }

    // (Write submenu)
    private void insertIntoCategory(TextField cnameTextField, TextField priceTextField) {
        String cname = cnameTextField.getText();
        double price = priceTextField.getText().isEmpty() ? 0.0 : Double.parseDouble(priceTextField.getText());

        try (Connection connection = DatabaseHelper.connect()) {
            String query = "INSERT INTO category (cname, price) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, cname);
                stmt.setDouble(2, price);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Category inserted successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoPizza(TextField pnameTextField, ComboBox<String> categoryComboBox, CheckBox vegetarianCheckBox) {
        String pname = pnameTextField.getText();
        String category = categoryComboBox.getValue();
        boolean vegetarian = vegetarianCheckBox.isSelected();

        try (Connection connection = DatabaseHelper.connect()) {
            String query = "INSERT INTO pizza (pname, categoryname, vegetarian) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, pname);
                stmt.setString(2, category);
                stmt.setBoolean(3, vegetarian);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Pizza inserted successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoOrders(TextField pizzanameTextField, TextField amountTextField, DatePicker dispatchedDatePicker) {
        String pizzaname = pizzanameTextField.getText();
        int amount = amountTextField.getText().isEmpty() ? 0 : Integer.parseInt(amountTextField.getText());
        Timestamp taken = new Timestamp(System.currentTimeMillis());
        Timestamp dispatched = dispatchedDatePicker.getValue() == null ? null : Timestamp.valueOf(dispatchedDatePicker.getValue().atStartOfDay());

        try (Connection connection = DatabaseHelper.connect()) {
            String query = "INSERT INTO orders (pizzaname, amount, taken, dispatched) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, pizzaname);
                stmt.setInt(2, amount);
                stmt.setTimestamp(3, taken);
                stmt.setTimestamp(4, dispatched);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Order inserted successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeDatabaseAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Add New Record");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // ComboBox para selecionar a tabela
        Label tableLabel = new Label("Select Table:");
        ComboBox<String> tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("Category", "Pizza", "Orders");

        // Layouts para cada tabela
        VBox categoryForm = new VBox(10);
        TextField cnameTextField = new TextField();
        TextField priceTextField = new TextField();
        categoryForm.getChildren().addAll(new Label("Category Name:"), cnameTextField, new Label("Price:"), priceTextField);

        VBox pizzaForm = new VBox(10);
        TextField pnameTextField = new TextField();
        ComboBox<String> categoryComboBox = new ComboBox<>(FXCollections.observableArrayList(getCategoryNamesFromDatabase()));
        CheckBox vegetarianCheckBox = new CheckBox("Vegetarian");
        pizzaForm.getChildren().addAll(new Label("Pizza Name:"), pnameTextField, new Label("Category:"), categoryComboBox, vegetarianCheckBox);

        VBox ordersForm = new VBox(10);
        TextField pizzanameTextField = new TextField();
        TextField amountTextField = new TextField();
        DatePicker dispatchedDatePicker = new DatePicker();
        ordersForm.getChildren().addAll(new Label("Pizza Name:"), pizzanameTextField, new Label("Amount:"), amountTextField,
                new Label("Dispatched Date:"), dispatchedDatePicker);

        // Botão de envio
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            if ("Category".equals(selectedTable)) {
                insertIntoCategory(cnameTextField, priceTextField);
            } else if ("Pizza".equals(selectedTable)) {
                insertIntoPizza(pnameTextField, categoryComboBox, vegetarianCheckBox);
            } else if ("Orders".equals(selectedTable)) {
                insertIntoOrders(pizzanameTextField, amountTextField, dispatchedDatePicker);
            }
        });

        // Atualiza o layout com base na seleção
        tableComboBox.setOnAction(e -> {
            vbox.getChildren().remove(1, vbox.getChildren().size() - 1);
            if ("Category".equals(tableComboBox.getValue())) {
                vbox.getChildren().add(categoryForm);
            } else if ("Pizza".equals(tableComboBox.getValue())) {
                vbox.getChildren().add(pizzaForm);
            } else if ("Orders".equals(tableComboBox.getValue())) {
                vbox.getChildren().add(ordersForm);
            }
            vbox.getChildren().add(submitButton);
        });

        vbox.getChildren().addAll(tableLabel, tableComboBox);

        Scene scene = new Scene(vbox, 620, 540);
        stage.setScene(scene);
        stage.show();
    }


    // (Change submenu)
    public void changeDatabaseAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Update Record");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // Dropdown para escolher a tabela
        Label tableLabel = new Label("Select Table:");
        ComboBox<String> tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("Category", "Pizza", "Orders");

        // Dropdown para selecionar a PK
        Label pkLabel = new Label("Select Record:");
        ComboBox<String> pkComboBox = new ComboBox<>();
        pkComboBox.setDisable(true);

        VBox fieldContainer = new VBox(10);

        // Botão para submeter a alteração
        Button updateButton = new Button("Update");
        updateButton.setDisable(true);

        // Ação ao selecionar tabela
        tableComboBox.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            if (selectedTable != null) {
                pkComboBox.setDisable(false);
                pkComboBox.getItems().clear();
                switch (selectedTable) {
                    case "Category" -> pkComboBox.getItems().addAll(getPrimaryKeysFromTable("Category", "cname"));
                    case "Pizza" -> pkComboBox.getItems().addAll(getPrimaryKeysFromTable("Pizza", "pname"));
                    case "Orders" -> pkComboBox.getItems().addAll(getPrimaryKeysFromTable("Orders", "id"));
                }
            }
        });

        // Ação ao selecionar PK
        pkComboBox.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            String selectedPk = pkComboBox.getValue();
            if (selectedPk != null) {
                fieldContainer.getChildren().clear();
                switch (selectedTable) {
                    case "Category" -> populateCategoryFields(fieldContainer, selectedPk);
                    case "Pizza" -> populatePizzaFields(fieldContainer, selectedPk);
                    case "Orders" -> populateOrdersFields(fieldContainer, selectedPk);
                }
                updateButton.setDisable(false); // Habilitar o botão de atualização após a seleção
            }
        });

        // Ação do botão de atualização
        updateButton.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            String selectedPk = pkComboBox.getValue();
            if (selectedTable != null && selectedPk != null) {
                switch (selectedTable) {
                    case "Category" -> updateCategory(selectedPk, fieldContainer);
                    case "Pizza" -> updatePizza(selectedPk, fieldContainer);
                    case "Orders" -> updateOrders(selectedPk, fieldContainer);
                }
            }
        });

        vbox.getChildren().addAll(tableLabel, tableComboBox, pkLabel, pkComboBox, fieldContainer, updateButton);
        Scene scene = new Scene(vbox, 620, 540);
        stage.setScene(scene);
        stage.show();
    }

    private List<String> getPrimaryKeysFromTable(String tableName, String pkColumn) {
        List<String> primaryKeys = new ArrayList<>();
        String query = "SELECT " + pkColumn + " FROM " + tableName;

        try (Connection connection = DatabaseHelper.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                primaryKeys.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return primaryKeys;
    }

    private void populateCategoryFields(VBox container, String cname) {
        String query = "SELECT cname, price FROM Category WHERE cname = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TextField cnameField = new TextField(rs.getString("cname"));
                TextField priceField = new TextField(String.valueOf(rs.getDouble("price")));

                container.getChildren().addAll(
                        new Label("Category Name:"), cnameField,
                        new Label("Price:"), priceField
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populatePizzaFields(VBox container, String pname) {
        String query = "SELECT pname, categoryname, vegetarian FROM Pizza WHERE pname = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TextField pnameField = new TextField(rs.getString("pname"));
                TextField categoryField = new TextField(rs.getString("categoryname"));
                CheckBox vegetarianField = new CheckBox("Vegetarian");
                vegetarianField.setSelected(rs.getBoolean("vegetarian"));

                container.getChildren().addAll(
                        new Label("Pizza Name:"), pnameField,
                        new Label("Category Name:"), categoryField,
                        vegetarianField
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateOrdersFields(VBox container, String id) {
        String query = "SELECT pizzaname, amount, dispatched FROM Orders WHERE id = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TextField pizzanameField = new TextField(rs.getString("pizzaname"));
                TextField amountField = new TextField(String.valueOf(rs.getInt("amount")));
                DatePicker dispatchedField = new DatePicker();
                if (rs.getTimestamp("dispatched") != null) {
                    dispatchedField.setValue(rs.getTimestamp("dispatched").toLocalDateTime().toLocalDate());
                }

                container.getChildren().addAll(
                        new Label("Pizza Name:"), pizzanameField,
                        new Label("Amount:"), amountField,
                        new Label("Dispatched Date:"), dispatchedField
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCategory(String selectedPk, VBox fieldContainer) {
        TextField cnameField = (TextField) fieldContainer.getChildren().get(1);
        TextField priceField = (TextField) fieldContainer.getChildren().get(3);

        String query = "UPDATE Category SET cname = ?, price = ? WHERE cname = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cnameField.getText());
            stmt.setDouble(2, Double.parseDouble(priceField.getText()));
            stmt.setString(3, selectedPk);
            executeUpdate(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePizza(String selectedPk, VBox fieldContainer) {
        TextField pnameField = (TextField) fieldContainer.getChildren().get(1);
        TextField categoryField = (TextField) fieldContainer.getChildren().get(3);
        CheckBox vegetarianField = (CheckBox) fieldContainer.getChildren().get(5);

        String query = "UPDATE Pizza SET pname = ?, categoryname = ?, vegetarian = ? WHERE pname = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pnameField.getText());
            stmt.setString(2, categoryField.getText());
            stmt.setBoolean(3, vegetarianField.isSelected());
            stmt.setString(4, selectedPk);
            executeUpdate(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrders(String selectedPk, VBox fieldContainer) {
        TextField pizzanameField = (TextField) fieldContainer.getChildren().get(1);
        TextField amountField = (TextField) fieldContainer.getChildren().get(3);
        DatePicker dispatchedField = (DatePicker) fieldContainer.getChildren().get(5);

        String query = "UPDATE Orders SET pizzaname = ?, amount = ?, dispatched = ? WHERE id = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, pizzanameField.getText());
            stmt.setInt(2, Integer.parseInt(amountField.getText()));
            stmt.setDate(3, dispatchedField.getValue() != null ? Date.valueOf(dispatchedField.getValue()) : null);
            stmt.setInt(4, Integer.parseInt(selectedPk));
            executeUpdate(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeUpdate(PreparedStatement stmt) throws SQLException {
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            showAlert("Success", "Record updated successfully!");
        } else {
            showAlert("Error", "Failed to update the record.");
        }
    }

    // (Delete submenu)
    public void deleteDatabaseAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Delete Record");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        // Dropdown para escolher a tabela
        Label tableLabel = new Label("Select Table:");
        ComboBox<String> tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("Category", "Pizza", "Orders");

        // Dropdown para selecionar a PK
        Label pkLabel = new Label("Select Record:");
        ComboBox<String> pkComboBox = new ComboBox<>();
        pkComboBox.setDisable(true);

        VBox fieldContainer = new VBox(10);


        Button deleteButton = new Button("Delete");
        deleteButton.setDisable(true);

        tableComboBox.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            if (selectedTable != null) {
                pkComboBox.setDisable(false);
                pkComboBox.getItems().clear();
                switch (selectedTable) {
                    case "Category" -> pkComboBox.getItems().addAll(getPrimaryKeysFromTable("Category", "cname"));
                    case "Pizza" -> pkComboBox.getItems().addAll(getPrimaryKeysFromTable("Pizza", "pname"));
                    case "Orders" -> pkComboBox.getItems().addAll(getPrimaryKeysFromTable("Orders", "id"));
                }
            }
        });

        pkComboBox.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            String selectedPk = pkComboBox.getValue();
            if (selectedPk != null) {
                fieldContainer.getChildren().clear();
                switch (selectedTable) {
                    case "Category" -> populateCategoryFields(fieldContainer, selectedPk);
                    case "Pizza" -> populatePizzaFields(fieldContainer, selectedPk);
                    case "Orders" -> populateOrdersFields(fieldContainer, selectedPk);
                }
                deleteButton.setDisable(false); // Habilitar o botão de exclusão após a seleção
            }
        });

        deleteButton.setOnAction(e -> {
            String selectedTable = tableComboBox.getValue();
            String selectedPk = pkComboBox.getValue();
            if (selectedTable != null && selectedPk != null) {
                switch (selectedTable) {
                    case "Category" -> deleteCategory(selectedPk);
                    case "Pizza" -> deletePizza(selectedPk);
                    case "Orders" -> deleteOrders(selectedPk);
                }
            }
        });

        vbox.getChildren().addAll(tableLabel, tableComboBox, pkLabel, pkComboBox, fieldContainer, deleteButton);
        Scene scene = new Scene(vbox, 620, 540);
        stage.setScene(scene);
        stage.show();
    }

    private void deleteCategory(String selectedPk) {
        String query = "DELETE FROM Category WHERE cname = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, selectedPk);
            executeDelete(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePizza(String selectedPk) {
        String query = "DELETE FROM Pizza WHERE pname = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, selectedPk);
            executeDelete(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteOrders(String selectedPk) {
        String query = "DELETE FROM Orders WHERE id = ?";
        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(selectedPk));
            executeDelete(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void executeDelete(PreparedStatement stmt) throws SQLException {
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            showAlert("Success", "Record deleted successfully!");
        } else {
            showAlert("Error", "Failed to delete the record.");
        }
    }



    // This method is called when the "Download" menu is clicked
    public void openDownloadForm(ActionEvent actionEvent) {
        // Create a new window
        Stage downloadStage = new Stage();
        downloadStage.setTitle("Download SOAP Data");

        // Create input fields
        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker(); // Use DatePicker for start date

        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker(); // Use DatePicker for end date

        Label currencyLabel = new Label("Currency:");
        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "HUF"); // Available currencies

        // Create a ProgressBar
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);

        // Create a button to start the download
        Button downloadButton = new Button("Download Data");
        downloadButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String selectedCurrency = currencyComboBox.getValue();

            if (startDate == null || endDate == null || selectedCurrency == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            // Validate that the start date is not after the end date
            if (startDate.isAfter(endDate)) {
                showAlert("Error", "Start date cannot be after the end date.");
                return;
            }

            // Start the download in a new thread to update the ProgressBar
            Thread downloadThread = new Thread(() -> {
                try {
                    // Simulate progress updates
                    for (int i = 0; i <= 100; i++) {
                        double progress = i / 100.0;
                        Thread.sleep(10); // Simulate progress delay
                        final int currentProgress = i;

                        // Update ProgressBar in the JavaFX Application Thread
                        javafx.application.Platform.runLater(() -> progressBar.setProgress(progress));
                    }

                    // Perform the actual download
                    String exchangeRates = downloadSoapData(startDate.toString(), endDate.toString(), selectedCurrency);

                    // Save the data to a file
                    saveDataToFile(exchangeRates);

                    // Show success alert (must run on JavaFX Application Thread)
                    javafx.application.Platform.runLater(() ->
                            showAlert("Download Successful", "All data has been downloaded to bank.txt.")
                    );

                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() ->
                            showAlert("Error", "An error occurred during the download: " + ex.getMessage())
                    );
                }
            });

            downloadThread.start();
        });

        // Add components to the new window
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                currencyLabel, currencyComboBox,
                progressBar, downloadButton
        );

        // Configure the scene and show the new window
        Scene scene = new Scene(vbox, 300, 350);
        downloadStage.setScene(scene);
        downloadStage.show();
    }

    // Method to call the SOAP service and fetch exchange rates
    private String downloadSoapData(String startDate, String endDate, String selectedCurrency) throws Exception {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        // Call the SOAP service to get exchange rates
        return service.getExchangeRates(startDate, endDate, selectedCurrency);
    }

    // Method to save the downloaded data directly to the project directory
    private void saveDataToFile(String data) {
        // Get the current project directory
        String projectDirectory = System.getProperty("user.dir");

        // Create the file path inside the project directory
        File file = new File(projectDirectory + File.separator + "bank.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            // Write the data to the file (overwrites the file if it already exists)
            writer.write(data);
        } catch (IOException e) {
            // Show error if unable to save the file
            showAlert("Error", "Failed to save the file: " + e.getMessage());
        }
    }

    public void downloadFilteredSoapData(ActionEvent actionEvent) {
        // Create a new stage for the form
        Stage filterStage = new Stage();
        filterStage.setTitle("Download Filtered SOAP Data");

        // Create input components
        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        Label currencyLabel = new Label("Currency:");
        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "HUF");

        Label optionsLabel = new Label("Additional Options:");
        CheckBox includeMetaDataCheckbox = new CheckBox("Include Metadata");
        RadioButton detailedDataRadio = new RadioButton("Detailed Data");
        RadioButton summaryDataRadio = new RadioButton("Summary Data");

        // Group radio buttons
        ToggleGroup dataOptionsGroup = new ToggleGroup();
        detailedDataRadio.setToggleGroup(dataOptionsGroup);
        summaryDataRadio.setToggleGroup(dataOptionsGroup);

        // Add a ProgressBar
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setVisible(false); // Hidden initially

        // Create download button
        Button downloadButton = new Button("Download Data");
        downloadButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String selectedCurrency = currencyComboBox.getValue();
            boolean includeMetaData = includeMetaDataCheckbox.isSelected();
            RadioButton selectedDataOption = (RadioButton) dataOptionsGroup.getSelectedToggle();

            if (startDate == null || endDate == null || selectedCurrency == null || selectedDataOption == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            if (startDate.isAfter(endDate)) {
                showAlert("Error", "Start date cannot be after end date.");
                return;
            }

            String dataOption = selectedDataOption.getText();

            // Start the SOAP data download
            progressBar.setVisible(true);
            downloadFilteredData(startDate.toString(), endDate.toString(), selectedCurrency, includeMetaData, dataOption, progressBar);
        });

        // Arrange components in a layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                currencyLabel, currencyComboBox,
                optionsLabel, includeMetaDataCheckbox, detailedDataRadio, summaryDataRadio,
                progressBar, downloadButton
        );

        // Set the scene and show the stage
        Scene scene = new Scene(vbox, 350, 400);
        filterStage.setScene(scene);
        filterStage.show();
    }

    // Method to handle SOAP data download based on filters
    private void downloadFilteredData(String startDate, String endDate, String currency, boolean includeMetaData, String dataOption, ProgressBar progressBar) {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        try {
            // Simulate data download progress
            for (int i = 0; i <= 100; i += 10) {
                final int progress = i;
                Thread.sleep(50); // Simulate processing delay
                progressBar.setProgress(progress / 100.0);
            }

            // Call SOAP service to get filtered data
            String exchangeRates = service.getExchangeRates(startDate, endDate, currency);

            // Add metadata or modify data if required
            if (includeMetaData) {
                exchangeRates += "\nMetadata: Data generated with option " + dataOption;
            }

            // Save data to Bank.txt
            saveDataToFile(exchangeRates);

            showAlert("Download Successful", "Filtered data has been saved to Bank.txt.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred during the download: " + e.getMessage());
        }
    }
    // Method to graph the data
    public void graphSoapData(ActionEvent actionEvent) {
        // Create a new window for the graph form
        Stage graphStage = new Stage();
        graphStage.setTitle("Graph SOAP Data");

        // Input fields and controls
        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        Label currencyLabel = new Label("Currency:");
        ComboBox<String> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().addAll("USD", "EUR", "HUF");

        CheckBox includeMetaDataCheckBox = new CheckBox("Include Metadata");

        Button generateGraphButton = new Button("Generate Graph");

        // Graph container
        VBox graphContainer = new VBox(10);

        generateGraphButton.setOnAction(e -> {
            String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : "";
            String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : "";
            String selectedCurrency = currencyComboBox.getValue();

            if (startDate.isEmpty() || endDate.isEmpty() || selectedCurrency == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            // Clear previous graph, if any
            graphContainer.getChildren().clear();

            // Generate a new graph
            LineChart<String, Number> lineChart = generateGraph(startDate, endDate, selectedCurrency, includeMetaDataCheckBox.isSelected());
            graphContainer.getChildren().add(lineChart);
        });

        // Layout setup
        VBox vbox = new VBox(10);
        vbox.setPadding(new javafx.geometry.Insets(10));
        vbox.getChildren().addAll(
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                currencyLabel, currencyComboBox,
                includeMetaDataCheckBox,
                generateGraphButton,
                graphContainer
        );

        Scene scene = new Scene(vbox, 800, 600);
        graphStage.setScene(scene);
        graphStage.show();
    }

    private LineChart<String, Number> generateGraph(String startDate, String endDate, String currency, boolean includeMetaData) {
        // Simulated data fetching and processing
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            data.add(new XYChart.Data<>("Day " + i, random.nextDouble() * 100)); // Simulated exchange rate
        }

        // Graph series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Exchange Rate for " + currency + " (" + startDate + " to " + endDate + ")");
        series.setData(data);

        // Include metadata, if selected
        if (includeMetaData) {
            series.setName(series.getName() + " - Metadata Included");
        }

        // Line chart setup
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Exchange Rate");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(series);
        lineChart.setTitle("Currency Exchange Rates");

        return lineChart;
    }





    public void parallelAction(ActionEvent actionEvent) {
        // Create the UI components
        Label labelOne = new Label("Label 1: Waiting...");
        Label labelTwo = new Label("Label 2: Waiting...");
        Button startButton = new Button("Start Parallel Tasks");

        // Start parallel tasks on button click
        startButton.setOnAction(e -> {
            startParallelTasks(labelOne, labelTwo);
        });

        // Layout for the new stage
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(labelOne, labelTwo, startButton);

        // Create and show the stage
        Stage parallelStage = new Stage();
        parallelStage.setTitle("Parallel Programming Demo");
        parallelStage.setScene(new Scene(layout, 300, 200));
        parallelStage.show();
    }

    private void startParallelTasks(Label labelOne, Label labelTwo) {
        // Task 1: Updates Label 1 every 1 second
        Task<Void> taskOne = new Task<>() {
            @Override
            protected Void call() {
                int count = 0;
                while (!isCancelled()) {
                    try {
                        Thread.sleep(1000); // Pause for 1 second
                    } catch (InterruptedException e) {
                        break;
                    }
                    int finalCount = count;
                    Platform.runLater(() -> labelOne.setText("Label 1: Count " + finalCount));
                    count++;
                }
                return null;
            }
        };

        // Task 2: Updates Label 2 every 2 seconds
        Task<Void> taskTwo = new Task<>() {
            @Override
            protected Void call() {
                int count = 0;
                while (!isCancelled()) {
                    try {
                        Thread.sleep(2000); // Pause for 2 seconds
                    } catch (InterruptedException e) {
                        break;
                    }
                    int finalCount = count;
                    Platform.runLater(() -> labelTwo.setText("Label 2: Count " + finalCount));
                    count++;
                }
                return null;
            }
        };

        // Start both tasks in separate threads
        Thread threadOne = new Thread(taskOne);
        threadOne.setDaemon(true);
        threadOne.start();

        Thread threadTwo = new Thread(taskTwo);
        threadTwo.setDaemon(true);
        threadTwo.start();
    }


    public void accountInformationAction(ActionEvent actionEvent) {
        // Crie um contexto com a URL e Token fornecidos
        Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

        // Janela de progresso
        Stage progressStage = new Stage();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Label progressLabel = new Label("Loading account information...");
        GridPane progressPane = new GridPane();
        progressPane.setHgap(10);
        progressPane.setVgap(10);
        progressPane.add(progressIndicator, 0, 0);
        progressPane.add(progressLabel, 0, 1);
        Scene progressScene = new Scene(progressPane, 300, 200);
        progressStage.setScene(progressScene);
        progressStage.setTitle("Please Wait");
        progressStage.show();

        // Tarefa para buscar dados em segundo plano
        Task<AccountSummary> task = new Task<>() {
            @Override
            protected AccountSummary call() throws Exception {
                // Solicita um resumo da conta usando o AccountID
                return ctx.account.summary(new AccountID(Config.ACCOUNTID)).getAccount();
            }
        };

        // Quando a tarefa for concluída com sucesso
        task.setOnSucceeded(workerStateEvent -> {
            progressStage.close(); // Fecha o indicador de progresso
            AccountSummary summary = task.getValue();

            // Cria uma nova janela para exibir as informações
            Stage infoStage = new Stage();
            GridPane infoPane = new GridPane();
            infoPane.setHgap(10);
            infoPane.setVgap(10);

            // Adiciona as informações formatadas
            infoPane.add(new Label("Account ID:"), 0, 0);
            infoPane.add(new Label(summary.getId().toString()), 1, 0);

            infoPane.add(new Label("Alias:"), 0, 1);
            infoPane.add(new Label(summary.getAlias()), 1, 1);

            infoPane.add(new Label("Currency:"), 0, 2);
            infoPane.add(new Label(summary.getCurrency().toString()), 1, 2);

            infoPane.add(new Label("Balance:"), 0, 3);
            infoPane.add(new Label(summary.getBalance().toString()), 1, 3);

            infoPane.add(new Label("NAV:"), 0, 4);
            infoPane.add(new Label(summary.getNAV().toString()), 1, 4);

            infoPane.add(new Label("Unrealized P/L:"), 0, 5);
            infoPane.add(new Label(summary.getUnrealizedPL().toString()), 1, 5);

            infoPane.add(new Label("Margin Rate:"), 0, 6);
            infoPane.add(new Label(summary.getMarginRate().toString()), 1, 6);

            infoPane.add(new Label("Open Trades:"), 0, 7);
            infoPane.add(new Label(String.valueOf(summary.getOpenTradeCount())), 1, 7);

            // Cria a cena da janela de informações
            Scene infoScene = new Scene(infoPane, 400, 300);
            infoStage.setScene(infoScene);
            infoStage.setTitle("Account Information");
            infoStage.show();
        });

        // Caso ocorra uma falha no carregamento
        task.setOnFailed(workerStateEvent -> {
            progressStage.close(); // Fecha o indicador de progresso
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Load Account Information");
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();
        });

        // Executa a tarefa em uma nova thread
        new Thread(task).start();
    }


    public void currentPricesAction(ActionEvent actionEvent) {
        // Create a new stage for the "Current Prices" submenu
        Stage priceStage = new Stage();
        priceStage.setTitle("Current Prices");

        // Main layout with centered elements
        VBox mainLayout = new VBox(15); // 15px spacing between elements
        mainLayout.setAlignment(Pos.CENTER); // Center-align all elements
        mainLayout.setPadding(new Insets(20)); // Internal padding of 20px

        // Section title
        Label titleLabel = new Label("Check Current Prices");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown for available currency pairs
        ComboBox<String> currencyPairDropdown = new ComboBox<>();
        currencyPairDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CHF");
        currencyPairDropdown.setPromptText("Select a currency pair");
        currencyPairDropdown.setPrefWidth(200);

        // Button to fetch the current price
        Button fetchPriceButton = new Button("Get Current Price");
        fetchPriceButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        // Label to display the current price
        Label priceLabel = new Label();
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-alignment: center;");

        // Loading indicator (spinner)
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false); // Initially hidden

        // Add all elements to the main layout
        mainLayout.getChildren().addAll(titleLabel, currencyPairDropdown, fetchPriceButton, loadingIndicator, priceLabel);

        // Set up the scene and display the stage
        Scene scene = new Scene(mainLayout, 400, 300); // Width: 400px, Height: 300px
        priceStage.setScene(scene);
        priceStage.show();

        // Configure the button to fetch prices
        fetchPriceButton.setOnAction(e -> {
            String selectedPair = currencyPairDropdown.getValue();

            if (selectedPair == null) {
                priceLabel.setText("Please select a currency pair.");
                return;
            }

            // Show the loading indicator while fetching data
            loadingIndicator.setVisible(true);
            priceLabel.setText("");

            // Connect to the OANDA API to fetch the price
            Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

            try {
                // Create a request to get current prices
                PricingGetRequest request = new PricingGetRequest(
                        new com.oanda.v20.account.AccountID(Config.ACCOUNTID),
                        List.of(selectedPair.replace("/", "_"))
                );

                // Execute the request and get the response
                PricingGetResponse pricingResponse = ctx.pricing.get(request);

                // Get the price for the selected currency pair
                // Get the price for the selected currency pair
                ClientPrice clientPrice = pricingResponse.getPrices().get(0);
                String bid = clientPrice.getBids().get(0).getPrice().toString();
                String ask = clientPrice.getAsks().get(0).getPrice().toString();

// Display the prices on the interface
                priceLabel.setText(String.format("Bid: %s | Ask: %s", bid, ask));


                // Display the prices on the interface
                priceLabel.setText(String.format("Bid: %s | Ask: %s", bid, ask));
            } catch (Exception ex) {
                priceLabel.setText("Failed to fetch price.");
                ex.printStackTrace();
            } finally {
                // Hide the loading indicator
                loadingIndicator.setVisible(false);
            }
        });
    }

    public void historicalPricesAction(ActionEvent actionEvent) {
        // Create a new stage for the "Historical Prices" submenu
        Stage historicalStage = new Stage();
        historicalStage.setTitle("Historical Prices");

        // Main layout with centered elements
        VBox mainLayout = new VBox(15); // 15px spacing between elements
        mainLayout.setAlignment(Pos.CENTER); // Center-align all elements
        mainLayout.setPadding(new Insets(20)); // Internal padding of 20px

        // Section title
        Label titleLabel = new Label("Check Historical Prices");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown for available currency pairs
        ComboBox<String> currencyPairDropdown = new ComboBox<>();
        currencyPairDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CHF");
        currencyPairDropdown.setPromptText("Select a currency pair");
        currencyPairDropdown.setPrefWidth(200);

        // Date pickers for start and end date
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        // Button to fetch historical prices
        Button fetchPriceButton = new Button("Get Historical Prices");
        fetchPriceButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        // Table to display historical prices
        TableView<PriceData> priceTable = new TableView<>();
        TableColumn<PriceData, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        TableColumn<PriceData, String> bidColumn = new TableColumn<>("Bid");
        bidColumn.setCellValueFactory(cellData -> cellData.getValue().bidProperty());
        TableColumn<PriceData, String> askColumn = new TableColumn<>("Ask");
        askColumn.setCellValueFactory(cellData -> cellData.getValue().askProperty());

        priceTable.getColumns().addAll(dateColumn, bidColumn, askColumn);

        // Add all elements to the main layout
        mainLayout.getChildren().addAll(titleLabel, currencyPairDropdown, startDatePicker, endDatePicker, fetchPriceButton, priceTable);

        // Set up the scene and display the stage
        Scene scene = new Scene(mainLayout, 600, 400); // Width: 600px, Height: 400px
        historicalStage.setScene(scene);
        historicalStage.show();

        // Configure the button to fetch historical prices
        fetchPriceButton.setOnAction(e -> {
            String selectedPair = currencyPairDropdown.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (selectedPair == null || startDate == null || endDate == null) {
                // Check if any field is empty
                showError("Please select a currency pair and dates.");
                return;
            }

            // Connect to the OANDA API to fetch historical prices
            Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

            try {
                // Create a request to get historical prices
                PricingGetRequest request = new PricingGetRequest(
                        new AccountID(Config.ACCOUNTID),
                        List.of(selectedPair.replace("/", "_"))
                );

                // Execute the request and get the response
                PricingGetResponse pricingResponse = ctx.pricing.get(request);

                // Process the response to get prices between the selected dates
                List<PriceData> historicalPrices = new ArrayList<>();
                for (ClientPrice clientPrice : pricingResponse.getPrices()) {
                    // Assuming the historical data is available in the ClientPrice object
                    String bid = clientPrice.getBids().get(0).getPrice().toString();
                    String ask = clientPrice.getAsks().get(0).getPrice().toString();
                    // Populate the list with the historical data
                    historicalPrices.add(new PriceData(clientPrice.getTime().toString(), bid, ask));
                }

                // Update the table with the historical prices
                priceTable.getItems().setAll(historicalPrices);
            } catch (Exception ex) {
                showError("Failed to fetch historical prices.");
                ex.printStackTrace();
            }
        });
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void positionOpeningAction(ActionEvent actionEvent) {

        Stage positionStage = new Stage();
        positionStage.setTitle("Open Position");

        // Layout principal com elementos centralizados
        VBox mainLayout = new VBox(15); // 15px de espaçamento entre os elementos
        mainLayout.setAlignment(Pos.CENTER); // Centraliza os elementos
        mainLayout.setPadding(new Insets(20)); // Padding interno de 20px

        // Título da seção
        Label titleLabel = new Label("Open a Position");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown para pares de moedas disponíveis
        ComboBox<String> currencyPairDropdown = new ComboBox<>();
        currencyPairDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "AUD/USD", "USD/CHF");
        currencyPairDropdown.setPromptText("Select a currency pair");
        currencyPairDropdown.setPrefWidth(200);

        // Campo de quantidade (Quantia de moedas)
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        quantityField.setPrefWidth(200);

        // Dropdown para direção (Buy/Sell)
        ComboBox<String> directionDropdown = new ComboBox<>();
        directionDropdown.getItems().addAll("Buy", "Sell");
        directionDropdown.setPromptText("Select direction");
        directionDropdown.setPrefWidth(200);

        // Botão para abrir a posição
        Button openPositionButton = new Button("Open Position");
        openPositionButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        // Label para exibir o resultado da ação
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-alignment: center;");

        // Adiciona todos os elementos no layout principal
        mainLayout.getChildren().addAll(titleLabel, currencyPairDropdown, quantityField, directionDropdown, openPositionButton, resultLabel);

        // Configura a cena e exibe a janela
        Scene scene = new Scene(mainLayout, 400, 300); // Largura: 400px, Altura: 300px
        positionStage.setScene(scene);
        positionStage.show();

        // Configura o evento do botão para abrir a posição
        openPositionButton.setOnAction(e -> {
            String selectedPair = currencyPairDropdown.getValue();
            String quantityText = quantityField.getText();
            String direction = directionDropdown.getValue();

            // Verifica se os campos necessários foram preenchidos
            if (selectedPair == null || quantityText.isEmpty() || direction == null) {
                resultLabel.setText("Please fill in all fields.");
                return;
            }

            // Tenta converter a quantidade para um número
            double quantity;
            try {
                quantity = Double.parseDouble(quantityText);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid quantity.");
                return;
            }

            // Exemplo de como você pode implementar a lógica para abrir a posição
            // A lógica de abrir a posição pode incluir chamadas à API de negociação, como a OANDA
            try {
                // Aqui você pode adicionar o código para abrir a posição no servidor, por exemplo:
                String action = (direction.equals("Buy")) ? "Buying" : "Selling";
                resultLabel.setText(String.format("Opening %s position for %s with quantity %.2f", action, selectedPair, quantity));

                // Simulação de uma chamada de API para abrir a posição
                // Por exemplo: OANDA API para abrir uma posição (esse é apenas um exemplo)
                // openPositionOnServer(selectedPair, quantity, direction);

            } catch (Exception ex) {
                resultLabel.setText("Failed to open position.");
                ex.printStackTrace();
            }
        });
    }



    public void positionClosingAction(ActionEvent actionEvent) {
        // Create a new stage for the "Position Closing" submenu
        Stage closingStage = new Stage();
        closingStage.setTitle("Close Position");

        // Main layout with centered elements
        VBox mainLayout = new VBox(15); // 15px spacing between elements
        mainLayout.setAlignment(Pos.CENTER); // Center-align all elements
        mainLayout.setPadding(new Insets(20)); // Internal padding of 20px

        // Section title
        Label titleLabel = new Label("Close Position");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Label for Position ID input
        Label positionIdLabel = new Label("Enter Position ID:");

        // Text field to enter the position ID
        TextField positionIdField = new TextField();
        positionIdField.setPromptText("Position ID");

        // Button to close the position
        Button closePositionButton = new Button("Close Position");
        closePositionButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px;");

        // Label to display the result of closing the position
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-alignment: center;");

        // Add all elements to the main layout
        mainLayout.getChildren().addAll(titleLabel, positionIdLabel, positionIdField, closePositionButton, resultLabel);

        // Set up the scene and display the stage
        Scene scene = new Scene(mainLayout, 400, 300); // Width: 400px, Height: 300px
        closingStage.setScene(scene);
        closingStage.show();

        // Configure the button to close the position
        closePositionButton.setOnAction(e -> {
            String positionId = positionIdField.getText().trim();

            if (positionId.isEmpty()) {
                resultLabel.setText("Please enter a position ID.");
                return;
            }



            // Connect to the OANDA API
            Context ctx = new Context("https://api-fxpractice.oanda.com", Config.TOKEN);

            try {
                // Use TradeSpecifier to specify the position (TradeID)
                TradeSpecifier tradeSpecifier = new TradeSpecifier(positionId); // Use o ID da posição

                // Create the close request with the AccountID and TradeSpecifier
                TradeCloseRequest closeRequest = new TradeCloseRequest(Config.ACCOUNTID, tradeSpecifier);

                // Execute the request to close the position
                TradeCloseResponse closeResponse = ctx.trade.close(closeRequest);

                // If successful, update the result label
                if (closeResponse != null) {
                    resultLabel.setText("Position closed successfully!");
                } else {
                    resultLabel.setText("Failed to close position.");
                }
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }


    public void openedPositionsAction(ActionEvent actionEvent) {
        // Cria uma nova janela para o submenu "Opened Positions"
        Stage openedPositionsStage = new Stage();
        openedPositionsStage.setTitle("Opened Positions");

        // Layout principal com elementos centralizados
        VBox mainLayout = new VBox(15); // 15px de espaçamento entre os elementos
        mainLayout.setAlignment(Pos.CENTER); // Centraliza os elementos
        mainLayout.setPadding(new Insets(20)); // Padding interno de 20px

        // Título da seção
        Label titleLabel = new Label("Opened Positions");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tabela para exibir as posições abertas
        TableView<com.dija.lecturehomework.model.Position> positionsTable = new TableView<>();
        positionsTable.setPrefWidth(400);

        // Colunas da tabela
        TableColumn<Position, String> currencyPairColumn = new TableColumn<>("Currency Pair");
        currencyPairColumn.setCellValueFactory(new PropertyValueFactory<>("currencyPair"));

        TableColumn<Position, String> directionColumn = new TableColumn<>("Direction");
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));

        TableColumn<Position, Double> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Adiciona as colunas na tabela
        positionsTable.getColumns().addAll(currencyPairColumn, directionColumn, quantityColumn);

        // Simula a obtenção das posições abertas (pode ser modificado para buscar dados de uma API ou banco de dados)
        ObservableList<Position> openedPositions = FXCollections.observableArrayList(
                new Position("EUR/USD", "Buy", 1000),
                new Position("GBP/USD", "Sell", 1500),
                new Position("USD/JPY", "Buy", 2000)
        );

        // Adiciona as posições na tabela
        positionsTable.setItems(openedPositions);

        // Adiciona todos os elementos no layout principal
        mainLayout.getChildren().addAll(titleLabel, positionsTable);

        // Configura a cena e exibe a janela
        Scene scene = new Scene(mainLayout, 500, 350); // Largura: 500px, Altura: 350px
        openedPositionsStage.setScene(scene);
        openedPositionsStage.show();
    }



}
