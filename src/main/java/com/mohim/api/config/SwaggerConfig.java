package com.mohim.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private static final String REFERENCE = "Bearer 토큰 값";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(true) // Swagger 에서 제공해주는 기본 응답 코드를 표시할 것이면 true
                .ignoredParameterTypes(AuthenticationPrincipal.class) // @AuthenticationPrincipal의 파라미터 요구 필드를 없애기 위함
                .securityContexts(List.of(this.securityContext())) // SecurityContext 설정
                .securitySchemes(List.of(this.bearerAuthSecurityScheme())) // ApiKey 설정
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mohim.api.controller")) // Controller가 들어있는 패키지. 이 경로의 하위에 있는 api만 표시됨.
                .paths(PathSelectors.any()) // 위 패키지 안의 api 중 지정된 path만 보여줌. (any()로 설정 시 모든 api가 보여짐)
                .build();
    }

    // JWT SecurityContext 구성
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference(REFERENCE, authorizationScopes));
    }

    // ApiKey 정의
    private HttpAuthenticationScheme bearerAuthSecurityScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name(REFERENCE).build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MOHIM WEB API Documentation")
                .version("0.1")
                .build();
    }
}
