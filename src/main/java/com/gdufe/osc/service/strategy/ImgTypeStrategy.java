package com.gdufe.osc.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author changwenbo
 * @date 2020/5/15 16:02
 */
@Service
@Slf4j
public abstract class ImgTypeStrategy {

	public abstract List<String> getImg(int offset, int limit);

	public abstract String getServiceName();
}
