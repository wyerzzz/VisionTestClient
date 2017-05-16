package com.zzz.wyer.coolpc.wyerzzz_visiontest;

/**
 * Created by coolpc on 2017/5/14.
 */
public class RecordBean {
    private String c_id;



    private String c_dateTime;
    private String c_name;
    private String c_l_vision;
    private String c_r_vision;
    public RecordBean(){}

    public RecordBean(String c_id, String c_dateTime, String c_name, String c_l_vision, String c_r_vision) {
        this.c_dateTime = c_dateTime;
        this.c_l_vision = c_l_vision;
        this.c_name = c_name;
        this.c_r_vision = c_r_vision;
        this.c_id = c_id;
    }

    public String getC_dateTime() {
        return c_dateTime;
    }

    public void setC_dateTime(String c_dateTime) {
        this.c_dateTime = c_dateTime;
    }

    public String getC_l_vision() {
        return c_l_vision;
    }

    public void setC_l_vision(String c_l_vision) {
        this.c_l_vision = c_l_vision;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_r_vision() {
        return c_r_vision;
    }

    public void setC_r_vision(String c_r_vision) {
        this.c_r_vision = c_r_vision;
    }
    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }
}
