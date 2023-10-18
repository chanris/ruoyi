package com.ruoyi.common.config.chain;

import cn.hyperchain.sdk.provider.DefaultHttpProvider;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenyue7@foxmail.com
 * @date 18/10/2023
 * @description
 */
@Configuration
public class HyperchainConfig {

    private final String nodeUrl = "localhost:8081";

    @Bean
    DefaultHttpProvider defaultHttpProvider() {
        return new DefaultHttpProvider.Builder().setUrl(nodeUrl).build();
    }

    @Bean
    ProviderManager providerManager(DefaultHttpProvider defaultHttpProvider) {
        return ProviderManager.createManager(defaultHttpProvider);
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
