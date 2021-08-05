/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.property;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.util.StringUtil;

public class DynamicSentinelProperty<T> implements SentinelProperty<T> {

    protected Set<PropertyListener<T>> listeners = Collections.synchronizedSet(new HashSet<PropertyListener<T>>());
    private T value = null;

    public DynamicSentinelProperty() {
    }

    public DynamicSentinelProperty(T value) {
        super();
        this.value = value;
    }

    @Override
    public void addListener(PropertyListener<T> listener) {
        listeners.add(listener);
        listener.configLoad(value);
    }

    @Override
    public void removeListener(PropertyListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean updateValue(T newValue) {
        if (isEqual(value, newValue)) {
            return false;
        }

        newValue = getFlowRulesWithIp(newValue);
        RecordLog.info("[DynamicSentinelProperty] Config will be updated to: " + newValue);

        value = newValue;
        for (PropertyListener<T> listener : listeners) {
            listener.configUpdate(newValue);
        }
        return true;
    }

    private T getFlowRulesWithIp (T newValue){
        if(null == newValue){
            return null;
        }
        String ipAddress = getIpAddress();
        List rules = (List) newValue;
        List<FlowRule> flowRules = new ArrayList<>();
        for (Object rule : rules){
            if(rule instanceof FlowRule){
                FlowRule flowRule = (FlowRule)rule;
                if(flowRule.isApplyAll()){
                    flowRules.add(flowRule);
                    continue;
                }
                if(StringUtil.isEmpty(flowRule.getIp()) || flowRule.getIp().equals(ipAddress)){
                    flowRules.add(flowRule);
                }
            }
        }
        if(!flowRules.isEmpty()){
            newValue = (T)flowRules;
        }
        return newValue;
    }


    public String getIpAddress(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp()){
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()){
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address){
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e){
            RecordLog.info("[DynamicSentinelProperty] Fail to get ip address: " + e.getMessage());
            throw new RuntimeException("获取本地ip地址失败");
        }
        throw new RuntimeException("获取本地ip地址失败");
    }

    private boolean isEqual(T oldValue, T newValue) {
        if (oldValue == null && newValue == null) {
            return true;
        }

        if (oldValue == null) {
            return false;
        }

        return oldValue.equals(newValue);
    }

    public void close() {
        listeners.clear();
    }
}
