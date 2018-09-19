package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.mapper.ComputerMapper;
import com.zorkdata.center.admin.rpc.service.Ifc.PermissionServiceIfc;
import com.zorkdata.center.admin.vo.ComputerTagVo;
import com.zorkdata.center.admin.vo.ComputerVo;
import com.zorkdata.center.admin.vo.ComputersSortByProductCode;
import com.zorkdata.center.admin.vo.WorkerNode;
import com.zorkdata.center.common.biz.BaseBiz;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class ComputerBiz extends BaseBiz<ComputerMapper, Computer> {
    @Autowired
    PermissionServiceIfc permissionServiceIfc;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Computer getComputerByComputerId(Long computerID) {
        return mapper.selectByPrimaryKey(computerID);
    }

    public Computer getComputerByAgentID(Long agetnID) {
        return mapper.getComputerByAgentID(agetnID);
    }

    public List<WorkerNode> getComputerAgentInfo() {
        return mapper.getComputerAgentInfo();
    }

    public List<Computer> getComputerByResourceID(Set<Long> resourceIds) {
        return mapper.getComputerByResourceID(resourceIds);
    }

    public void insertComputer(Computer computer) {
        super.insertSelective(computer);
    }

    public void updateComputer(Computer computer) {
        mapper.updateSelectiveById(computer);
    }

    public void deleteComputer(Long computerID) {

        mapper.deleteById(computerID);
    }

    public List<Computer> getComputerList(Long userId) {
        return permissionServiceIfc.getComputerByUserId(userId);
//        return mapper.selectAll();
    }

    public List<Computer> selectAllComputer() {
        return mapper.selectAllComputer();
    }

    public Computer getComputerByComputerIP(String computerIP) {
        return mapper.getComputerByComputerIP(computerIP);
    }

    public void deleteByIds(Set<Long> computerIDlist) {
        mapper.deleteByIds(computerIDlist);
    }

    @Override
    public void insertSelective(Computer entity) {
//        EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insertComputer(entity);
    }

    public List<ComputerVo> selectAllComputerAndAgent() {
        return mapper.selectAllComputerAndAgent();
    }

    public List<ComputerVo> getComputerAndAgentByResourceID(Set<Long> computerIds) {
        return mapper.getComputerAndAgentByResourceID(computerIds);
    }

    public List<ComputerTagVo> getComputerAndTag(Set<Long> comuputerIds) {
        if (comuputerIds != null && comuputerIds.size() != 0) {
            return mapper.getComputerAndTag(comuputerIds);
        } else {
            return mapper.getAllComputerAndTag();
        }

    }

    public void insertBatch(List<Computer> computers) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        ComputerMapper mapper = sqlSession.getMapper(ComputerMapper.class);
        for (Computer computer : computers) {
            if(computer.getComputerType()==null){
                computer.setComputerType("windowscomputer");
            }
            mapper.insertBatch(computer);
        }
        sqlSession.commit();
    }

    public List<Computer> getComputersByIps(List<String> ips) {
        return mapper.getComputersByIps(ips);
    }

    //sql语句不支持find_in_set和其他条件一起查询
    public void updateComputerCiCodeBatch(List<Computer> computers) {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        ComputerMapper mapper = sqlSession.getMapper(ComputerMapper.class);
        for (Computer computer : computers) {
            if (computer.getIp() != null) {
                List<String> ips = new ArrayList<>();
                for (String ip : computer.getIp().split(",")) {
                    ips.add(ip);
                }
                List<Computer> computersByIps = mapper.getComputersByIps(ips);
                if (computersByIps != null && computersByIps.size() > 0) {
                    for (Computer oldComputer : computersByIps) {
                        if (oldComputer.getCiCode() == null) {
                            mapper.updateComputerCiCodeBatch(computer.getCiCode(), oldComputer.getComputerID());
                            break;
                        }
                    }
                }
            }
        }
        sqlSession.commit();
    }

    public List<ComputersSortByProductCode> getComputersSortByProductCode(String productCode){
        return mapper.getComputersSortByProductCode(productCode);
    }

}
