package com.my.shirospringboot.shiro.core.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Gzy
 * @version 1.0
 * @Description 角色或者认证过滤器
 */
public class RolesOrAuthorizationFilter extends AuthorizationFilter{

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request,response);
        //① 拥有哪些角色可以通过
        String[] rolesArray = (String[])((String[])mappedValue);
        if (rolesArray != null && rolesArray.length != 0) {
            Set<String> roles = CollectionUtils.asSet(rolesArray);
            List<String> rolesList = new ArrayList<>();
            for (String s:roles ) {
                rolesList.add(s);
            }
            //获取拥有的角色列表
            boolean[] booleans = subject.hasRoles(rolesList);
            for (boolean s:booleans) {
                //如果拥有其中一个角色，则允许通过
                if(s){
                    return s;
                }
            }
        }
        //
        return false;

    }
}
