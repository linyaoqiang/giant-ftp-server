package com.giant.ftp.server.main;

import com.giant.ftp.server.controller.*;
import com.giant.ftp.server.property.BaseSettingsModel;
import com.giant.ftp.server.property.OtherSettingsModel;
import com.giant.ftp.server.property.UserProperty;
import com.giant.ftp.server.utils.GiantFxUtils;
import com.giant.ftp.server.view.UserTableContextMenu;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.log4j.Logger;

import static com.giant.ftp.server.constant.FXMLConstant.*;
import static com.giant.ftp.server.constant.ImageConstant.*;

import java.io.File;
import java.io.IOException;

/**
 * 窗口主程序类
 */
public class MainApplication extends Application {
    private static Logger logger = Logger.getLogger(MainApplication.class);
    /**
     * 控制器列表
     */
    private MainController mainController;
    private BaseSettingsController baseSettingsController;
    private UserSettingsController userSettingsController;
    private BlackSettingsController blackSettingsController;
    private OtherSettingsController otherSettingsController;


    /**
     * 服务器提示信息等
     */
    private static final String ANONYMOUS_CHOOSER_HOME_DIRECTORY = "匿名用户主目录";
    private static final String START_FTP_SERVER_ERROR_HEADER = "启动FTP服务器错误";
    private static final String START_FTP_SERVER_ERROR_PORT_CONTENT = "监听的端口必须是数字,并且端口必须大于0";
    private static final String CLOSE_STAGE_ERROR_HEADER = "关闭程序错误";
    private static final String CLOSE_STAGE_ERROR_STARING_CONTENT = "FTP服务器运行中,请关闭FTP服务器再关闭程序";

    /**
     * 启动服务器按钮的文字常量
     */
    private static final String FTP_SERVER_STARTING_TEXT = "关闭";
    private static final String FTP_SERVER_STOP_TEXT = "开启";

    /**
     * 窗口常量
     */
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;
    private static final boolean WINDOW_RESIZEABLE = false;
    private static final String WINDOW_TITLE = "FTP Server";


    /**
     * 成员变量
     * (1) 提示框
     * (2) 当前窗口
     * (3) 主面板
     */
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private Stage stage;
    private AnchorPane root;

    /**
     * 启动FX程序时执行
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 赋值为成员变量
        this.stage = primaryStage;

        logger.debug("Application Will initial Components");
        // 初始化组件
        initComponents();
        logger.debug("Application Did Initial Components");


        logger.debug("Application Will initial Stage");
        // 初始化窗口
        initStage(primaryStage);
        logger.debug("Application Did Initial Stage");

    }

    // 初始化窗口
    private void initStage(Stage primaryStage) {
        // 设置场景
        primaryStage.setScene(new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT));
        // 设置图标
        primaryStage.getIcons().add(getImage(IMAGE_ICON));
        // 设置窗口不可变大小
        primaryStage.setResizable(WINDOW_RESIZEABLE);
        // 设置关闭窗口请求时的处理
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                String text = baseSettingsController.getBtn().getText();
                // FTP服务器运行中，提示并阻止窗口关闭
                if (FTP_SERVER_STARTING_TEXT.equals(text)) {
                    // 阻止窗口关闭
                    event.consume();
                    primaryStage.show();

                    // 弹出提示
                    alert.setHeaderText(CLOSE_STAGE_ERROR_HEADER);
                    alert.setContentText(CLOSE_STAGE_ERROR_STARING_CONTENT);
                    alert.showAndWait();
                }
            }
        });
        // 设置标题
        primaryStage.setTitle(WINDOW_TITLE);
        // 显示窗口
        primaryStage.show();
    }

    // 初始化组件
    private void initComponents() throws IOException {

        // 加载主组件
        FXMLLoader loader = GiantFxUtils.getFXMLLoader(FXML_FILE_MAIN_PATH);
        this.root = loader.load();
        this.mainController = loader.getController();

        logger.debug("Application Will initial Tabs");
        // 初始化选项卡面板
        initTabs(this.mainController);
        logger.debug("Application Did initial Tabs");

        logger.debug("Application Will initial Base Settings");
        // 初始化基本设置
        initBaseSettings(this.baseSettingsController);
        logger.debug("Application Did initial Base Settings");

        logger.debug("Application Will initial User Settings");
        // 初始化用户设置
        initUserSettings(this.userSettingsController);
        logger.debug("Application Did initial User Settings");


        logger.debug("Application Will initial Black Settings");
        // 初始化黑名单设置
        initBlackSettings(this.blackSettingsController);
        logger.debug("Application Did initial Black Settings");


        logger.debug("Application Will initial Other Settings");
        // 初始化其他设置
        initOtherSettings(this.otherSettingsController);
        logger.debug("Application Did initial Other Settings");
    }

    // 开启FTP服务器的方法
    private boolean startFtp() {
        try {
            /**
             * 处理监听相关的内容
             */
            BaseSettingsModel baseSettingsModel = this.baseSettingsController.toModel();

