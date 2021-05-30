package com.bgg.shiro;

import cn.hutool.json.JSONUtil;
import com.bgg.common.lang.Result;
import com.bgg.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//jwt过滤器    继承shiro认证过滤器
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;


    /**
     * 继承自AuthenticatingFilter  当AuthenticatingFilter调用executeLogin()方法时，会调用所重写的的createToken()方法，
     * 返回AuthenticationToken(被JwtToken实现了)类型的jwtToken对象，再进行登录处理
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");//获取jwt
        if(StringUtils.isEmpty(jwt)){
            return null;
        }
        //返回自定义的继承自AuthenticationToken的JwtToken对象
        return new JwtToken(jwt);
    }

    //拦截    率先执行
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");//从request请求头 获取jwt
        if(StringUtils.isEmpty(jwt)){
            //无jwt的时候，不需要让shiro进行处理，而是直接访问未被标权限注解的方法，返回true直接访问
            return true;
        }else {
            //有jwt时
            //校验jwt
            //获取jwt声明 claim
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if(claim==null || jwtUtils.isTokenExpired(claim.getExpiration())){  //为空或者过期
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setCharacterEncoding("utf-8");
                Result result = new Result();
                result.setCode(401).setMsg("账号已失效，请重新登录！").setData(null);
                response.getWriter().print(JSONUtil.toJsonStr(result));
                return false;
            }
            //执行校验
            return executeLogin(servletRequest,servletResponse);
        }
    }

    //重写父类的登录异常处理
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();

        //对异常信息进行封装
        Result result = Result.fail(throwable.getMessage());

        //转换为json字符串
        String json = JSONUtil.toJsonStr(result);

        try {
            //异常数据返回给前端
            httpServletResponse.getWriter().println(json);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    //首先解决跨域问题
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
