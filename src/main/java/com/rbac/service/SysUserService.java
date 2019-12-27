package com.rbac.service;

import com.rbac.dao.SysUserMapper;
import com.rbac.model.SysUser;
import com.rbac.param.UserParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    public List<SysUser> getUsers(UserParam param) {
        Integer userId = param.getUserId();
        List<SysUser> users = null;
        if (userId != null) {
            SysUser user = sysUserMapper.selectByPrimaryKey(userId);
            users.add(user);
        } else if (StringUtils.isBlank(param.getTelephone()) && StringUtils.isBlank(param.getMail())) {
            users = sysUserMapper.selectByPhoneOrMail(param);
        } else {
            users = sysUserMapper.selectByModel(param);
        }
        return users;
    }

    public boolean insert(SysUser user) {
        user.setOperatorTime(new Date());
        int res = sysUserMapper.insertSelective(user);
        return res == 1;
    }

}