            // 端口出现错误时，处理提示
            if (baseSettingsModel.getPort() <= 0) {
                // 错误提示
                this.alert.setHeaderText(START_FTP_SERVER_ERROR_HEADER);
                this.alert.setContentText(START_FTP_SERVER_ERROR_PORT_CONTENT);
                this.alert.showAndWait();
                // 返回启动状态值
                return false;
            }
            // 配置监听
            processListenerFactory(baseSettingsModel);

            /**
             * 获取用户权限信息
             */
            ObservableList<UserProperty> userList = this.userSettingsController.getUserTableView().getItems();
            BaseUser[] baseUsers = userList.toArray(new BaseUser[0]);

            /**
             * 获取黑名单数据
             */
            ObservableList<String> items = this.blackSettingsController.getBlackListView().getItems();
            String[] blacks = items.toArray(new String[0]);

            /**
             * 处理匿名登录及其他服务器配置
             */
            OtherSettingsModel otherSettingsModel = this.otherSettingsController.toModel();
            processOtherSettings(otherSettingsModel);

            /**
             * 开启服务器
             */
            ServerMaster.getInstance().run(baseUsers, blacks, otherSettingsModel);
            // 基本设置面板 => 禁用状态
            this.baseSettingsController.disableBasePane();
            // 其他选项卡 => 禁用状态
            this.mainController.disableTabs();

