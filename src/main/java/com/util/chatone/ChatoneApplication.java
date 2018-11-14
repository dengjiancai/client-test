package com.util.chatone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ChatoneApplication {

    @Value("${linkfin.active.swagger}")
    private boolean activeSwagger;
    public static void main(String[] args) {
        SpringApplication.run(ChatoneApplication.class, args);
    }

    /**
     * swagger的配置
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()  // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.basePackage("com.util.chatone.controller")) // 对所有api进行监控
                .paths(PathSelectors.any()) // 对所有路径进行监控
                .build().enable(activeSwagger);
    }

}
