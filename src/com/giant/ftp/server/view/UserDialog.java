package com.giant.ftp.server.view;

import com.giant.ftp.server.controller.EditUserController;
import com.giant.ftp.server.controller.UserSettingsController;
import com.giant.ftp.server.utils.GiantFxUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.giant.ftp.server.constant.FXMLConstant.FXML_FILE_EDIT_USER_PATH;
import static com.giant.ftp.server.constant.ImageConstant.getImage;
import static com.giant.ftp.server.constant.ImageConstant.IMAGE_HOME;

/**
 * 编辑用户信息的对话框，应用单例设计模式
 */
public class UserDialog extends Dialog<ButtonType> {
    private static Logger logger = Logger.getLogger(UserDialog.class);
    // 对话框控制器
    private EditUserController controller;
    // 当前类实例
    private static UserDialog instance;
    // 对话框标题
    private static final String TITLE = "用户权限表记录添加界面";
    // 选择目录对话框的标题
    private static final String CHOOSER_USER_HOME_DIRECTORY_TITLE = "用户主目录";

    // 主窗口程序实例，运行时注入
    private Stage stage;


    private UserDialog() throws IOException {
        logger.debug("user dialog will initial");
        // 初始化
        init();
        logger.debug("user dialog did initial");
    }

    private void init() throws IOException {
        // 初始化对话框
        initDialog();
        initEditUser(this.controller);
    }


    private void initDialog() throws IOException {
        FXMLLoader loader = GiantFxUtils.getFXMLLoader(FXML_FILE_EDIT_USER_PATH);
        Pane pane = loader.load();
        this.controller = loader.getController();
        this.setTitle(TITLE);
        this.getDialogPane().setContent(pane);
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);
        this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    }

    // 初始化编辑用户的组件
    private void initEditUser(EditUserController controller) {
        // 获取组件
        Button homeDirectoryBtn = controller.getHomeDirectoryBtn();
        TextField homeDirectoryField = controller.getHomeDirectoryField();
        // 设置图标
        homeDirectoryBtn.setGraphic(new ImageView(getImage(IMAGE_HOME)));
        // 设置点击按钮的时间
        homeDirectoryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chooserUserHomeDirectoryHandler(homeDirectoryField);
            }
        });
    }

    // 选择用户主目录的处理方法
    private void chooserUserHomeDirectoryHandler(TextField homeDirectoryField) {
        File directory = GiantFxUtils.chooserDirectory(null, CHOOSER_USER_HOME_DIRECTORY_TITLE,
                (File) null, this.stage);
        if (directory != null) {
            homeDirectoryField.setText(directory.getAbsolutePath());
        }
    }

    // 保存用户主方法
    public void save(UserSettingsController controller, Alert alert) {
        this.controller.save(controller, alert);
    }

    // 获取当前类单例实例的方法
    public static UserDialog getInstance() throws IOException {
        if (instance == null) {
            instance = new UserDialog();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
