package com.ruoyi.common.config.chain;

import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 18/10/2023
 * @description
 */
@Configuration
public class HyperchainConfig {

    private static final Logger log = LoggerFactory.getLogger(HyperchainConfig.class);

    @Value("${hyperchain.nodes}")
    private String nodes;

    @Bean
    ProviderManager providerManager() {
        // 转化json数据
        List<String> urls = new ArrayList<>();
        ObjectMapper om = new ObjectMapper();
        try {
            urls = om.readValue(nodes, om.getTypeFactory().constructParametricType(List.class, String.class));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("msg.error.blockchainInitError: " + e.getMessage());
        }

        // 创建providerManager
        ProviderManager providerManager = null;
        if (urls != null && urls.size() != 0) {
            List<DefaultHttpProvider> httpProviders = new ArrayList<>(urls.size());
            urls.forEach(item -> httpProviders.add(new DefaultHttpProvider.Builder().setUrl(item).build()));
            providerManager = ProviderManager.createManager(httpProviders.toArray(new DefaultHttpProvider[urls.size()]));
        }else {
            throw new RuntimeException("msg.error.blockchainInitError: " +  "无节点可用");
        }
        return providerManager;
    }

    @Bean
    NodeService nodeService(ProviderManager providerManager) {
        return ServiceManager.getNodeService(providerManager);
    }

    @Bean
    TxService txService(ProviderManager providerManager) {
        return ServiceManager.getTxService(providerManager);
    }

    @Bean
    BlockService blockService(ProviderManager providerManager) {
        return ServiceManager.getBlockService(providerManager);
    }

    @Bean
    AccountService accountService(ProviderManager providerManager) {
        return ServiceManager.getAccountService(providerManager);
    }
}
