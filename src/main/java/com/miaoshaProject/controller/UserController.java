package com.miaoshaProject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaProject.controller.viewobject.UserVO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.response.CommonReturnType;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller("user")
@RequestMapping("/user")
//@CrossOrigin
//@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")

public class UserController extends BaseController{

    @Autowired
    private UserService  userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用戶登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 入参校验
        if(StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务.来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone, this.EncoderByMd5(password));

        //将登录凭证加入到用户登录成功的session中
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
        return CommonReturnType.creat(null);

    }

    private String EncoderByMd5(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder base64en = Base64.getEncoder();
        //加密字符串
        String newStr = base64en.encodeToString(md5.digest(password.getBytes("utf-8")));
        return newStr;
    }


    //用戶注冊接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Byte gender,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "age") Integer age
                                     ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String inSessionOptCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(!StringUtils.equals(otpCode, inSessionOptCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合！");
        }
        UserModel userModel = new UserModel();
        userModel.setAge(age);
        //密码是明文，所以要加密
        userModel.setEncrptPassword(this.EncoderByMd5(password));
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setName(name);
        userModel.setRegisterMode("byphone");
        userModel.setTelphone(telphone);


        userService.register(userModel);


        return CommonReturnType.creat(null);

    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;  // 随机数【10000，109999}
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联,使用httpsession的方式绑定他的手机号与OTPCODE
        //这里跨域请求问题
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        //将OTP验证码通过短信通道发送给用户， 省略
        System.out.println("tel = " + telphone + "& optCode = " + otpCode);
        return CommonReturnType.creat(null);

    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        //调用service服务获取对应的id的用户对象并返回给前端
        UserModel user = userService.getUserById(id);

        //若获取的对应用户信息不存在，
        if(user == null){
          //  user.setEncrptPassword("123");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        // 将核心领域模型使用的用户对象转换成可供UI使用的viewobject
        UserVO userVO = convertFromModel(user);
        //返回通用对象
        return CommonReturnType.creat(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
    }
