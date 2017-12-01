package com.example.oauth.utils;

import com.example.oauth.http.OkHttpUtils;
import com.example.oauth.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Response;
import org.apache.commons.httpclient.NameValuePair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiMengXi on 2017/11/30.
 */
public class GetMessage {
    /**
     * 获得授权用户的owner
     */
    public void getOwner(HttpSession session,String accessToken){
        //拼接url
        String url="https://gitee.com/api/v5/user";
        HashMap<String,String> hashMap=new HashMap<String,String>();
        hashMap.put("access_token",accessToken);
        //get请求，获取并处理响应
        OkHttpUtils okHttpUtils=OkHttpUtils.getInstance();
        Response  response =null;
        try {
            response = okHttpUtils.get(url,hashMap).execute();
            if(response.isSuccessful()){
                //获取json字符串
                String json=response.body().string();
                //将json字符串转换成对象，并获取owner值
                Gson gson=new Gson();
                Owner operationFile=gson.fromJson(json,Owner.class);
                String owner=operationFile.getOwner();

                session.setAttribute("owner",owner);
            }else{
                throw new IOException("Unexpected code:"+response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增文件
     * @param request
     * @param session
     * @param accessToken
     * @param projectPathName
     * @param path
     * @param content
     * @param message
     * @param owner
     * @return
     */
    public String addNewFileSubmit(HttpServletRequest request,HttpSession session,String accessToken,
                                   String projectPathName,String path,String content,String message,String owner) {
        //把URL拼接完整
        String url = "https://gitee.com/api/v5/repos/" +
                owner + "/" +
                projectPathName + "/" +
                "contents" + "/" +
                path;
        System.out.println(url);

        //构建请求参数
        HashMap<String, String> param = new HashMap<>(7);
        param.put("access_token", accessToken);
        param.put("content", content);
        param.put("message", message);

        //创建OKHttpUtils实例
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();

        //post同步请求execute()
        Response response = null;
        try {
            response = okHttpUtils.post(url, param).execute();
            if (response.isSuccessful()) {
                return "addNewFileSuccess";
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    /**
     * 列出授权用户的所有项目
     * @param session
     * @return
     */
    public List<AllProjects> getAllProjects(HttpSession session) {
        //1.获取accessToken的值
        String accessToken = (String) session.getAttribute("accessToken");

        //2.拼接url
        String url = "https://gitee.com/api/v5/user/repos";

        //3.建立键值对
        HashMap<String, String> param = new HashMap<>();
        param.put("access_token", accessToken);
        param.put("sort", "full_name");
        param.put("page", "1");
        param.put("per_page", "20");

        // 创建OKHttpUtils实例
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();

        //获取及处理响应
        Response response = null;
        AllProjects allProjects = null;
        List<AllProjects> list = null;
        try {
            response = okHttpUtils.get(url, param).execute();
            if (response.isSuccessful()) {
                //获取Json
                String data = response.body().string();
                //将Json字符串转换成对象格式
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<AllProjects>>() {
                }.getType();
                List<AllProjects> allProjectsArray = gson.fromJson(data, type);
                //建立Map对象
                list = new ArrayList<>();
                //遍历对象数组
                for (int i = 0; i < allProjectsArray.size(); i++) {
                    allProjects = new AllProjects();

                    int projectId = allProjectsArray.get(i).getId();
                    String projectPath = allProjectsArray.get(i).getPath();
                    allProjects.setId(projectId);
                    allProjects.setPath(projectPath);

                    list.add(allProjects);
                }
                return list;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从项目中获得文件
     * @param request
     * @param session
     * @param projectPathName
     * @param sha
     * @return
     */
    public List<AllFilesChild> getFile(HttpServletRequest request, HttpSession session, String projectPathName, String sha) {
        //读取access令牌及其他信息
        String accessToken = (String) session.getAttribute("accessToken");
        String owner = (String)session.getAttribute("owner");
        //拼接URL
        String url = "https://gitee.com/api/v5/repos/" +
                owner + "/" +
                projectPathName + "/git/trees/" +
                sha;
        //建立键值对
        HashMap<String, String> param = new HashMap<>();
        param.put("access_token", accessToken);
        //创建OKHttpUtils实例
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();

        //获取及处理响应
        Response response = null;
        List<AllFilesChild> repoList = null;
        AllFilesChild allFilesChild = null;
        try {
            response = okHttpUtils.get(url, param).execute();
            if (response.isSuccessful()) {
                //ResponseBody
                String responseBody = response.body().string();
                //json转换成对象
                Gson gson = new Gson();
                AllFilesFather allFilesFather = gson.fromJson(responseBody, AllFilesFather.class);
                //遍历tree属性的集合
                List<AllFilesChild> allFilesChildren2 = allFilesFather.getTree();
                int size = allFilesChildren2.size();
                repoList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    allFilesChild = allFilesChildren2.get(i);
                    repoList.add(allFilesChild);
                }
                return repoList;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选择单个文件后，就可以对文件进行后续的更新
     * @param request
     * @param session
     * @param repo
     * @param sha
     * @return
     */
    public HashMap<String, String> getFileContent(HttpServletRequest request, HttpSession session,String repo, String sha) {
        //1.获取accessToken的值
        String accessToken = (String) session.getAttribute("accessToken");
        String owner = (String) session.getAttribute("owner");
        //2.拼接url地址  /v5/repos/{owner}/{repo}/git/blobs/{sha}
        String url = "https://gitee.com/api/v5/repos/" +
                owner + "/" +
                repo + "/" +
                "git" + "/" + "blobs" + "/" +
                sha;
        //3.存储键值对
        HashMap<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("owner", owner);
        params.put("repo",repo);
        params.put("sha",sha);
        //4.发送请求，处理响应
        Response response = null;
        GetFileContent getFileContent = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
            response = okHttpUtils.get(url, params).execute();
            if (response.isSuccessful()) {
                //获取json格式的字符串
                String respJson = response.body().string();
                //将json转换成对象
                Gson gson = new Gson();
                getFileContent = gson.fromJson(respJson, GetFileContent.class);
                //获取GetFileContent的content和sha值
                Base64Test base64Test = new Base64Test();
                String getContent = base64Test.base64ToString(getFileContent.getContent());
                String getSha = getFileContent.getSha();
                //放入Map
                map.put("content", getContent);
                map.put("sha", getSha);

                return map;
            } else {
                throw new IOException("Unexpected code" + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新文件
     * @param session
     * @param request
     * @param projectPathName
     * @param path
     * @param content
     * @param sha
     * @param message
     * @return
     */
    public String updateFile(HttpSession session, HttpServletRequest request,String projectPathName,String path,String content, String sha,String message){
        //1.获取accessToken
        String accessToken = (String)session.getAttribute("accessToken");
        String owner = (String) session.getAttribute("owner");
        //1.1转换content的值为base64
        content = new Base64Test().stringToBase64(content);
        //2.拼接url /v5/repos/{owner}/{repo}/contents/{path}
        String url = "https://gitee.com/api/v5/repos/"+
                owner+"/"+
                projectPathName+"/"+
                "contents/"+path;
        //3.存储键值对
        NameValuePair[] nameValuePairs=new NameValuePair[7];
        nameValuePairs[0]=new NameValuePair("access_token",accessToken);
        nameValuePairs[1]=new NameValuePair("owner", owner);
        nameValuePairs[2]=new NameValuePair("repo",projectPathName);
        nameValuePairs[3]=new NameValuePair("path",path);
        nameValuePairs[4]=new NameValuePair("content",content);
        nameValuePairs[5]=new NameValuePair("sha",sha);
        nameValuePairs[6]=new NameValuePair("message",message);

        //4.发送put请求，处理响应
        try {
            //获取json数据
            String putJson=OkHttpUtils.PUT(url,nameValuePairs);
            System.out.println(putJson);
            if(putJson.isEmpty()){
                return "error";
            }
            //json转换成对象
            Gson gson=new Gson();
            UpdateFileFather updateFileFather=gson.fromJson(putJson,UpdateFileFather.class);

            String getMessage=updateFileFather.getCommit().getMessage();
            if(!getMessage.isEmpty()&&getMessage.equals(message)){
                return "updateFileSuccess";
            }else{
                throw new IOException("Unexpected code"+putJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 删除文件
     * @param session
     * @param request
     * @param projectPathName
     * @param path
     * @param sha
     * @param message
     * @return
     */
    public String deleteFile(HttpSession session, HttpServletRequest request,String projectPathName,String path,String sha,String message){
        //1.获取accessToken
        String accessToken = (String) session.getAttribute("accessToken");
        String owner = (String) session.getAttribute("owner");
        //2.拼接url /v5/repos/{owner}/{repo}/contents/{path}
        String url="https://gitee.com/api/v5/repos/"+
                owner+"/"+
                projectPathName+"/"+
                "contents/"+path;
        //3.存储键值对
        HashMap<String,Object> hashMap=new HashMap<String,Object>();
        hashMap.put("access_token", accessToken);
        hashMap.put("owner", owner);
        hashMap.put("repo",projectPathName);
        hashMap.put("sha",sha);
        hashMap.put("message",message);
        //4.
        String deleteJson=OkHttpUtils.DELETE(url,hashMap);
        //5.
        Gson gson=new Gson();
        UpdateFileFather updateFileFather=gson.fromJson(deleteJson,UpdateFileFather.class);
        String getMessage=updateFileFather.getCommit().getMessage();
        if(!getMessage.isEmpty()&&getMessage.equals(message)){
            return "deleteSuccess";
        }else{
            return "error";
        }
    }
}
