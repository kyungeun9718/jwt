# Used Prompts

## SQLite 관련 참고 자료
https://heodolf.tistory.com/140
https://docko.tistory.com/entry/Java-SQLite-데이터베이스-사용하기
https://we-always-fight-with-code.tistory.com/4

## JWT 관련 참고 자료
https://samori.tistory.com/64
https://this-circle-jeong.tistory.com/162
https://sjh9708.tistory.com/83

### jwt를 활용해서 개발할건데 java 버전 몇으로 하는게 좋을까?
JWT를 활용한 Spring Boot 기반 프로젝트에서는 Java 17 버전을 사용하는 것이 가장 권장됩니다. 이유는 다음과 같습니다:

✅ Java 17을 추천하는 이유
LTS(Long-Term Support) 버전
Java 17은 2021년 9월 출시된 LTS 버전으로, 기업 및 커뮤니티에서 안정성과 보안 측면에서 장기 지원을 받습니다.
Spring Boot 호환성
최신 Spring Boot 3.x 버전은 Java 17 이상을 필요로 하며, JWT 구현 시 사용하는 보안 라이브러리들(jjwt, auth0, spring-security-oauth2, 등)과도 완벽하게 호환됩니다.
성능 및 보안 개선
Java 8 대비 GC(가비지 컬렉션) 성능, Native 지원, Record 등 다양한 기능이 향상되어 보안 토큰 처리 및 API 서버 성능 최적화에 유리합니다.
추가 보너스: 더 적은 코드량
Java 17의 record, var, switch 개선, sealed class 같은 문법은 JWT 사용자 정보 관리나 DTO 처리에 코드량을 줄이는 데 도움이 됩니다.
❌ Java 8이나 11은 피하는 이유
Java 8은 구버전이며, Spring Boot 3.x에서는 아예 지원하지 않습니다.
Java 11도 여전히 많이 쓰이긴 하지만 곧 지원 종료 및 라이브러리 최신 호환성에서 제한이 있습니다.

### Sqltie3랑 jwt인증을 기반으로 api를 구현할거야.Java 17, maven을 사용해서 spring boot로 개발할거고, 진행할 api항목은
### 회원가입, 로그인, 내정보조회, 내정보수정, 회원탈퇴, todo생성, todo조회, 특정todo조회, todo수정, todo삭제, todo검색인데
### 필요한 라이브러리가 있으면 추천해줘

정확한 방향 잡으셨어요!
📌 Java 17 + Maven + Spring Boot + SQLite3 + JWT 인증 기반의 API를 구현한다면,
당신이 계획한 API 항목에 맞춰 필요한 라이브러리를 아래처럼 정리할 수 있습니다.

✅ 핵심 구현 사항

JWT 기반 인증 (Access + Refresh Token)
SQLite3 데이터베이스 사용
회원 관리 + TODO CRUD API
Spring Boot + Maven + Java 17
✅ 필수 라이브러리 목록 (pom.xml 기준)

라이브러리	설명	필요 여부
spring-boot-starter-web	REST API 개발의 기본	✅
spring-boot-starter-data-jpa	JPA ORM 사용, SQLite 연동	✅
sqlite-jdbc	SQLite 드라이버	✅
spring-boot-starter-security	로그인 & JWT 인증 처리	✅
jjwt (io.jsonwebtoken)	JWT 생성 및 검증	✅
lombok	DTO/엔티티 코드 간결화	🟡 (편리함)
spring-boot-starter-validation	입력값 검증 (ex. @Valid)	✅
spring-boot-devtools	자동 리로딩 등 개발 편의 기능	🟡 (선택)
spring-boot-starter-test	단위 & 통합 테스트	✅

### https://sjh9708.tistory.com/83 [데굴데굴 개발자의 기록:티스토리]. 의 configure 처럼 작성을 할건데,
### WebSecurityConfigurerAdapter버전이 안맞아서 사용이 안돼. sprint security 6이상으로 사용할 수 있는 방법으로 알려
WebSecurityConfigurerAdapter는 Spring Security 5.7부터 deprecated 되었고, Spring Security 6 이상에서는 완전히 제거되었습니다. 따라서, 현재 Spring Security 최신 버전에서는 람다 스타일의 SecurityFilterChain 빈 등록 방식으로 설정을 해야 합니다.

당신이 공유한 설정 내용을 기준으로 최신 방식으로 재작성해드리면 다음과 같습니다:

