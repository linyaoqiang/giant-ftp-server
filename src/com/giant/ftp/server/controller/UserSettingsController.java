package com.giant.ftp.server.controller;

import com.giant.ftp.server.property.UserProperty;
import com.giant.ftp.server.view.UserDialog;
import com.giant.ftp.server.view.UserTableContextMenu;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * 用户设置面板的控制器
 */
public class UserSettingsController {
    @FXML
    private TableView<UserProperty> userTableView;
    @FXML
    private TableColumn<UserProperty, String> nameColumn;
    @FXML
    private TableColumn<UserProperty, String> passwordColumn;
    @FXML
    private TableColumn<UserProperty, Boolean> writeableColumn;
    @FXML
    private TableColumn<UserProperty, String> homeDirectoryColumn;
    @FXML
    private TableColumn<UserProperty, Integer> maxIdleTimeColumn;
    @FXML
    private TableColumn<UserProperty, Boolean> enabledColumn;
    @FXML
    private Button addBtn;

    private UserTableContextMenu contextMenu;
    private UserDialog userDialog;

    public UserSettingsController() throws IOException {
        // 初始化内部使用的组件
        this.contextMenu = UserTableContextMenu.getInstance();
        this.userDialog = UserDialog.getInstance();
    }

    public Button getAddBtn() {
        return addBtn;
    }

    public TableView<UserProperty> getUserTableView() {
        return userTableView;
    }

    public TableColumn<UserProperty, String> getNameColumn() {
        return nameColumn;
    }

    public TableColumn<UserProperty, Boolean> getEnabledColumn() {
        return enabledColumn;
    }

    public TableColumn<UserProperty, Boolean> getWriteableColumn() {
        return writeableColumn;
    }

    public TableColumn<UserProperty, Integer> getMaxIdleTimeColumn() {
        return maxIdleTimeColumn;
    }

    public TableColumn<UserProperty, String> getHomeDirectoryColumn() {
        return homeDirectoryColumn;
    }

    public TableColumn<UserProperty, String> getPasswordColumn() {
        return passwordColumn;
    }

    public void add(UserProperty userProperty) {
        this.userTableView.getItems().add(userProperty);
    }

    public UserTableContextMenu getContextMenu() {
        return contextMenu;
    }

    public UserDialog getUserDialog() {
        return userDialog;
    }

    // 调用该方法，打开对话框用于添加用户权限信息
    public void toAdd(Alert alert) {
        // 判断对话框是否有效，这一步其实是废话，不需要判断也可以
        if (this.userDialog != null) {
            // 打开对话框
            Optional<ButtonType> buttonType = this.userDialog.showAndWait();
            // 判断按下的按钮是否是确定按钮
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                // 保存用户权限信息
                this.userDialog.save(this, alert);
            }
        }
    }
}
