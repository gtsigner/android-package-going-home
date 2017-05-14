package com.onynet.a30home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 时 间: 2016/12/30 0030
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class SmsListModel {

    /**
     * pools : [{"id":"11","order_no":"20161230GC309201998122d","uid":"12","money":"0.00","total_count":"20","last_count":"20","create_time":"1483092019","status":"1","pay_type":"0"}]
     * static : {"total_count":1}
     */

    @SerializedName("static")
    private StaticBean staticX;
    private List<PoolsBean> pools;

    public StaticBean getStaticX() {
        return staticX;
    }

    public void setStaticX(StaticBean staticX) {
        this.staticX = staticX;
    }

    public List<PoolsBean> getPools() {
        return pools;
    }

    public void setPools(List<PoolsBean> pools) {
        this.pools = pools;
    }

    public static class StaticBean {
        /**
         * total_count : 1
         */

        private int total_count;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }
    }

    public static class PoolsBean {
        /**
         * id : 11
         * order_no : 20161230GC309201998122d
         * uid : 12
         * money : 0.00
         * total_count : 20
         * last_count : 20
         * create_time : 1483092019
         * status : 1
         * pay_type : 0
         */

        private String id;
        private String order_no;
        private String uid;
        private String money;
        private String total_count;
        private String last_count;
        private String create_time;
        private String status;
        private String pay_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public String getLast_count() {
            return last_count;
        }

        public void setLast_count(String last_count) {
            this.last_count = last_count;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }
    }
}
