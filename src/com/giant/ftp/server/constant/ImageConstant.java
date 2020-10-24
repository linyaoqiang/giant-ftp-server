package com.giant.ftp.server.constant;

import javafx.scene.image.Image;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageConstant {
    private static Logger logger = Logger.getLogger(ImageConstant.class);
    // 图片的根目录
    private static final String BASE_IMAGE_PATH = "images";
    // 图片目录File对象
    private static final File BASE_IMAGE_FILE = new File(BASE_IMAGE_PATH);
    // 图片名称(Key)-图片的Image对象(Value)的映射容器
    private static final Map<String, Image> IMAGES = new HashMap<>();

    /**
     * 图标图片资源的获取名称
     */
    public static final String IMAGE_ADD_BTN = "add-btn";
    public static final String IMAGE_DEL_BTN = "del-btn";
    public static final String IMAGE_START_BTN = "start-btn";
    public static final String IMAGE_STOP_BTN = "stop-btn";

    public static final String IMAGE_USER = "user";
    public static final String IMAGE_HOME = "home";
    public static final String IMAGE_ICON = "icon";
    public static final String IMAGE_LOCK = "lock";
    public static final String IMAGE_LOGO = "logo";
    public static final String IMAGE_PASSWORD = "password";
    public static final String IMAGE_TIME = "time";
    public static final String IMAGE_WRITEABLE = "writeable";

    public static final String IMAGE_BASE_SETTINGS = "base-settings";
    public static final String IMAGE_USER_SETTINGS = "user-settings";
    public static final String IMAGE_BLACK_SETTINGS = "black-settings";
    public static final String IMAGE_OTHER_SETTINGS = "other-settings";


    static {
        /**
         * 初始化图片资源
         */
        File[] files = BASE_IMAGE_FILE.listFiles();
        assert files != null;
        for (File file : files) {
            String fileURI = file.toURI().toString();
            String[] strings = file.getName().split("\\.");
            IMAGES.put(strings[0], new Image(fileURI));
            logger.debug("loading image name=" + strings[0] + " image url=" + fileURI);
        }
    }

    // 获取图片实例的辅助方法
    public static Image getImage(String imageName) {
        return IMAGES.get(imageName);
    }
}
