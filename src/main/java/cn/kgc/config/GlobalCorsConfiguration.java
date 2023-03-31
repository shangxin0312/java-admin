package cn.kgc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfiguration {

    //添加corsFilter，用来全局解决跨域访问的请求
    @Bean
    public CorsFilter corsFilter(){
        //准备跨域访问配置类
        CorsConfiguration config = new CorsConfiguration();
        //往配置类中添加跨域策略
        config.addAllowedOrigin("http://localhost:9528");
        config.addAllowedOrigin("http://127.0.0.1:7070");
        config.addAllowedOrigin("http://localhost:801");
        config.setAllowCredentials(true);
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedHeader("*");
        config.setMaxAge(180l);

        //基于配置类指定拦截规则
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**",config);

        return new CorsFilter(configSource);
    }
}
