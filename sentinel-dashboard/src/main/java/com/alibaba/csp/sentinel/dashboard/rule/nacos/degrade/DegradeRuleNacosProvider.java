package com.alibaba.csp.sentinel.dashboard.rule.nacos.degrade;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfig;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConsantTxt;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.RuleTypeEnum;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("degradeRuleNacosProvider")
public class DegradeRuleNacosProvider implements DynamicRuleProvider<List<DegradeRuleEntity>>{


    @Autowired
    private ConfigService configService;

    @Autowired
    NacosConfig<DegradeRuleEntity> nacosConfig;

    @Override
    public List<DegradeRuleEntity> getRules (String appName) throws Exception{
        String rules = configService.getConfig(appName + NacosConsantTxt.SENTINEL_POSTFIX + RuleTypeEnum.DEGRADE.getPostfix(), NacosConsantTxt.GROUP_ID, 3000);
        if (StringUtil.isEmpty(rules)){
            return new ArrayList<>();
        }
        return nacosConfig.getRuleEntityDecoder(DegradeRuleEntity.class).convert(rules);
    }
}
