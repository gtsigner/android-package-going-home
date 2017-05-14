package com.onynet.a30home.config;

/**
 * 时 间: 2016/12/27 0027
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class ApiConfig {

    //    public static final String API_HOST = "http://www.30dj.me/api.php";
//    public static final String APP_PATH = "http://www.30dj.me";
//    public static final String API_HOST = "http://10.1.56.117/client_300kd/api.php";
//    public static final String APP_PATH = "http://10.1.56.117/client_300kd";
//    public static final String API_HOST = "http://120.24.229.99/api.php";
//    public static final String APP_PATH = "http://120.24.229.99";
    public static final String API_HOST = "http://30dj.oeynet.com/api.php";
    public static final String API_INDEX = "http://30dj.oeynet.com/index.php";
    public static final String APP_PATH = "http://30dj.oeynet.com";

    /**
     * 帮助页面
     */
    public static final String HELP_PAGE = APP_PATH+"/index.php/pages/help";
    /**
     * 关于我们
     */
    public static final String ABOUT_US = APP_PATH+"/index.php/pages/about_us";
    /**
     * 移库说明
     */
    public static final String MOVE_HELP = APP_PATH+"/index.php/pages/move_help";
    /**
     * 登录获取token
     * 参数:
     * 1.username 用户名
     * 2.password 密码
     */
    public static final String GET_TOKEN = API_HOST + "/protal/getToken";
    /**
     * 获取账户信息
     */
    public static final String GET_WALLET = API_HOST + "/user/get_wallet";
    /**
     * 获取基础资料
     */
    public static final String GET_PROFILE = API_HOST + "/user/get_profile";
    /**
     * 获取消息资料
     */
    public static final String GET_NOTICE = API_HOST + "/user/get_notice";
    /**
     * 消息详情页
     */
    public static final String NOTICE = API_INDEX + "/app/notice/?id=";
    /**
     * 设置门店信息
     * 参数:
     * 1.sms_notice_status  是否短信通知：0,1
     * 2.wx_notice_status   是否微信通知：0,1
     * 3.open_date          营业时间
     * 4.token
     */
    public static final String SET_PROFILE = API_HOST + "/user/set_profile";

    /**
     * 获取快递公司列表
     */
    public static final String GET_COMPANY_LIST = API_HOST + "/express/get_company_list";

    /**
     * 获取包裹类型
     */
    public static final String GET_PACKAGE_TYPES = API_HOST + "/package/get_package_types";
    /**
     * 包裹入库
     * 参数:
     * 1.yun_no  		    运单号
     * 2.company_id         公司ID
     * 3.get_address        获取地址
     * 4.get_phone          手机号
     * 5.get_name           姓名
     * 6.get_no             取单号
     * 7.package_type_id    包裹类型
     */
    public static final String PUSH_PACKAGE = API_HOST + "/package/push_package";

    /**
     * 包裹出库
     * 参数:
     * 1.yun_no  		    运单号
     */
    public static final String POP_PACKAGE = API_HOST + "/package/pop_package";

    /**
     * 包裹出库
     * 参数:
     * 1.token  		    token
     * 2.get_name
     * 3.get_phone
     * 4.p
     * 5.yun_no
     * 6.get_address
     */
    public static final String QUERY_PACKAGE = API_HOST + "/package/search_package";
    public static final String GET_ALL_PACKAGE = API_HOST + "/package/get_all_package";
    /**
     * 自动匹配手机号
     */
    public static final String SEARCH_SENDER = API_HOST + "/package/search_sender";

    /**
     * 获取短信发送条数
     */
    public static final String GET_SMS_LIST = API_HOST + "/package/get_sms_list";
    /**
     * 发送短信
     */
    public static final String SEND_SMS = API_HOST + "/package/send_sms";
    /**
     * 获取短信充值信息
     */
    public static final String GET_POOL = API_HOST + "/sms/get_pool";

    /**
     * 获取短信模版列表
     */
    public static final String GET_TPL_LIST = API_HOST + "/sms/get_tpl_list";

    /**
     * 设置默认短信
     */
    public static final String SET_DEFAULT_TPL = API_HOST + "/sms/set_default_tpl";

    /**
     * 删除短信模版
     */
    public static final String DEL_TPL = API_HOST + "/sms/del_tpl";
    /**
     * 添加短信模版
     */
    public static final String ADD_TPL = API_HOST + "/sms/add_tpl";
    /**
     * 获取轮播图
     */
    public static final String GET_BANNER = API_HOST + "/app/get_banner";
    /**
     * 获取更新
     */
    public static final String GET_UPDATE = API_HOST + "/app/get_update";
    /**
     * 获取单个包裹信息
     */
    public static final String GET_PACKAGE = API_HOST + "/package/get_package";
    /**
     * 包裹移库
     */
    public static final String MOVE_PACKAGE = API_HOST + "/package/move_package";
    /**
     * 包裹移库
     */
    public static final String HANDLE_PACKAGE = API_HOST + "/package/handle_package";


}
