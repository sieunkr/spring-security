# 정리 안된 글;; 







# 작성 중



# Spring Security
스프링 시큐리티 관련해서 기본적인 내용을 공부하고, 간단한 로그인 페이지를 구현한다. 추가로, 구글 oAuth 연동 테스트를 진행한다. 


# Spring Security 설정(boot 1.5.12)
기본적인 설정 및 간단한 테스트를 진행한다. 

#### Spring Security Gradle 연동

```java
compile('org.springframework.boot:spring-boot-starter-security')
```

#### 계정 설정

```java
@Configuration  
@EnableWebSecurity  
public class SecurityConfig extends WebSecurityConfigurerAdapter {  
  
    @Override  
  protected void configure(AuthenticationManagerBuilder auth)  
            throws Exception {  
        auth  
                .inMemoryAuthentication()  
                .withUser("user")  
                .password("password")  
                .roles("USER")  
                .and()  
                .withUser("admin")  
                .password("admin")  
                .roles("USER", "ADMIN");  
  }  
  
    @Override  
  protected void configure(HttpSecurity http) throws Exception {  
        http  
                .authorizeRequests()  
                .anyRequest()  
                .authenticated()  
                .and()  
                .httpBasic();  
  }  
}
```
로그인이 잘 된다. 

#### 프로세스

Client -- Filter -- FilterChainProxy -- Filter -- .. 등


# [참고]Spring Security 설정(boot 2.0.1)
Spring Boot 2.0 에서는 기존 1.5.X 버전과는 차이점이 있다. 

https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Security-2.0

#### Gradle 연동
생략...


#### 기본 로그인 화면
기본 사용자 계정은 user 이고, 비밀번호는 Intellij 에서 Spring Boot 를 실행할때 자동으로 생성된다. Console 에 비밀번호 메시지가 표시된다.user + 해당 비번으로 로그인할 수 있다.  여기까지는 1.5.12와 같다. 
```
Using generated security password: 88549d9a-3a21-450c-9938-8dd916d27fb6
```

하지만, 로그인 화면이 다르다.

캡처 01
해당 화면은 어디서 보여주는 것인가? 

패키지:org.springframework.security.web.authentication.ui 
클래스:DefaultLoginPageGeneratingFilter
메서드:generateLoginPageHtml

#### 커스텀 설정
1.5.X 와 마찬가지로 WebSecurityConfigurerAdapter 클래스를 상속받아서, 커스텀 설정을 추가하면 된다. 만약 WebSecurityConfigurerAdapter 만 추가하고 아무 설정도 안하면, 위에 나왔던 로그인화면은 실행되지 않고, 일반적인 alert 창이 실행된다.  WebSecurityConfigurerAdapter 를 상속받아서 구현하였기 때문에, Auto-Configuration 이 적용되지 않았기 때문이다. 

캡처2


# 커스텀 로그인 페이지
커스텀 로그인 화면 구현해보자. 

#### 기본 설정
```java
생략..
.formLogin()  
    .loginPage("/cafe_login")  
    .permitAll()
생략...
```

일단, 로그인페이지는 권한이 없어도 접근이 가능해야 한다. 그래서 permitAll 를 설정한다. 만약  .permitAll() 를 적용하지 않으면 어떻게 될까? 

권한 없음 --> 로그인 화면 --> 로그인화면도 권한 없음 --> 다시 로그인 화면

> 무한루프가 실행될 것이다. 

#### 로그인 Controller, View 작성
필자는 템플릿 엔진으로 프리마커를 사용할 것이다. 
```java
@GetMapping("/cafe_login")  
public String login(){  
    return "login";  
}
```

```html
<html>  
<head>  
</head>  
<body>  
<h1>커스텀 로그인 페이지</h1>  
<form name='f' action="cafe_login" method='POST'>  
 <table> <tr> <td>User:</td>  
 <td><input type='text' name='username' value=''></td>  
 </tr> <tr> <td>Password:</td>  
 <td><input type='password' name='password' /></td>  
 </tr> <tr> <td><input name="submit" type="submit" value="로그인" /></td>  
 </tr> </table> <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>  
</form>  
</body>  
</html>
```

로그인 페이지에서 Form 전송으로 로그인을 요청할 것이다. 


#### CSRF 
만약 CSRF input 태그가 없다면 어떻게 될까? 로그인 시 아래와 같은 메시지가 노출될 것이다. 

> Could not verify the provided CSRF token because your session was not found.

최근(아마도 스프링3.2+) Spring Security 는 CSRF 방어 기능을 제공한다. Form 으로 로그인 사용자 정보를 전달할 때 CSRF 토큰을 같이 전달해야 한다. CSRF 가 뭔지에 대해서는 아래 링크를 참고하자. 
https://ko.wikipedia.org/wiki/%EC%82%AC%EC%9D%B4%ED%8A%B8_%EA%B0%84_%EC%9A%94%EC%B2%AD_%EC%9C%84%EC%A1%B0

필자는 FreeMarker 에서의 적용인데, 다른 템플릿팅 엔진에서는 정확히는 모르겠다. 일단 스프링 가이드에서는 아래와 같이 나와있다. 

We use Thymeleaf to automatically add the CSRF token to our form. If we were not using Thymleaf or Spring MVCs taglib we could also manually add the CSRF token using `<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>`.

https://docs.spring.io/spring-security/site/docs/current/guides/html5/form-javaconfig.html#creating-a-login-view

어쨋든, 태그만 넣으면 CSRF 토큰은 자동으로 생성 될 것이다. 아마도 @EnableWebSecurity 어노테이션 사용으로 인해서 적용이 되는 것 같다. 

#### 로그아웃
로그아웃 역시 유사한 방법으로 구현하면 된다. 

```java
.logout()  
    .logoutUrl("/cafe_logout")  
    .logoutSuccessUrl("/cafe_login")
```

```html
<form name='f' action="cafe_logout" method='POST'>  
 <input type="submit" value="Logout"/>  
 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>  
</form>
```
로그인에서도 적용했듯이 CSRF 태그를 반드시 추가해야 한다. 











