# spring dynamic language 扩展

> spring 中已经支持了动态语言的功能,但是有一个很大的缺点

1. 官方的功能只能够在文件或者xml中定义实现,相对来说不是那么的动态,运维存在相当大的压力,如果有集群,那么实现的更新就又是一个问题
2. 官方只能将bean定义在xml中,如果不想用xml的话就没法使用相应功能


# 编译&安装
> 之前一直用的自己的代码仓库，所以一直没有发布到中央仓库中去，感兴趣的同学可以install到本地试玩

```shell
mvn install
```

```xml
    <dependency>
        <groupId>com.caiqy</groupId>
        <artifactId>dynamic-language-spring-boot-starter</artifactId>
        <version>0.0.1</version>
    </dependency>
```

# 简单使用
```java
    /**
     * 开启动态语言功能
     */
    @SpringBootApplication
    @EnableDynamicLanguage(basePackages = "com.caiqy.common.dynamic.language.service")
    public class GroovyTestApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(GroovyTestApplication.class, args);
        }
    
    }
```

```java
    /**
     * 定义动态接口
     * 以下例子使用了http请求
     */
    @GroovyDynamicLanguageService(id = "userService", scriptSource = "http://192.168.50.143:8000/test2.groovy", refreshCheckDelay = 50)
    public interface UserService {
    
        String hello();
    
    }
```

> 最近重装了电脑，test2.groovy 的代码等一段时间再放