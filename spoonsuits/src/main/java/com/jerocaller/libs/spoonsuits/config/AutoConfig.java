package com.jerocaller.libs.spoonsuits.config;

import com.jerocaller.libs.spoonsuits.web.jwt.JwtProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan("com.jerocaller.libs.spoonsuits")
@EnableConfigurationProperties({JwtProperties.class})
public class AutoConfig {

}
