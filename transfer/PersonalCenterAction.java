package cn.com.sinosoft.action.mall;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONObject;

import cn.com.sinosoft.action.shop.BaseShopAction;
import cn.com.sinosoft.entity.Member;
import cn.com.sinosoft.entity.MemberContacts;
import cn.com.sinosoft.service.PersonalCenterService;

/**
 * 个人中心相关接口
 * @author 89007406
 *
 */
@ParentPackage("mall")
public class PersonalCenterAction extends BaseShopAction{

	private static final long serialVersionUID = 1L;

	@Resource
	private PersonalCenterService personalCenterService;
	private HttpServletRequest request = ServletActionContext.getRequest();

	private File file;
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 查看个人信息
	 * @param username
	 * @return
	 */
	public String getPersonalInfo() {
		String id = request.getParameter("id");
		Map<Object,Object> map = personalCenterService.getPersonalInfo(id);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 修改个人信息
	 * @param member
	 * @return
	 */
	public String modifyPersonalInfo() {
		String id = request.getParameter("id");
		String username = request.getParameter("username");
		String newUsername = request.getParameter("newUsername");
		String sex = request.getParameter("sex");
		String realName = request.getParameter("realName");
		String birthday = request.getParameter("birthday");
		String location = request.getParameter("location");
		String address = request.getParameter("address");
		String personalURL = request.getParameter("personalURL");
		String isfirstMC = request.getParameter("isfirstMC");
		Member member = new Member();
		member.setId(id);;
		member.setUsername(username);
		member.setSex(sex);
		member.setRealName(realName);
		member.setBirthday(birthday);
		member.setLocation(location);
		member.setAddress(address);
		member.setPersonalURL(personalURL);
		member.setIsfirstMC(isfirstMC);
		Map<Object,Object> map = personalCenterService.modifyPersonalInfo(member,newUsername);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 获得联系人列表
	 * @param username
	 * @return
	 */
	public String getContactsList(){
		String id = request.getParameter("id");
		List<Map<Object,Object>> list = personalCenterService.getContactsList(id);
		JSONArray jsonObject = JSONArray.fromObject(list);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 获得某一联系人详细信息
	 * @param id
	 * @return
	 */
	public String getOneContactInfo(){
		String id = request.getParameter("id");
		Map<Object,Object> map = personalCenterService.getOneContactInfo(id);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 新增/修改联系人
	 * @param memberContacts
	 * @return
	 */
	public String modifyContactInfo(){
		String mid = request.getParameter("mid");
		String id = request.getParameter("id");
		String relationship = request.getParameter("relationship");
		String realName = request.getParameter("realName");
		String idType = request.getParameter("idType");
		String idCard = request.getParameter("idCard");
		String birthday = request.getParameter("birthday");
		String enName = request.getParameter("enName");
		String sex = request.getParameter("sex");
		String location = request.getParameter("location");
		String address = request.getParameter("address");
		String email = request.getParameter("email");
		String zipCode = request.getParameter("zipCode");
		String phone = request.getParameter("phone");
		MemberContacts memberContacts = new MemberContacts(id, mid, relationship, realName, idType, idCard, birthday, enName, sex, location, address, email, zipCode, phone);
		Map<Object,Object> map = personalCenterService.modifyContactInfo(memberContacts);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 删除联系人
	 * @param id
	 * @return
	 */
	public String deleteContactInfo(){
		String id = request.getParameter("id");
		Map<Object,Object> map = personalCenterService.deleteContactInfo(id);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 获取账户安全信息
	 * @param id
	 * @return
	 */
	public String getContactSaveInfo(){
		String id = request.getParameter("id");
		Map<Object,Object> map = personalCenterService.getContactSafeInfo(id);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 上传头像信息
	 * @param id
	 * @param file
	 * @return
	 */
	public String uploadHeadImage(){
		String id = request.getParameter("id");
		File file = this.file;
		Map<Object,Object> map = personalCenterService.uploadHeadImage(id,file);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 展示头像
	 * @return
	 */
	public String showHeadImage(){
		String id = request.getParameter("id");
		Map<Object,Object> map = personalCenterService.showHeadImage(id);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 获取推荐信息
	 * @param id
	 * @return
	 */
	public String getRecommendInfo(){
		String id = request.getParameter("id");
		Map<Object,Object> map = personalCenterService.getRecommendInfo(id);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}

}
