package com.giant.ftp.server.utils;

import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;

public class GiantFxUtils {
    private static final DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();

    public static File chooserDirectory(DirectoryChooser chooser, String title, String initialDirectory, Window ownerWindow) {
        File initialFile = null;
        if (initialDirectory != null) {
            initialFile = new File(initialDirectory);
        }
        return chooserDirectory(chooser, title, initialFile, ownerWindow);
    }

    /**
     * 打开目录的静态方法
     *
     * @param chooser          目录选择器，一般传入null就可以了
     * @param title            目录选择器标题
     * @param initialDirectory 目录选择器的初始化目录
     * @param ownerWindow      父窗口,用于拟态窗口
     * @return File|null 选择的目录
     */
    public static File chooserDirectory(DirectoryChooser chooser, String title, File initialDirectory, Window ownerWindow) {
        if (chooser == null) {
            chooser = DIRECTORY_CHOOSER;
        }
        if (title != null) {
            chooser.setTitle(title);
        }
        if (initialDirectory != null) {
            chooser.setInitialDirectory(initialDirectory);
        }
        return chooser.showDialog(ownerWindow);
    }

    public static File chooserDirectory(DirectoryChooser chooser, String title, File initialDirectory) {
        return chooserDirectory(chooser, title, initialDirectory, null);
    }

    public static File chooserDirectory(DirectoryChooser chooser, String title) {
        return chooserDirectory(chooser, title, null);
    }

    // 加载fxml文件的工具静态方法
    public static FXMLLoader getFXMLLoader(String path) {
        return new FXMLLoader(GiantFxUtils.class.getClassLoader().getResource(path));
    }
}
