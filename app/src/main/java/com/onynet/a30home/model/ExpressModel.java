package com.onynet.a30home.model;

/**
 * 时 间: 2016/12/28 0028
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

/**
 * 快递公司数据模型
 */
public class ExpressModel {

    /**
     * id : 1
     * name : 申通快递
     * code : 1232222
     * create_time : 0
     * del_status : 0
     */

    private String id;
    private String name;
    private String code;
    private String create_time;
    private String del_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDel_status() {
        return del_status;
    }

    public void setDel_status(String del_status) {
        this.del_status = del_status;
    }
}
