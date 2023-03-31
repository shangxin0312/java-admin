package cn.kgc.handler;

import cn.hutool.json.JSONUtil;
import cn.kgc.utils.Result;
import cn.kgc.utils.StatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AcceDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");

        ServletOutputStream outputStream = response.getOutputStream();
        Result result = Result.fail(StatusCode.ACCESS_DENIED,"权限不足 请联系管理员");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("utf-8"));
        outputStream.close();
    }
}
