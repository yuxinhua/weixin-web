package org.yuxinhua.weixin.message;

import java.io.InputStream;

import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

import org.yuxinhua.weixin.util.WxXStreamTransformer;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 重新定义接受消息类：
 * 1. 增加WxMpXmlMessage缺少的消息内容；
 * 2. 使用WxXStreamTransformer解析消息
 * 
 * @author yxinhua
 *
 */
@XStreamAlias("xml")
public class WxMpReceivedMessage extends WxMpXmlMessage {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("DeviceType")
	@XStreamConverter(value = XStreamCDataConverter.class)
	private String deviceType;

	@XStreamAlias("DeviceID")
	@XStreamConverter(value = XStreamCDataConverter.class)
	private String deviceId;
	
	@XStreamAlias("SessionID")
	private String sessionId;
	
	@XStreamAlias("OpenID")
	@XStreamConverter(value = XStreamCDataConverter.class)
	private String openId;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	public static WxMpReceivedMessage fromXml(String xml) {
		return WxXStreamTransformer.fromXml(WxMpReceivedMessage.class, xml);
	}

	/**
	 * 重新定义fromXml方法，用自定义的WxXStreamTransformer而不是XStreamTransformer来解析扩展的消息格式
	 * @param is
	 * @return
	 */
	public static WxMpReceivedMessage fromXml(InputStream is) {
		return (WxMpReceivedMessage)WxXStreamTransformer.fromXml(WxMpReceivedMessage.class, is);
	}
	
	@Override
	  public String toString() {
	    return "WxMpReceivedMessage{" +
	        "toUserName='" + getToUserName() + '\'' +
	        ", fromUserName='" + getFromUserName() + '\'' +
	        ", createTime=" + getCreateTime() +
	        ", msgType='" + getMsgType() + '\'' +
	        ", deviceType='" + getDeviceType() + '\'' +
	        ", deviceId='" + getDeviceId() + '\'' +
	        ", sessionId='" + getSessionId() + '\'' +
	        ", openId='" + getOpenId() + '\'' +
	        ", content='" + getContent() + '\'' +
	        ", msgId=" + getMsgId() +
	        ", picUrl='" + getPicUrl() + '\'' +
	        ", mediaId='" + getMediaId() + '\'' +
	        ", format='" + getFormat() + '\'' +
	        ", thumbMediaId='" + getThumbMediaId() + '\'' +
	        ", locationX=" + getLocationX() +
	        ", locationY=" + getLocationY() +
	        ", scale=" + getScale() +
	        ", label='" + getLabel() + '\'' +
	        ", title='" + getTitle() + '\'' +
	        ", description='" + getDescription() + '\'' +
	        ", url='" + getUrl() + '\'' +
	        ", event='" + getEvent() + '\'' +
	        ", eventKey='" + getEventKey() + '\'' +
	        ", ticket='" + getTicket() + '\'' +
	        ", latitude=" + getLatitude() +
	        ", longitude=" + getLongitude() +
	        ", precision=" + getPrecision() +
	        ", recognition='" + getRecognition() + '\'' +
	        ", status='" + getStatus() + '\'' +
	        ", totalCount=" + getTotalCount() +
	        ", filterCount=" + getFilterCount() +
	        ", sentCount=" + getSentCount() +
	        ", errorCount=" + getErrorCount() +
	        ", scanCodeInfo=" + getScanCodeInfo() +
	        ", sendPicsInfo=" + getSendPicsInfo() +
	        ", sendLocationInfo=" + getSendLocationInfo() +
	        '}';
	  }

	
	public static void main(String[] args) {
		/*String s = "AAhLUzI=";
		byte[] reqRaw = Base64.decodeBase64(s);
		ByteBuffer buf = ByteBuffer.wrap(reqRaw);*/
		String s = "<xml><ToUserName><![CDATA[yxh]]></ToUserName><DeviceType><![CDATA[testType]]></DeviceType><DeviceID><![CDATA[testId]]></DeviceID></xml>";
		WxMpReceivedMessage m = fromXml(s);
		System.out.println(m.toString());
	}
	
}
