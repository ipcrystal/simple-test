package org.example.simpletest.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlusConfig
 *
 * @author zhuzhenjie
 * @since 2020/12/7
 */
@Configuration
@MapperScan("org.example.simpletest.mapper")
public class MyBatisPlusConfig {

}