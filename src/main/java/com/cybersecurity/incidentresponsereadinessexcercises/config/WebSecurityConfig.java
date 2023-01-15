package com.cybersecurity.incidentresponsereadinessexcercises.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final MainConfiguration cfg;
	
	public WebSecurityConfig(MainConfiguration cfg) {
		this.cfg = cfg;
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(new HttpBasicFilter(cfg), UsernamePasswordAuthenticationFilter.class);
        SecurityFilterChain chain = http.build();
        System.out.println("^^^^^^^^^^^^ HTTP FILTERS "+chain.getFilters());
        return chain;
    }
	
	@Bean
    public UserDetailsManager users() {
        InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
        return users;
    }
}