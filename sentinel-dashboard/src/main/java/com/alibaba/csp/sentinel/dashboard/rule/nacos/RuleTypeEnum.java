package com.alibaba.csp.sentinel.dashboard.rule.nacos;


public enum RuleTypeEnum {

    /**
     * 规则类型枚举
     */
    FLOW("flow","流控规则",0),
    DEGRADE("degrade","降级规则",1),
    PARAMS("params","热点规则",2),
    SYSTEM("system","系统规则",3),
    AUTHORITY("authority","授权规则",4)
    ;

    private final String postfix;
    private final String description;
    private final int code;

    RuleTypeEnum (String postfix, String description, int code){
        this.postfix = postfix;
        this.description = description;
        this.code = code;
    }

    public String getPostfix (){
        return postfix;
    }

    public String getDescription (){
        return description;
    }

    public int getCode (){
        return code;
    }
}
