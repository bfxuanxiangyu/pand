package com.weeds.pand.service.pay.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * wxpay 实体类
 * 由GenEntityMysql类自动生成
 * Wed Oct 26 14:27:54 CST 2016
 * @xuanxy
 */ 
@Entity
@Table(name="wxpay")
public class Wxpay {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	/**
	* 引用的外键主键
	*/ 
	@Column(name="registration_id")
	private String registrationId;

	/**
	* 我的价格
	*/ 
	@Column(name="my_total_fee")
	private String myTotalFee;

	/**
	* 微信订单url
	*/ 
	@Column(name="code_url")
	private String codeUrl;

	/**
	* 公众账号ID
	*/ 
	@Column(name="appid")
	private String appid;

	/**
	* 商户号
	*/ 
	@Column(name="mch_id")
	private String mchId;

	/**
	* 设备号
	*/ 
	@Column(name="device_info")
	private String deviceInfo;

	/**
	* 随机字符串
	*/ 
	@Column(name="nonce_str")
	private String nonceStr;

	/**
	* 签名
	*/ 
	@Column(name="sign")
	private String sign;

	/**
	* 返回状态码
	*/ 
	@Column(name="return_code")
	private String returnCode;

	/**
	* 返回信息
	*/ 
	@Column(name="return_msg")
	private String returnMsg;

	/**
	* 业务结果
	*/ 
	@Column(name="result_code")
	private String resultCode;

	/**
	* 错误代码
	*/ 
	@Column(name="err_code")
	private String errCode;

	/**
	* 错误代码描述
	*/ 
	@Column(name="err_code_des")
	private String errCodeDes;

	/**
	* 用户标识
	*/ 
	@Column(name="openid")
	private String openid;

	/**
	* 是否关注公众账号
	*/ 
	@Column(name="is_subscribe")
	private String isSubscribe;

	/**
	* 交易类型
	*/ 
	@Column(name="trade_type")
	private String tradeType;

	/**
	* 付款银行
	*/ 
	@Column(name="bank_type")
	private String bankType;

	/**
	* 订单金额
	*/ 
	@Column(name="total_fee")
	private String totalFee;

	/**
	* 应结订单金额
	*/ 
	@Column(name="settlement_total_fee")
	private String settlementTotalFee;

	/**
	* 货币种类
	*/ 
	@Column(name="fee_type")
	private String feeType;

	/**
	* 现金支付金额
	*/ 
	@Column(name="cash_fee")
	private String cashFee;

	/**
	* 现金支付货币类型
	*/ 
	@Column(name="cash_fee_type")
	private String cashFeeType;

	/**
	* 代金券金额
	*/ 
	@Column(name="coupon_fee")
	private String couponFee;

	/**
	* 代金券使用数量
	*/ 
	@Column(name="coupon_count")
	private String couponCount;

	/**
	* 代金券类型
	*/ 
	@Column(name="coupon_type_n")
	private String couponTypeN;

	/**
	* 代金券ID
	*/ 
	@Column(name="coupon_id_n")
	private String couponIdN;

	/**
	* 单个代金券支付金额
	*/ 
	@Column(name="coupon_fee_n")
	private String couponFeeN;

	/**
	* 微信支付订单号
	*/ 
	@Column(name="transaction_id")
	private String transactionId;

	/**
	* 商户订单号
	*/ 
	@Column(name="out_trade_no")
	private String outTradeNo;

	/**
	* 商家数据包
	*/ 
	@Column(name="attach")
	private String attach;

	/**
	* 支付完成时间
	*/ 
	@Column(name="time_end")
	private String timeEnd;

	/**
	* 0-正常，1-被删除
	*/ 
	@Column(name="delete_flag")
	private Integer deleteFlag;

