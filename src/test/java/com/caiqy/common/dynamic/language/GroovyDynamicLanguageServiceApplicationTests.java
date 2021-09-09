/*
 * Copyright 2021 caiqy
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

import com.caiqy.common.dynamic.language.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 动态语言测试
 */
@SpringBootTest(classes = GroovyTestApplication.class)
class GroovyDynamicLanguageServiceApplicationTests {

    @Autowired(required = false)
    private UserService userService;

    @Test
    void contextLoads() {
        if (userService != null) {
            System.out.println(userService.hello("dynamic language"));
        }
    }

}
