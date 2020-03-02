package cn.com.sinosoft.action.mall;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONObject;

import cn.com.sinosoft.action.shop.BaseShopAction;
import cn.com.sinosoft.service.LoginService;

/**
 * 登录注册相关接口
 * @author 89007406
 *
 */
@ParentPackage("mall")
public class LoginAction extends BaseShopAction{
	
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	
	@Resource
	private LoginService loginService;
	
	/**
	 * 获取手机验证码
	 * @param phone
	 * @param type 0-登录 1-注册 2-找回密码 3-绑定手机
	 * @param flag 0-未验证手机号 1-已验证手机号
	 * @return
	 */
	public String getPhoneCode() {
		String phone = request.getParameter("phone");
		String type = request.getParameter("type");
		String flag = request.getParameter("flag");
		Map<Object,Object> map = loginService.getPhoneCode(phone,type,flag);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 获取邮箱验证码
	 * @param email
	 * @param type 0-登录 1-注册 2-找回密码 3-绑定邮箱
	 * @param flag 0-未验证邮箱号 1-已验证邮箱号
	 * @return
	 */
	public String getEmailCode() {
		String email = request.getParameter("email");
		String type = request.getParameter("type");
		String flag = request.getParameter("flag");
		Map<Object,Object> map = loginService.getEmaileCode(email,type,flag);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	/**
	 * 登录
	 * @param id
	 * @param password
	 * @param type 0-账号密码登录 1-手机验证码登录
	 * @return
	 */
	public String loginSystem(){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String type = request.getParameter("type");
		String ip = request.getRemoteAddr();
		Map<Object,Object> map = loginService.getloginSystem(username,password,type,ip);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 注册
	 * @param phone
	 * @param password1
	 * @param password2
	 * @param code
	 * @return
	 */
	public String registerSystem(){
		String phone = request.getParameter("phone");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String code = request.getParameter("code");
		String ip = request.getRemoteAddr();
		Map<Object,Object> map = loginService.getRegisterSystem(phone,password1,password2,code,ip);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}

	/**
	 * 找回密码
	 * @param phone
	 * @param password1
	 * @param password2
	 * @param code
	 * @return
	 */
	public String findPassword(){
		String phone = request.getParameter("phone");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String code = request.getParameter("code");
		Map<Object,Object> map = loginService.findPassword(phone,password1,password2,code);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	/**
	 * 通过手机号发送验证码找回密码
	 * @return
	 */
	public String findPasswordByPhone(){
		String phone = request.getParameter("phone");
		String code = request.getParameter("code");		
		Map<Object,Object> map = loginService.findPasswordByPhone(phone,code);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 修改密码
	 * @param id
	 * @param oldPassword
	 * @param newPassword1
	 * @param newPassword2
	 * @return
	 */
	public String modifyPassword(){
		String id = request.getParameter("id");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword1 = request.getParameter("newPassword1");
		String newPassword2 = request.getParameter("newPassword2");
		Map<Object,Object> map = loginService.modifyPassword(id,oldPassword,newPassword1,newPassword2);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	/**
	 * 修改密码--通过手机发送验证码找回密码
	 * @param id
	 * @param newPassword1
	 * @param newPassword2
	 * @return
	 */
	public String modifyPasswordByPhone(){
		String id = request.getParameter("id");
		String newPassword1 = request.getParameter("newPassword1");
		String newPassword2 = request.getParameter("newPassword2");
		Map<Object,Object> map = loginService.modifyPasswordByPhone(id,newPassword1,newPassword2);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 绑定新的手机号
	 * @param id
	 * @param phone
	 * @param code
	 * @return
	 */
	public String bindPhone(){
		String id = request.getParameter("id");
		String phone = request.getParameter("phone");
		String code = request.getParameter("code");
		Map<Object,Object> map = loginService.bindPhone(id,phone,code);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 绑定新的邮箱
	 * @param id
	 * @param email
	 * @param code
	 * @return
	 */
	public String bindEmail(){
		String id = request.getParameter("id");
		String email = request.getParameter("email");
		String code = request.getParameter("code");
		Map<Object,Object> map = loginService.bindEmail(id,email,code);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
}
