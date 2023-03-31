package cn.kgc.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "kgc.jwt")
public class JwtProperties {
    private String secret;      // 密钥
    private Integer tokenExpire; // 令牌过期时间
    private Integer refreshTokenExpire; // 延时令牌过期时间
}
