package com.giant.ftp.server.property;

/**
 * 基本设置配置模型
 */
public class BaseSettingsModel {
    private int port;
    private int passiveStart;
    private int passiveArea;

    public BaseSettingsModel(int port, int passiveStart, int passiveArea) {
        this.port = port;
        this.passiveStart = passiveStart;
        this.passiveArea = passiveArea;
    }

    public BaseSettingsModel() {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPassiveStart() {
        return passiveStart;
    }

    public void setPassiveStart(int passiveStart) {
        this.passiveStart = passiveStart;
    }

    public int getPassiveArea() {
        return passiveArea;
    }

    public void setPassiveArea(int passiveArea) {
        this.passiveArea = passiveArea;
    }
}
