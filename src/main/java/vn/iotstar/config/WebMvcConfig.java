package vn.iotstar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.iotstar.interceptor.AdminSecurityInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AdminSecurityInterceptor adminSecurityInterceptor;

    public WebMvcConfig(AdminSecurityInterceptor adminSecurityInterceptor) {
        this.adminSecurityInterceptor = adminSecurityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminSecurityInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "/static/");
    }
}
