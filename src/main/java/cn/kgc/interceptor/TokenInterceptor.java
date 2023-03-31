package cn.kgc.interceptor;

import cn.hutool.json.JSONUtil;
import cn.kgc.dto.PayloadDto;
import cn.kgc.utils.JwtUtils;
import cn.kgc.utils.Result;
import cn.kgc.utils.StatusCode;
import cn.kgc.utils.TokenTypes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置响应的内容类型
        response.setContentType("application/json;charset=UTF-8");
        //从响应中获取输出流
        OutputStream out = response.getOutputStream();

        String token = null;
        Result result = null;
        try{
            token = request.getHeader("X-Token");   //从请求头中获取令牌
            jwtUtils.parseToke(token);                       //解析令牌
            return  true;
        }catch (ExpiredJwtException e){
            try{
                String refreshToken = request.getHeader("Refresh-Token");  //从请求头中获取刷新令牌
                PayloadDto payload = jwtUtils.parseToke(refreshToken);

                //给令牌续命,即生成新令牌，过期时间重新计算
                token = jwtUtils.generateToken(payload, TokenTypes.TOKEN);
                refreshToken = jwtUtils.generateToken(payload,TokenTypes.REFRESH_TOKEN);

                //暴露响应头
                response.addHeader("Access-Control-Expose-Headers","new-token");
                response.addHeader("Access-Control-Expose-Headers","new-refresh-token");

                //将新令牌放到响应头中返回给客户端
                response.addHeader("new-token",token);
                response.addHeader("new-refresh-token",refreshToken);

                return true;

            }catch (ExpiredJwtException ex){
                result = Result.fail(StatusCode.EXPIRED_TOKEN,"令牌已过期，请重新登录");
            }
        }catch (SignatureException e){
            result = Result.fail(StatusCode.ILLEGAL_TOKEN,"令牌非法!");
        }catch (Exception e){
            result = Result.fail(StatusCode.ILLEGAL_TOKEN,"令牌解析异常！");
        }

        out.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        out.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
