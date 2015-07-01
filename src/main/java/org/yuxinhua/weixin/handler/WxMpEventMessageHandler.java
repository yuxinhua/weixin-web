package org.yuxinhua.weixin.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 微信事件消息处理器
 * @author yxinhua
 *
 */
@Component
public class WxMpEventMessageHandler implements WxMpMessageHandler {

	@Autowired
	private WxMpService wxMpService;
	
	@Value("${weixin.rankingTemplateId}")
	private String rankingTemplateId;

	private static final Log log = LogFactory
			.getLog(WxMpEventMessageHandler.class);

	/**
	 * 事件处理方法
	 */
	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {

		String eventKey = wxMessage.getEventKey();
		log.info("weixin event:" + eventKey);
		if ("E1001_RANKING".equals(eventKey)) {
			handleRanking(wxMessage, context, wxMpService, sessionManager);
			return null;
		}

		return null;
	}

	protected WxMpXmlOutMessage handleRanking(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		
		//TODO 事件处理逻辑
		return null;
	}

	
	
}
