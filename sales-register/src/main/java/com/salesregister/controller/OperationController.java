package com.salesregister.controller;

import com.salesregister.Main;
import com.salesregister.controller.popup.PopupWindow;
import com.salesregister.domain.Operation;
import com.salesregister.domain.Products;
import com.salesregister.request.OperationRequest;
import com.salesregister.request.ProductsRequest;
import com.salesregister.service.OperationService;
import com.salesregister.service.ProductService;
import com.salesregister.service.UserService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Controller
public class OperationController {
    private final ProductService productService;
    private final OperationService service;
    private final UserService userService;

    @FXML
    private TableView<Operation> table;

    @FXML
    private TableView<Products> tableProducts;

    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private TextArea description;


    @FXML
    private TextField priceF;

    @FXML
    private TextField saleF;

    @FXML
    private TextField fullPriceF;

    @Autowired
    public OperationController(OperationService service, UserService userService, ProductService productService) {
        this.service = service;
        this.userService = userService;
        this.productService = productService;
    }

    @FXML
    void add(ActionEvent event) {


        if (name.getText().length() == 0) {
            PopupWindow.openWindow("Ошибка", "Заполните название");
            return;
        }

        if (description.getText().length() == 0) {
            PopupWindow.openWindow("Ошибка", "Заполните описание");
            return;
        }

        if (!price.getText().matches("[0-9]+[.,]?[0-9]*")) {
            PopupWindow.openWindow("Ошибка", "Цена может содержать только цифры и точку(если требуется)");
            return;
        }


        OperationRequest request = new OperationRequest();
        request.setName(name.getText());
        request.setDescription(description.getText());
        request.setPrice(new BigDecimal(price.getText().replace(',', '.')));

        service.addOperation(request);

        name.setText("");
        description.setText("");
        price.setText("");

        updateTable();
        updateTableProducts();
    }

    private void updateTableProducts() {

        TableColumn<Products, Products> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Products, Products> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Products, Products> delete = new TableColumn<>("");
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Products, Products>() {
            private final Button deleteButton = new Button("Удалить");

            @Override
            protected void updateItem(Products products, boolean empty) {
                super.updateItem(products, empty);

                if (products == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    try {
                        productService.deleteProduct(products.getId());
                        updateTableProducts();

                    } catch (Exception e) {
                        PopupWindow.openWindow("Ошибка", "Запись не найдена");
                    }
                });
            }
        });

        List<Products> products = productService.getProductsForCurrentUser();
        BigDecimal price = getFullPrice(products);
        BigDecimal sale = BigDecimal.ZERO;

        if (products.size() > 50) {
            sale = price.multiply(new BigDecimal(0.1));
        } else if (products.size() > 10) {
            sale = price.multiply(new BigDecimal(0.05));
        } else if (products.size() > 2) {
            sale = price.multiply(new BigDecimal(0.01));
        } else {
            sale = BigDecimal.ZERO;
        }

        priceF.setText(String.valueOf(price.doubleValue()));
        saleF.setText(String.valueOf(sale.doubleValue()));
        fullPriceF.setText(String.valueOf(price.doubleValue() - sale.doubleValue()));


        ObservableList<Products> values = FXCollections.observableArrayList(products);
        tableProducts.setItems(values);
        tableProducts.getColumns().clear();

        tableProducts.getColumns().addAll(
                nameCol,
                priceCol,
                delete
        );
    }

    private BigDecimal getFullPrice(List<Products> products) {
        BigDecimal price = BigDecimal.ZERO;

        for (Products product : products) {
            price = price.add(product.getPrice());
        }

        return price;
    }

    private void updateTable() {
        TableColumn<Operation, Operation> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Operation, Operation> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Operation, Operation> descriptionCol = new TableColumn<>("Описание");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Operation, Operation> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Operation, Operation> dateCol = new TableColumn<>("Дата регистрации");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Operation, Operation> add = new TableColumn<>("");
        add.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        add.setCellFactory(param -> new TableCell<Operation, Operation>() {
            private final Button addButton = new Button("Добавить");

            @Override
            protected void updateItem(Operation operation, boolean empty) {

                if (operation == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(addButton);
                addButton.setOnAction(event -> {

                        ProductsRequest request = new ProductsRequest();
                        request.setName(operation.getName());
                        request.setPrice(operation.getPrice());

                        productService.addProduct(request);
                        updateTableProducts();

                });
            }
        });

        TableColumn<Operation, Operation> delete = new TableColumn<>("");
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Operation, Operation>() {
            private final Button deleteButton = new Button("Удалить");

            @Override
            protected void updateItem(Operation operation, boolean empty) {
                super.updateItem(operation, empty);

                if (operation == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    try {
                        service.deleteOperation(operation.getId());
                        updateTable();
                        updateTableProducts();
                    } catch (Exception e) {
                        PopupWindow.openWindow("Ошибка", "Запись не найдена");
                    }
                });
            }
        });

        ObservableList<Operation> values = FXCollections
                .observableArrayList(service.getExcursionsForCurrentUser());

        table.setItems(values);
        table.getColumns().

                clear();

        table.getColumns().addAll(
                idCol, nameCol,
                descriptionCol,
                priceCol,
                dateCol, add, delete
        );

    }

    @FXML
    private void deleteAllFromCart() {
        ObservableList<Products> values = FXCollections
                .observableArrayList(productService.getProductsForCurrentUser());
        for (Products value : values) {
            productService.deleteProduct(value.getId());
        }

        updateTable();
        updateTableProducts();

    }

    @FXML
    public void initialize() {
        deleteAllFromCart();
        updateTable();
        updateTableProducts();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        userService.logout();
        AuthenticationController.load();
    }

    public static void load() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/operations.fxml"));
        loader.setControllerFactory(Main.getApplicationContext().getBeanFactory()::getBean);
        Parent view = loader.load();
        Main.getStage().setScene(new Scene(view));
        Main.getStage().show();
    }
}
