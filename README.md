<div align="center">
  <img src="https://github.com/JeroCaller/Spoon-Suits/blob/main/docs-resources/spoon-suits-icon.png" width="40%" />
</div>

# Spoon Suits - Simple Utils for SpringBoot   
스프링부트에서 사용할 간단한 유틸 라이브러리

| 최신 버전 - Latest version |
|:---:|
| [![](https://jitpack.io/v/JeroCaller/Spoon-Suits.svg)](https://jitpack.io/#JeroCaller/Spoon-Suits) |

## 주요 기능 - Main features
* Cookie
  * cookie 추가, 삭제 및 Java 객체 직렬화, 역직렬화하여 쿠키에 추가 및 추출 기능
* JWT
  * JWT Default Authentication 및 Filter 제공
* REST API
  * HTTP Response로 전달될 JSON 데이터와 매핑될 POJO 내 특정 필드값의 사이즈가 0 또는 null일 경우 
    JSON 데이터로의 직렬화에서 제외하는 필터링 기능
* JPA
  * Paging
    * Zero based to one based index - 0부터 시작하는 페이지 인덱스를 1부터 시작하도록 하는 기능
    * Page 객체의 크기가 0 또는 null인지 판별하는 기능
* Validation
  * 유효성 검사 실패 시 실패한 필드명 및 실패 원인을 Map 객체로 반환하는 기능.

## 라이브러리 추가 및 사용 방법 - How to use
* Gradle(Groovy)
```gradle
// build.gradle

repositories {
    // ...
    maven { url 'https://jitpack.io' }   // 추가
}

dependencies {
    // com.github.JeroCaller:Spoon-Suits:v<버전>
    implementation 'com.github.JeroCaller:Spoon-Suits:v1.0.3'
    
    // === 필수 ===
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    
    implementation 'org.springframework.boot:spring-boot-starter-web' // 웹 앱 개발 시에는 필수
    
    // === 선택 ===
    // JWT & Spring Security
    implementation 'org.springframework.boot:spring-boot-starter-security'
    
    // jjwt
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
    
    // validation
    implementation 'org.springframework.boot:spring-boot-starter-validation'
	
    // Spring Data JPA
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
}
```

* 이 라이브러리에는 JWT, JPA 등 다양한 기능들을 제공합니다. 특정 기능들만 골라 사용할 수 있게 하기 위해 각각 필요한 
  Spring Boot 제공 dependency들은 `implementation`이 아닌 `compileOnly`로 해두었습니다. 따라서 특정 
  기능들을 사용하고자 한다면 위 build.gradle 코드처럼 각각의 Spring Boot dependency들을 
  `implementation` 으로 삽입해야 사용할 수 있습니다. 한 편, 각각의 기능에 필요한 dependency들은 
  Javadoc의 패키지 페이지에도 작성하였으니 참고바랍니다. 

## 정보 - Info
각 기능의 자세한 사용 방법은 다음의 API Reference를 참고바랍니다.   
* API Reference: [최신 버전 Javadoc](https://jitpack.io/com/github/JeroCaller/Spoon-Suits/latest/javadoc/)
* 이전 버전의 API Reference를 원하신다면 브라우저 주소 창에 `https://jitpack.io/com/github/JeroCaller/Spoon-Suits/<원하는 버전>/javadoc/` 형태로 입력하면 됩니다. 
* 버전 목록은 ["Spoon Suits" Github repository](https://github.com/JeroCaller/Spoon-Suits) 의 화면 우측에 있는 "Releases" 목록을 참고하면 됩니다.
  * ["Spoon Suits" Github Releases](https://github.com/JeroCaller/Spoon-Suits/releases)

그 외 정보들은 [Spoon Suits Wiki](https://github.com/JeroCaller/Spoon-Suits/wiki)를 참고 바랍니다. 

## 구조 - Structures

* Repository
```
/spoonsuits : 라이브러리 폴더
/spoonsuits-local-test : 라이브러리 테스트용 폴더
/docs : Javadoc 생성 파일 모음 (Jitpack 자체 Javadoc 제공 기능으로 인해 현재는 사용하지 않음)
/docs-resources : README.md 작성에 필요한 이미지, 아이콘 등의 리소스 모음
```

* Spoon Suits 라이브러리 (v1.0.3 기준)
```
/spoonsuits
│
└───src
    ├───main
    │   ├───java
    │   │   └───com
    │   │       └───jerocaller
    │   │           └───libs
    │   │               └───spoonsuits
    │   │                   ├───config
    │   │                   │       AutoConfig.java
    │   │                   │
    │   │                   └───web
    │   │                       │   package-info.java
    │   │                       │
    │   │                       ├───cookie
    │   │                       │   │   CookieConfigurer.java
    │   │                       │   │   CookieRequest.java
    │   │                       │   │   CookieUtils.java
    │   │                       │   │   package-info.java
    │   │                       │   │
    │   │                       │   └───impl
    │   │                       │           DefaultCookieConfigurerImpl.java
    │   │                       │
    │   │                       ├───jpa
    │   │                       │       package-info.java
    │   │                       │       PageUtils.java
    │   │                       │
    │   │                       ├───jwt
    │   │                       │   │   DefaultJwtAuthenticationFilter.java
    │   │                       │   │   JwtAuthenticationProvider.java
    │   │                       │   │   JwtProperties.java
    │   │                       │   │   package-info.java
    │   │                       │   │
    │   │                       │   └───impl
    │   │                       │           DefaultJwtAuthenticationProviderImpl.java
    │   │                       │
    │   │                       ├───rest
    │   │                       │   └───json
    │   │                       │       │   JsonFilterOfEmptyFields.java
    │   │                       │       │   package-info.java
    │   │                       │       │
    │   │                       │       └───dto
    │   │                       │               JsonFilterOfEmptyFieldsArgs.java
    │   │                       │
    │   │                       └───validation
    │   │                               package-info.java
    │   │                               ValidationUtils.java
    │   │
    │   └───resources
    │       │   application.yml
    │       │
    │       ├───META-INF
    │       │   └───spring
    │       │           org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

## 사용 기술 - Stacks

<table>
<tr>
  <td>

| In library |
|:---:|
| <img src="https://img.shields.io/badge/Framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.x.x-%23121011?style=for-the-badge"> |
| <img src="https://img.shields.io/badge/Language-%23121011?style=for-the-badge">![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)<img src="https://img.shields.io/badge/21-%23121011?style=for-the-badge"> |
| <img src="https://img.shields.io/badge/build tool-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> |
| <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> |
| <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge"> |
| <img src="https://img.shields.io/badge/spring validation-6DB33F?style=for-the-badge"> |
| ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) |
| <img src="https://img.shields.io/badge/lombok-b13a22?style=for-the-badge"> |
  </td>
  <td>

| Test |
|:---:|
| <img src="https://img.shields.io/badge/test framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> |
| <img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"> |
| <img src="https://img.shields.io/badge/selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white"> |
| <img src="https://img.shields.io/badge/db-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white"> |
  </td>
  <td>

| Git |
|:--:|
| <img src="https://img.shields.io/badge/sourcetree-0052CC?style=for-the-badge&logo=sourcetree&logoColor=white"> |
| <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> |
  </td>
</tr>
<tr>
  <td>

| Development Environment |
|:---:|
| <img src="https://img.shields.io/badge/os-%23121011?style=for-the-badge">![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)<img src="https://img.shields.io/badge/10-%23121011?style=for-the-badge"> |
| <img src="https://img.shields.io/badge/ide-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellijidea"> |
  </td>
  <td>

| Project Management |
|:---:|
| <img src="https://img.shields.io/badge/project-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/github projects-181717?style=for-the-badge&logo=github&logoColor=white"> |
| <img src="https://img.shields.io/badge/issue tracker-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/github issues-181717?style=for-the-badge&logo=github&logoColor=white"> |
  </td>
</tr>
</table>

| CI / CD | |
|:---:|:---:|
| Skills | Details |
| <img src="https://img.shields.io/badge/jitpack-000000?style=for-the-badge&logo=jitpack&logoColor=white"> | 라이브러리 온라인 자동 배포. Javadoc 문서 제공 |
| <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"> | Github Tag & Release 및 버전 번호 부여 자동화 |

## 개발 일지 - Development log

Spoon Suits 라이브러리를 개발하며 작성한 글입니다. 

[[Roadmap][Personal Project] Spoon Suits 관련 글 로드맵](https://jerocaller.github.io/roadmap/spoon-suits-personal-project-roadmap/)

