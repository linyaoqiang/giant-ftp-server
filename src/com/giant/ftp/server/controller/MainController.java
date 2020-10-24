package com.giant.ftp.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * 主界面控制器
 */
public class MainController {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab baseTab;
    @FXML
    private Tab userTab;
    @FXML
    private Tab blackTab;
    @FXML
    private Tab otherTab;


    public Tab getBaseTab() {
        return baseTab;
    }

    public Tab getBlackTab() {
        return blackTab;
    }

    public Tab getUserTab() {
        return userTab;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public Tab getOtherTab() {
        return otherTab;
    }


    // 禁用三个选项卡
    public void disableTabs() {
        this.userTab.setDisable(true);
        this.blackTab.setDisable(true);
        this.otherTab.setDisable(true);
    }

    // 启用三个选项卡
    public void enableTabs() {
        this.userTab.setDisable(false);
        this.blackTab.setDisable(false);
        this.otherTab.setDisable(false);
    }
}