	@Column(name="create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime;

	@Column(name="create_by")
	private String createBy;

	@Column(name="update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;

	@Column(name="update_by")
	private String updateBy;


	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id=id;
	}

	public String getRegistrationId(){
		return registrationId;
	}

	public void setRegistrationId(String registrationId){
		this.registrationId=registrationId;
	}

	public String getMyTotalFee(){
		return myTotalFee;
	}

	public void setMyTotalFee(String myTotalFee){
		this.myTotalFee=myTotalFee;
	}

	public String getCodeUrl(){
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl){
		this.codeUrl=codeUrl;
	}

	public String getAppid(){
		return appid;
	}

	public void setAppid(String appid){
		this.appid=appid;
	}

	public String getMchId(){
		return mchId;
	}

	public void setMchId(String mchId){
		this.mchId=mchId;
	}

	public String getDeviceInfo(){
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo){
		this.deviceInfo=deviceInfo;
	}

	public String getNonceStr(){
		return nonceStr;
	}

	public void setNonceStr(String nonceStr){
		this.nonceStr=nonceStr;
	}

	public String getSign(){
		return sign;
	}

	public void setSign(String sign){
		this.sign=sign;
	}

	public String getReturnCode(){
		return returnCode;
	}

	public void setReturnCode(String returnCode){
		this.returnCode=returnCode;
	}

	public String getReturnMsg(){
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg){
		this.returnMsg=returnMsg;
	}

	public String getResultCode(){
		return resultCode;
	}

	public void setResultCode(String resultCode){
		this.resultCode=resultCode;
	}

	public String getErrCode(){
		return errCode;
	}

	public void setErrCode(String errCode){
		this.errCode=errCode;
	}

	public String getErrCodeDes(){
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes){
		this.errCodeDes=errCodeDes;
	}

	public String getOpenid(){
		return openid;
	}

	public void setOpenid(String openid){
		this.openid=openid;
	}

	public String getIsSubscribe(){
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe){
		this.isSubscribe=isSubscribe;
	}

	public String getTradeType(){
		return tradeType;
	}

	public void setTradeType(String tradeType){
		this.tradeType=tradeType;
	}

	public String getBankType(){
		return bankType;
	}

	public void setBankType(String bankType){
		this.bankType=bankType;
	}

	public String getTotalFee(){
		return totalFee;
	}

	public void setTotalFee(String totalFee){
		this.totalFee=totalFee;
	}

	public String getSettlementTotalFee(){
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(String settlementTotalFee){
		this.settlementTotalFee=settlementTotalFee;
	}

	public String getFeeType(){
		return feeType;
	}

	public void setFeeType(String feeType){
		this.feeType=feeType;
	}

	public String getCashFee(){
		return cashFee;
	}

	public void setCashFee(String cashFee){
		this.cashFee=cashFee;
	}

	public String getCashFeeType(){
		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType){
		this.cashFeeType=cashFeeType;
	}

	public String getCouponFee(){
		return couponFee;
	}

	public void setCouponFee(String couponFee){
		this.couponFee=couponFee;
	}

	public String getCouponCount(){
		return couponCount;
	}

	public void setCouponCount(String couponCount){
		this.couponCount=couponCount;
	}

	public String getCouponTypeN(){
		return couponTypeN;
	}

	public void setCouponTypeN(String couponTypeN){
		this.couponTypeN=couponTypeN;
	}

	public String getCouponIdN(){
		return couponIdN;
	}

	public void setCouponIdN(String couponIdN){
		this.couponIdN=couponIdN;
	}

	public String getCouponFeeN(){
		return couponFeeN;
	}

	public void setCouponFeeN(String couponFeeN){
		this.couponFeeN=couponFeeN;
	}

	public String getTransactionId(){
		return transactionId;
	}

	public void setTransactionId(String transactionId){
		this.transactionId=transactionId;
	}

	public String getOutTradeNo(){
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo){
		this.outTradeNo=outTradeNo;
	}

	public String getAttach(){
		return attach;
	}

	public void setAttach(String attach){
		this.attach=attach;
	}

	public String getTimeEnd(){
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd){
		this.timeEnd=timeEnd;
	}

	public Integer getDeleteFlag(){
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag){
		this.deleteFlag=deleteFlag;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public String getCreateBy(){
		return createBy;
	}

	public void setCreateBy(String createBy){
		this.createBy=createBy;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public String getUpdateBy(){
		return updateBy;
	}

	public void setUpdateBy(String updateBy){
		this.updateBy=updateBy;
	}

}

