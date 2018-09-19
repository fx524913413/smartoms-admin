package com.zorkdata.center.admin.rpc.service.Impl;


import com.zorkdata.center.admin.entity.Computer;
import com.zorkdata.center.admin.rpc.service.Ifc.TestportServiceIfc;
import com.zorkdata.center.admin.vo.TestPortResults;
import com.zorkdata.center.admin.vo.TestPortResultsInSetup;
import com.zorkdata.center.common.util.NetTelnet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestportServiceImpl implements TestportServiceIfc {
    @Override
    public List<TestPortResults> test22port(String tarhosts) {
        String result = "";
        List<TestPortResults> results = new ArrayList<TestPortResults>();
        int port = 22;
        for (String tarhost : tarhosts.split(";")) {
            if (NetTelnet.telnet(tarhost, port)) {
                result = "connected";
                TestPortResults testPortResults = new TestPortResults();
                testPortResults.setIp(tarhost);
                testPortResults.setResult(result);
                results.add(testPortResults);
            } else {
                result = "can not connection";
                TestPortResults testPortResults = new TestPortResults();
                testPortResults.setIp(tarhost);
                testPortResults.setResult(result);
                results.add(testPortResults);
            }
            ;
        }
        return results;
    }

    @Override
    public List<TestPortResultsInSetup> testportinsetup(List<Computer> computerList) {
        List<TestPortResultsInSetup> results = new ArrayList<TestPortResultsInSetup>();
        for (Computer computer : computerList) {
            if (NetTelnet.telnet(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()), 22)) {
                TestPortResultsInSetup testPortResultsInSetup = new TestPortResultsInSetup();
                testPortResultsInSetup.setIp(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()));
                testPortResultsInSetup.setResult(true);
                testPortResultsInSetup.setPort(22);
                results.add(testPortResultsInSetup);
            } else {
                TestPortResultsInSetup testPortResultsInSetup = new TestPortResultsInSetup();
                testPortResultsInSetup.setIp(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()));
                testPortResultsInSetup.setResult(false);
                testPortResultsInSetup.setPort(22);
                results.add(testPortResultsInSetup);
            }
            if (NetTelnet.telnet(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()), 8000)) {
                TestPortResultsInSetup testPortResultsInSetup = new TestPortResultsInSetup();
                testPortResultsInSetup.setIp(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()));
                testPortResultsInSetup.setResult(true);
                testPortResultsInSetup.setPort(8000);
                results.add(testPortResultsInSetup);
            } else {
                TestPortResultsInSetup testPortResultsInSetup = new TestPortResultsInSetup();
                testPortResultsInSetup.setIp(NetTelnet.getConnectedIp(computer.getIp(), computer.getComputerType()));
                testPortResultsInSetup.setResult(false);
                testPortResultsInSetup.setPort(8000);
                results.add(testPortResultsInSetup);
            }
        }
        return results;
    }
}
