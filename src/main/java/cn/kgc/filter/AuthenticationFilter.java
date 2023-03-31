package cn.kgc.filter;

import cn.hutool.json.JSONUtil;
import cn.kgc.dto.PayloadDto;
import cn.kgc.utils.JwtUtils;
import cn.kgc.utils.Result;
import cn.kgc.utils.StatusCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
//@Component   //没有无参构造方法 不能使用Component注解
public class AuthenticationFilter extends BasicAuthenticationFilter {
    @Resource
    private JwtUtils jwtUtils;
    //只有带参构造方法
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            //从请求头中获取令牌
            String refreshToken = request.getHeader("Refresh-Token")!=null?request.getHeader("Refresh-Token"):request.getHeader("jwt");
            //从令牌中获取权限编码字符串
            PayloadDto payloadDto = jwtUtils.parseToke(refreshToken);
            String authority = payloadDto.getAuthority();
            //准备Authentication对象
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken
                            (payloadDto.getUserId(),null,
                                    AuthorityUtils.commaSeparatedStringToAuthorityList(authority));
            //将Authentication对象保存到上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //放行请求
            chain.doFilter(request,response);
        }catch (ExpiredJwtException ex){
            Result result = Result.fail(StatusCode.EXPIRED_TOKEN,"令牌已过期，请重新登录");
            response.setContentType("application/json;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            out.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
            out.close();
        }
    }
}
