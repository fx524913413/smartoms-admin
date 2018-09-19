package com.zorkdata.center.admin.mapper;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

@Repository
public interface ComputerMapper extends Mapper<Computer> {
    /**
     * 通过权限表的资源id查询主机
     *
     * @return
     */
    List<Computer> getComputerByResourceID(@Param("resourceIds") Set<Long> resourceIds);

    Computer getComputerByAgentID(@Param("agentID") Long agentID);

    List<WorkerNode> getComputerAgentInfo();

    Computer getComputerByComputerIP(String computerIP);

    void updateSelectiveById(Computer computer);

    void deleteById(Long computerID);

    void insertComputer(Computer computer);

    void deleteByIds(@Param("computerIDlist") Set<Long> computerIDlist);

    /**
     * 获取所有电脑主机并包含agent
     *
     * @return
     */
    List<ComputerVo> selectAllComputerAndAgent();

    /**
     * 获取用户下电脑主机并包含agent
     *
     * @return
     */
    List<ComputerVo> getComputerAndAgentByResourceID(@Param("computerIds") Set<Long> computerIds);

    List<Computer> selectAllComputer();

    /**
     * 获取选择的机器和所属的标签
     *
     * @param computerIds
     * @return
     */
    List<ComputerTagVo> getComputerAndTag(@Param("computerIds") Set<Long> computerIds);

    /**
     * 获取所有的机器和所属的标签
     *
     * @return
     */
    List<ComputerTagVo> getAllComputerAndTag();

    /**
     * 批量导入电脑数据
     *
     * @param computer
     */
    void insertBatch(Computer computer);

    /**
     * 根据ips获取符合条件的Computer
     *
     * @param ips
     * @return
     */
    List<Computer> getComputersByIps(@Param("ips") List<String> ips);

    void updateComputerCiCodeBatch(@Param("ciCode") String ciCode, @Param("computerID") Long computerID);

    List<ComputersSortByProductCode> getComputersSortByProductCode(@Param("productCode")String productCode);

}
