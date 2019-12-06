package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sun.media.jfxmedia.logging.Logger;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 对admin的操作进行过滤
 */
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre"; //设置过滤器类型
    }

    @Override
    public int filterOrder() {
        return 0;  //优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true;  //// 是否执行该过滤器，此处为true，说明需要过滤
    }

    /**
     * 过滤器内执行的操作  return 任何object的值都表示继续执行
     * setsendzoolResponse(false)表示不再继续执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //获取request的上下文
        RequestContext context = RequestContext.getCurrentContext();
        //request域
        HttpServletRequest request = context.getRequest();
        if(request.getMethod().equals("OPTIONS")){
            return null;//放行
        }

        String url=request.getRequestURL().toString();
//        if(url.indexOf("/admin/login")>0){
//            System.out.println("登陆页面"+url);
//            return null;//放行
//        }
        if(url.indexOf("login")>0){
            return null;
        }
        String head = request.getHeader("Authorization");
        if(head!=null && !"".equals(head)){
            if(head.startsWith("Bearer")){
                String token = head.substring(7);
                try{
                      Claims claims = jwtUtil.parseJWT(token);
                      String roles = (String)claims.get("roles");
                      if(roles.equals("admin")){
                          //把头信息转发出去并且放行
                          context.addZuulResponseHeader("Authorization",head);
                          return null;
                      }
                }catch (Exception e){
                    context.setSendZuulResponse(false);
                }
                //终止运行
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(403);
                context.setResponseBody("权限不足");
                context.getResponse().setContentType("text/html;charset=utf-8");
                return null;
            }
        }
        return null;
    }
}
