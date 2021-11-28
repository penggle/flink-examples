package com.penglecode.flink.common.model;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/24 21:58
 */
public class GoogleAccount {

    private String gmail;

    private String clientId;

    private String clientSecret;

    private String loginUrl;

    public GoogleAccount() {
    }

    public GoogleAccount(String gmail, String clientId, String clientSecret, String loginUrl) {
        this.gmail = gmail;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.loginUrl = loginUrl;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public String toString() {
        return "GoogleAccount{" +
                "gmail='" + gmail + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                '}';
    }
}
