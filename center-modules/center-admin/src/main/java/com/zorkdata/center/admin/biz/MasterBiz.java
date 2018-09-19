package com.zorkdata.center.admin.biz;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.mapper.MasterMapper;
import com.zorkdata.center.admin.vo.MasterInfoVo;
import com.zorkdata.center.admin.vo.MasterVo2;
import com.zorkdata.center.common.biz.BaseBiz;
import com.zorkdata.center.common.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class MasterBiz extends BaseBiz<MasterMapper, Master> {
    public Master getMasterByMasterId(Long masterId) {
        Master Master = new Master();
        Master.setMasterID(masterId);
        return mapper.selectOne(Master);
    }

    public List<Master> getMasterList() {
        return mapper.selectAll();
    }

    public void insertMaster(Master master) {
        mapper.insertMaster(master);
    }

    public void updateMaster(Master master) {
        super.updateSelectiveById(master);
    }

    public void deleteMaster(Long masterid) {
        super.deleteById(masterid);
    }

    public void deleteMasterByComputerID(Long computerID) {
        mapper.deleteByComputerID(computerID);
    }

    public void updataMasterState(Long masterid, Integer state) {
        Master master = getMasterByMasterId(masterid);
        master.setState(state);
        super.updateSelectiveById(master);
    }

    public Master selectMasterByComputerID(Long computerID) {
        return mapper.selectByComputerID(computerID);
    }

    public Master selectMasterByComputerIP(String computerIP) {
        return mapper.selectByComputerIP(computerIP);
    }

    public void deleteByComputerIds(Set<Long> computerIDlist) {
        mapper.deleteByComputerIds(computerIDlist);
    }

    public void updateMaster(Map<String, Object> map) {
        mapper.updateMaster(map);
    }

    public void updateMasterbyIP(Map<String, Object> map) {
        mapper.updateMasterbyIP(map);
    }

    public List<Map<String, Object>> getApiInfo() {
        return mapper.getApiInfo();
    }

    public List<MasterVo2> getMasterVo2() {
        return mapper.getMasterVo2();
    }

    public List<Computer> getMasterComputerByClusterID(Long clusterID){
        return mapper.getMasterComputerByClusterID(clusterID);
    }

    public MasterInfoVo getMasterIPAndOstypeByAgentName(String agentName) {
        return mapper.getMasterIPAndOstypeByAgentName(agentName);
    }
}
