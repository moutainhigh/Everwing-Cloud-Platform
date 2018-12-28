package com.everwing.coreservice.common.platform.entity.extra;

import java.io.Serializable;

/**
 * Created by shiny on 2017/5/31.
 * 通过mysql 的序列来生成id
 */
public class IdGen implements Serializable{

    private int id;

    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IdGen{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
