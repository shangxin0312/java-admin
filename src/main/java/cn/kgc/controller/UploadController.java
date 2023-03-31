package cn.kgc.controller;

import cn.kgc.service.UploadService;
import cn.kgc.utils.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

//@CrossOrigin  //允许跨域访问
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Resource
    private UploadService uploadService;

    @PostMapping("/image")
    public Result uploadImage(MultipartFile image) throws Exception{
        return Result.success(uploadService.uploadImage(image));
    }
}
