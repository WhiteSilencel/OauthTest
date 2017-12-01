package com.example.oauth.controller;

import com.example.oauth.http.Constants;
import com.example.oauth.http.OkHttpUtils;
import com.example.oauth.model.AccessTokenMember;
import com.example.oauth.utils.GetMessage;
import com.google.gson.Gson;
import okhttp3.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author LiMengXi
 */
@Controller
@RequestMapping(value = "/gitee")
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView=new ModelAndView();
        String url = Constants.GET_CLIENT_CODE +
                "client_id=" + Constants.CLIENT_ID +
                "&redirect_uri=" + Constants.REDIRECT_URI +
                "&response_type=code";

        //RedirectView 是SpringMVC的重定向。
        //https://gitee.com/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code
        return new ModelAndView(new RedirectView(url));
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String getAccessToken(HttpServletRequest request, ModelMap model, HttpSession session) {
        ModelAndView modelAndView=new ModelAndView();

        try {
            //1.获取码云反馈的code
            String code = request.getParameter("code");
            System.out.println("code="+code);
            if (code == null || "".equals(code)) {
                throw new Exception("获取code失败");
            }

            //2.构建请求参数
            HashMap<String, String> param = new HashMap<>(5);
            param.put("grant_type", Constants.GRANT_TYPE);
            param.put("code", code);
            param.put("client_id", Constants.CLIENT_ID);
            param.put("redirect_uri", Constants.REDIRECT_URI);
            param.put("client_secret", Constants.CLIENT_SECRET);

            // 3.获取okHttp实例
            OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();

            //post同步请求execute()
            Response response = okHttpUtils.
                    post(Constants.POST_ACCESS_TOKEN, param).execute();
            if (response.isSuccessful()) {
                //获取tokenJson
                String data = response.body().string();
                System.out.print(data);

                Gson gson=new Gson();
                AccessTokenMember accessTokenMember=gson.fromJson(data,AccessTokenMember.class);
                String accessToken=accessTokenMember.getAccess_token();
                session.setAttribute("accessToken",accessToken);

                //将owner的值传入session
                GetMessage getMessage=new GetMessage();
                getMessage.getOwner(session,accessToken);

                return "loginSuccess";
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg","请求失败");
            model.addAttribute("access_token", e.toString());
        }
        return null;
    }
}
