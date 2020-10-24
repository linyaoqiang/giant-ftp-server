package com.giant.ftp.server.controller;

import com.giant.ftp.server.property.BaseSettingsModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;


/**
 * 基本设置面板的控制器
 */
public class BaseSettingsController {
    private static Logger logger = Logger.getLogger(BaseSettingsController.class);
    @FXML
    private TextField portField;
    @FXML
    private TextField passiveAreaField;
    @FXML
    private TextField passiveStartField;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button btn;

    @FXML
    private AnchorPane basePane;

    public TextField getPortField() {
        return portField;
    }

    public TextField getPassiveAreaField() {
        return passiveAreaField;
    }

    public TextField getPassiveStartField() {
        return passiveStartField;
    }

    public ImageView getLogoImageView() {
        return logoImageView;
    }

    public Button getBtn() {
        return btn;
    }

    public AnchorPane getBasePane() {
        return basePane;
    }

    // 将当前面板中的配置输出成配置模型
    public BaseSettingsModel toModel() {
        // 初始化数据
        int portN = 0;
        int passiveStartN = 0;
        int passiveAreaN = 0;
        // 处理数据
        try {
            String port = this.getPortField().getText();
            String passiveStart = this.getPassiveStartField().getText();
            String passiveArea = this.getPassiveAreaField().getText();
            portN = Integer.parseInt(port);
            passiveStartN = Integer.parseInt(passiveStart);
            passiveAreaN = Integer.parseInt(passiveArea);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        // 创建返回模型实例
        return new BaseSettingsModel(portN, passiveStartN, passiveAreaN);
    }


    // 禁用配置面板
    public void disableBasePane() {
        this.basePane.setDisable(true);
    }

    // 启用配置面板
    public void enableBasePane() {
        this.basePane.setDisable(false);
    }

}
