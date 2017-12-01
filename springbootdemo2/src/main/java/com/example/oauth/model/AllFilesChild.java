package com.example.oauth.model;

/**
 * 响应体:
 * {
 "path": ".gitee",
 "mode": "040000",
 "type": "tree",
 "sha": "4bafccede93bb487cbb18f11510faeb8956c8f9c",
 "url": "https://gitee.com/api/v5/repos/limengxi/OauthDemo/git/trees/4bafccede93bb487cbb18f11510faeb8956c8f9c"
 }
 * Created by LiMengXi on 2017/11/29.
 */
public class AllFilesChild extends AllFilesFather {
    private String path;
    private String mode;
    private String type;
    private int size;
    private String sha;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
