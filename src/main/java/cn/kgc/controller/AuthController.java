package cn.kgc.controller;

import cn.hutool.core.map.MapUtil;
import cn.kgc.dto.PayloadDto;
import cn.kgc.entity.Role;
import cn.kgc.entity.User;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserService;
import cn.kgc.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@CrossOrigin           //允许跨域访问
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private UserService userService;
    @Resource
    private JwtUtils jwtUtils;

    @GetMapping("/info")
    public Result UserInfo(@RequestHeader("Refresh-Token") String token){
        PayloadDto payloadDto = jwtUtils.parseToke(token);
        User user = userService.getOne(new QueryWrapper<User>().eq("username", payloadDto.getName()));
        return  Result.success(MapUtil.builder()
                .put("name",user.getUsername())
                .put("avatar",user.getAvatar())
                .put("realName",user.getRealName())
                .build());   //解析令牌
    }


}
