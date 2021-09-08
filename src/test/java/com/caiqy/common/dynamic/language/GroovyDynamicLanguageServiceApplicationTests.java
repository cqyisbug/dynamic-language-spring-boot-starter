package com.caiqy.common.dynamic.language;

import com.caiqy.common.dynamic.language.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = GroovyTestApplication.class)
class GroovyDynamicLanguageServiceApplicationTests {

    @Autowired(required = false)
    private UserService userService;

    @Test
    void contextLoads() {
        if (userService != null) {
            System.out.println(userService.hello());
        }
    }

}
