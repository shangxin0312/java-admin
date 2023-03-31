package cn.kgc.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "upload.image")
public class UploadProperties {
    private Integer maxSize;
    private List<String> allowTypes;
}
