package com.wheel.app.mytransaction.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/22 11:30
 * @Version 1.0
 */
@Component
@Slf4j
@Scope("prototype")   //每个事务都是新的，解决线程安全问题，多例
public class MyTransaction {

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 开启事务
     */
    public TransactionStatus begin(){
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("开启事务");
        // 得到事务状态
        return transaction;
    }

    /**
     * 提交事务
     */
    public void commit(TransactionStatus transaction){
       dataSourceTransactionManager.commit(transaction);
        log.info("提交事务");
    }

    /**
     * 回滚事务
     */
    public void rollback(TransactionStatus transaction){
        dataSourceTransactionManager.rollback(transaction);
        log.info("回滚事务");
    }
}
