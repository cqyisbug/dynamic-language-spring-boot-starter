package com.caiqy.common.dynamic.language;

import com.caiqy.common.dynamic.language.annotation.EnableDynamicLanguage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created on 2021/6/24.
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
