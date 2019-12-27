package com.rbac.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.rbac.dao.SysDeptMapper;
import com.rbac.dto.DeptLevelDTO;
import com.rbac.handler.LevelHandler;
import com.rbac.model.SysDept;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SysTreeService {
    @Resource
    private SysDeptMapper deptMapper;

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
            return o1.getSeq()-o2.getSeq();//当该方法返回正数时，以第一个参数大于第二个，反之亦然
        }
    };
}

