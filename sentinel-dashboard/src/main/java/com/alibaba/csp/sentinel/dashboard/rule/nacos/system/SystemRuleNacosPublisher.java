package com.alibaba.csp.sentinel.dashboard.rule.nacos.system;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfig;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConsantTxt;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.RuleTypeEnum;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("systemRuleNacosPublisher")
public class SystemRuleNacosPublisher implements DynamicRulePublisher<List<SystemRuleEntity>>{


    @Autowired
    private ConfigService configService;

    @Autowired
    private NacosConfig<SystemRuleEntity> nacosConfig;

    @Override
    public void publish(String app, List<SystemRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, NacosConsantTxt.APP_NAME_NULL_ERROR);
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + NacosConsantTxt.SENTINEL_POSTFIX + RuleTypeEnum.SYSTEM.getPostfix(), NacosConsantTxt.GROUP_ID, nacosConfig.ruleEntityEncoder().convert(rules));
    }
}
