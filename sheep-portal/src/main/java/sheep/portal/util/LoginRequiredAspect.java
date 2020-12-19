package sheep.portal.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sheep.portal.mapper.PortalAndUserMapper;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginRequiredAspect {
    @Autowired
    PortalAndUserMapper portalAndUserMapper;

    @Pointcut("@annotation(loginRequired)")
    public void doAuthToken(LoginRequired loginRequired) {
    }

    @Around("doAuthToken(loginRequired)")
    public Object deBefore(ProceedingJoinPoint pjp, LoginRequired loginRequired) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String user_id = request.getHeader("X-UserId");
        // 验证是否具有id即可。
        if (user_id != null && !user_id.equals("")) {
            // 已登录，执行原方法并返回即可。
            return pjp.proceed();
        }
        // 未登录，不执行方法，直接返回错误信息
        return "请登陆后再试！";

    }

}