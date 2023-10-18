package com.ruoyi.common.utils.chain;

import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.block.BlockNumberResponse;
import cn.hyperchain.sdk.response.node.NodeResponse;
import cn.hyperchain.sdk.response.tx.TxCountResponse;
import cn.hyperchain.sdk.response.tx.TxCountWithTSResponse;
import cn.hyperchain.sdk.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * @author chenyue7@foxmail.com
 * @date 18/10/2023
 * @description 趣链工具类
 */
@Component
public class HyperchainUtils {

    private static final Logger log = LoggerFactory.getLogger(HyperchainUtils.class);

    @Resource
    private NodeService nodeService;

    @Resource
    private TxService txService;

    @Resource
    private BlockService blockService;

    @Resource
    private AccountService accountService;

    /**
     * 获得总节点数量
     * @return 节点数量
     */
    public Integer getNodeNum() {
        Request<NodeResponse> request = nodeService.getNodes();
        try {
            NodeResponse response = request.send();
            return response.getCode() == 0 ? response.getResult().size() : 0;
        }catch (Exception e) {
            log.error("获得节点数量失败 exMsg: {}", e.getClass().getName() + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获得区块高度
     * @return 区块高度
     */
    public Integer getBlockHeight() {
        Request<BlockNumberResponse> request = blockService.getChainHeight();
        try {
            BlockNumberResponse response = request.send();
            return response.getCode() == 0 ? hexStr2Int(response.getResult()) : 0;
        } catch (Exception e) {
            log.error("获得区块高度失败 exMsg: {}", e.getClass().getName() + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获得区块交易总量
     * @return 交易总量
     */
    public Integer getTxNum() {
        Request<TxCountWithTSResponse> request = txService.getTransactionsCount();
        try {
            TxCountWithTSResponse response = request.send();
            return response.getCode() == 0 ? hexStr2Int(response.getResult().getCount()) : 0;
        }catch (Exception e) {
            log.error("获得区块交易总量失败  exMsg: {}", e.getClass().getName() + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获得当天的交易量
     * @return 交易量
     */
    public String getTxNumWithinToday() {
        // 获得当天的起止时间
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MILLISECOND, 999);
        // 获得交易量
        Request<TxCountResponse> request = txService.getTxsCountByTime(BigInteger.valueOf(startTime.getTimeInMillis()).multiply(BigInteger.valueOf(1000000L)),
                BigInteger.valueOf(endTime.getTimeInMillis()).multiply(BigInteger.valueOf(1000000L)));
        try {
            TxCountResponse response = request.send();
            Assert.state(0 == response.getCode(), "查询失败，请稍后重试");
            return response.getResult();
        }catch (Exception e) {
            log.error("获得当天交易总量失败  exMsg: {}", e.getClass().getName() + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    public static Integer hexStr2Int(String origin) {
        return Integer.valueOf(origin.substring(2), 16);
    }

    public static Long hexStr2Long(String origin) {
        return Long.valueOf(origin.substring(2), 16);
    }
}
