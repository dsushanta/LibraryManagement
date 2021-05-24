package com.bravo.johny.security;

import com.bravo.johny.service.LibraryUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    //LibraryUserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());

        System.out.println(this +" Config auth builder");

        /*auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password("library")
                .roles("USER");*/
    }

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                //.antMatchers("/**").hasRole("ADMIN")
                //.antMatchers("/LibraryManagement").permitAll()
                .and().authorizeRequests();

        System.out.println(this +" Config http security");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .hasRole("ADMIN");

        System.out.println(this +" Config http security");
    }

    /*@Bean
    public UserDetailsService getUserDetails(){
        return new LibraryUserDetailsService();
    }*/

    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println(this +" user details bean");
        return new LibraryUserDetailsService();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        System.out.println(" SecurityConfig password encoder");
        return NoOpPasswordEncoder.getInstance();
    }

    /*@Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(ProjectConfig.BCRYPT_STRENGTH);
    }*/
}