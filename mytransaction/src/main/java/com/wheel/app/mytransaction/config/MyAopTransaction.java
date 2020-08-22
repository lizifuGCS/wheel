package com.wheel.app.mytransaction.config;

import com.wheel.app.mytransaction.core.MyTransaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Method;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/22 11:09
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class MyAopTransaction {

    @Autowired
    private MyTransaction myTransaction;

    @Pointcut("execution(* com.wheel.app.mytransaction.dao..*(..))")
    public void pointcut(){
    }

    @Around(value = "pointcut()")
    public void around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        //1:获取代理对象的方法,判断该方法上是否加上注解
        boolean methodTranstation = getMethodTranstation(proceedingJoinPoint);
        if (methodTranstation){
            // 开启事务    调用方法之前执行
            log.info("进入事务");
            TransactionStatus transactionStatus = myTransaction.begin();
            try {
                // 代理调用方法
                proceedingJoinPoint.proceed();
                myTransaction.commit(transactionStatus);
                log.info("提交事务");
            } catch (Throwable throwable) {
                // 提交事务  调用方法之后执行
                myTransaction.rollback(transactionStatus);
                log.error("回滚事务");
                throwable.printStackTrace();
            }
        }else{
            // 代理调用方法
            proceedingJoinPoint.proceed();
        }

    }

    private boolean getMethodTranstation(ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        //方法签名
        String methodName = proceedingJoinPoint.getSignature().getName();
        //获取目标对象
        Class<?> classTarget = proceedingJoinPoint.getTarget().getClass();
        //获取目标对象类型
        Class<?>[] par = ((MethodSignature)proceedingJoinPoint.getSignature()).getParameterTypes();
        //获取目标对象方法
        Method objMethod = classTarget.getMethod(methodName,par);
        return objMethod.isAnnotationPresent(com.wheel.app.mytransaction.annotation.MyTransaction.class);
    }
}
