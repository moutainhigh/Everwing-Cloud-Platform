package com.everwing.coreservice.common.wy.entity.business.watermeter;
import com.everwing.coreservice.common.Page;
public class TcWaterMeterSearch extends TcWaterMeter {

    private static final long serialVersionUID = -8189726304849845243L;

    private Page page;

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public void setPage(Page page) { this.page = page;
    }

    @Override
    public String toString() {
        return "TcWaterMeterSearch{" +
                "page=" + page +
                '}';
    }
}
