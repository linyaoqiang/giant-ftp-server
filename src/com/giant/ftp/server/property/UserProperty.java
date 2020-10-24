package com.giant.ftp.server.property;

import javafx.beans.property.*;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户权限实体，继承至BaseUser
 */
public class UserProperty extends BaseUser {
    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty passwordProperty = new SimpleStringProperty();
    private BooleanProperty writeableProperty = new SimpleBooleanProperty();
    private StringProperty homeDirectoryProperty = new SimpleStringProperty();
    private IntegerProperty maxIdleTimeProperty = new SimpleIntegerProperty();
    private BooleanProperty enabledProperty = new SimpleBooleanProperty();


    public String getNameProperty() {
        return nameProperty.get();
    }

    public StringProperty namePropertyProperty() {
        return nameProperty;
    }

    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public String getPasswordProperty() {
        return passwordProperty.get();
    }

    public StringProperty passwordPropertyProperty() {
        return passwordProperty;
    }

    public void setPasswordProperty(String passwordProperty) {
        this.passwordProperty.set(passwordProperty);
    }

    public boolean isWriteableProperty() {
        return writeableProperty.get();
    }

    public BooleanProperty writeablePropertyProperty() {
        return writeableProperty;
    }

    public void setWriteableProperty(boolean writeableProperty) {
        this.writeableProperty.set(writeableProperty);
    }

    public String getHomeDirectoryProperty() {
        return homeDirectoryProperty.get();
    }

    public StringProperty homeDirectoryPropertyProperty() {
        return homeDirectoryProperty;
    }

    public void setHomeDirectoryProperty(String homeDirectoryProperty) {
        this.homeDirectoryProperty.set(homeDirectoryProperty);
    }

    public int getMaxIdleTimeProperty() {
        return maxIdleTimeProperty.get();
    }

    public IntegerProperty maxIdleTimePropertyProperty() {
        return maxIdleTimeProperty;
    }

    public void setMaxIdleTimeProperty(int maxIdleTimeProperty) {
        this.maxIdleTimeProperty.set(maxIdleTimeProperty);
    }

    public boolean isEnabledProperty() {
        return enabledProperty.get();
    }

    public BooleanProperty enabledPropertyProperty() {
        return enabledProperty;
    }

    public void setEnabledProperty(boolean enabledProperty) {
        this.enabledProperty.set(enabledProperty);
    }

    public void resolve() {
        this.setName(getNameProperty());
        this.setPassword(getPasswordProperty());
        int maxIdleTime = getMaxIdleTimeProperty();
        if (maxIdleTime > 0) {
            this.setMaxIdleTime(getMaxIdleTimeProperty());
        }
        if (isWriteableProperty()) {
            List<Authority> authorities = new ArrayList<>();
            authorities.add(new WritePermission());
            setAuthorities(authorities);
        } else {
            setAuthorities(null);
        }
        this.setEnabled(isEnabledProperty());
        this.setHomeDirectory(getHomeDirectoryProperty());
    }
}
