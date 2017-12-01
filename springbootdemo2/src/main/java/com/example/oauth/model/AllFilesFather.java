package com.example.oauth.model;

import java.util.List;

/**
 * 响应体:
 * "tree": [
 {
 "path": ".gitee",
 "mode": "040000",
 "type": "tree",
 "sha": "4bafccede93bb487cbb18f11510faeb8956c8f9c",
 "url": "https://gitee.com/api/v5/repos/limengxi/OauthDemo/git/trees/4bafccede93bb487cbb18f11510faeb8956c8f9c"
 },
 {
 "path": "1",
 "mode": "100644",
 "type": "blob",
 "sha": "274c0052dd5408f8ae2bc8440029ff67d79bc5c3",
 "size": 4,
 "url": "https://gitee.com/api/v5/repos/limengxi/OauthDemo/git/blobs/274c0052dd5408f8ae2bc8440029ff67d79bc5c3"
 }}]
 * Created by LiMengXi on 2017/11/29.
 */
public class AllFilesFather {
    private List<AllFilesChild> tree;

    public List<AllFilesChild> getTree() {
        return tree;
    }

    public void setTree(List<AllFilesChild> tree) {
        this.tree = tree;
    }
}
