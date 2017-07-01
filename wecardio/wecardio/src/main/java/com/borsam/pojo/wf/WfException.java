package com.borsam.pojo.wf;

/**
 * 流程运行异常类
 * Created by Sebarswee on 2015/7/3.
 */
public class WfException extends RuntimeException {

    /**
     * 流程错误代码
     */
    public enum Code {
        invalidParam("1000", "参数错误"),
        undefineAct("1001", "未定义活动"),
        canFindActor("1002", "找不到参与者"),
        unexisWorkItem("1003", "当前工作项不存在"),
        workItemStatus("1004", "工作项状态异常"),
        wfError("2000", "流程错误");

        private String code;
        private String name;

        Code(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }

    private Code code;

    public WfException(Code code, String message) {
        super("error[" + code.getCode() + "] " + message);
        this.code = code;
    }

    public WfException(Code code, String message, Throwable cause) {
        super("error[" + code.getCode() + "] " + message, cause);
        this.code = code;
    }

    /**
     * 获取错误代码
     * @return 错误代码
     */
    public Code getCode() {
        return code;
    }

    /**
     * 设置错误代码
     * @param code 错误代码
     */
    public void setCode(Code code) {
        this.code = code;
    }
}
