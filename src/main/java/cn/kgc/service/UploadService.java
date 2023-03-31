package cn.kgc.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadService {

    /**
     * 上传图片
     * @param file
     * @return
     * @throws Exception
     */
    public Map<String,String> uploadImage(MultipartFile file) throws Exception;
}
