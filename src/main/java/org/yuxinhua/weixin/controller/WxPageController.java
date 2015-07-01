package org.yuxinhua.weixin.controller;

import javax.servlet.http.HttpSession;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * 公众号内页面Controlloer
 * @author yxinhua
 *
 */
@Controller
@RequestMapping("/weixinmp*")
public class WxPageController {

	@Autowired
	private WxMpService wxMpService;

	private static final Log log = LogFactory.getLog(WxPageController.class);

	/**
	 * ”运动数据”页面（Demo）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sportsData", method = RequestMethod.GET)
	public ModelAndView showSportsData() {

		return new ModelAndView("weixin/sportsData");
	}

	/**
	 * weixin oauth 接口
	 * 微信页面统一的入口， 如运动数据页面的地址为：https://open.weixin.qq.com/connect/oauth2/authorize?appid=<<appid>>&redirect_uri=http://<<your-server-address>>/weixin-web/weixinmp/oauth&response_type=code&scope=snsapi_base&state=sportsData#wechat_redirect
	 * @param code
	 * @param session
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/oauth", method = RequestMethod.GET)
	public ModelAndView oauth(@RequestParam String code, HttpSession session,
			@RequestParam String state) {
		try {
			log.info("oauth request with code: " + code);
			String openId = (String) session.getAttribute("deviceId");

			if (StringUtils.isBlank(openId)) { // session timeout
				// 网页授权获取用户基本信息
				WxMpOAuth2AccessToken accessToken = wxMpService
						.oauth2getAccessToken(code);
				openId = accessToken.getOpenId();
				WxMpUser wxUser = wxMpService.userInfo(openId, "zh_CN");
				log.info("oauth access token : " + openId);
				session.setAttribute("openId", openId);
				session.setAttribute("wxNickName", wxUser.getNickname());

				session.setAttribute("deviceId", openId);
			}

			//根据state重定向到页面 
			if (StringUtils.isBlank(state) || "sportsData".equals(state)) { 
				return new ModelAndView("redirect:/weixinmp/sportsData");
			}

		} catch (WxErrorException e) {
			log.error(e);
		}
		return null;
	}

}
