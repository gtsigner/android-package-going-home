package com.onynet.a30home.model;

/**
 * 时 间: 2016/12/31 0031
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class SmsTplListModel {


    /**
     * id : 22
     * uid : 7
     * tpl_content : 亲，您的{$company_name}包裹已到{$address}，请凭取货码{$get_no}速来领取，感谢您得合作，祝您生活愉快。
     * create_time : 1483086652
     * is_default : 1
     * u_mark : 备注信息
     * a_mark :
     * status : 1
     */

    private String id;
    private String uid;
    private String tpl_content;
    private String create_time;
    private String is_default;
    private String u_mark;
    private String a_mark;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTpl_content() {
        return tpl_content;
    }

    public void setTpl_content(String tpl_content) {
        this.tpl_content = tpl_content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getU_mark() {
        return u_mark;
    }

    public void setU_mark(String u_mark) {
        this.u_mark = u_mark;
    }

    public String getA_mark() {
        return a_mark;
    }

    public void setA_mark(String a_mark) {
        this.a_mark = a_mark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
