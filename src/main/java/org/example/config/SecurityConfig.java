package org.example.config;

import org.example.filter.JWTAuthenticationFilter;
import org.example.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


//到这里基本操作都写好啦，现在就需要我们将这些辛苦写好的“组件”组合到一起发挥作用了，那就需要配置了。
// 需要开启一下注解@EnableWebSecurity然后再继承一下WebSecurityConfigurerAdapter就可以啦，springboot就是可以为所欲为~
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)// 至于为什么要配置这个，嘿嘿，卖个关子
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")//因为UserDetailsService的实现类实在太多啦，这里设置一下我们要注入的实现类
    private UserDetailsService userDetailsService;

    // 加密密码的，安全第一嘛~（springsecurity自己实现的一个加密方法，足够强大，不用再去自己实现PasswordEncoder(密码加密接口)）
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    //重写这个 就是更改security默认的登录方式
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //（常见）基于HttpBasic认证
        http.cors().and().csrf().disable()
                //授权配置
                .authorizeRequests()
                // 测试用资源，需要验证了的用户才能访问
                .antMatchers("/tasks/**").authenticated()
                // 需要角色为ADMIN才能删除该资源
                .antMatchers(HttpMethod.DELETE, "/tasks/**").hasAuthority("ROLE_ADMIN")
                // 其他都放行了
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}