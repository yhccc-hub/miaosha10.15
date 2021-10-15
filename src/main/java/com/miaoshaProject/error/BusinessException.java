package com.miaoshaProject.error;


//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    //用于构造业务异常
    public BusinessException(CommonError commonError) {
        //这里调用父类Exception 的方法，理由是 Exception 对自身有一些初始化的机制
        super();
        this.commonError = commonError;
    }

    //接收自定义errMsg的方式 构造业务异常
    public BusinessException(CommonError commonError,String errMsg) {
        //这里调用父类Exception 的方法，理由是 Exception 对自身有一些初始化的机制
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
         this.commonError.setErrMsg(errMsg);
         return this;

    }
}
