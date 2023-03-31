package cn.kgc.service.impl;

import cn.kgc.exception.UploadException;
import cn.kgc.properties.UploadProperties;
import cn.kgc.service.UploadService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableConfigurationProperties(UploadProperties.class)
@Service
public class UploadServiceImpl implements UploadService {
    @Resource
    private FastFileStorageClient storageClient;
    @Resource
    private ThumbImageConfig thumbImageConfig;
    @Resource
    private UploadProperties uploadProperties;

    @Override
    public Map<String, String> uploadImage(MultipartFile file) throws Exception {
        Map<String,String> map = new HashMap<>();                        //保存上传的结果

        String originalFilename = file.getOriginalFilename();             //获取原始文件名
        String extension = FilenameUtils.getExtension(originalFilename);  //获取原始文件名的后缀

        //校验图片文件大小
        Integer maxSize = uploadProperties.getMaxSize();
        long size = file.getSize();
        if(size > 1024 * 1024 * maxSize){
            throw new UploadException("图片大小不能超过"+maxSize+"M");
        }

        //校验图片文件的类型
        String contentType = file.getContentType();
        List<String> allowTypes = uploadProperties.getAllowTypes();
        if (!allowTypes.contains(contentType)){
            throw new UploadException("只支持.jpg,.jpeg,.gif,.png,.bmp格式的图片");
        }

        //校验图片文件的内容
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if(bufferedImage == null){
            throw new UploadException("非法图片！");
        }

        //执行上传
        InputStream inputStream = file.getInputStream();
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(inputStream, inputStream.available(), extension, null);

        String fullPath = storePath.getFullPath();
        String thumbImagePath = thumbImageConfig.getThumbImagePath(fullPath);

        map.put("fullPath",fullPath);
        map.put("thumbImagePath",thumbImagePath);

        return map;
    }
}
