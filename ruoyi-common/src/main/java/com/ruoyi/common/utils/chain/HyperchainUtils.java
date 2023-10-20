package com.ruoyi.common.utils.chain;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.response.block.BlockNumberResponse;
import cn.hyperchain.sdk.response.node.NodeResponse;
import cn.hyperchain.sdk.response.tx.TxCountResponse;
import cn.hyperchain.sdk.response.tx.TxCountWithTSResponse;
import cn.hyperchain.sdk.response.tx.TxResponse;
import cn.hyperchain.sdk.service.*;
import cn.hyperchain.sdk.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    public Long getTxSum() {
        Request<TxCountWithTSResponse> request = txService.getTransactionsCount();
        try {
            TxCountWithTSResponse response = request.send();
            return response.getCode() == 0 ? hexStr2Long(response.getResult().getCount()) : 0;
        }catch (Exception e) {
            log.error("获得区块交易总量失败  exMsg: {}", e.getClass().getName() + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * todo 23/10/19 Method not found: the method tx_getTransactionsCountByTime does not exist/is not available
     * 获得当天的交易量
     * @return 交易量
     */
    public Integer getTxNumWithinToday() {
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
                BigInteger.valueOf(endTime.getTimeInMillis()).multiply(BigInteger.valueOf(1000000L)), 1);
        try {
            TxCountResponse response = request.send();
            Assert.state(0 == response.getCode(), "查询失败，请稍后重试");
            return hexStr2Int(response.getResult().getCount());
        }catch (Exception e) {
            log.error("获得当天交易总量失败  exMsg: {}", e.getClass().getName() + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 链上存储数据
     * @param data
     * @return
     */
    public String saveData(String data) {
        Account account = accountService.genAccount(Algo.SMRAW);
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).transfer(account.getAddress(), 0L).extra(data).build();
        transaction.sign(account);

        // 发送交易
        ReceiptResponse receiptResponse = null;
        try {
            receiptResponse = txService.sendTx(transaction).send().polling();
            Assert.state(0 == receiptResponse.getCode(), "上链交易处理失败，请稍后重试");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("数据上链失败，请稍后重试");
        }
        String txHash = receiptResponse.getTxHash();
        log.debug("上链成功, 交易txHash: {}", txHash);
        return txHash;
    }

    /**
     * 根据txHash获得交易信息
     * @return List<TxResponse.Transaction>
     */
    public List<TxResponse.Transaction> getTxInfoByHash(String txHash) {
        Request<TxResponse> request = txService.getTxByHash(txHash);
        TxResponse response = null;
        try {
            response = request.send();
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("查询失败，请稍后重试");
        }
        return response.getCode() == 0 ? response.getResult() : new ArrayList<>();
    }


    public static Integer hexStr2Int(String origin) {
        return Integer.valueOf(origin.substring(2), 16);
    }

    public static Long hexStr2Long(String origin) {
        return Long.valueOf(origin.substring(2), 16);
    }
}
