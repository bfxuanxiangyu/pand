package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * pand_order 实体类
 * pand订单表
 * 由GenEntityMysql类自动生成
 * Tue Nov 13 14:26:25 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_order")
public class PandOrder {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name="create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime;

	@Column(name="update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;

	/**
	* 用户id
	*/ 
	@Column(name="pand_user_id")
	private String pandUserId;
	
	/**
	 * 服务id
	 */ 
	@Column(name="service_id")
	private String serviceId;
	
	/**
	 * 店铺id
	 */ 
	@Column(name="shop_id")
	private String shopId;

	/**
	* 商铺用户id
	*/ 
	@Column(name="pand_shoper_id")
	private String pandShoperId;
	
	/**
	 * 联系人姓名
	 */ 
	@Column(name="contact_name")
	private String contactName;

	/**
	* 联系人手机
	*/ 
	@Column(name="contact_phone")
	private String contactPhone;

	/**
	* 服务地址
	*/ 
	@Column(name="address")
	private String address;

	/**
	* 服务时间段
	*/ 
	@Column(name="time_period")
	private String timePeriod;

	/**
	* 服务数量
	*/ 
	@Column(name="service_count")
	private Integer serviceCount;

	/**
	* 留言
	*/ 
	@Column(name="message")
	private String message;

	/**
	* 订单状态  0下单  1取消订单  2删除  3接单  4订单完成
	*/ 
	@Column(name="status")
	private Integer status;

	/**
	* 订单金额
	*/ 
	@Column(name="service_money")
	private Double serviceMoney;

	/**
	* 订单编号
	*/ 
	@Column(name="order_num")
	private String orderNum;
	
	@Transient
	private String userNickname;
	@Transient
	private String serviceTitle;
	@Transient
	private String shopUserHead;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id=id;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public String getPandUserId(){
		return pandUserId;
	}

	public void setPandUserId(String pandUserId){
		this.pandUserId=pandUserId;
	}

	public String getServiceId(){
		return serviceId;
	}

	public void setServiceId(String serviceId){
		this.serviceId=serviceId;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getTimePeriod(){
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod){
		this.timePeriod=timePeriod;
	}

	public Integer getServiceCount(){
		return serviceCount;
	}

	public void setServiceCount(Integer serviceCount){
		this.serviceCount=serviceCount;
	}

	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
		this.message=message;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Double getServiceMoney(){
		return serviceMoney;
	}

	public void setServiceMoney(Double serviceMoney){
		this.serviceMoney=serviceMoney;
	}

	public String getOrderNum(){
		return orderNum;
	}

	public void setOrderNum(String orderNum){
		this.orderNum=orderNum;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getPandShoperId() {
		return pandShoperId;
	}

	public void setPandShoperId(String pandShoperId) {
		this.pandShoperId = pandShoperId;
	}

	@Override
	public String toString() {
		return "PandOrder [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", pandUserId="
				+ pandUserId + ", serviceId=" + serviceId + ", shopId=" + shopId + ", pandShoperId=" + pandShoperId
				+ ", address=" + address + ", timePeriod=" + timePeriod + ", serviceCount=" + serviceCount
				+ ", message=" + message + ", status=" + status + ", serviceMoney=" + serviceMoney + ", orderNum="
				+ orderNum + "]";
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getServiceTitle() {
		return serviceTitle;
	}

	public void setServiceTitle(String serviceTitle) {
		this.serviceTitle = serviceTitle;
	}

	public String getShopUserHead() {
		return shopUserHead;
	}

	public void setShopUserHead(String shopUserHead) {
		this.shopUserHead = shopUserHead;
	}
	
	
}

