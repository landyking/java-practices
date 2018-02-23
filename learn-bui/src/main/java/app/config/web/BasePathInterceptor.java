package app.config.web;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasePathInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    /**
     * 为了兼容URL转发功能,不能将ip地址放到basePath中
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String path = request.getContextPath();
            String baseUrl = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path;
            String basePath = path + "/";
            request.setAttribute("basePath", basePath);
            request.setAttribute("ctx", path);
            request.setAttribute("baseUrl", baseUrl);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}