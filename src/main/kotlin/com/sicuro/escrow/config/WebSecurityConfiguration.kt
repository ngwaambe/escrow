package com.sicuro.escrow.config

import com.sicuro.escrow.service.AuthTokenFilter
import com.sicuro.escrow.service.UserDetailsServiceImpl
import com.sicuro.escrow.util.security.AuthEntryPointJwt
import com.sicuro.escrow.util.security.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration @Autowired constructor(
    val userDetailService: UserDetailsServiceImpl,
    val unauthorizedHandler: AuthEntryPointJwt,
    val jwtUtile: JwtUtils) : WebSecurityConfigurerAdapter(){

    fun authenticationJwtTokenFilter() = AuthTokenFilter(jwtUtile, userDetailService)

    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(userDetailService)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean();
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    override fun configure(http: HttpSecurity){
        http.cors().and().csrf().disable()
            .authorizeRequests().antMatchers("/api/auth/token").permitAll()
                                .antMatchers("/api/auth/check_token").permitAll()
                                .antMatchers("/api/auth/signup").permitAll()
                                .antMatchers("/api/auth/activate").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}
