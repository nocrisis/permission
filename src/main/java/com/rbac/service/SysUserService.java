package com.rbac.service;

import com.rbac.common.PageResult;
import com.rbac.dao.SysUserMapper;
import com.rbac.model.SysUser;
import com.rbac.param.ListUserParam;
import com.rbac.param.UserParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    public SysUser getUserByUserId(Integer id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    public List<SysUser> getUsers(UserParam param) {
        Integer userId = param.getId();
        List<SysUser> users = null;
        if (userId != null) {//注册过查找
            SysUser user = sysUserMapper.selectByPrimaryKey(userId);
            users.add(user);
        } else if (StringUtils.isBlank(param.getTelephone()) && StringUtils.isBlank(param.getMail())) {
            //登录
            users = sysUserMapper.selectByPhoneOrMail(param);
        } else {//注册，model组合多种多样，因此不能用于判断手机号邮箱是否注册过
            users = sysUserMapper.selectByModel(param);
        }
        return users;
    }

    public boolean insert(SysUser user) {
        user.setOperatorTime(new Date());
        int res = sysUserMapper.insertSelective(user);
        return res == 1;
    }

    public boolean update(UserParam userParam) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userParam, user);
        user.setOperatorTime(new Date());
        int res = sysUserMapper.updateByPrimaryKey(user);
        return res == 1;
    }

    public boolean delete(Integer id) {
        int res = sysUserMapper.updateStatusByPrimaryKey(id, 2);
        return res == 1;
    }

    //冻结用户
    public boolean blocked(Integer id) {
        int res = sysUserMapper.updateStatusByPrimaryKey(id, 0);
        return res == 1;
    }

    public PageResult<SysUser> listUsers(ListUserParam param) {
        int size = sysUserMapper.countUsersByDeptId(param.getDeptId());
        if (size > 0) {
            List<SysUser> sysUsers = sysUserMapper.listUsersByDeptId(param);
            return PageResult.<SysUser>builder().data(sysUsers).total(size).build();
        } else {
            return PageResult.<SysUser>builder().total(0).build();
        }
    }

    public boolean checkNumberExits(String phone, String mail) {
        return sysUserMapper.checkMailIsExited(mail) + sysUserMapper.checkPhoneIsExited(phone) == 0;
    }

}
