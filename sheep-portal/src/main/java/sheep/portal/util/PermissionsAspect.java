package sheep.portal.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import sheep.portal.entity.PortalAndUser;
import sheep.portal.mapper.PortalAndUserMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Aspect
@Component
public class PermissionsAspect {
    @Autowired
    PortalAndUserMapper portalAndUserMapper;

    @Pointcut("@annotation(permissions)")
    public void check(Permissions permissions) {
    }

    @Around("check(permissions)")

    public Object deBefore(ProceedingJoinPoint pjp, Permissions permissions) throws Throwable
    {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String role=request.getHeader("X-Role");
        if(permissions.role().equals("isOwnerOrAdmin"))
        {

            String user_id = request.getHeader("X-UserId");
            Object[] objects = pjp.getArgs();
            String portal_id =objects[0].toString();
            //是管理员
            if(role!=null&&role.equals("admin"))
            {
                return pjp.proceed();
            }
            //是owner
            else if(role==null)
            {
                int count= portalAndUserMapper.selectCount(new QueryWrapper<PortalAndUser>().eq("portal_id", portal_id).eq("user_id",user_id));
                if(count>0)
                {
                    return pjp.proceed();
                }
            }
            else
                return "无权限操作";
        }
        else if(permissions.role().equals("isAdmin"))
        {
            //是管理员
            if(role!=null&&role.equals("admin"))
            {
                return pjp.proceed();
            }
            else
                return "无权限操作";
        }

        return "无权限操作";


    }

}