package com.alibaba.csp.sentinel.dashboard.rule.nacos.degrade;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfig;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConsantTxt;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.RuleTypeEnum;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>>{


    @Autowired
    private ConfigService configService;

    @Autowired
    private NacosConfig<DegradeRuleEntity> nacosConfig;

    @Override
    public void publish (String app, List<DegradeRuleEntity> rules) throws Exception{
        AssertUtil.notEmpty(app, NacosConsantTxt.APP_NAME_NULL_ERROR);
        if (rules == null){
            return;
        }
        configService.publishConfig(app + NacosConsantTxt.SENTINEL_POSTFIX + RuleTypeEnum.DEGRADE.getPostfix(), NacosConsantTxt.GROUP_ID, nacosConfig.ruleEntityEncoder().convert(rules));
    }
}
