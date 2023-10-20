package com.ruoyi.web.controller.chain;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.chain.HyperchainUtils;
import com.ruoyi.domain.ChainStatsInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chenyue7@foxmail.com
 * @date 18/10/2023
 * @description
 */
@RestController
@RequestMapping("/chain")
class ChainOperateController {

    @Resource
    private HyperchainUtils hyperchainUtils;

    /**
     * 获得链统计信息
     * @return
     */
    @GetMapping("/getChainStatsInfo")
    public AjaxResult getChainStatsInfo() {
        ChainStatsInfo chainStatsInfo = new ChainStatsInfo();
        chainStatsInfo.setBlockHeight(hyperchainUtils.getBlockHeight());
        chainStatsInfo.setNodeNum(hyperchainUtils.getNodeNum());
        chainStatsInfo.setTxSum(hyperchainUtils.getTxSum());
//        chainStatsInfo.setTodayTxNum(hyperchainUtils.getTxNumWithinToday());
        return AjaxResult.success("success", chainStatsInfo);
    }

    /**
     *
     */
    @GetMapping("/getTxInfoByHash")
    public AjaxResult getTxInfoByHash(String txHash) {
        return AjaxResult.success("success", hyperchainUtils.getTxInfoByHash(txHash));
    }

    /**
     * 上链测试
     * @return
     */
    @PostMapping("/upChain")
    public AjaxResult upChain() {
        String data = "{\"msg\": \"测试上链\"}";
        String txHash = hyperchainUtils.saveData(data);
        System.out.println("txHash: " + txHash);
        return AjaxResult.success();
    }
}
