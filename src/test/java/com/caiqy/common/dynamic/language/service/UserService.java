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

package com.caiqy.common.dynamic.language.service;

import com.caiqy.common.dynamic.language.annotation.GroovyDynamicLanguageService;

/**
 * 启用动态语言功能，从source中加载代码
 *
 * @author caiqy
 */
@GroovyDynamicLanguageService(id = "userService", scriptSource = "http://192.168.3.29:8000/UserServiceImpl.groovy", refreshCheckDelay = 50)
public interface UserService {

    String hello(String name);

}
