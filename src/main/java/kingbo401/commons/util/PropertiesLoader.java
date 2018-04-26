package kingbo401.commons.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class PropertiesLoader{
	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	public static Properties load(String fileName){
		Assert.hasText(fileName, "fileName不能为空");
		Properties properties = new Properties();
		if(!fileName.startsWith("/")){
			fileName = "/"+fileName;
		}
		InputStream is = null;
		try {
			is = PropertiesLoader.class.getResourceAsStream(fileName);
			properties.load(is);
		} catch (Exception e) {
			logger.error("系统配置文件读取失败，文件：" + fileName , e);
		}finally{
			CloseUtil.close(is);
		}
		return properties;
	}
}
