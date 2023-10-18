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
    private Integer txNum;
    /**总节点数*/
    private Integer nodeNum;
    /**当天交易数*/
    private Integer todayTxNum;

    public ChainStatsInfo() {
    }

    public ChainStatsInfo(Integer blockHeight, Integer txNum, Integer nodeNum, Integer todayTxNum) {
        this.blockHeight = blockHeight;
        this.txNum = txNum;
        this.nodeNum = nodeNum;
        this.todayTxNum = todayTxNum;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Integer getTxNum() {
        return txNum;
    }

    public void setTxNum(Integer txNum) {
        this.txNum = txNum;
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
}
