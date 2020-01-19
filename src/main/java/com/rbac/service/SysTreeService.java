package com.rbac.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.rbac.dao.SysAclMapper;
import com.rbac.dao.SysAclModuleMapper;
import com.rbac.dao.SysDeptMapper;
import com.rbac.dto.AclDTO;
import com.rbac.dto.AclModuleLevelDTO;
import com.rbac.dto.DeptLevelDTO;
import com.rbac.handler.LevelHandler;
import com.rbac.model.SysAcl;
import com.rbac.model.SysAclModule;
import com.rbac.model.SysDept;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysTreeService {
    @Resource
    private SysDeptMapper deptMapper;
    @Resource
    private SysAclModuleMapper aclModuleMapper;
    @Autowired
    private SysCoreService sysCoreService;
    @Resource
    private SysAclMapper sysAclMapper;

    public List<AclModuleLevelDTO> roleTree(int roleId) {
        //1.当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        //2.当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        //3.当前系统所有的权限点
        List<AclDTO> aclDTOList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());

        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList) {
            AclDTO dto = AclDTO.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);//当前用户有该权限
            }
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);//当前角色该权限默认选中
            }
            aclDTOList.add(dto);
        }
        return aclListToTree(aclDTOList);
    }

    public List<AclModuleLevelDTO> aclListToTree(List<AclDTO> aclDTOList) {
        if (CollectionUtils.isEmpty(aclDTOList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDTO> aclModuleLevelList = moduleTree();
        Multimap<Integer, AclDTO> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDTO acl : aclDTOList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }
    //把权限点绑定到指定的权限模块上
    private void bindAclsWithOrder(List<AclModuleLevelDTO> aclModuleLevelList, Multimap<Integer, AclDTO> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for (AclModuleLevelDTO dto : aclModuleLevelList) {
            List<AclDTO> aclDTOList = (List<AclDTO>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDTOList)) {
                Collections.sort(aclDTOList,aclSeqComparator);
                dto.setAclDTOList(aclDTOList);
            }
            bindAclsWithOrder(dto.getAclModuleList(),moduleIdAclMap);
        }
    }

    public List<DeptLevelDTO> deptTree() {
        List<SysDept> deptList = deptMapper.getAllDept();
        List<DeptLevelDTO> deptLevelList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            //sysDept转为DeptLevelDTO copyProperties dto<-dept
            DeptLevelDTO dto = DeptLevelDTO.adapt(dept);
            deptLevelList.add(dto);
        }
        return deptListToTree(deptLevelList);
    }

    public List<DeptLevelDTO> deptListToTree(List<DeptLevelDTO> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        //level->{dept1,dept2,...} Map<String,List<Object>>
        Multimap<String, DeptLevelDTO> levelMultimap = ArrayListMultimap.create();
        List<DeptLevelDTO> rootList = Lists.newArrayList();
        for (DeptLevelDTO dto : deptLevelList) {
            levelMultimap.put(dto.getLevel(), dto);
            if (LevelHandler.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        //按照seq从小到大排序
        Collections.sort(rootList, deptSeqComparator);
        //递归生成树
        transformDeptTree(deptLevelList, LevelHandler.ROOT, levelMultimap);
        return rootList;
    }

    //level:0 , 0, all 0->0.0,0.2
    //level:0.1
    //level:0.2
    public void transformDeptTree(List<DeptLevelDTO> deptLevelList, String level, Multimap<String, DeptLevelDTO> deptLevelMultimap) {
        for (int i = 0; i < deptLevelList.size(); i++) {
            //遍历该层的每个元素
            DeptLevelDTO deptLevelDTO = deptLevelList.get(i);
            //处理当前层级的数据//
            String nextLevel = LevelHandler.calculateLevel(level, deptLevelDTO.getId());
            //处理下一层
            List<DeptLevelDTO> tempDeptList = (List<DeptLevelDTO>) deptLevelMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                //排序
                Collections.sort(tempDeptList, deptSeqComparator);
                /********************计算核心**********************/
                //设置下一层部门
                deptLevelDTO.setDeptList(tempDeptList);
                /*************************************************/
                //进入到下一层处理
                transformDeptTree(tempDeptList, nextLevel, deptLevelMultimap);
            }
        }
    }

    public Comparator<DeptLevelDTO> deptSeqComparator = new Comparator<DeptLevelDTO>() {
        @Override
        public int compare(DeptLevelDTO o1, DeptLevelDTO o2) {
            /*//o1,o2
            if (o1.getSeq() > o2.getSeq()) {
                return 1;
            } else if (o1.getSeq() < o2.getSeq()) {
                return -1;
            } else if (o1.getId() >= o2.getId()) {
                return 1;
            } else {
                return -1;
            }*/
            return o1.getSeq() - o2.getSeq();//当该方法返回正数时，升序，否则逆序
        }
    };

    public List<AclModuleLevelDTO> moduleTree() {
        List<SysAclModule> aclModules = aclModuleMapper.getAllModule();
        List<AclModuleLevelDTO> aclModuleLevelList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModules) {
            AclModuleLevelDTO dto = AclModuleLevelDTO.adapt(aclModule);
            aclModuleLevelList.add(dto);
        }
        return aclModuleListToTree(aclModuleLevelList);
    }

    public List<AclModuleLevelDTO> aclModuleListToTree(List<AclModuleLevelDTO> aclModuleLevelList) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return Lists.newArrayList();
        }
        //level->{module1,module2,...} Map<String,List<Object>> / Map<K, Collection<V>>
//        Multimap可以包含有几个重复Key的value，可put进入多个不同value但是相同的key，但是又不是让后面覆盖前面的内容
        Multimap<String, AclModuleLevelDTO> levelMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDTO> rootList = Lists.newArrayList();
        for (AclModuleLevelDTO dto : aclModuleLevelList) {
            //相同level的形成一个List
            levelMultimap.put(dto.getLevel(), dto);
            if (LevelHandler.ROOT.equals(dto.getLevel())) {
                //加入最顶层没有父级的level为0的
                rootList.add(dto);
            }
        }
        //todo check desc
        Collections.sort(rootList, moduleSeqComparator);
        transformModuleTree(aclModuleLevelList, LevelHandler.ROOT, levelMultimap);
        return rootList;
    }


    public void transformModuleTree(List<AclModuleLevelDTO> moduleLevelList, String level, Multimap<String, AclModuleLevelDTO> moduleLevelMultimap) {
        for (int i = 0; i < moduleLevelList.size(); i++) {
            AclModuleLevelDTO aclModuleLevelDTO = moduleLevelList.get(i);
            String nextLevel = LevelHandler.calculateLevel(level, aclModuleLevelDTO.getId());
            List<AclModuleLevelDTO> tempModuleList = (List<AclModuleLevelDTO>) moduleLevelMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempModuleList)) {
                Collections.sort(tempModuleList, moduleSeqComparator);
                aclModuleLevelDTO.setAclModuleList(tempModuleList);
                transformModuleTree(tempModuleList, nextLevel, moduleLevelMultimap);
            }//如果子levelList为空，即没有这个key的level,就不用再setSubList,该dto递归结束
        }
    }


    public Comparator<AclModuleLevelDTO> moduleSeqComparator = new Comparator<AclModuleLevelDTO>() {
        @Override
        public int compare(AclModuleLevelDTO o1, AclModuleLevelDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public Comparator<AclDTO> aclSeqComparator = new Comparator<AclDTO>() {
        @Override
        public int compare(AclDTO o1, AclDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}

