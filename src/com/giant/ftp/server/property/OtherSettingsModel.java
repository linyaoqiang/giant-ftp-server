package com.giant.ftp.server.property;

/**
 * 其他设置配置模型
 */
public class OtherSettingsModel {
    private int maxLogins;
    private int maxThreads;
    private int loginFailureDelay;
    private int maxLoginFailures;
    private boolean enabledAnonymousLogin;
    private int maxAnonymousLogins;
    private boolean anonymousLoginWriteable;
    private String anonymousHomeDirectory;


    public OtherSettingsModel(int maxLogins, int maxThreads, int loginFailureDelay, int maxLoginFailures, boolean enabledAnonymousLogin, int maxAnonymousLogins, boolean anonymousLoginWriteable,String anonymousHomeDirectory) {
        this.maxLogins = maxLogins;
        this.maxThreads = maxThreads;
        this.loginFailureDelay = loginFailureDelay;
        this.maxLoginFailures = maxLoginFailures;
        this.enabledAnonymousLogin = enabledAnonymousLogin;
        this.maxAnonymousLogins = maxAnonymousLogins;
        this.anonymousLoginWriteable = anonymousLoginWriteable;
        this.anonymousHomeDirectory = anonymousHomeDirectory;
    }

    public OtherSettingsModel() {
    }

    public int getMaxLogins() {
        return maxLogins;
    }

    public void setMaxLogins(int maxLogins) {
        this.maxLogins = maxLogins;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getLoginFailureDelay() {
        return loginFailureDelay;
    }

    public void setLoginFailureDelay(int loginFailureDelay) {
        this.loginFailureDelay = loginFailureDelay;
    }

    public int getMaxLoginFailures() {
        return maxLoginFailures;
    }

    public void setMaxLoginFailures(int maxLoginFailures) {
        this.maxLoginFailures = maxLoginFailures;
    }

    public boolean isEnabledAnonymousLogin() {
        return enabledAnonymousLogin;
    }

    public void setEnabledAnonymousLogin(boolean enabledAnonymousLogin) {
        this.enabledAnonymousLogin = enabledAnonymousLogin;
    }

    public int getMaxAnonymousLogins() {
        return maxAnonymousLogins;
    }

    public void setMaxAnonymousLogins(int maxAnonymousLogins) {
        this.maxAnonymousLogins = maxAnonymousLogins;
    }

    public boolean isAnonymousLoginWriteable() {
        return anonymousLoginWriteable;
    }

    public void setAnonymousLoginWriteable(boolean anonymousLoginWriteable) {
        this.anonymousLoginWriteable = anonymousLoginWriteable;
    }

    public String getAnonymousHomeDirectory() {
        return anonymousHomeDirectory;
    }

    public void setAnonymousHomeDirectory(String anonymousHomeDirectory) {
        this.anonymousHomeDirectory = anonymousHomeDirectory;
    }
}
