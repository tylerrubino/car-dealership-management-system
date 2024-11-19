import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.*;

public class AutoParkApp extends Application {
        private AutoPark autoPark;
        private AutoParkView view;

        public void start(Stage primaryStage){
            autoPark = AutoPark.createPark();
            view = new AutoParkView();

            primaryStage.setTitle(autoPark.getName() + " - Sales and Inventory");
            primaryStage.setScene(view.getScene());
            primaryStage.setResizable(false);
            primaryStage.show();

            initializeGUI();
        }

        private void initializeGUI(){
            // disable buttons initially
            view.addToCartButton.setDisable(true);
            view.removeItemButton.setDisable(true);
            view.completeSaleButton.setDisable(true);

            // initialize TextFields
            view.salesField.setText("0");
            view.revenueField.setText(String.format("$%.2f", autoPark.getRevenue()));
            view.salesAverageField.setText("N/A");

            // populate inventory listview
            for (Item item : autoPark.getItems()){
                if (item != null){
                    view.inventoryList.getItems().add(item);
                }
            }

            // populate popular items
            for (int i = 0; i < autoPark.getItems().length && i < 3; i++) {
                Item item = autoPark.getItems()[i];
                if (item != null) {
                    view.popularItemsList.getItems().add(item);
                }
            }

            // enable/disable add to cart button
            view.inventoryList.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Item selectedItem = view.inventoryList.getSelectionModel().getSelectedItem();
                    view.addToCartButton.setDisable(selectedItem == null);
                }
            });

            // add to cart functionality
            view.addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Item selectedItem = view.inventoryList.getSelectionModel().getSelectedItem();
                    if (selectedItem != null && selectedItem.getInvQuantity() > 0) {
                        selectedItem.decreaseStock();
                        cartItems.put(selectedItem, cartItems.getOrDefault(selectedItem, 0) + 1);
                        updateCartListView();
                        updateCartTotal();
                        view.completeSaleButton.setDisable(cartItems.isEmpty());
                        // enable remove button when items are added
                        view.removeItemButton.setDisable(false);

                        if (selectedItem.getInvQuantity() == 0) {
                            view.inventoryList.getItems().remove(selectedItem);
                        }
                        updateInventoryListView();
                    }
                }
            });

            // remove button functionality
            view.removeItemButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                     Item selectedItem = view.cartList.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        int quantity = cartItems.get(selectedItem);
                        if (quantity > 1) {
                            cartItems.put(selectedItem, quantity - 1);
                        } else {
                            cartItems.remove(selectedItem);
                        }
                        selectedItem.increaseStock();
                        updateCartListView();
                        updateInventoryListView();
                        updateCartTotal();
                        view.removeItemButton.setDisable(cartItems.isEmpty());
                    }
                }
            });

            // complete sale button functionality
            view.completeSaleButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!cartItems.isEmpty()) {
                        double totalCartValue = 0;
                        for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
                            Item item = entry.getKey();
                            int quantity = entry.getValue();
                            totalCartValue += item.getPrice() * quantity;
                            item.sellUnits(item.getSoldQuantity() + quantity);
                        }

                        // record sale in the model
                        autoPark.recordSale(totalCartValue);

                        // update gui
                        view.updateSalesDisplay(autoPark.getTotalSales(), autoPark.getRevenue(), autoPark.getAverageSaleAmount());

                        // clear and update cart list
                        cartItems.clear();
                        updateCartListView();

                        // reset total dollar amount in brackets
                        view.setCartLabel(0.0);

                        // update popular items
//                        updateMostPopularItems();
                    }
                }
            });

            // reset stock button functionality
            view.resetStockButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    autoPark = AutoPark.createPark();
                    view.updateSalesDisplay(autoPark.getTotalSales(), autoPark.getRevenue(), autoPark.getAverageSaleAmount());
                    List<Item> itemsList = Arrays.asList(autoPark.getItems());
                    view.updateInventoryListView(itemsList);
                    updateCartListView();
                }
            });


        }

       private Map<Item, Integer> cartItems = new HashMap<>();
        private void updateCartListView() {
            view.cartList.getItems().clear();
            for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                view.cartList.getItems().add(item);
//                String displayText = String.format("%d x %s", quantity, item.toString());
//                view.cartList.getItems().add(displayText);
            }
        }

        private void updateInventoryListView(){
            view.inventoryList.refresh();
        }

        // change total dollar amount beside current cart
        private void updateCartTotal() {
            double total = 0.0;
            for (Map.Entry<Item, Integer> entry : cartItems.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                total += item.getPrice() * quantity;
            }
            view.setCartLabel(total);
        }

        public static void main(String[] args){
            launch(args);
        }
    }
