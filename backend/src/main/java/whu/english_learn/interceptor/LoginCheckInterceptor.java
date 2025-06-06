package whu.english_learn.interceptor;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import whu.english_learn.result.Result;
import whu.english_learn.utils.JwtUtils;

/**
 * 重写拦截器方法以实现JWT鉴权
 */
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求url
        String url = request.getRequestURI().toString();

        //判断是否是登录请求，如果url中包含login则为登录请求，放行
        if(url.contains("login")) {
            return true;
        }
        if(url.contains("sendmail")) {
            return true;
        }
        if(url.contains("register")) {
            return true;
        }

        //获取请求头的jwt令牌
        String jwt = request.getHeader("token");

        //判断令牌是否存在，如果不存在，返回错误信息（未登录）
        if(!StringUtils.hasLength(jwt)) {
            Result error = Result.error("NOT_LOGIN");
            //手动转换对象为json格式
             String notLogin = JSONObject.toJSONString(error);
             response.getWriter().write(notLogin);
             return false;
        }

        //解析token，如果解析失败，返回错误结果（未登录）
        try {
            JwtUtils.parseJwt(jwt);
        }
        catch (Exception e) {
            e.printStackTrace();
            Result error = Result.error("NOT_LOGIN");
            //手动转换对象为json格式
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
