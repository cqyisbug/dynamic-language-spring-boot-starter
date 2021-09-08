package com.caiqy.common.dynamic.language.annotation;

import com.caiqy.common.dynamic.language.register.DynamicLanguageRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用动态语言注入
 *
 * @author caiqy
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DynamicLanguageRegister.class)
public @interface EnableDynamicLanguage {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @ComponentScan("cn.islaus")} instead of
     * {@code @ComponentScan(basePackages="cn.islaus")}.
     *
     * @return the array of 'basePackages'.
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.
     * <p>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     * <p>
     *
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};
}
