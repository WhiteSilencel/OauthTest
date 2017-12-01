package com.example.oauth.http;

/**
 * 静态资源
 * @author LiMengXi
 */
public interface Constants {
    /**Client Id*/
    public static final String CLIENT_ID = "c9e14b1811c64b29361641993ed69d09a69a17fc61341610dfc5f14539992e38";
    /**Client Secret*/
    public static final String CLIENT_SECRET = "d7eae07b10ad888078de4cbbbd70bfec74bbf0d83d8cb20415598cae968aeb91";
    /**Redirect Uri*/
    public static final String REDIRECT_URI = "http://localhost:8080/gitee/redirect";
    /**Grant Type*/
    public static final String GRANT_TYPE = "authorization_code";
    /**获取code*/
    public static final String GET_CLIENT_CODE = "https://gitee.com/oauth/authorize?";
    /**获取ACCESS_TOKEN*/
    public static final String POST_ACCESS_TOKEN = "https://gitee.com/oauth/token?";
}
