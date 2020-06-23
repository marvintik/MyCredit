package myCredit.config;

import myCredit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/", "/index", "/about", "/resources/**", "/api/**", "/css/**", "/js/**", "/img/**", "/error", "/account/**").permitAll()
                .antMatchers("/admin/**", "/mycredit/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**", "/mycredit/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .requiresChannel().antMatchers("/", "/index", "/about", "/resources/**", "/api/**", "/css/**", "/js/**", "/img/**", "/error", "/account/**").requiresInsecure()
                .and()
                .csrf().disable()
                .requiresChannel()
                .anyRequest().requiresSecure()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/oauth2LoginSuccess")
                .permitAll()
                .and()
                .logout()
                .permitAll();

    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}
