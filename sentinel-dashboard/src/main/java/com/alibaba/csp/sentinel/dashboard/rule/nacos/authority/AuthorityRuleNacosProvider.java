package com.alibaba.csp.sentinel.dashboard.rule.nacos.authority;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
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

@Component("authorityRuleNacosProvider")
public class AuthorityRuleNacosProvider implements DynamicRuleProvider<List<AuthorityRuleEntity>>{


    @Autowired
    private ConfigService configService;

    @Autowired
    NacosConfig<AuthorityRuleEntity> nacosConfig;

    @Override
    public List<AuthorityRuleEntity> getRules (String appName) throws Exception{
        String rules = configService.getConfig(appName + NacosConsantTxt.SENTINEL_POSTFIX + RuleTypeEnum.AUTHORITY.getPostfix(), NacosConsantTxt.GROUP_ID, 3000);
        if (StringUtil.isEmpty(rules)){
            return new ArrayList<>();
        }
        return nacosConfig.getRuleEntityDecoder(AuthorityRuleEntity.class).convert(rules);
    }
}
