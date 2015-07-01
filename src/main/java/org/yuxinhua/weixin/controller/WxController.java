package org.yuxinhua.weixin.controller;

import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yuxinhua.weixin.message.WxMpReceivedMessage;

/**
 * 微信接口Controller
 * @author yxinhua
 *
 */
@Controller
@RequestMapping("/rest/weixinmp")
public class WxController {

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private WxMpMessageRouter router;
	
	@Autowired
	private WxMpConfigStorage wxMpConfigStorage;

	private static final Log log = LogFactory.getLog(WxController.class);

	/**
	 * 签名验证接口
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.GET })
	public @ResponseBody String authGet(
			@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("echostr") String echostr) {
		if (wxMpService.checkSignature(timestamp, nonce, signature)) {
			if (StringUtils.isNotBlank(echostr)) {
				log.info("received authentication message from Weixin Server.");
				return echostr;
			}
		}
		return null;
	}

	/**
	 * 接收消息统一接口
	 * @param encyptType
	 * @param msgSignature
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.POST })
	public @ResponseBody String post(
			@RequestParam(value="encrypt_type",required=false ) String encyptType,
			@RequestParam(value="msg_signature",required=false) String msgSignature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("signature") String signature,
			HttpServletRequest request) throws Exception {

		if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			log.error("Invalid request!");
			return null;
		}
		WxMpXmlMessage inMessage = null;
		log.debug("New message.");
		if (StringUtils.isBlank(encyptType) || "raw".equals(encyptType)) { 
			inMessage = WxMpReceivedMessage.fromXml(request.getInputStream());
		} else if ("aes".equals(encyptType)) {
			inMessage = WxMpReceivedMessage.fromEncryptedXml(
					request.getInputStream(), wxMpConfigStorage, timestamp,
					nonce, msgSignature);
		}
		if (null == inMessage) {
			log.error("Parse Request failure!");
			return "Error_10001";
		}
		
		log.info("received message:" + inMessage);
		WxMpXmlOutMessage outMessage = router.route(inMessage);
		
		return null == outMessage ? null : outMessage.toXml();
	}
	

}
