package com.caiqy.common.dynamic.language.service;

import com.caiqy.common.dynamic.language.annotation.GroovyDynamicLanguageService;

/**
 * Created on 2021/6/24.
 *
 * @author caiqy
 */
@GroovyDynamicLanguageService(id = "userService", scriptSource = "http://192.168.50.143:8000/test2.groovy", refreshCheckDelay = 50)
public interface UserService {

    String hello();

}
