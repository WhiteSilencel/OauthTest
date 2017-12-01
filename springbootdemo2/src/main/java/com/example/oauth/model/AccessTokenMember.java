package com.example.oauth.model;

/**
 * Created by LiMengXi on 2017/11/27.
 */
public class AccessTokenMember {

    /**
     * access_token : 8547808c3a3b4d20c6edc3fb43be6017
     * token_type : bearer
     * expires_in : 86400
     * refresh_token : 8d9bb885a1eeb03b1230900e442239d5ab60edffd662ae7a70c06adf8ae42e76
     * scope : user_info projects pull_requests issues notes keys hook groups gists
     * created_at : 1511750812
     */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private String scope;
    private int created_at;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }
}
