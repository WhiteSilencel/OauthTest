package com.example.oauth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiMengXi on 2017/11/29.
 * 目的：获取授权用户的所有项目
 *
 * 响应体：
 * {
 "id": 2726122,
 "full_name": "limengxi/OauthDemo",
 "url": "https://gitee.com/api/v5/repos/limengxi/OauthDemo",
 "path": "OauthDemo",
 "name": "OauthDemo",
 "owner": {
 "id": 1647371,
 "login": "limengxi",
 "name": "limengxi",
 "avatar_url": "http://secure.gravatar.com/avatar/bf229f4003da3fe1b858cb761c2aafce?s=40&d=mm",
 "url": "https://gitee.com/api/v5/users/limengxi",
 "html_url": "https://gitee.com/limengxi",
 "followers_url": "https://gitee.com/api/v5/users/limengxi/followers",
 "following_url": "https://gitee.com/api/v5/users/limengxi/following_url{/other_user}",
 "gists_url": "https://gitee.com/api/v5/users/limengxi/gists{/gist_id}",
 "starred_url": "https://gitee.com/api/v5/users/limengxi/starred{/owner}{/repo}",
 "subscriptions_url": "https://gitee.com/api/v5/users/limengxi/subscriptions",
 "organizations_url": "https://gitee.com/api/v5/users/limengxi/orgs",
 "repos_url": "https://gitee.com/api/v5/users/limengxi/repos",
 "events_url": "https://gitee.com/api/v5/users/limengxi/events{/privacy}",
 "received_events_url": "https://gitee.com/api/v5/users/limengxi/received_events",
 "type": "User",
 "site_admin": false
 }
 */
public class AllProjects {
    /**
     * 只需获得id和path的值
     */
    @SerializedName("id")
    private int id;

    @SerializedName("path")
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
