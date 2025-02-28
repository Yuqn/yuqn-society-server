package com.yuqn.config;

import com.yuqn.component.CustomAuthenticationProvider;
import com.yuqn.filter.JwtAuthenticationTokenFilter;
import com.yuqn.handler.AccessDeniedHandlerImpl;
import com.yuqn.handler.AuthenticationEntryPointImpl;
import com.yuqn.service.impl.PhoneNumberUserService;
import com.yuqn.service.impl.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * @author: yuqn
 * @Date: 2024/5/22 23:56
 * @description:
 * security 配置
 * 注入加密器，security通过加密器，将传递的密码进行加密，再与sql表进行比较（前提是sql表的密码已经是使用加密器加密过了）
 * 请求放行
 * 异常处理
 * @version: 1.0
 */
@EnableWebSecurity
@Configuration
// 开启权限控制功能的注解
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        // 返回你的UserDetailsService实现
        return new UserDetailsService();
    }
    @Bean
    public PhoneNumberUserService phoneNumberUserService(){
        return new PhoneNumberUserService();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService(), passwordEncoder(),phoneNumberUserService());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        // .requestMatchers(mvcMatcherBuilder.pattern("/hello")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/user/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/demo/set")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/test/getmessage")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/sys/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/sys/getOne")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/register")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/getDataTree")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/getDataGrade")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/userLogin")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/teacherLogin")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/isRegister")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/society/changePassword")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/error/**")).permitAll()
                        //  .requestMatchers().anonymous() 匿名验证，未登录可以访问，登录不可以访问
                        .anyRequest().authenticated()
                )
                // stateless authentication
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 在 UsernamePasswordAuthenticationFilter 过滤器之前加上 jwtAuthenticationTokenFilter 过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class);
        // 配置异常处理
        http
                .exceptionHandling((exception)-> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        // .accessDeniedPage("/error/accedd-denied")
                        .accessDeniedHandler(accessDeniedHandler)
                );
        // 允许跨域
        http.cors(Customizer.withDefaults());
        return http.build();
    }

}
