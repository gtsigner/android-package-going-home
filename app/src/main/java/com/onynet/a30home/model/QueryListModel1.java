package com.onynet.a30home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 时 间: 2016/12/29 0029
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class QueryListModel1 {

    /**
     * static : {"total_count":"2","in_count":"0","p_num":"1"}
     * package_list : [{"id":"78","uid":"12","yun_no":"1256655668","sender_id":"29","company_id":"2","get_address":"看来-看来","get_phone":"13258668789","get_no":"101","get_name":"扣扣","package_type_id":"1","package_type_text":"","notice_wx_status":"0","notice_sms_status":"0","push_time":"1483002102","pop_time":"1483012190","out_img_id":"4","status":"5","company_name":"圆通快递","package_type_name":"A普通件","out_img_path":"./Public/uploads/package/2016-12-29/5864f85ec80f6.png"}]
     */

    @SerializedName("static")
    private StaticBean staticX;
    private List<PackageListBean> package_list;

    public StaticBean getStaticX() {
        return staticX;
    }

    public void setStaticX(StaticBean staticX) {
        this.staticX = staticX;
    }

    public List<PackageListBean> getPackage_list() {
        return package_list;
    }

    public void setPackage_list(List<PackageListBean> package_list) {
        this.package_list = package_list;
    }

    public static class StaticBean {
        /**
         * total_count : 2
         * in_count : 0
         * p_num : 1
         */

        private String total_count;
        private String in_count;
        private String p_num;

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public String getIn_count() {
            return in_count;
        }

        public void setIn_count(String in_count) {
            this.in_count = in_count;
        }

        public String getP_num() {
            return p_num;
        }

        public void setP_num(String p_num) {
            this.p_num = p_num;
        }
    }

    public static class PackageListBean {
        /**
         * id : 78
         * uid : 12
         * yun_no : 1256655668
         * sender_id : 29
         * company_id : 2
         * get_address : 看来-看来
         * get_phone : 13258668789
         * get_no : 101
         * get_name : 扣扣
         * package_type_id : 1
         * package_type_text :
         * notice_wx_status : 0
         * notice_sms_status : 0
         * push_time : 1483002102
         * pop_time : 1483012190
         * out_img_id : 4
         * status : 5
         * company_name : 圆通快递
         * package_type_name : A普通件
         * out_img_path : ./Public/uploads/package/2016-12-29/5864f85ec80f6.png
         * address_desc": "重庆市"
         */

        private String id;
        private String uid;
        private String yun_no;
        private String sender_id;
        private String company_id;
        private String get_address;
        private String get_phone;
        private String get_no;
        private String get_name;
        private String package_type_id;
        private String package_type_text;
        private String notice_wx_status;
        private String notice_sms_status;
        private String push_time;
        private String pop_time;
        private String out_img_id;
        private String status;
        private String company_name;
        private String package_type_name;
        private String out_img_path;
        private String address_desc;

        public PackageListBean() {
        }

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

        public String getYun_no() {
            return yun_no;
        }

        public void setYun_no(String yun_no) {
            this.yun_no = yun_no;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getGet_address() {
            return get_address;
        }

        public void setGet_address(String get_address) {
            this.get_address = get_address;
        }

        public String getGet_phone() {
            return get_phone;
        }

        public void setGet_phone(String get_phone) {
            this.get_phone = get_phone;
        }

        public String getGet_no() {
            return get_no;
        }

        public void setGet_no(String get_no) {
            this.get_no = get_no;
        }

        public String getGet_name() {
            return get_name;
        }

        public void setGet_name(String get_name) {
            this.get_name = get_name;
        }

        public String getPackage_type_id() {
            return package_type_id;
        }

        public void setPackage_type_id(String package_type_id) {
            this.package_type_id = package_type_id;
        }

        public String getPackage_type_text() {
            return package_type_text;
        }

        public void setPackage_type_text(String package_type_text) {
            this.package_type_text = package_type_text;
        }

        public String getNotice_wx_status() {
            return notice_wx_status;
        }

        public void setNotice_wx_status(String notice_wx_status) {
            this.notice_wx_status = notice_wx_status;
        }

        public String getNotice_sms_status() {
            return notice_sms_status;
        }

        public void setNotice_sms_status(String notice_sms_status) {
            this.notice_sms_status = notice_sms_status;
        }

        public String getPush_time() {
            return push_time;
        }

        public void setPush_time(String push_time) {
            this.push_time = push_time;
        }

        public String getPop_time() {
            return pop_time;
        }

        public void setPop_time(String pop_time) {
            this.pop_time = pop_time;
        }

        public String getOut_img_id() {
            return out_img_id;
        }

        public void setOut_img_id(String out_img_id) {
            this.out_img_id = out_img_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getPackage_type_name() {
            return package_type_name;
        }

        public void setPackage_type_name(String package_type_name) {
            this.package_type_name = package_type_name;
        }

        public String getOut_img_path() {
            return out_img_path;
        }

        public void setOut_img_path(String out_img_path) {
            this.out_img_path = out_img_path;
        }

        public String getAddress_desc() {
            return address_desc;
        }

        public void setAddress_desc(String address_desc) {
            this.address_desc = address_desc;
        }
    }
}
