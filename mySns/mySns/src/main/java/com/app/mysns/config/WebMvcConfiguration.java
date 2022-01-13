package com.app.mysns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer { 
//   private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };


//   @Override
//   public void addResourceHandlers(ResourceHandlerRegistry registry) { 
//     // 기본 resourceHandler 유지하면서 추가
//     registry.addResourceHandler("/img/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"img/").setCachePeriod(31536000);
//     registry.addResourceHandler("/images/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"images/").setCachePeriod(31536000);
//     registry.addResourceHandler("/css/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"css/").setCachePeriod(31536000);
//     registry.addResourceHandler("/js/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"js/").setCachePeriod(31536000);
//     registry.addResourceHandler("/vender/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"vender/").setCachePeriod(31536000);

    
//     // registry.addResourceHandler("/img/**",
//     //                             "/css/**",
//     //                             "/js/**",
//     //                             "/vendor/**")
//     //          .addResourceLocations("classpath:/static/img/",
//     //                                "classpath:/static/css/",
//     //                                "classpath:/static/js/",
//     //                                "classpath:/static/vendor/");
//   }
// }
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
        registry.addResourceHandler("/*.html").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
 
}