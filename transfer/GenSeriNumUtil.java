package com.dSheng.asset.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Random;

public class GenSeriNumUtil {

	/**
	 * 流水号共用拼接符
	 */
	private static String comStr = "";
	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
    public static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
    public static final String NUMBERCHAR = "0123456789";  
  
    /** 
     * 返回一个定长的随机字符串(只包含大小写字母、数字) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 返回一个定长的随机纯数字字符串(只包含数字) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateNumString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(NUMBERCHAR.length())));  
        }  
        return sb.toString();  
    } 
    
    /** 
     * 返回一个定长的随机纯字母字符串(只包含大小写字母) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateMixString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(LETTERCHAR.length())));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateLowerString(int length) {  
        return generateMixString(length).toLowerCase();  
    }  
  
    /** 
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateUpperString(int length) {  
        return generateMixString(length).toUpperCase();  
    }  
  
    /** 
     * 生成一个定长的纯0字符串 
     *  
     * @param length 
     *            字符串长度 
     * @return 纯0字符串 
     */  
    public static String generateZeroString(int length) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            sb.append('0');  
        }  
        return sb.toString();  
    }  
  
    /** 
     * 根据数字生成一个定长的字符串，长度不够前面补0 
     *  
     * @param num 
     *            数字 
     * @param fixdlenth 
     *            字符串长度 
     * @return 定长的字符串 
     */  
    public static String toFixdLengthString(long num, int fixdlenth) {  
        StringBuffer sb = new StringBuffer();  
        String strNum = String.valueOf(num);  
        if (fixdlenth - strNum.length() >= 0) {  
            sb.append(generateZeroString(fixdlenth - strNum.length()));  
        } else {  
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth  
                    + "的字符串发生异常！");  
        }  
        sb.append(strNum);  
        return sb.toString();  
    }  
  
    /** 
     * 每次生成的len位数都不相同 
     *  
     * @param param 
     * @return 定长的数字 
     */  
    public static int getNotSimple(int[] param, int len) {  
        Random rand = new Random();  
        for (int i = param.length; i > 1; i--) {  
            int index = rand.nextInt(i);  
            int tmp = param[index];  
            param[index] = param[i - 1];  
            param[i - 1] = tmp;  
        }  
        int result = 0;  
        for (int i = 0; i < len; i++) {  
            result = result * 10 + param[i];  
        }  
        return result;  
    }   
    
    
    /**
     * 根据前缀生成流水号，组成：前缀+年月日时分秒+随机数
     * @param seriType
     * @param randLen
     * @return
     * @author: duyubo  
     * @version 1.0.0
     * @createTime: 2016年8月31日 下午9:41:07
     */
    public static String genSeriNumByPerfixStr(String seriType,int randLen){
		StringBuffer seriNumBuf = new StringBuffer(seriType);
        String dateTimeStr = DateFormatUtils.format(new Date(), "yyMMddHHmmss");
		//生成随机数
		String random4Str = generateNumString(randLen);
		return seriNumBuf.append(dateTimeStr).append(random4Str).toString();
	}
    
	
	/**
	 * 生成资产流水号
	 * @fileName: OrderGenerateUtil.java
	 * @param merchantId
	 * @return
	 * @author: duyubo  
	 * @version 1.0.0
	 * @createTime: 2016年6月3日 下午1:04:23
	 */
	public static String genAssetSeriNum(String merchantId){
		StringBuffer perfixStrbuf = new StringBuffer(comStr);
		merchantId = StringUtils.isBlank(merchantId)?"":merchantId;
		String perfixStr = perfixStrbuf.append("11").append(merchantId).toString();
		return genSeriNumByPerfixStr(perfixStr, 4);
	}
	
	/**
	 * 生成订单流水号
	 * @fileName: RandomUtil.java
	 * @return
	 * @author: duyubo  
	 * @version 1.0.0
	 * @createTime: 2016年6月3日 下午3:35:31
	 */
	public static String genAstOrderSeriNum(){
		return genSeriNumByPerfixStrMS("21", 3);
	}
	
	/**
	 * 生成资金计划流水号
	 * @return
	 */
	public static String genCapitalPlanSeriNum(){
		return genSeriNumByPerfixStr("PB", 4);
	}
	
	/**
     * 根据前缀生成流水号，组成：前缀+年月日时分秒毫秒+随机数
     * @fileName: RandomUtil.java
     * @param seriType
     * @param randLen
     * @return
     * @author: duyubo  
     * @version 1.0.0
     * @createTime: 2016年8月31日 下午9:41:16
     */
    public static String genSeriNumByPerfixStrMS(String seriType,int randLen){
        StringBuffer seriNumBuf = new StringBuffer(seriType);
        //生成时间字符串
        String dateTimeStr = DateFormatUtils.format(new Date(), "yyMMddHHmmssSSS");
        //生成随机数
        String random4Str = RandomStringUtils.randomNumeric(randLen);
        return seriNumBuf.append(dateTimeStr).append(random4Str).toString();
    }
    

	
	public static void main(String[] args) {
		for(int i=0;i<2;i++){
			System.out.println("【资产流水号】"+genAssetSeriNum("1002")+" , i:"+i);
			System.err.println("【资金计划流水号】"+genCapitalPlanSeriNum()+" , i:"+i);
			System.err.println("【生成订单流水号】"+genAstOrderSeriNum()+" , i:"+i);
		}
	}
	
}
