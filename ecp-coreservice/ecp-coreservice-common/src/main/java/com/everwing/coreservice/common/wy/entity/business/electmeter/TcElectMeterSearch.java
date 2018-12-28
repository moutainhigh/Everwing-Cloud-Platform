package com.everwing.coreservice.common.wy.entity.business.electmeter;
import com.everwing.coreservice.common.Page;

public class TcElectMeterSearch extends ElectMeter {

    private static final long serialVersionUID = -8189726404849845243L;

    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "TcElectMeterSearch{" +
                "page=" + page +
                '}';
    }
}
