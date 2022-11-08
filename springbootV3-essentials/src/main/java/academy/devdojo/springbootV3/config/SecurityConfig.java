package academy.devdojo.springbootV3.config;

import academy.devdojo.springbootV3.service.DevDojoUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Log4j2
//Habilita a validação em métodos globlemente
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * BasicAutheticationFilter -> Autentica se temos uma autorização em cima de info. base64
     * UsernamePasswordAuthenticationFilter -> Autentica nome e senha
     * DefaultLoginPageGeneratingFilter -> Pagina de login padrão
     * DefaultLogoutPageGeneratingFilter -> Pagina de logout padrão
     * FilterSecurityInterceptor -> Autentica se estou autorizado
     * Authentication -> Autorization
     * http://localhost:8080/login -> Aparecerá a tela para login
     * http://localhost:8080/logout -> Aoarecerpa a tela para logout
     */
    @Autowired
    private DevDojoUserDetailsService devDojoUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Desabilita o token
        http.csrf().disable().authorizeHttpRequests()
                //Gera o token para habilitação
                //http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .anyRequest()
                .authenticated()
                .and()
                //habilita formulários de login and logout
                .formLogin()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {}", passwordEncoder.encode("academy"));
        auth.inMemoryAuthentication()
                .withUser("junior")
                .password(passwordEncoder.encode("academy"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("maycon")
                .password(passwordEncoder.encode("academy"))
                .roles("USER");
        auth.userDetailsService(devDojoUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
