package cn.com.sinosoft.action.mall;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.sinosoft.framework.utility.StringUtil;

import cn.com.sinosoft.action.shop.BaseShopAction;
import cn.com.sinosoft.action.supplier.AppliInsureAction;
import cn.com.sinosoft.service.WechatService;
/**
 * 微信相关接口
 * @author 89007406
 *
 */
@ParentPackage("mall")
public class WeChatAction extends BaseShopAction{
	
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(WeChatAction.class);

	@Resource
	private WechatService weChatService;
	HttpServletRequest request = ServletActionContext.getRequest();
	//服务器默认token
	public static final String TOKEN="sfcmsweixinflag";
	
	/**
	 * 微信校验接口联调
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param token
	 * @return
	 */
	public String checkWeChat() {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println("微信传过来的echostr："+echostr);
		String msg = weChatService.checkWeChat(signature,timestamp,nonce,echostr,TOKEN);
		logger.info("微信校验接口联调-返回:{}", msg);
		if(StringUtil.isEmpty(msg)){
			msg = "";
		}
		return ajax(msg, "text/html");
	}
	
	/**
	 * 创建微信菜单
	 * @param menuJson
	 * @return
	 */
	public String createMenu() {
		String menuJson = "{\"button\":["
						+ "{\"name\":\"保险精选\",\"sub_button\":["
						+ "{\"type\":\"view\",\"name\":\"爆款1\",\"url\":\"http://www.tmall.com/\"},"
						+ "{\"type\":\"view\",\"name\":\"爆款2\",\"url\":\"http://www.jd.com/\"},"
						+ "{\"type\":\"view\",\"name\":\"员工车险\",\"url\":\"http://www.baidu.com/\"},"
						+ "{\"type\":\"view\",\"name\":\"企业专区\",\"url\":\"http://www.soso.com/\"},"
						+ "{\"type\":\"view\",\"name\":\"查看更多\",\"url\":\"http://www.soso.com/\"}"
						+ "]},"
						+ "{\"type\":\"view\",\"name\":\"选购攻略\",\"url\":\"http://www.soso.com/\"},"
						+ "{\"name\":\"我的\",\"sub_button\":["
						+ "{\"type\":\"view\",\"name\":\"福利活动\",\"url\":\"http://www.soso.com/\"},"
						+ "{\"type\":\"view\",\"name\":\"个人中心\",\"url\":\"http://www.soso.com/\"},"
						+ "{\"type\":\"view\",\"name\":\"关于我们\",\"url\":\"http://www.soso.com/\"}"
						+ "]}"
						+ "]}";
		Map<String,Object> map = weChatService.createMenu(menuJson);
		JSONObject json = JSONObject.fromObject(map);
		return ajax(json.toString(), "text/html");
	}
	
	/**
	 *	校验企业员工
	 * @param idNo
	 * @return
	 */
	public String checkIdNo(){
		String idNo = request.getParameter("idNo");
		Map<String,Object> map = weChatService.checkIdNo(idNo);
		JSONObject json = JSONObject.fromObject(map);
		return ajax(json.toString(), "text/html");
	}
	
	/**
	 * 雇主责任险校验empId
	 * @param empid
	 * @return
	 */
	public String checkEmpId(){
		String empId = request.getParameter("empId");
		Map<String,Object> map = weChatService.checkEmpId(empId);
		JSONObject json = JSONObject.fromObject(map);
		return ajax(json.toString(), "text/html");
	}
	
	/**
	 * 获取员工信息
	 * @param empId
	 * @return
	 */
	public String getEmpInfo(){
		String empId = request.getParameter("empId");
		String url = request.getParameter("url");
		Map<String,Object> map = weChatService.getEmpInfo(empId,url);
		JSONObject json = JSONObject.fromObject(map);
		return ajax(json.toString(), "text/html");
	}
	
	/**
	 * 根据链接生成二维码
	 * @return
	 */
	public String getQrCode(){
		String url = request.getParameter("url");
		Map<String,Object> map = weChatService.getQrCode(url);
		JSONObject json = JSONObject.fromObject(map);
		return ajax(json.toString(), "text/html");
	}
}
