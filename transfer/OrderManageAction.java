package cn.com.sinosoft.action.mall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONObject;

import cn.com.sinosoft.action.shop.BaseShopAction;
import cn.com.sinosoft.service.OrderManageService;
import cn.com.sinosoft.service.RedirectService;

import com.sinosoft.framework.data.QueryBuilder;
import com.sinosoft.framework.data.Transaction;
import com.sinosoft.framework.utility.DateUtil;

/**
 * 订单管理接口
 * @author 89007406
 *
 */
@ParentPackage("mall")
public class OrderManageAction extends BaseShopAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource
	private OrderManageService orderManageService;
	@Resource
	private RedirectService redirectService;
	private HttpServletRequest request = ServletActionContext.getRequest();

	/**
	 * 获取评价列表
	 * @param username
	 * @return
	 */
	public String getCommentList() {
		String id = request.getParameter("id");
		List<Map<Object,Object>> list = orderManageService.getCommentList(id);
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("commentList", list);
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 评价商品
	 * @param zcComment
	 * @return
	 */
	public String modifyCommentInfo() {
		Map<Object,Object> map=new HashMap<Object, Object>();
		try{
			String Content = request.getParameter("Content");
			String Score = request.getParameter("Score");
			String IsBuy = request.getParameter("IsBuy");
			String AddUser  = request.getParameter("AddUser");
			String Title = request.getParameter("Title");
			String OrderSn = request.getParameter("OrderSn");
			String cha_id = request.getParameter("cha_id");
			String sql="insert into zccomment (RelaID,CatalogID,CatalogType,SiteID,title,content,addUser,addTime,prop1,Score,IsBuy,VerifyFlag) value (?,?,?,?,?,?,?,?,?,?,?,?)";
			QueryBuilder qb=new QueryBuilder(sql);
			qb.add("1");
			qb.add("1");
			qb.add("1");
			qb.add(cha_id);
			qb.add(Title);
			qb.add(Content);
			qb.add(AddUser);
			qb.add(DateUtil.getCurrentDateTime());
			qb.add(OrderSn);
			qb.add(Score);
			qb.add(IsBuy);	
			qb.add("X");		
			Transaction trans=new Transaction();
			trans.add(qb);
			if(trans.commit()){
				map.put("status", "0");
				map.put("message", "评论成功！");
			}else{
				map.put("status", "1");
				map.put("message", "评论失败！");
	
			}
		}catch(Exception e){
			map.put("status", "1");
			map.put("message", "评论失败！");
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject(map);
			return ajax(jsonObject.toString(), "text/html");
		}
//		ZCCommentSchema zcComment = new ZCCommentSchema();
//		Map<Object,Object> map = orderManageService.modifyCommentInfo();
		JSONObject jsonObject = new JSONObject(map);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	/**
	 * 获取订单列表
	 * @param id
	 * @return
	 */
	public String getOrderList() {
		String id = request.getParameter("id");
		List<Map<Object,Object>> list = orderManageService.getOrderList(id);
		JSONArray json = JSONArray.fromObject(list);
		return ajax(json.toString(), "text/html");
	}
	
	/**
	 * 获取搜索列表
	 * @param search
	 * @param type 0-险种 1-名称
	 * @return
	 */
	public String getSearchList() {
		String search = request.getParameter("search");
		String type = request.getParameter("type");
		String chaId = request.getParameter("cha_id");
		List<Map<Object,Object>> list = orderManageService.getSearchList(search,type,chaId);
		JSONArray json = JSONArray.fromObject(list);
		return ajax(json.toString(), "text/html");
	}
	
}
