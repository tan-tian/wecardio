package com.hiteam.common.web.controller.guest;

import com.hiteam.common.service.CaptchaService;
import com.hiteam.common.service.RSAService;
import com.hiteam.common.web.controller.BaseController;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller("commonController")
@RequestMapping({ "/guest/common" })
public class CommonController extends BaseController {

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	
	@Resource(name = "localeResolver")
	private LocaleResolver localeResolver;

	@RequestMapping({ "/resourceNotFound" })
	public String resourceNotFound() {
		return "/common/404";
	}

	@RequestMapping({ "/error" })
	public String error(Exception exception) {
		return "/common/error";
	}
	
	@RequestMapping({ "/casFailure" })
	public String casFailure() {
		return "/common/casFailure";
	}
	
	@RequestMapping("/unauthorized")
	public String unauthorized() {
		return "/common/unauthorized";
	}
	
	@RequestMapping(value = { "/main" }, method = { RequestMethod.GET })
	public String main() {
		return "/admin/main";
	}

	@RequestMapping(value = { "/publicKey" }, method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, String> publicKey(HttpServletRequest request) {
		RSAPublicKey rsaPublicKey = this.rsaService.generateKey();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("modulus", Base64.encodeBase64String(rsaPublicKey.getModulus().toByteArray()));
		map.put("exponent", Base64.encodeBase64String(rsaPublicKey.getPublicExponent().toByteArray()));
		return map;
	}


	/**
	 * 生成验证码
	 */
	@RequestMapping(value = { "/captcha" }, method = { RequestMethod.GET })
	public void image(HttpServletResponse response,HttpSession httpSession) {

		String str1 = new StringBuffer().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String str2 = new StringBuffer().append(".").append("mae").append("tih").reverse().toString();
		response.addHeader(str1, str2);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");	// 防止Firefox缓存
		response.setDateHeader("Expires", 0L);
		response.setContentType("image/jpeg");
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			BufferedImage bufferedImage = this.captchaService.buildImage(httpSession.getId());
			ImageIO.write(bufferedImage, "jpg", outputStream);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	@RequestMapping("/changeLocale/{locale}")
	public String changeLocale(@PathVariable String locale, HttpServletRequest request, HttpServletResponse response) {
		Locale l = new Locale(locale);
		localeResolver.setLocale(request, response, l);
		return "redirect:/";
	}

}
