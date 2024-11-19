import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoParkView {
    private Scene scene;
    private Pane aPane;

    // Components
    ListView<Item> inventoryList = new ListView<>();
    ListView<Item> cartList = new ListView<>();
    ListView<Item> popularItemsList = new ListView<>();
    TextField salesField = new TextField("0");
    TextField revenueField = new TextField("$0.00");
    TextField salesAverageField = new TextField("N/A");
    Button addToCartButton = new Button("Add to Cart");
    Button removeItemButton = new Button("Remove Item");
    Button completeSaleButton = new Button("Complete Sale");
    Button resetStockButton = new Button("Reset Stock");
    private Label cartLabel;

    public AutoParkView() {
        aPane = new Pane();

        // Initialize and position all components
        init();

        scene = new Scene(aPane, 800, 400);
    }

    private void init() {
        Label inventoryLabel = new Label("Park Inventory:");
        inventoryLabel.setLayoutX(10);
        inventoryLabel.setLayoutY(10);
        inventoryList.setLayoutX(10);
        inventoryList.setLayoutY(40);
        inventoryList.setPrefSize(250, 300);

        cartLabel = new Label("Current Cart ($0.00):");
        cartLabel.setLayoutX(270);
        cartLabel.setLayoutY(10);
        cartList.setLayoutX(270);
        cartList.setLayoutY(40);
        cartList.setPrefSize(250, 300);

        Label summaryLabel = new Label("Park Summary");
        summaryLabel.setLayoutX(530);
        summaryLabel.setLayoutY(10);

        Label salesLabel = new Label("# Sales:");
        salesLabel.setLayoutX(530);
        salesLabel.setLayoutY(35);
        salesField.setLayoutX(590);
        salesField.setLayoutY(30);
        salesField.setPrefSize(60, 20);
        salesField.setEditable(false);

        Label revenueLabel = new Label("Revenue:");
        revenueLabel.setLayoutX(530);
        revenueLabel.setLayoutY(60);
        revenueField.setLayoutX(590);
        revenueField.setLayoutY(55);
        revenueField.setPrefSize(60, 20);
        revenueField.setEditable(false);

        Label salesAverageLabel = new Label("$ / Sale:");
        salesAverageLabel.setLayoutX(530);
        salesAverageLabel.setLayoutY(85);
        salesAverageField.setLayoutX(590);
        salesAverageField.setLayoutY(80);
        salesAverageField.setPrefSize(60, 20);
        salesAverageField.setEditable(false);

        Label popularItemsLabel = new Label("Most Popular Items:");
        popularItemsLabel.setLayoutX(530);
        popularItemsLabel.setLayoutY(110);
        popularItemsList.setLayoutX(530);
        popularItemsList.setLayoutY(130);
        popularItemsList.setPrefSize(180, 210);

        addToCartButton.setLayoutX(10);
        addToCartButton.setLayoutY(350);
        removeItemButton.setLayoutX(270);
        removeItemButton.setLayoutY(350);
        completeSaleButton.setLayoutX(390);
        completeSaleButton.setLayoutY(350);
        resetStockButton.setLayoutX(530);
        resetStockButton.setLayoutY(350);

        aPane.getChildren().addAll(
                inventoryLabel, inventoryList, cartLabel, cartList,
                summaryLabel, salesLabel, salesField, revenueLabel, revenueField,
                salesAverageLabel, salesAverageField, popularItemsLabel, popularItemsList,
                addToCartButton, removeItemButton, completeSaleButton, resetStockButton
        );
    }

    public void setCartLabel(double total){
        cartLabel.setText(String.format("Current Cart ($%,.2f)", total));
    }

    public void updateSalesDisplay(int totalSales, double totalRevenue, double averageSale) {
        salesField.setText(String.valueOf(totalSales));
        revenueField.setText(String.format("$%,.2f", totalRevenue));
        salesAverageField.setText(String.format("$%,.2f", averageSale));
    }

    public void updateInventoryListView(List<Item> items){
        inventoryList.getItems().clear();
        inventoryList.getItems().addAll(items);
    }

    public Scene getScene() {
        return scene;
    }
}
