package com.miaoshaProject.service.model;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 这个model 里面的 属性 有一个加密信息，不应该返回给前端，所以要用MVC的原则
 */
public class UserModel {


    private Integer id;

    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotNull(message = "你没有性别吗？")
    private Byte gender;
    @NotNull(message = "你没有年龄吗？")
    @Min(value = 0,message = "年龄必须大于0")
    @Max(value = 150,message = "年龄必须小于150")
    private Integer age;

    @NotBlank(message = "手机号不能为空")
    private String telphone;

    private String registerMode;

    private String thirdPartyId;

    @NotBlank(message = "密码不能为空")
    private String encrptPassword;

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", telphone='" + telphone + '\'' +
                ", registerMode='" + registerMode + '\'' +
                ", thirdPartyId='" + thirdPartyId + '\'' +
                ", encrptPassword='" + encrptPassword + '\'' +
                '}';
    }
}
