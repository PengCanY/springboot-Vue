package com.yuan.springboot.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createDocket01() {
        return new Docket(DocumentationType.OAS_30)// 指定 Swagger3 版本号
                .groupName("other 组")
                .enable(true)// 关闭文档
                .select()
                .paths(PathSelectors.ant("/other/**"))
                .build()
                .apiInfo(createApiInfo());
    }

    @Bean
    public Docket createDocket02() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("test 组")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yuan.springboot.controller"))
                .build()
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("Swagger3 文档案例")
                                .build()
                );
    }

    @Bean
    public ApiInfo createApiInfo() {
//        // 写法一
//        return new ApiInfoBuilder()
//                .title("Swagger3 文档案例")
//                .description("Swagger ：一套围绕 Open API 规范构建的一款 RESTful 接口的文档在线自动生成和功能测试 API 。")
//                .version("1.0.1")
//                .contact(
//                        // name url email
//                        new Contact("364.99°的文档", // 文档发布者名称
//                                "https://blog.csdn.net/m0_54355172", // 文档发布者的网站地址
//                                "2190826197@qq.com" // 文档发布者的邮箱
//                        )
//                )
//                .build();
        // 写法二
        return new ApiInfo(
                "Swagger3 文档案例",
                "Swagger ：一套围绕 Open API 规范构建的一款 RESTful 接口的文档在线自动生成和功能测试 API 。",
                "1.0.1",
                "https://blog.csdn.net/m0_54355172",
                new Contact("364.99°的文档", // 文档发布者名称
                        "https://blog.csdn.net/m0_54355172", // 文档发布者的网站地址
                        "2190826197@qq.com" // 文档发布者的邮箱
                ),
                "364.99°",
                "https://blog.csdn.net/m0_54355172",
                new HashSet<>());
    }
}
