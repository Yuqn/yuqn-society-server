package com.yuqn.component;

import com.yuqn.service.impl.PhoneNumberUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private PhoneNumberUserService phoneNumberUserService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, PhoneNumberUserService phoneNumberUserService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.phoneNumberUserService = phoneNumberUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 接收认证信息
        String credentials = (String) authentication.getCredentials();
        String principal = (String) authentication.getPrincipal();
        // 判断是账号登录还是手机号登录，这里简单通过前缀区分
        UserDetails userDetails = null;
        if (principal.startsWith("username:")) {
            // 账号登录
            String username = principal.substring("username:".length());
            userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(credentials, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
        } else if (principal.startsWith("phone:")) {
            // 手机号登录
            // 这里需要有一个根据手机号加载用户信息的方法，比如 userDetailsService.loadUserByPhoneNumber(phoneNumber)
            // 但由于UserDetailsService没有提供这样的方法，所以这里只是一个示例，你需要自己实现这个逻辑
            String phoneNumber = principal.substring("phone:".length());
            // 手机号码登录
            userDetails = phoneNumberUserService.loadUserByPhoneNumber(phoneNumber);
        } else {
            throw new BadCredentialsException("Invalid principal format");
        }
        // 如果用户信息验证成功，则创建一个新的已认证令牌并返回
        UsernamePasswordAuthenticationToken authenticatedToken = new UsernamePasswordAuthenticationToken(userDetails, credentials, userDetails.getAuthorities());
        authenticatedToken.setDetails(authentication.getDetails());
        return authenticatedToken;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
