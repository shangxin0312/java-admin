package cn.kgc.utils;

import cn.kgc.dto.PayloadDto;
import cn.kgc.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtUtils {

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 生成令牌
     * @param payload 令牌中所携带的数据
     * @return  令牌字符串
     */
    public String generateToken(PayloadDto payload,TokenTypes type){
        Date now = new Date();      //签发时间
        int expire = type == TokenTypes.TOKEN ? jwtProperties.getTokenExpire() : jwtProperties.getRefreshTokenExpire();
        Date expireDate = new Date(now.getTime() + expire * 1000);  //过期时间

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .signWith(SignatureAlgorithm.HS512,jwtProperties.getSecret())
                .claim("userId",payload.getUserId())
                .claim("name",payload.getName())
                .claim("realName",payload.getRealName())
                .claim("avatar",payload.getAvatar())
                .claim("authority",payload.getAuthority())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .compact();                  //合并生成令牌
    }


    /**
     * 解析令牌，取出信息中所携带的用户身份信息
     * @param token  被解析的令牌
     * @return 封装用户身份信息的载荷对象
     */
    public PayloadDto parseToke(String token){
        Claims body = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();

        Integer userId = (Integer)body.get("userId");
        String name = (String)body.get("name");
        String realName = (String)body.get("realName");
        String avatar = (String)body.get("avatar");
        String authority = (String)body.get("authority");

        return new PayloadDto(userId,name,realName,avatar,authority);
    }

}
