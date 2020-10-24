package com.giant.ftp.server.controller;


import com.giant.ftp.server.property.UserProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * 编辑用户权限界面的控制器
 */
public class EditUserController {
    private static Logger logger = Logger.getLogger(EditUserController.class);
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField homeDirectoryField;
    @FXML
    private TextField maxIdleTimeField;
    @FXML
    private Button homeDirectoryBtn;
    @FXML
    private CheckBox writeableCheckBox;
    @FXML
    private CheckBox enabledCheckBox;

    /**
     * 提示信息常量
     */
    private static final String EDIT_USER_ERROR_HEADER = "创建用户错误";
    private static final String EDIT_USER_ERROR_NAME_PASSWORD_HOME_DIRECTORY_CONTENT = "用户名,密码不能为空,并且目录必须是一个存在的目录";

    public TextField getHomeDirectoryField() {
        return homeDirectoryField;
    }

    public Button getHomeDirectoryBtn() {
        return homeDirectoryBtn;
    }

    // 添加用户的核心方法
    public void save(UserSettingsController controller, Alert alert) {
        // 获取数据
        String name = nameField.getText();
        String password = passwordField.getText();
        String homeDirectory = homeDirectoryField.getText();
        String maxIdleTimeStr = maxIdleTimeField.getText();
        boolean writeable = writeableCheckBox.isSelected();
        boolean enabled = enabledCheckBox.isSelected();
        int maxIdleTime = 0;
        // 处理数据
        try {
            maxIdleTime = Integer.parseInt(maxIdleTimeStr);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        // 判断数据是否有效
        File homeDirectoryFile = new File(homeDirectory);
        if (name.isEmpty() || password.isEmpty() || !homeDirectoryFile.isDirectory() || !homeDirectoryFile.exists()) {
            alert.setHeaderText(EDIT_USER_ERROR_HEADER);
            alert.setContentText(EDIT_USER_ERROR_NAME_PASSWORD_HOME_DIRECTORY_CONTENT);
            alert.showAndWait();
            return;
        }
        // 创建用户
        UserProperty userProperty = new UserProperty();
        userProperty.setNameProperty(name);
        userProperty.setPasswordProperty(password);
        userProperty.setHomeDirectoryProperty(homeDirectory);
        userProperty.setEnabledProperty(enabled);
        userProperty.setWriteableProperty(writeable);
        userProperty.setMaxIdleTime(maxIdleTime);
        // 添加用户
        controller.add(userProperty);

        // 清空内容
        clear();
    }

    // 清空文本框以及复选框的内容
    private void clear() {
        this.nameField.setText("");
        this.passwordField.setText("");
        this.homeDirectoryField.setText("");
        this.writeableCheckBox.setSelected(false);
        this.enabledCheckBox.setSelected(false);
    }
}
