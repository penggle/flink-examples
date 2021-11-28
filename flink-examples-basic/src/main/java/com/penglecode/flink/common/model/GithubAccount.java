package com.penglecode.flink.common.model;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/24 22:13
 */
public class GithubAccount {

    private String userName;

    private String appId;

    private String appSecret;

    private String loginUrl;

    public GithubAccount() {
    }

    public GithubAccount(String userName, String appId, String appSecret, String loginUrl) {
        this.userName = userName;
        this.appId = appId;
        this.appSecret = appSecret;
        this.loginUrl = loginUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public String toString() {
        return "GithubAccount{" +
                "userName='" + userName + '\'' +
                ", appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                '}';
    }
}
