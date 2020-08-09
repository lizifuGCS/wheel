package com.wheel.app.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Target注解指定注解能修饰的目标(这里表示只能标注在类及接口)
//@Retention用于指定该Annotation可以保留多长时间(RetentionPolicy.RUNTIME: 编译器将把注释记录在class文件中。当运行Java程序时，JVM也会保留注释，程序可以通过反射获取该注释。)
//@Documented用于指定该元Annotation修饰的Annotation类将被javadoc工具提取成文档
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
    /**
     * 表示给controller注册别名
     * @return
     */
    String value() default "";

}