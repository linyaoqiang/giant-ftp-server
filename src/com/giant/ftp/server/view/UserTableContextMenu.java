package com.giant.ftp.server.view;

import com.giant.ftp.server.property.UserProperty;
import com.giant.ftp.server.utils.GiantFxUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

import java.io.File;

import static com.giant.ftp.server.constant.ImageConstant.*;

/**
 * 针对用户权限表自定义的上下文菜单组件
 */
public class UserTableContextMenu extends ContextMenu {
    private static Logger logger = Logger.getLogger(UserTableContextMenu.class);
    /**
     * 成员变量
     * (1) 删除菜单项
     * (2) 状态菜单项
     * (3) 写权限菜单项
     * (4) 选择主目录菜单项
     */
    private MenuItem deleteItem;
    private MenuItem stateItem;
    private MenuItem writeableItem;
    private MenuItem homeDirectoryItem;


    /**
     * 运行时进行注入的变量
     * (1) 用户权限表组件
     * (2) 当前用户记录
     */
    private TableView<UserProperty> userTableView;
    private UserProperty currentUser;


    /**
     * 菜单项内容常量
     */
    private static final String DELETE_ITEM_TEXT = "删除";
    private static final String CHOOSER_HOME_DIRECTORY_TEXT = "选择主目录";

    private static final String TO_ENABLE_TEXT = "禁用";
    private static final String TO_DISABLE_TEXT = "启用";

    private static final String TO_WRITEABLE_TEXT = "开启写权限";
    private static final String TO_UN_WRITEABLE_TEXT = "关闭写权限";

    /**
     * 选择主目录窗口时的标题
     */
    private static final String CHOOSER_HOME_DIRECTORY_TITLE = "用户主目录";

    // 父窗口
    private Stage stage;

    // 当前类实例，单例模式
    private static UserTableContextMenu instance;

    private UserTableContextMenu() {
        logger.debug("user table context menu will initial");
        // 初始化
        init();
        logger.debug("user table context menu did initial");
    }

    private void init() {
        // 初始化组件
        initComponents();

        // 初始化事件
        initAction();

        // 初始化图标
        initGraphic();
    }

    // 初始化组件
    private void initComponents() {
        // 生成菜单项，设置菜单项内容
        this.deleteItem = new MenuItem(DELETE_ITEM_TEXT);
        this.stateItem = new MenuItem();
        this.writeableItem = new MenuItem();
        this.homeDirectoryItem = new MenuItem(CHOOSER_HOME_DIRECTORY_TEXT);
        // 当前上下文菜单中添加菜单项
        this.getItems().addAll(deleteItem, stateItem, writeableItem, homeDirectoryItem);
    }

    // 初始化事件
    private void initAction() {
        // 准备显示ContextMenu时调用
        this.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                /**
                 * 获取当前TableView是索引值
                 *  (1) 索引值为-1,则TableView内容为空
                 *  (2) 索引值>=0,则TableView的焦点值为所在对象的内容
                 */
                // 获取焦点索引
                int focusedIndex = userTableView.getSelectionModel().getFocusedIndex();
                // 焦点索引的判断,赋值为当前用户数据
                if (focusedIndex >= 0) {
                    currentUser = userTableView.getItems().get(focusedIndex);
                } else {
                    currentUser = null;
                }
                // 对菜单项内容进行处理
                if (currentUser != null) {
                    if (currentUser.isEnabledProperty()) {
                        stateItem.setText(TO_ENABLE_TEXT);
                    } else {
                        stateItem.setText(TO_DISABLE_TEXT);
                    }

                    if (currentUser.isWriteableProperty()) {
                        writeableItem.setText(TO_UN_WRITEABLE_TEXT);
                    } else {
                        writeableItem.setText(TO_WRITEABLE_TEXT);
                    }
                }
            }
        });

        // 点击删除菜单时调用
        this.deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 判断并删除
                if (currentUser != null) {
                    userTableView.getItems().remove(currentUser);
                }
            }
        });

        // 点击用户状态菜单时调用
        this.stateItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 判断并改变其状态
                if (currentUser != null) {
                    currentUser.setEnabledProperty(!currentUser.isEnabledProperty());
                }
            }
        });

        // 点击写权限写权限状态时调用
        this.writeableItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // 判断并修改
                if (currentUser != null) {
                    currentUser.setWriteableProperty(!currentUser.isWriteableProperty());
                }
            }
        });
        this.homeDirectoryItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (currentUser != null) {
                    File directory = GiantFxUtils.chooserDirectory(null, CHOOSER_HOME_DIRECTORY_TITLE, (File) null, stage);
                    if (directory != null) {
                        currentUser.setHomeDirectoryProperty(directory.getAbsolutePath());
                    }
                }
            }
        });
    }

    // 初始化图标
    private void initGraphic() {
        this.deleteItem.setGraphic(new ImageView(getImage(IMAGE_DEL_BTN)));
        this.stateItem.setGraphic(new ImageView(getImage(IMAGE_LOCK)));
        this.writeableItem.setGraphic(new ImageView(getImage(IMAGE_WRITEABLE)));
        this.homeDirectoryItem.setGraphic(new ImageView(getImage(IMAGE_HOME)));
    }

    public void setUserTableView(TableView<UserProperty> userTableView) {
        this.userTableView = userTableView;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // 获取当前类实例
    public static UserTableContextMenu getInstance() {
        if (instance == null) {
            instance = new UserTableContextMenu();
        }
        return instance;
    }
}
