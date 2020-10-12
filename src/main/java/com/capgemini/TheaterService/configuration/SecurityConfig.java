package com.capgemini.TheaterService.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
//                .anyRequest().permitAll()
//                .and()
//                .httpBasic();

//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/theaters/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/theaters/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/theaters/**").hasRole("ADMIN")
//                .antMatchers("/theaters/**").hasAnyRole("ADMIN", "USER")
//                .anyRequest().permitAll()
//                .and()
//                .httpBasic();

          http.csrf().disable()
                  .authorizeRequests()
                  .anyRequest().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("pass").roles("USER")
                .and()
                .withUser("admin").password("pass").roles("ADMIN");
    }

}
