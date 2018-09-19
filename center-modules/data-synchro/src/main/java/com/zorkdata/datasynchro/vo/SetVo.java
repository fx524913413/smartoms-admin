package com.zorkdata.datasynchro.vo;

import java.util.List;

public class SetVo {

    private String setId;
    private String setName;
    private List<ModuleVo> modules;

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

    public List<ModuleVo> getModules() {
        return modules;
    }

    public void setModules(List<ModuleVo> modules) {
        this.modules = modules;
    }
}