            // 返回启动状态值
            return true;
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 发送错误,提示信息
            this.alert.setHeaderText(START_FTP_SERVER_ERROR_HEADER);
            this.alert.setContentText(e.getMessage());
            this.alert.showAndWait();
        }
        // 返回启动状态值
        return false;

    }

    // 关闭FTP服务器的方法
    private void stopFtp() {
        // 关闭服务器
        ServerMaster.getInstance().stop();
        // 基本设置面板 => 可用状态
        this.baseSettingsController.enableBasePane();
        // 其他选项卡 => 可用状态
        this.mainController.enableTabs();
    }

    // 处理基本设置面板中配置，并配置监听
    private void processListenerFactory(BaseSettingsModel model) {
        // 获取监听工厂
        ListenerFactory listenerFactory = ServerMaster.getInstance().getListenerFactory();
        // 设置端口
        listenerFactory.setPort(model.getPort());
        // 获取被动端口起始端口和端口范围
        int passiveStart = model.getPassiveStart();
        int passiveArea = model.getPassiveArea();
        // 判断是否有效,进行配置
        if (passiveStart >= 0 && passiveArea > 1000) {
            // 创建数据链接工厂
            DataConnectionConfigurationFactory factory = new DataConnectionConfigurationFactory();
            // 设置被动端口
            factory.setPassivePorts(passiveStart + "-" + (passiveStart + passiveArea));
            // 监听中配置数据链接
            listenerFactory.setDataConnectionConfiguration(factory.createDataConnectionConfiguration());
        }
    }

    // 处理其他设置面板中配置
    private void processOtherSettings(OtherSettingsModel model) {
        // 创建ftp链接工厂
        ConnectionConfigFactory factory = new ConnectionConfigFactory();
        // 组装数据
        int maxLogins = model.getMaxLogins();
        if (maxLogins != 0) {
            factory.setMaxLogins(maxLogins);
        }
        int maxThreads = model.getMaxThreads();
        if (maxThreads != 0) {
            factory.setMaxThreads(maxThreads);
        }
        int maxLoginFailures = model.getMaxLoginFailures();
        if (maxLoginFailures != 0) {
            factory.setMaxLoginFailures(maxLoginFailures);
        }
        int maxAnonymousLogins = model.getMaxAnonymousLogins();
        if (maxAnonymousLogins != 0) {
            factory.setMaxAnonymousLogins(maxAnonymousLogins);
        }
        // 不能直接设置不允许匿名登录，不然匿名登录会直接失败，而不是弹出其他用户登录界面
        // factory.setAnonymousLoginEnabled(model.isEnabledAnonymousLogin());
        // 为ServerMaster实例中FtpServerFactory注入对应的配置
        ServerMaster.getInstance().getFtpServerFactory().setConnectionConfig(factory.createConnectionConfig());
    }

    // 初始化主界面选项卡面板
    private void initTabs(MainController controller) throws IOException {
        initTabs0(controller);
        initTabsGraphic(controller);
    }

    // 初始化主界面选项卡图标
    private void initTabsGraphic(MainController controller) {
        controller.getBaseTab().setGraphic(new ImageView(getImage(IMAGE_BASE_SETTINGS)));
        controller.getUserTab().setGraphic(new ImageView(getImage(IMAGE_USER_SETTINGS)));
        controller.getBlackTab().setGraphic(new ImageView(getImage(IMAGE_BLACK_SETTINGS)));
        controller.getOtherTab().setGraphic(new ImageView(getImage(IMAGE_OTHER_SETTINGS)));
    }

    // 初始化组件，绑定组件到对应的Tab中
    private void initTabs0(MainController controller) throws IOException {
        // 加载基本设置组件
        FXMLLoader loader = GiantFxUtils.getFXMLLoader(FXML_FILE_BASE_SETTINGS_PATH);
        // 加载获取面板
        Pane pane = loader.load();
        // 获取面板对应的控制器
        this.baseSettingsController = loader.getController();
        // 设置基本设置面板到对应的选项卡中
        controller.getBaseTab().setContent(pane);

        // 加载用户设置组件
        loader = GiantFxUtils.getFXMLLoader(FXML_FILE_USER_SETTINGS_PATH);
        pane = loader.load();
        this.userSettingsController = loader.getController();
        controller.getUserTab().setContent(pane);


        // 加载黑名单设置组件
        loader = GiantFxUtils.getFXMLLoader(FXML_FILE_BLACK_SETTINGS_PATH);
        pane = loader.load();
        this.blackSettingsController = loader.getController();
        controller.getBlackTab().setContent(pane);

        // 加载其他设置组件
        loader = GiantFxUtils.getFXMLLoader(FXML_FILE_OTHER_SETTINGS_PATH);
        pane = loader.load();
        this.otherSettingsController = loader.getController();
        controller.getOtherTab().setContent(pane);
    }

    // 初始化基本设置面板的UI界面和监听
    private void initBaseSettings(BaseSettingsController controller) {
        controller.getLogoImageView().setImage(getImage(IMAGE_LOGO));
        Button btn = controller.getBtn();
        btn.setGraphic(new ImageView(getImage(IMAGE_START_BTN)));
        ImageView graphic = (ImageView) btn.getGraphic();

        // 处理开启和关闭FTP服务器的事件
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (FTP_SERVER_STOP_TEXT.equals(btn.getText())) {
                    // FTP服务器开启成功，则修改状态
                    if (startFtp()) {
                        btn.setText(FTP_SERVER_STARTING_TEXT);
                        graphic.setImage(getImage(IMAGE_STOP_BTN));
                    }
                } else {
                    // 停止服务器
                    stopFtp();
                    // 修改状态
                    btn.setText(FTP_SERVER_STOP_TEXT);
                    graphic.setImage(getImage(IMAGE_START_BTN));
                }

            }
        });
    }

    // 初始其他设置面板的UI界面和监听
    public void initOtherSettings(OtherSettingsController controller) {
        // 是否匿名登录复选框
        CheckBox anonymousLoginableCheckBox = controller.getAnonymousLoginableCheckBox();
        // 匿名用户主目录选择按钮
        Button anonymousHomeDirectoryBtn = controller.getAnonymousHomeDirectoryBtn();
        // 匿名用户主目录按钮设置图标
        anonymousHomeDirectoryBtn.setGraphic(new ImageView(getImage(IMAGE_HOME)));
        // 匿名用户主目录文本框
        TextField anonymousHomeDirectoryField = controller.getAnonymousHomeDirectoryField();

        // 匿名登录配置的面板
        AnchorPane anonymousLoginPane = controller.getAnonymousLoginPane();

        // 默认是不可用的,默认复选框是不勾选的
        anonymousLoginPane.setDisable(!anonymousLoginableCheckBox.isSelected());

        // 监听复选框的勾选状态
        anonymousLoginableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // 设置匿名登录面板是否可用
                anonymousLoginPane.setDisable(!newValue);
            }
        });
        // 匿名用户选择目录时回调
        anonymousHomeDirectoryBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File directory = chooserDirectory(ANONYMOUS_CHOOSER_HOME_DIRECTORY);
                if (directory != null) {
                    anonymousHomeDirectoryField.setText(directory.getAbsolutePath());
                }
            }
        });
    }

    // 初始化黑名单界面图标
    public void initBlackSettings(BlackSettingsController controller) {
        // controller.getBlackListView().getItems().addAll("127.0.0.1", "192.168.43.189");
        controller.getDeleteBtn().setGraphic(new ImageView(getImage(IMAGE_DEL_BTN)));
        controller.getAddBtn().setGraphic(new ImageView(getImage(IMAGE_ADD_BTN)));
    }

    // 初始化用户权限表的列名对应的属性
    private void initUserTableColumnValue(UserSettingsController controller) {
        controller.getNameColumn().setCellValueFactory(new PropertyValueFactory<>("nameProperty"));
        controller.getPasswordColumn().setCellValueFactory(new PropertyValueFactory<>("passwordProperty"));
        controller.getWriteableColumn().setCellValueFactory(new PropertyValueFactory<>("writeableProperty"));
        controller.getMaxIdleTimeColumn().setCellValueFactory(new PropertyValueFactory<>("maxIdleTimeProperty"));
        controller.getHomeDirectoryColumn().setCellValueFactory(new PropertyValueFactory<>("homeDirectoryProperty"));
        controller.getEnabledColumn().setCellValueFactory(new PropertyValueFactory<>("enabledProperty"));

    }

    // 初始化用户权限表的列的图标
    private void initUserTableColumnGraphic(UserSettingsController controller) {
        controller.getNameColumn().setGraphic(new ImageView(getImage(IMAGE_USER)));
        controller.getPasswordColumn().setGraphic(new ImageView(getImage(IMAGE_PASSWORD)));
        controller.getWriteableColumn().setGraphic(new ImageView(getImage(IMAGE_WRITEABLE)));
        controller.getMaxIdleTimeColumn().setGraphic(new ImageView(getImage(IMAGE_TIME)));
        controller.getHomeDirectoryColumn().setGraphic(new ImageView(getImage(IMAGE_HOME)));
        controller.getEnabledColumn().setGraphic(new ImageView(getImage(IMAGE_LOCK)));
    }

    // 初始化用户权限表
    private void initUserTable(UserSettingsController controller) {
        initUserTableColumnValue(controller);
        initUserTableColumnGraphic(controller);
    }

    // 上下文菜单初始化和添加按钮的设置
    private void initUserSettings0(UserSettingsController controller) {
        controller.getUserDialog().setStage(this.stage);

        UserTableContextMenu contextMenu = controller.getContextMenu();
        TableView<UserProperty> tableView = controller.getUserTableView();
        Button addBtn = controller.getAddBtn();

        tableView.setContextMenu(contextMenu);
        contextMenu.setStage(this.stage);
        contextMenu.setUserTableView(tableView);
        addBtn.setGraphic(new ImageView(getImage(IMAGE_ADD_BTN)));
        // 设置添加事件
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.toAdd(alert);
            }
        });

    }

    // 初始化用户设置界面
    private void initUserSettings(UserSettingsController controller) {
        // 初始化上下文菜单和按钮
        initUserSettings0(controller);
        // 初始化用户权限表
        initUserTable(controller);
    }

    // 选择目录的辅助方法
    public File chooserDirectory(String title) {
        return GiantFxUtils.chooserDirectory(null, title, (File) null, this.stage);
    }
}
