package com.zorkdata.datasynchro.vo;

import java.util.List;

public class ModuleVo {
    private long moduleId;
    private String moduleName;
    private List<ComputerVo> computers;

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<ComputerVo> getComputers() {
        return computers;
    }

    public void setComputers(List<ComputerVo> computers) {
        this.computers = computers;
    }
}