✅ 최신 Spring Security 6+ 버전용 SecurityConfig 예시
package com.aladin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-resources/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
### 나는 이거를 과제로 제출할 예정인데 JWT key가 포함되어있는 application.properties 를 git에 올리지 못하잖아. 그럼 어떻게 하는게 좋을까?
좋은 고민입니다! 과제 제출을 위해 GitHub에 올려야 하지만, JWT 시크릿 키는 절대 외부에 공개하면 안 됩니다.
그렇다면 어떻게 해야 할까요? 안전하면서도 과제를 제출할 수 있는 방법을 아래에 정리해드릴게요.

✅ 🔐 민감 정보(GitHub에 올리면 안 되는 정보)

jwt.secret
DB 비밀번호
OAuth client ID/secret 등
✅ 과제 제출용 권장 방법 (보안 + 평가 모두 만족)

✔ 1. application.properties에서 키 주석처리
 실제 시크릿 키는 환경변수 또는 제출용 문서로 따로 전달
 jwt.secret=S%Xz2!q9vKf@D7LgN$8w#ZbE4rMm!AkP
 
✔ 2. README 또는 별도 문서에 시크릿 키 안내
📌 [환경변수 설정 안내]

- JWT 시크릿 키는 보안을 위해 Git에 포함하지 않았습니다.
- 테스트 시 application.properties에 다음 값을 추가해주세요:

##비밀번호 암호화하는 방법을 알려줘.
1. 의존성 추가 (pom.xml)

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
✅ 2. 비밀번호 암호화 구성

🔹 PasswordEncoderConfig.java
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

##This generated password is for development use only. Your security configuration must be updated before running your application in production. 해당 에러가 발생했는, 내 SecurityConfig 파일의 내용은 이렇게 돼.public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/login", "/users/signup").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

이 메시지는 Spring Boot 3.x 또는 Spring Security 6.x 이상에서 기본 보안 설정이 활성화된 경우 출력되는 경고입니다.

✅ 의미

This generated password is for development use only.
Your security configuration must be updated before running your application in production.
👉 이는 Spring Security가 기본 로그인 페이지와 기본 사용자 계정 (user)을 자동으로 생성했기 때문에 뜨는 메시지입니다.

기본 계정:

username: user
password: 로그 출력에 표시된 무작위 값
📌 왜 발생하나?

아직 Security 설정을 직접 구성하지 않았거나,
formLogin() 또는 JWT 기반 로그인 설정을 하지 않았기 때문입니다.

✅ 해결 방법

방법 1: Security 설정에서 기본 로그인 제거
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .formLogin().disable() // 기본 로그인 폼 제거
            .httpBasic().disable() // 기본 인증 헤더 제거
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/signup", "/users/login").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}

### The error may involve com.aladin.user.mapper.UserMapper.findByMemberNoForUpdate
### The error occurred while executing a query
### SQL: SELECT member_no         FROM TB_MEMBER         WHERE member_no = ?         FOR UPDATE
### Cause: org.sqlite.SQLiteException: [SQLITE_ERROR] SQL error or missing database (near "FOR": syntax error)
###; uncategorized SQLException; SQL state [null]; error code [1]; [SQLITE_ERROR] SQL error or missing database (near "FOR": syntax error)] with root cause

###org.sqlite.SQLiteException: [SQLITE_ERROR] SQL error or missing database (near "FOR": syntax error) 해당 에러가 왜 발생할까?

이 에러는 다음 쿼리에서 발생했습니다:

SELECT member_no FROM TB_MEMBER WHERE member_no = ? FOR UPDATE
그리고 SQLite에서는 이 구문이 지원되지 않기 때문에 오류가 발생한 것입니다.

🔥 원인

SQLite는 SELECT ... FOR UPDATE 구문을 지원하지 않습니다.

이 구문은 MySQL, PostgreSQL 등에서 레코드를 잠금(Lock) 하기 위해 사용하지만,
SQLite는 단일 파일 기반이고 동시성 제어 방식이 다르기 때문에 해당 문법을 허용하지 않습니다.

✅ 해결 방법

🚫 사용 불가: SELECT ... FOR UPDATE
SQLite는 전체 트랜잭션을 통해 락을 관리합니다. 그래서 별도의 FOR UPDATE 없이도 다음과 같이 하면 됩니다.

✅ 대체 방법: 트랜잭션을 이용한 시나리오 제어
트랜잭션 안에서 먼저 SELECT
그 이후 INSERT 또는 UPDATE 수행

### + 오타찾기, 메소드명 추천받기, 로그찍어달라는 요청사항에 AI사용하였습니다.
