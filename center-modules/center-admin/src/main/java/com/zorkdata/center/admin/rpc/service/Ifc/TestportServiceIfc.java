package com.zorkdata.center.admin.rpc.service.Ifc;

import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.vo.TestPortResults;
import com.zorkdata.center.admin.vo.TestPortResultsInSetup;

import java.util.List;

public interface TestportServiceIfc {
    List<TestPortResults> test22port(String tarhosts);

    List<TestPortResultsInSetup> testportinsetup(List<Computer> computerList);
}
