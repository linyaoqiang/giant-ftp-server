package com.giant.ftp.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * 黑名单设置面板的控制器
 */
public class BlackSettingsController {
    @FXML
    private ListView<String> blackListView;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button addBtn;

    @FXML
    private TextField addressField;

    public ListView<String> getBlackListView() {
        return blackListView;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    // 删除一条黑名单
    public void delete() {
        int selectedIndex = this.blackListView.getSelectionModel().getSelectedIndex();
        if (this.blackListView.getItems().size() > 0) {
            this.blackListView.getItems().remove(selectedIndex);
        }
    }

    // 添加一条黑名单
    public void add() {
        String address = addressField.getText();
        addressField.setText("");
        this.blackListView.getItems().add(address);
    }

}
