package com.example.portfolio.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration //config Bean이라는 것을 명시해주는 어노테이션
@EnableWebSecurity // Spring Security config를 할 클래스라는 것을 명시
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean // 패스워드 암호화 관련 메소드
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
        web
                .ignoring()
                .antMatchers(HttpMethod.GET, "/exception/**");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// 세션을 사용하지 않고 JWT 토큰을 활용하여 진행, csrf토큰검사를 비활성화
                .authorizeRequests() // 인증절차에 대한 설정을 진행
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/Mypage/**").hasRole("MEMBER")
                .antMatchers("/**").permitAll() // 설정된 url은 인증되지 않더라도 누구든 접근 가능
                .anyRequest().authenticated()// 위 페이지 외 인증이 되어야 접근가능(ROLE에 상관없이)
                .and()
                .formLogin().loginPage("/user/login")  // 접근이 차단된 페이지 클릭시 이동할 url
                .loginProcessingUrl("/") // 로그인시 맵핑되는 url
                .usernameParameter("userid")      // view form 태그 내에 로그인 할 id 에 맵핑되는 name ( form 의 name )
                .passwordParameter("pw")      // view form 태그 내에 로그인 할 password 에 맵핑되는 name ( form 의 name )
                .permitAll()
                .and()
                .logout() // 로그아웃 설정
                .logoutUrl("/logout") // 로그아웃시 맵핑되는 url
                .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
                .invalidateHttpSession(true) // 세션 clear
                .and() //세션 관리
                .sessionManagement(s -> s
                        .maximumSessions(200) //세션 허용 개수
                        .sessionRegistry(sessionRegistry())
                        .maxSessionsPreventsLogin(false)); //동시 로그인 차단, false 인 경우 기존 세션 만료
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSecuritySessionRegistImpl();
    }

    /* 관리자 아이디 파라미터 이름 */
    public static final String USERNAME_PARAM = "admin";

    /* 관리자 비밀번호 파라미터 이름 */
    public static final String PASSWORD_PARAM = "1111";

}