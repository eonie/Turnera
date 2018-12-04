package org.turnera.server.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Created by guozp on 2017/8/28.
 *                       ???????
 */
//@Configuration //事务失效,都移动到一个方法不失效
//@Component // 事务可行，不用都移动到一个方法
public class TxOtherConfigDefaultBean {

    public static final String transactionExecution = "execution (* org.turnera..service.*.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    //@Bean
    //@ConditionalOnMissingBean
    //public PlatformTransactionManager transactionManager() {
    //    return new DataSourceTransactionManager(dataSource);
    //}


    @Bean
    public TransactionInterceptor transactionInterceptor() {
        Properties attributes = new Properties();
        attributes.setProperty("get*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        //TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager(), attributes);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, attributes);
        return txAdvice;
    }


    //@Bean
    //public AspectJExpressionPointcut aspectJExpressionPointcut(){
    //    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    //    pointcut.setExpression(transactionExecution);
    //    return pointcut;
    //}

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(){
        //AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        //pointcut.setExpression(transactionExecution);
        //DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        //advisor.setPointcut(pointcut);
        //advisor.setAdvice(transactionInterceptor());
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(transactionExecution);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        Properties attributes = new Properties();
        attributes.setProperty("get*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("find*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, attributes);
        advisor.setAdvice(txAdvice);
        return advisor;
    }
}