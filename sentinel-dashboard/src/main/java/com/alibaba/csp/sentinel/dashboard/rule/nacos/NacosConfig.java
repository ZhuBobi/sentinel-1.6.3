package com.alibaba.csp.sentinel.dashboard.rule.nacos;


import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import java.util.List;

/**
 * @author zhubo
 */
@Configuration
public class NacosConfig<T>{

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

    /**
     * @return 规则编码器
     */
    @Bean
    public Converter<List<T>, String> ruleEntityEncoder() {
        return JSON::toJSONString;
    }

    /**
     * @return 规则解码器
     */
    public Converter<String, List<T>> getRuleEntityDecoder (Class<T> clz){
        return s -> JSON.parseArray(s, clz);
    }

//    /**
//     * -------------flow--------------
//     * @return flow 类规则编码器
//     */
//    @Bean
//    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
//        return s -> JSON.parseArray(s, FlowRuleEntity.class);
//    }
//
//    /**
//     * -------------authority--------------
//     * @return authority 类规则编码器
//     */
//    @Bean
//    public Converter<String, List<AuthorityRuleEntity>> authorityRuleEntityDecoder() {
//        return s -> JSON.parseArray(s, AuthorityRuleEntity.class);
//    }
//
//
//    /**
//     * -------------degrade--------------
//     * @return authority 类规则编码器
//     */
//    @Bean
//    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
//        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
//    }
//
//    /**
//     * -------------params--------------
//     * @return authority 类规则编码器
//     */
//    @Bean
//    public Converter<String, List<ParamFlowRuleEntity>> paramFlowRuleEntityDecoder() {
//        return s -> JSON.parseArray(s, ParamFlowRuleEntity.class);
//    }
//
//
//    /**
//     * -------------system--------------
//     * @return authority 类规则编码器
//     */
//    @Bean
//    public Converter<String, List<SystemRuleEntity>> systemRuleEntityDecoder() {
//        return s -> JSON.parseArray(s, SystemRuleEntity.class);
//    }


    @Bean
    public ConfigService nacosConfigService() throws Exception {
        Properties properties = new Properties();
        String address = "localhost";
        if(StringUtil.isNotEmpty(serverAddr)){
            address = serverAddr.split(":")[0];
        }
        properties.put(PropertyKeyConst.SERVER_ADDR, address);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        return ConfigFactory.createConfigService(properties);
    }

}
