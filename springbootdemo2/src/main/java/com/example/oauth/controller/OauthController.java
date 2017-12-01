package com.example.oauth.controller;

import com.example.oauth.dao.DiaryMapper;
import com.example.oauth.http.OkHttpUtils;
import com.example.oauth.model.AllFilesChild;
import com.example.oauth.model.AllProjects;
import com.example.oauth.model.Diary;
import com.example.oauth.utils.Base64Test;
import com.example.oauth.utils.GetMessage;
import com.example.oauth.utils.Owner;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiMengXi on 2017/11/27.
 */
@Controller
@RequestMapping(value="/oauth")
public class OauthController {

    @Autowired
    private DiaryMapper diaryMapper;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String returnLogin(){
        return "loginSuccess";
    }

    @RequestMapping(value="/getOwner",method=RequestMethod.GET)
    public void getOwner(HttpSession session){
        String accessToken=(String)session.getAttribute("accessToken");

    }

    @RequestMapping(value="/addNewFile",method = RequestMethod.GET)
    public String addNewFile(HttpSession session,HttpServletRequest request){
        List<AllProjects> list = new GetMessage().getAllProjects(session);
        if(list.size()==0){
            return "error";
        }
        request.setAttribute("lists",list);
        return "addNewFile";
    }

    @RequestMapping(value="/addNewFileSubmit",method = RequestMethod.POST)
    public String addNewFileSubmit(HttpServletRequest request,HttpSession session){
        //1.获取accessToken的值
        String accessToken=(String) session.getAttribute("accessToken");
        //获取前端传递的参数值
        String projectPathName = request.getParameter("projectPathName");
        String path = request.getParameter("path");
        String content = new Base64Test().stringToBase64(request.getParameter("content"));
        String message = request.getParameter("message");
        String owner = (String)session.getAttribute("owner");

        String result=new GetMessage().addNewFileSubmit(request,session,accessToken,projectPathName,path,content,message,owner);
        if(!result.isEmpty()&&result.equals("addNewFileSuccess")){
            //更新日志
            Diary diary=new Diary();
            diary.setName("Add New File,File's name is "+path);
            diary.setDate(new Date());
            diaryMapper.insertDiary(diary);

            return "addNewFileSuccess";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "/selectProject",method = RequestMethod.GET)
    public String selectProject(HttpSession session,HttpServletRequest request){
        List<AllProjects> list = new GetMessage().getAllProjects(session);
        if(list.size()==0){
            return "error";
        }
        request.setAttribute("lists",list);
        return "selectProject";
    }

    @RequestMapping(value = "/selectFile",method = RequestMethod.POST)
    public String selectFile(HttpServletRequest request,HttpSession session){
        //获取repo
        String projectPathName=request.getParameter("projectPathName");
        //获取sha
        String sha=request.getParameter("sha");
        session.setAttribute("projectSha",sha);
        //获取repo项目下的files
        List<AllFilesChild> fileList=new GetMessage().getFile(request,session,projectPathName,sha);
        if(fileList.size()==0){
            return "error";
        }
        request.setAttribute("projectPathName",projectPathName);
        request.setAttribute("fileList",fileList);
        return "selectFiles";
    }

    @RequestMapping(value = "/getContent",method = RequestMethod.GET)
    public String getContent(HttpServletRequest request,HttpSession session){
        //1.获得file名称（path）
        String repo=request.getParameter("repo");
        String sha=request.getParameter("sha");
        String path=request.getParameter("path");
        //获取文件的值
        HashMap<String,String> hashMap=new GetMessage().getFileContent(request,session,repo,sha);
        String pathContent=hashMap.get("content");
        if(pathContent.isEmpty()){
            return "error";
        }

        request.setAttribute("content",pathContent);
        request.setAttribute("path",path);
        request.setAttribute("sha",sha);
        return "appearFileContent";
    }

    @RequestMapping(value = "/updateFile",method = RequestMethod.GET)
    public String updateFile(HttpServletRequest request,HttpSession session){
        //1.获得file名称（path）
        String path=request.getParameter("path");
        String sha=request.getParameter("sha");
        String projectPathName=request.getParameter("projectPathName");
        //获取文件的值
        HashMap<String,String> hashMap=new GetMessage().getFileContent(request,session,projectPathName,sha);
        String getContent=hashMap.get("content");
        String getSha=hashMap.get("sha");
        if(getContent.isEmpty()||getSha.isEmpty()){
            return "error";
        }

        request.setAttribute("content",getContent);
        request.setAttribute("path",path);
        request.setAttribute("sha",getSha);
        request.setAttribute("projectPathName",projectPathName);

        return "updateFiles";
    }

    @RequestMapping(value="/updateFileSubmit",method = RequestMethod.POST)
    public String updateFileSubmit(HttpServletRequest request,HttpSession session){
        //1.获得file名称（path）
        String path=request.getParameter("path");
        String sha=request.getParameter("sha");
        String projectPathName=request.getParameter("projectPathName");
        String content=request.getParameter("content");
        String message=request.getParameter("message");

        String result=new GetMessage().updateFile(session,request,projectPathName,path,content,sha,message);
        if(!result.isEmpty()&&result.equals("updateFileSuccess")){
            //更新日志
            Diary diary=new Diary();
            diary.setName("Update One File,File's name is "+path);
            diary.setDate(new Date());
            diaryMapper.insertDiary(diary);
            //返回结果
            return "updateFileSuccess";
        }else {
            return "error";
        }
    }

    @RequestMapping(value="/deleteFile",method = RequestMethod.GET)
    public String deleteFile(HttpServletRequest request,HttpSession session){
        //1.获得file名称（path）
        String path=request.getParameter("path");
        String sha=request.getParameter("sha");
        String projectPathName=request.getParameter("projectPathName");
        String message=request.getParameter("message");
        //
        String deleteSuccess=new GetMessage().deleteFile(session,request,projectPathName,path,sha,message);
        if(!deleteSuccess.isEmpty()&&deleteSuccess.equals("deleteSuccess")){
            String projectSha=(String)session.getAttribute("projectSha");
            List<AllFilesChild> allFilesChildren=new GetMessage().getFile(request,session,projectPathName,projectSha);
            if(allFilesChildren.size()==0){
                return "error";
            }
            request.setAttribute("projectPathName",projectPathName);
            request.setAttribute("fileList",allFilesChildren);
            //更新日志
            Diary diary=new Diary();
            diary.setName("Delete One File,File's name is "+path);
            diary.setDate(new Date());
            diaryMapper.insertDiary(diary);
            //回到“获取某个项目下的所有文件页面”，相当于刷新
            return "selectFiles";
        }
        return "error";
    }
}
