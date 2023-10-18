package com.ruoyi.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author chenyue7@foxmail.com
 * @date 18/10/2023
 * @description
 */
public class ChainStatsInfo extends BaseEntity {
    /** 区块高度*/
    private Integer blockHeight;
    /** 总交易数*/
    private Long txSum;
    /**总节点数*/
    private Integer nodeNum;
    /**当天交易数*/
    private Integer todayTxNum;

    public ChainStatsInfo() {
    }

    public ChainStatsInfo(Integer blockHeight, Long txSum, Integer nodeNum, Integer todayTxNum) {
        this.blockHeight = blockHeight;
        this.txSum = txSum;
        this.nodeNum = nodeNum;
        this.todayTxNum = todayTxNum;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Long getTxSum() {
        return txSum;
    }

    public void setTxSum(Long txSum) {
        this.txSum = txSum;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Integer nodeNum) {
        this.nodeNum = nodeNum;
    }

    public Integer getTodayTxNum() {
        return todayTxNum;
    }

    public void setTodayTxNum(Integer todayTxNum) {
        this.todayTxNum = todayTxNum;
    }

    @Override
    public String toString() {
        return "ChainStatsInfo{" +
                "blockHeight=" + blockHeight +
                ", txSum=" + txSum +
                ", nodeNum=" + nodeNum +
                ", todayTxNum=" + todayTxNum +
                '}';
    }
}
