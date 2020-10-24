package com.giant.ftp.server.main;

import com.giant.ftp.server.filter.GiantSessionFilter;
import com.giant.ftp.server.property.OtherSettingsModel;
import com.giant.ftp.server.property.UserProperty;
import javafx.stage.Stage;
import org.apache.ftpserver.*;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.ipfilter.SessionFilter;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FTP服务器总线
 */
public class ServerMaster {
    public static Logger logger = Logger.getLogger(ServerMaster.class);

    /**
     * FTP服务器运行时所需要的API
     */
    private FtpServerFactory ftpServerFactory;
    private ListenerFactory listenerFactory;
    private PropertiesUserManagerFactory propertiesUserManagerFactory;
    private FtpServer ftpServer;

    // 当前类实例
    private static ServerMaster instance;

    private ServerMaster() {
        this.ftpServerFactory = new FtpServerFactory();
        this.listenerFactory = new ListenerFactory();
        this.propertiesUserManagerFactory = new PropertiesUserManagerFactory();
    }

    // 获取当前类实例
    public static ServerMaster getInstance() {
        if (instance == null) {
            instance = new ServerMaster();
        }
        return instance;
    }

    public FtpServerFactory getFtpServerFactory() {
        return ftpServerFactory;
    }


    public ListenerFactory getListenerFactory() {
        return listenerFactory;
    }


    /**
     * 启动FTP服务器的核心方法
     *
     * @param baseUsers 用户权限集合
     * @param blackList 黑名单列表
     * @param model     其他设置模型
     * @throws FtpException 启动FTP失败抛出的异常
     */
    public void run(BaseUser[] baseUsers, String[] blackList, OtherSettingsModel model) throws FtpException {
        // 设置监听过滤器
        listenerFactory.setSessionFilter(resolveSessionFilter(blackList));
        // 设置默认监听
        ftpServerFactory.addListener("default", listenerFactory.createListener());
        // 设置用户集，resolveBaseUsers=>解析UserProperty
        ftpServerFactory.setUserManager(resolveBaseUsers(baseUsers));
        // 处理匿名登录
        resolveAnonymous(model);
        this.ftpServer = ftpServerFactory.createServer();
        this.ftpServer.start();

        logger.info("FTP server opening in port=" + listenerFactory.getPort());
    }

    /**
     * 关闭FTP服务器核心方法,重置工厂数据
     */
    public void stop() {
        if (this.ftpServer != null && !this.ftpServer.isStopped()) {
            this.ftpServer.stop();
            this.listenerFactory = new ListenerFactory();
            this.ftpServerFactory = new FtpServerFactory();
            logger.info("FTP server stopped,Data reset");
        }
    }

    // 处理匿名登录
    private void resolveAnonymous(OtherSettingsModel model) throws FtpException {
        // 不应当使用connectionConfig中的配置，当有该用户时，就会自动匿名登录
        boolean enabled = model.isEnabledAnonymousLogin();
        // 允许匿名登录的话，创建对象
        if (enabled) {
            BaseUser user = new BaseUser();
            user.setEnabled(true);
            user.setName("anonymous");
            // 判断是否可写
            if (model.isAnonymousLoginWriteable()) {
                // 增加写入权限
                List<Authority> authorityList = new ArrayList<>();
                authorityList.add(new WritePermission());
                user.setAuthorities(authorityList);
            }
            // 设置主目录
            user.setHomeDirectory(model.getAnonymousHomeDirectory());
            // 添加用户
            ftpServerFactory.getUserManager().save(user);

            logger.info("enable anonymous login:" + user);
        }
    }


    // 处理用户权限集合数据
    private UserManager resolveBaseUsers(BaseUser[] baseUsers) throws FtpException {
        // 创建用户管理器
        UserManager userManager = propertiesUserManagerFactory.createUserManager();
        // 遍历解析
        for (BaseUser baseUser : baseUsers) {
            if (baseUser instanceof UserProperty) {
                ((UserProperty) baseUser).resolve();
                logger.info("loading user:" + baseUser);
            }
            // 添加用户
            userManager.save(baseUser);
            logger.info("save user:" + baseUser);
        }
        // 返回用户管理器
        return userManager;
    }

    // 处理黑名单的SessionFilter
    private SessionFilter resolveSessionFilter(String[] blacks) {
        GiantSessionFilter sessionFilter = new GiantSessionFilter();
        sessionFilter.addAll(Arrays.asList(blacks));
        logger.info("resolve black list:" + Arrays.toString(blacks));
        return sessionFilter;
    }
}
