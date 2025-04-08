/**
 * <p>
 *     JWT 기반 인증을 구현하기 위한 도구 제공 패키지.
 * </p>
 * <p>
 *     JWT 기반 인증 구현을 위한 Jwt Authentication Provider & Filter 제공.
 * </p>
 * <p>
 *     여기에선 <a href="https://github.com/jwtk/jjwt">jjwt</a> 의존성을 사용하며,
 *     사용을 위해선 Gradle(Groovy)에서 다음의 의존성 추가 필요.
 * </p>
 * <pre><code>
 * // jjwt
 * implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
 * runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
 * runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
 *
 * // Spring Security
 * implementation 'org.springframework.boot:spring-boot-starter-security'
 * </code></pre>
 * @see <a href="https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api">jjwt-api</a>
 * @see <a href="https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl">jjwt-impl</a>
 * @see <a href="https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson">jjwt-jackson</a>
 * @see <a href="https://github.com/jwtk/jjwt">jjwt github and docs</a>
 */
package com.jerocaller.libs.spoonsuits.web.jwt;