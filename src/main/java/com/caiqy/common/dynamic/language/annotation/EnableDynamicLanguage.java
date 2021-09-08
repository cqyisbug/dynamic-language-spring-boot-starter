/*
 * Copyright [2021] [caiqy]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caiqy.common.dynamic.language.annotation;

import com.caiqy.common.dynamic.language.register.DynamicLanguageRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用动态语言
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
