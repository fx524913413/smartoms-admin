package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.entity.Master;
import com.zorkdata.center.admin.vo.MasterInfo;
import com.zorkdata.center.admin.vo.MasterVo;
import com.zorkdata.center.admin.vo.MasterVo2;

import java.util.List;

public interface MasterServiceIfc {
    MasterInfo findMasterinfo(Long masterinfo);

    void insertMasterEntity(Master master);

    void updateMasterEntity(Master master);

    void deleteMasterEntity(Long masterid);

    Boolean insMaster(MasterVo masterVo, Long clusterID, Long userID, String productCode);

    List<Long> batchInsMas(MasterVo masterVo, Long clusterID, Long userID, String productCode);

    void changeMasterState(Long masterid, Integer state);

    List<MasterVo2> findAllMaster(Long userId);

    String getCommand();

    void uninstallMaster(MasterVo masterVo);

}
