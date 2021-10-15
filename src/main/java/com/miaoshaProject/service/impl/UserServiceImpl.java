package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.UserDOMapper;
import com.miaoshaProject.dao.UserPasswordDOMapper;
import com.miaoshaProject.dataObject.UserDO;
import com.miaoshaProject.dataObject.UserPasswordDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserModel;
import com.miaoshaProject.validator.ValidationResult;
import com.miaoshaProject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.PhantomReference;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        //通过用户获取到 加密信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);
        return userModel;
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 这里导入apache。conmmons 的包， StringUtils 用来判断String类型的
//        if (StringUtils.isEmpty(userModel.getName())
//                                || userModel.getGender() == null
//                                || userModel.getAge() == null
//                                || StringUtils.isEmpty(userModel.getTelphone())){
//            try {
//                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//            } catch (BusinessException e) {
//                e.printStackTrace();
//            }
//        }
        ValidationResult result = validator.validationResult(userModel);
        if(result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }


        // 实现model -》 dataObject 方法，因为dao层认的是dataobject
        UserDO userDO = convertFromModel(userModel);
        //System.out.println("1");
        //这里和下面为什么用 insertSelective而不用insert
        // insert  如果有一个字段是null 的话，会直接覆盖数据库给的默认值
        // 而insertSelective 不会，如果是空的话，会赋默认值
        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复注册");
        }

        //System.out.println("2");

        //这里有问题，model里面没有id，所以导致后面的获取不到
        UserDO userDO1 = userDOMapper.selectBytelphone(userModel.getTelphone());
        System.out.println(userDO1.toString());
        userModel.setId(userDO1.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        //System.out.println("3");
        //插入失败，可以打印 123， 但是打印不了4，最后是库主键没有数据自增导致，因为一开始没有
        //   0，所以只成功了一次，后面再也没有成功，
        userPasswordDOMapper.insertSelective(userPasswordDO);
       // System.out.println("4");
        return;
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户手机获取用户信息
        // 去Mapper 中写sql 语句，通过电话查用户信息
        UserDO userDO = userDOMapper.selectBytelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO, userPasswordDO);

        //比较用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){

        if(userModel==null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        System.out.println(userModel.toString());
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId()); // userModel里面没有id

        return userPasswordDO;
    }



    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }



    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);

        if (userPasswordDO != null) {
            // 这里的密码为什么不能用copy方法，因为这里的password 这是一个属性，里面还有一个自增的主类
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }



}
