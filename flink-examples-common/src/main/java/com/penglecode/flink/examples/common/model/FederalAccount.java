package com.penglecode.flink.examples.common.model;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/24 22:20
 */
public class FederalAccount {

    private String userName;

    private String clientId;

    private String clientSecret;

    private String loginUrl;

    private String acctType;

    public FederalAccount() {
    }

    public FederalAccount(String userName, String clientId, String clientSecret, String loginUrl, String acctType) {
        this.userName = userName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.loginUrl = loginUrl;
        this.acctType = acctType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    @Override
    public String toString() {
        return "FederalAccount{" +
                "userName='" + userName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                ", acctType='" + acctType + '\'' +
                '}';
    }
}
