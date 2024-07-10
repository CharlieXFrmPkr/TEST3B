package com.example.pc3test3b;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.pc3test3b.model.PizzaOrder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PizzaOrderController {

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField mobileNumberField;
    @FXML
    private CheckBox sizeXL;
    @FXML
    private CheckBox sizeL;
    @FXML
    private CheckBox sizeM;
    @FXML
    private CheckBox sizeS;
    @FXML
    private TextField numberOfToppingsField;

    @FXML
    private TableView<PizzaOrder> orderTable;
    @FXML
    private TableColumn<PizzaOrder, String> customerNameColumn;
    @FXML
    private TableColumn<PizzaOrder, String> mobileNumberColumn;
    @FXML
    private TableColumn<PizzaOrder, String> pizzaSizeColumn;
    @FXML
    private TableColumn<PizzaOrder, Integer> numberOfToppingsColumn;
    @FXML
    private TableColumn<PizzaOrder, Double> totalBillColumn;

    private ObservableList<PizzaOrder> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        pizzaSizeColumn.setCellValueFactory(new PropertyValueFactory<>("pizzaSize"));
        numberOfToppingsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfToppings"));
        totalBillColumn.setCellValueFactory(new PropertyValueFactory<>("totalBill"));
        orderTable.setItems(orderList);
    }

    @FXML
    private void onHelloButtonClick(ActionEvent event) {
        System.out.println("Hello button clicked!");
    }

    @FXML
    public void createOrder(ActionEvent event) {
        String customerName = customerNameField.getText();
        String mobileNumber = mobileNumberField.getText();
        String pizzaSize = getPizzaSize();
        int numberOfToppings = Integer.parseInt(numberOfToppingsField.getText());
        double totalBill = calculateTotalBill(pizzaSize, numberOfToppings);

        PizzaOrder order = new PizzaOrder(customerName, mobileNumber, pizzaSize, numberOfToppings, totalBill);
        orderList.add(order);
        insertOrderIntoDatabase(order);
    }

    @FXML
    public void readOrder(ActionEvent event) {
        // Implement fetch order logic here (if needed)
    }

    @FXML
    public void updateOrder(ActionEvent event) {
        String customerName = customerNameField.getText();
        String mobileNumber = mobileNumberField.getText();
        String pizzaSize = getPizzaSize();
        int numberOfToppings = Integer.parseInt(numberOfToppingsField.getText());
        double totalBill = calculateTotalBill(pizzaSize, numberOfToppings);

        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza_ordering";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "UPDATE pizza_orders SET mobile_number = '" + mobileNumber + "', pizza_size = '" + pizzaSize + "', number_of_toppings = " + numberOfToppings + ", total_bill = " + totalBill + " WHERE customer_name = '" + customerName + "'";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteOrder(ActionEvent event) {
        String customerName = customerNameField.getText();

        if (customerName.isEmpty()) {
            System.out.println("Please enter a customer name to delete.");
            return;
        }

        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza_ordering";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "DELETE FROM pizza_orders WHERE customer_name = '" + customerName + "'";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getPizzaSize() {
        if (sizeXL.isSelected()) return "XL";
        if (sizeL.isSelected()) return "L";
        if (sizeM.isSelected()) return "M";
        return "S";
    }

    public double calculateTotalBill(String pizzaSize, int numberOfToppings) {
        double basePrice;
        switch (pizzaSize) {
            case "XL":
                basePrice = 15.00;
                break;
            case "L":
                basePrice = 12.00;
                break;
            case "M":
                basePrice = 10.00;
                break;
            default:
                basePrice = 8.00;
                break;
        }
        double toppingsCost = numberOfToppings * 1.50;
        double totalBeforeTax = basePrice + toppingsCost;
        return totalBeforeTax * 1.15; // Adding 15% HST
    }

    private void insertOrderIntoDatabase(PizzaOrder order) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza_ordering";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO pizza_orders (customer_name, mobile_number, pizza_size, number_of_toppings, total_bill) VALUES ('"
                    + order.getCustomerName() + "', '"
                    + order.getMobileNumber() + "', '"
                    + order.getPizzaSize() + "', "
                    + order.getNumberOfToppings() + ", "
                    + order.getTotalBill() + ")";
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadOrdersFromDatabase() {
        orderList.clear();

        String jdbcUrl = "jdbc:mysql://localhost:3306/pizza_ordering";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "SELECT * FROM pizza_orders";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String customerName = resultSet.getString("customer_name");
                String mobileNumber = resultSet.getString("mobile_number");
                String pizzaSize = resultSet.getString("pizza_size");
                int numberOfToppings = resultSet.getInt("number_of_toppings");
                double totalBill = resultSet.getDouble("total_bill");

                PizzaOrder order = new PizzaOrder(customerName, mobileNumber, pizzaSize, numberOfToppings, totalBill);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void fetchOrder(ActionEvent event) {
        loadOrdersFromDatabase();
    }
}
