package cn.kgc.config;

import cn.kgc.filter.AuthenticationFilter;
import cn.kgc.handler.AcceDeniedHandler;
import cn.kgc.handler.ExitSuccessHandler;
import cn.kgc.handler.LoginFailHandler;
import cn.kgc.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
public class SecurityConfigutation extends WebSecurityConfigurerAdapter {
    @Resource
    private LoginSuccessHandler loginSuccessHandler;
    @Resource
    private LoginFailHandler loginFailHandler;
    @Resource
    private ExitSuccessHandler exitSuccessHandler;
    @Resource
    private AcceDeniedHandler acceDeniedHandler;
    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception{
        return new AuthenticationFilter(authenticationManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录流程
        http.formLogin()
                       .successHandler(loginSuccessHandler)  //登录成功的处理
                       .failureHandler(loginFailHandler);   //登录失败的处理

        //登出流程
        http.logout()
                      .logoutSuccessHandler(exitSuccessHandler);

        //权限控制
        http.authorizeRequests()
                      //.antMatchers("/upload").permitAll()  //对请求进行权限校验
                      //.antMatchers("/upload/**").permitAll()
                      .anyRequest().authenticated();                         //所有请求都需要进行校验

        //添加自定义过滤器
        http.addFilter(authenticationFilter());

        //异常处理
        http.exceptionHandling()
                       .accessDeniedHandler(acceDeniedHandler);  //缺少权限

        //允许跨域访问
        http.cors();

        //不阻止跨站攻击
        http.csrf().disable();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
