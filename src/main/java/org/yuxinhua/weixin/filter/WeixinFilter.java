package org.yuxinhua.weixin.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.JsonObject;

/**
 * 微信页面拦截器
 * 拦截 请求“/weixinmp/”，判断是否已Oauth认证
 * 
 * @author yxinhua
 *
 */
public class WeixinFilter extends OncePerRequestFilter {

	private String ignorePath;

	private String[] ignorePathArray;

	private final Log log = LogFactory.getLog(WeixinFilter.class);

	public static final String REQUEST_URI_PREFIX = "/app/weixinmp/";

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestUri = request.getRequestURI();
		HttpSession session = request.getSession();
		String path = requestUri.substring(request.getContextPath().length()
				+ REQUEST_URI_PREFIX.length());
		boolean ignore = false;
		
		if (null != ignorePathArray) {
			for (int i = 0; i < ignorePathArray.length; i++) {
				String p = StringUtils.trim(ignorePathArray[i]);
				if (path.startsWith(p)) {
					ignore = true;
					break;
				}
			}
		}

		if (!ignore) {
			// 判断是否有session
			String openId = (String) session.getAttribute("openId");
			if (StringUtils.isBlank(openId)) {
				log.error("invalid request:" + requestUri);
				// ajax 请求
				if ("XMLHttpRequest".equals(request
						.getHeader("X-Requested-With"))) {
					JsonObject json = new JsonObject();
					json.addProperty("timeout", true);
					response.getWriter().write(json.toString());
					return;
				}
				// html 请求
				request.getRequestDispatcher(
						"/WEB-INF/pages/weixin/sessionTimeoutError.jsp")
						.forward(request, response);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	public void setIgnorePath(String _ignorePath) {
		this.ignorePath = _ignorePath;
		if (StringUtils.isNoneBlank(ignorePath)) {
			ignorePathArray = ignorePath.split(",");
		}
	}

}
