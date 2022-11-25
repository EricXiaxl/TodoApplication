package com.deloitte.todoapplication.config;

import com.deloitte.todoapplication.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //创建BCryptPasswordEncoder注入容器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //disable csrf
                .csrf().disable()
                //Get SecurityContext without Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/static/**", "/css/**", "/webjars/**","/js/**","/","/loginPage","/indexPage","/registerPage","/h2-console/**","/createTodoPage","/showUpdateForm/**").permitAll()
                // For the login and registration interfaces, allow anonymous access
                .antMatchers("/login", "/register").anonymous()
                // All requests except the above require authentication
                .anyRequest().authenticated();

        //This line must be added, otherwise nothing can be seen after the h2-console page is opened
        http.headers().frameOptions().disable();

        //Add a filter, add the token verification filter to the filter chain, and add it to the UsernamePasswordAuthenticationFilter filter before intercepting and filtering
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //Configure exception handler
        http.exceptionHandling()
                //Configuring Authentication Exception Handling
                .authenticationEntryPoint(authenticationEntryPoint).
                //Configure authorization exception handling
                accessDeniedHandler(accessDeniedHandler);
    }

    //Rewrite the method and inject the AuthenticationManager into the current spring container
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
