package cn.kgc.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.kgc.dto.PayloadDto;
import cn.kgc.entity.AccountUser;
import cn.kgc.entity.Role;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserService;
import cn.kgc.utils.JwtUtils;
import cn.kgc.utils.Result;
import cn.kgc.utils.TokenTypes;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //取出登录账户的信息
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();

        //准备载荷
        PayloadDto payloadDto = new PayloadDto();
        payloadDto.setUserId(accountUser.getUserId());
        payloadDto.setName(accountUser.getUsername());
        payloadDto.setRealName(accountUser.getRealName());
        //准备到数据库中查询出权限编码
        String authorityInfo = userService.getUserAuthorityInfo(accountUser.getUserId());
        payloadDto.setAuthority(authorityInfo);

        //生成令牌
        String token = jwtUtils.generateToken(payloadDto, TokenTypes.TOKEN);
        String refreshToken = jwtUtils.generateToken(payloadDto,TokenTypes.REFRESH_TOKEN);

        //返回令牌给前端
        Result result = Result.success(MapUtil.builder()
                                       .put("token",token)
                                       .put("refreshToken",refreshToken)
                                       .build());
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("utf-8"));
        outputStream.close();

    }
}
