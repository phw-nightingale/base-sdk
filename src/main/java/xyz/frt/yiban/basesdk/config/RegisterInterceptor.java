package xyz.frt.yiban.basesdk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.frt.yiban.basesdk.interceptor.UserInterceptor;

@Configuration
public class RegisterInterceptor implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;

    @Autowired
    public RegisterInterceptor(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    /**
     * 添加拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test-api/**", // 为了方便测试某些Api，可以暂时写到这下面
                        "/authorize",
                        "/sign-in",
                        "/sign-out",
                        "/sign-up",
                        "/401",
                        "/403",
                        "/404",
                        "/");
    }


}
