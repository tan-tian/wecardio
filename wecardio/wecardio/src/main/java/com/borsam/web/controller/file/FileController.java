package com.borsam.web.controller.file;

import com.borsam.pojo.file.FileInfo;
import com.borsam.service.pub.FileService;
import com.hiteam.common.util.ConfigUtils;
import com.hiteam.common.util.Multimedia;
import com.hiteam.common.util.json.JsonUtils;
import com.hiteam.common.web.Message;
import com.hiteam.common.web.controller.BaseController;
import com.hiteam.common.web.template.freemarker.FreemarkerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 文件
 * Created by Sebarswee on 2015/6/30.
 */
@Controller("fileController")
@RequestMapping("/file")
public class FileController extends BaseController {

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    /**
     * 上传
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
    public void upload(FileInfo.FileType fileType, MultipartFile file, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<String, Object>();
        System.out.println(fileType+file.getOriginalFilename());
        if (!fileService.isValid(fileType, file)) {
        	data.put("message", Message.warn("common.upload.invalid"));
        } else {
            String url = fileService.upload(fileType, file, false);
            if (url == null) {
                data.put("message", Message.warn("common.upload.error"));
            } else {
                data.put("message", SUCCESS_MSG);
                data.put("url", url);
            }
        }
        try {
            response.setContentType("text/html; charset=UTF-8");
            JsonUtils.writeValue(response.getWriter(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件浏览
     */
    @RequestMapping
    public String browser(String path, HttpServletResponse response) throws Exception {
        Assert.notNull(path);

        File file = new File(ConfigUtils.config.getProperty("uploadPath"), path);
        if (!file.exists()) {
            return null;
        }
        byte[] data = FileUtils.readFileToByteArray(file);
        response.reset();
        response.setContentType(Multimedia.MIME_TYPES.get(FilenameUtils.getExtension(path)));
        response.addHeader("Content-Length", "" + data.length);
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();

        return null;
    }

    /**
     * 头像裁剪页面
     */
    @RequestMapping(value = "/crop", method = RequestMethod.GET)
    public String crop(String image, Model model) {
        File file = new File(ConfigUtils.config.getProperty("uploadPath"), image);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            model.addAttribute("width", bufferedImage.getWidth());
            model.addAttribute("height", bufferedImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("image", image);
        return "/common/crop";
    }

    /**
     * 头像裁剪
     */
    @RequestMapping(value = "/crop", method = RequestMethod.POST)
    @ResponseBody
    public Message crop(String image, float x, float y, float w, float h, float width, float height, float scale) {
        try {
            String uploadPath = ConfigUtils.config.getProperty("uploadPath");
            File source = new File(uploadPath, image);
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
            String destPath = FreemarkerUtils.process(ConfigUtils.config.getProperty("imageUploadPath"), model);
            String uuid = UUID.randomUUID().toString();
            String headPath = destPath + uuid + "-head.jpg";

            /**
             * 源图片在界面上进行了缩放
             * 裁剪时需要对裁剪位置进行相应的缩放
             */
            int x1 = (int) (x / scale);
            int y1 = (int) (y / scale);
            int w1 = (int) (w / scale);
            int h1 = (int) (h / scale);

            Multimedia.saveImage(source, uploadPath + headPath, x1, y1, w1, h1, (int) width, (int) height);
            return SUCCESS_MSG.addResult(headPath);
        } catch (Exception e) {
            return ERROR_MSG;
        }
    }
}
