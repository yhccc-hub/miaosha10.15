package com.miaoshaProject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验方式并返回校验结果
    public ValidationResult validationResult(Object bean){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size() > 0){
            //有错误
            result.setHasError(true);
            constraintViolationSet.forEach(constraintViolation ->{
               String errMsg = constraintViolation.getMessage();
               String propertyName = constraintViolation.getPropertyPath().toString();
               result.getErrorMsgMap().put(propertyName, errMsg);
            });
        }
        return result;
    }





    /**
     * 初始化完成之后 回调这个函数
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //将hebernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
