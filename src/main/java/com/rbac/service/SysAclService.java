package com.rbac.service;

import com.google.common.base.Preconditions;
import com.rbac.common.bean.PageResult;
import com.rbac.common.exception.ParamException;
import com.rbac.dao.SysAclMapper;
import com.rbac.model.SysAcl;
import com.rbac.param.AclParam;
import com.rbac.param.ListAclParam;
import com.rbac.util.BeanValidator;
import com.rbac.util.IPUtil;
import com.rbac.util.RequestHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class SysAclService {
    @Resource
    private SysAclMapper sysAclMapper;

    public boolean save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).seq(param.getSeq())
                .status(param.getStatus()).type(param.getType()).url(param.getUrl()).memo(param.getMemo()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUsername());
        acl.setOperatorTime(new Date());
        acl.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        return sysAclMapper.insertSelective(acl) == 1;
    }

    public SysAcl getAclByAclId(int id) {
        return sysAclMapper.selectByPrimaryKey(id);
    }

    public boolean update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");
        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).seq(param.getSeq())
                .status(param.getStatus()).type(param.getType()).url(param.getUrl()).memo(param.getMemo()).build();
        after.setOperator(RequestHolder.getCurrentUsername());
        after.setOperatorTime(new Date());
        after.setOperatorIp(IPUtil.getIpAddr(RequestHolder.getCurrentRequest()));
        return sysAclMapper.updateByPrimaryKeySelective(after) == 1;
    }

    //这里aclId不能为int,会爆空指针，因为null值并不能转化为基本类型0,而Integer包装类可以接收null值,
    public boolean checkExist(Integer aclModuleId, String aclName, Integer aclId) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, aclName, aclId) > 0;
    }

    public String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + new Random().nextInt(100);
    }

    public PageResult<SysAcl> getPageListByAclModuleId(ListAclParam listAclParam) {
        BeanValidator.check(listAclParam);
        int count = sysAclMapper.countByAclModuleId(listAclParam.getAclModuleId());
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(listAclParam);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().total(0).build();
    }

    public boolean delete(int id) {
        int res = sysAclMapper.updateStatusByPrimaryKey(id, 2);
        return res == 1;
    }
}
