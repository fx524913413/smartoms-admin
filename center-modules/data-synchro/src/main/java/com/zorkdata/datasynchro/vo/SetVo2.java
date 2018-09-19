package com.zorkdata.datasynchro.vo;

import java.util.List;

public class SetVo2 {

    private String setId;
    private String setName;
    private List<MoudleComputerVo> modules;

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public List<MoudleComputerVo> getModules() {
        return modules;
    }

    public void setModules(List<MoudleComputerVo> modules) {
        this.modules = modules;
    }
}
