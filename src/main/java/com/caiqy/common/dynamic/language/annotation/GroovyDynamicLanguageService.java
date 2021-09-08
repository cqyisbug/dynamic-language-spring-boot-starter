package com.caiqy.common.dynamic.language.annotation;

import java.lang.annotation.*;

/**
 * groovy 动态语言注解
 *
 * @author caiqy
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GroovyDynamicLanguageService {

    /**
     * bean 名称
     */
    String id() default "";

    /**
     * 脚本资源路径
     * 支持spring官方协议(eg. classpath:xxx.groovy)
     * 支持自定义协议 database: redis:
     */
    String scriptSource() default "";

    /**
     * 如果没有指定scriptSource,就读取inlineScript中的内容,不推荐这么使用,不符合Dynamic这个定义
     */
    String inlineScript() default "";

    /**
     * 更新检查延时
     */
    long refreshCheckDelay() default -1L;

    /**
     * groovy 自定义 bean 名称
     */
    String customizerRef() default "";

}
