package com.everwing.coreservice.platform.core.service.impl.other;

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.service.other.SmsService;
import com.everwing.coreservice.common.utils.HttpUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.everwing.coreservice.common.utils.CommonUtils.returnSuccess;

@Service
public class SmsServiceImpl implements SmsService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SpringRedisTools springRedisTools;

	private final String REDIS_KEY_PREFIX_VERIFY_CODE = "mobile_verify_code:";

	@Value("${sms.password}")
	private String password;
	@Value("${sms.account}")
	private String account;
	@Value("${sms.userid}")
	private String userid;// 企业ID

	@Override
	public RemoteModelResult<Void> sendVerifyCode(String phoneNum) {
		// 生成验证码
		String code = getRandomSixNum();
		// 发送
		sendText("【翔恒科技】您的验证码为：" + code + "，请在5分钟内使用，超时请重新获取。", phoneNum);
		// 存入redis
		springRedisTools.addData(generateRedisKeyByPhoneNum(phoneNum),code, 5, TimeUnit.MINUTES);
		return returnSuccess();
	}

	/**
	 * @description 发送文本短信
	 */
	@Override
	public RemoteModelResult<Void> sendText(String content, String phoneNum)  {
		String url="http://114.55.42.151:8860";
		String password = "U8ZZ7G7FH7";
		String customer_code = "950142";
		String mobibes = phoneNum;
		content = URLEncoder.encode(content);

		Map<String, String> params=new HashMap<String, String>();
		params.put("cust_code", customer_code);
		params.put("content", content);
		params.put("destMobiles", mobibes);
		params.put("sign", DigestUtils.md5Hex(content+password));
		String result = null;
		try {
			result = HttpUtils.doPost(url,HttpUtils.convertToQueryStr(params).substring(1));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		/*-
		 SUCCESS:%E6%8F%90%E4%BA%A4%E6%88%90%E5%8A%9F%EF%BC%81
		18924269055:59106105261521698235:0
		17727958997:59106105261521698236:0
		 * */
		logger.info("手机号码：{} | 内容：{}", phoneNum, URLDecoder.decode(content));
		logger.info(result);
		if (result.contains("SUCCESS")) {
			return new RemoteModelResult<>();
		} else {
			logger.error(result);
			throw new ECPBusinessException(ReturnCode.PF_SMS_FAIL);
		}
	}

	@Override
	public RemoteModelResult<Void> deletePhoneNum(String phoneNum){
		springRedisTools.deleteByKey(generateRedisKeyByPhoneNum(phoneNum));
		return returnSuccess();
	}

	@Override
	public RemoteModelResult<Boolean> isVerifyCodeCorrect(String phoneNum, String verifyCode, boolean delete) {
		if (StringUtils.isBlank(verifyCode)) {
			return new RemoteModelResult<Boolean>(false);
		}
		if("000000".equals(verifyCode)){//万能验证码，开发阶段使用
			return new RemoteModelResult<Boolean>(true);
		}
		String key = generateRedisKeyByPhoneNum(phoneNum);
		if (verifyCode.equals(springRedisTools.getByKey(key))) {
			springRedisTools.deleteByKey(key);
			return new RemoteModelResult<Boolean>(true);
		} else {
			return new RemoteModelResult<Boolean>(false);
		}
	}

	private String generateRedisKeyByPhoneNum(String phoneNum) {
		return REDIS_KEY_PREFIX_VERIFY_CODE + phoneNum;
	}

	/**
	 * @description 生成6位随机数字
	 */
	private String getRandomSixNum() {
		return Double.valueOf(((Math.random() + 1) * 100000)).intValue() + "";
	}
	
}
