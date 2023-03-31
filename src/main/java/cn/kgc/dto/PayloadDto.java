package cn.kgc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadDto {
    private Integer userId;
    private String name;
    private String realName;
    private String avatar;
    private String authority;
}
