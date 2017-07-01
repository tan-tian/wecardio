package com.hiteam.common.util.express;


public class Constats {
    /**快递100密钥*/
    protected static final String KD100_KEY = "588afcae3fabb1a6";

    /***
     * 查询返回的数据类型
     */
    protected enum QueryResultType{
        JSON,
        XML,
        HTML,
        TEXT
    }

    /***
     * 快递状态
     * 0：在途，即货物处于运输过程中；
     * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
     * 2：疑难，货物寄送过程出了问题；
     * 3：签收，收件人已签收；
     * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
     * 5：派件，即快递正在进行同城派件；
     * 6：退回，货物正处于退回发件人的途中；
     */
    public enum ExState{
        /**在途：即货物处于运输过程中*/
        OnWay,
        /**揽件，货物已由快递公司揽收并且产生了第一条跟踪信息*/
        Gather,
        /**疑难，货物寄送过程出了问题*/
        Bug,
        /**签收，收件人已签收*/
        Sign,
        /**退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收*/
        SignOut,
        /**派件，即快递正在进行同城派件*/
        Send,
        /**退回，货物正处于退回发件人的途中*/
        Back,
        /**未知状态*/
        Unknown
    }

    /**
     * 查询状态
     */
    public enum ExQueryStatus{
        /**查询无结果*/
        NoResult,
        /**查询成功*/
        Success,
        /**查询异常*/
        Error
    }
}
