package com.gdufe.osc.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author changwenbo
 * @date 2019/10/22 16:23
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class FileController {

	@RequestMapping("/img/url/{id}")
	public void readImgUrl(HttpServletResponse response,
	                       @PathVariable(value = "id", required = false) String id, String index) {
		try {
			byte[] bytes = FileUtils.readFileToByteArray(new File("/home/tomcat/apache-tomcat-8.5.23/workspace/osc/img/" + id + "/" + index + ".jpg"));
			if (bytes == null) {
				log.error("读取图片出错,该图片可能不存在");
			}
			response.getOutputStream().write(bytes);
			response.flushBuffer();
			log.info("成功读取到了图片，位置为：{}", id);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("读取图片出错，message={}", e.getMessage());
		}
	}
}
