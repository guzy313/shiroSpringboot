package com.my.shirospringboot.shiro.core.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色或者认证过滤器
 */
public class RolesOrAuthorizationFilter extends AuthorizationFilter{

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {



//        Subject subject = getSubject(request,response);
//        String[] rolesArray = (String[])((String[])mappedValue);
//        if (rolesArray != null && rolesArray.length != 0) {
//            Set<String> roles = CollectionUtils.asSet(rolesArray);
//            return subject.hasAllRoles(roles);
//        } else {
//            return true;
//        }
        return true;

    }
}
