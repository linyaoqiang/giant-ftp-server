package com.giant.ftp.server.controller;


import com.giant.ftp.server.property.OtherSettingsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

/**
 * 其他设置面板的控制器
 */
public class OtherSettingsController {
    private static Logger logger = Logger.getLogger(OtherSettingsController.class);
    @FXML
    private TextField maxLoginsField;
    @FXML
    private TextField maxThreadsField;
    @FXML
    private TextField loginFailureDelayField;
    @FXML
    private TextField maxLoginFailuresField;


    @FXML
    private CheckBox anonymousLoginableCheckBox;
    @FXML
    private AnchorPane anonymousLoginPane;
    @FXML
    private TextField maxAnonymousLoginsField;
    @FXML
    private TextField anonymousHomeDirectoryField;
    @FXML
    private Button anonymousHomeDirectoryBtn;

    @FXML
    private CheckBox anonymousLoginWriteableCheckBox;

    public TextField getMaxLoginsField() {
        return maxLoginsField;
    }

    public TextField getMaxThreadsField() {
        return maxThreadsField;
    }

    public TextField getLoginFailureDelayField() {
        return loginFailureDelayField;
    }

    public TextField getMaxLoginFailuresField() {
        return maxLoginFailuresField;
    }

    public CheckBox getAnonymousLoginableCheckBox() {
        return anonymousLoginableCheckBox;
    }

    public AnchorPane getAnonymousLoginPane() {
        return anonymousLoginPane;
    }

    public TextField getMaxAnonymousLoginsField() {
        return maxAnonymousLoginsField;
    }

    public CheckBox getAnonymousLoginWriteableCheckBox() {
        return anonymousLoginWriteableCheckBox;
    }

    public Button getAnonymousHomeDirectoryBtn() {
        return anonymousHomeDirectoryBtn;
    }

    public TextField getAnonymousHomeDirectoryField() {
        return anonymousHomeDirectoryField;
    }

    // 将当前面板中的配置输出成配置模型
    public OtherSettingsModel toModel() {
        // 初始化数据
        boolean enabledAnonymousLogin = false;
        boolean anonymousLoginWriteable = false;
        int maxLoginsNum = 0;
        int maxThreadsNum = 0;
        int maxLoginFailuresNum = 0;
        int loginFailureDelayNum = 0;
        int maxAnonymousLoginsNum = 0;
        String homeDirectory = anonymousHomeDirectoryField.getText();
        // 处理数据
        try {
            String maxLogins = this.getMaxLoginsField().getText();
            String maxThreads = this.getMaxThreadsField().getText();
            String loginFailureDelay = this.getLoginFailureDelayField().getText();
            String maxLoginFailures = this.getMaxLoginFailuresField().getText();
            enabledAnonymousLogin = this.getAnonymousLoginableCheckBox().isSelected();
            String maxAnonymousLogins = this.getMaxAnonymousLoginsField().getText();
            anonymousLoginWriteable = this.getAnonymousLoginWriteableCheckBox().isSelected();
            maxLoginsNum = Integer.parseInt(maxLogins);
            maxThreadsNum = Integer.parseInt(maxThreads);
            maxLoginFailuresNum = Integer.parseInt(maxLoginFailures);
            loginFailureDelayNum = Integer.parseInt(loginFailureDelay);
            maxAnonymousLoginsNum = Integer.parseInt(maxAnonymousLogins);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        // 创建返回配置模型对象
        return new OtherSettingsModel(maxLoginsNum, maxThreadsNum, loginFailureDelayNum, maxLoginFailuresNum, enabledAnonymousLogin, maxAnonymousLoginsNum, anonymousLoginWriteable, homeDirectory);
    }
}
