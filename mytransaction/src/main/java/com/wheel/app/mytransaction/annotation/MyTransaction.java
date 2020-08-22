package com.wheel.app.mytransaction.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @Author zifu.li@hand-china.com
 * @Date 2020/8/22 11:06
 * @Version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({PlatformTransactionManager.class})
public @interface MyTransaction {
}
