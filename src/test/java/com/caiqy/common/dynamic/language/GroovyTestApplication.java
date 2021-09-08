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

package com.caiqy.common.dynamic.language;

import com.caiqy.common.dynamic.language.annotation.EnableDynamicLanguage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 动态语言测试配置
 *
 * @author caiqy
 */
@SpringBootApplication
@EnableDynamicLanguage(basePackages = "com.caiqy.common.dynamic.language.service")
public class GroovyTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroovyTestApplication.class, args);
    }

}
