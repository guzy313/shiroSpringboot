package com.my.shirospringboot.shiro.core.filter;

import com.alibaba.fastjson.JSONObject;
import com.my.shirospringboot.shiro.constant.ShiroConstant;
import com.my.shirospringboot.shiro.core.base.BaseResponse;
import com.my.shirospringboot.utils.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author Gzy
 * @version 1.0
 * @Description: 资源(权限)过滤器
 */
public class JwtPermsFilter extends PermissionsAuthorizationFilter {

    /**
     * @Description: 发生拒绝时调用
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String jwtToken = httpServletRequest.getHeader(ShiroConstant.JWT_TOKEN);
        if(StringUtils.isNotEmpty(jwtToken)){
            //如果存在jwtToken
            BaseResponse baseResponse = new BaseResponse(ShiroConstant.NO_PERMS_CODE,
                    ShiroConstant.NO_PERMS_MESSAGE);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONObject.toJSONString(baseResponse));
        }
        //不存在jwtToken
        return super.onAccessDenied(request, response);
    }
}
