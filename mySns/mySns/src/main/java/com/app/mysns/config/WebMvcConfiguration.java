package com.app.mysns.config;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
 
    private static final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:/templates/";
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 캐시 설정이 필요할 수 있음
        registry.addResourceHandler("/fonts/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/images/");
        registry.addResourceHandler("/img/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/img/");
        registry.addResourceHandler("/css/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/js/");
        registry.addResourceHandler("/vendor/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/vendor/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"static/images/icons/phodo.ico");
        // registry.addResourceHandler("/*.html").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
 
}