package com.task.movies.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter,CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;


    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
//        // set the name of the attribute the CsrfToken will be populated on
//        delegate.setCsrfRequestAttributeName("_csrf");
//        // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
//        // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandle
//        CsrfTokenRequestHandler requestHandler = delegate::handle;


        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable

//                                .ignoringRequestMatchers("/api/auth/**")
//                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add the JWT filter before UsernamePasswordAuthenticationFilter
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()

                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)  // Use custom entry point for unauthorized access
                );


        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173")); // Use setAllowedOriginPatterns instead
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);  // If you allow credentials (cookies, auth)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//
//    final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
//        private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();
//
//        @Override
//        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
//            /*
//             * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
//             * the CsrfToken when it is rendered in the response body.
//             */
//            this.delegate.handle(request, response, csrfToken);
//        }
//
//        @Override
//        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
//            /*
//             * If the request contains a request header, use CsrfTokenRequestAttributeHandler
//             * to resolve the CsrfToken. This applies when a single-page application includes
//             * the header value automatically, which was obtained via a cookie containing the
//             * raw CsrfToken.
//             */
//            if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
//                return super.resolveCsrfTokenValue(request, csrfToken);
//            }
//            /*
//             * In all other cases (e.g. if the request contains a request parameter), use
//             * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
//             * when a server-side rendered form includes the _csrf request parameter as a
//             * hidden input.
//             */
//            return this.delegate.resolveCsrfTokenValue(request, csrfToken);
//        }
//    }
//
//
//    final class CsrfCookieFilter extends OncePerRequestFilter {
//
//        @Override
//        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//                throws ServletException, IOException {
//            CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
//            // Render the token value to a cookie by causing the deferred token to be loaded
//            csrfToken.getToken();
//
//            filterChain.doFilter(request, response);
//        }
//    }
}
