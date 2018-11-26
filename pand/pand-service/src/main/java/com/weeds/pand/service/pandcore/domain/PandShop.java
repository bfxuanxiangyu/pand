package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weeds.pand.service.system.domain.Skills; 
/**
 * pand_shop 实体类
 * 由GenEntityMysql类自动生成
 * Tue Oct 23 12:35:31 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_shop")
public class PandShop {

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
	* 联系方式
	*/ 
	@Column(name="shop_tel")
	private String shopTel;

	/**
	* 店铺名称
	*/ 
	@Column(name="shop_name")
	private String shopName;

	/**
	* 店铺简介
	*/ 
	@Column(name="shop_des")
	private String shopDes;

	/**
	* 店铺营业时间
	*/ 
	@Column(name="shop_time")
	private String shopTime;

	/**
	* 店铺形象图
	*/ 
	@Column(name="shop_img")
	private String shopImg;

	/**
	* 店铺状态 0可用  1不可用  默认0
	*/ 
	@Column(name="shop_status")
	private Integer shopStatus;
	
	@Transient
	private int orderTotal;//接单数量
	@Transient
	private String totalScore;//综合评分
	@Transient
	private List<Skills> skills;//服务技能列表

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

	public String getShopTel(){
		return shopTel;
	}

	public void setShopTel(String shopTel){
		this.shopTel=shopTel;
	}

	public String getShopName(){
		return shopName;
	}

	public void setShopName(String shopName){
		this.shopName=shopName;
	}

	public String getShopDes(){
		return shopDes;
	}

	public void setShopDes(String shopDes){
		this.shopDes=shopDes;
	}

	public String getShopTime(){
		return shopTime;
	}

	public void setShopTime(String shopTime){
		this.shopTime=shopTime;
	}

	public String getShopImg(){
		return shopImg;
	}

	public void setShopImg(String shopImg){
		this.shopImg=shopImg;
	}

	public Integer getShopStatus(){
		return shopStatus;
	}

	public void setShopStatus(Integer shopStatus){
		this.shopStatus=shopStatus;
	}

	@Override
	public String toString() {
		return "PandShop [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", pandUserId="
				+ pandUserId + ", shopTel=" + shopTel + ", shopName=" + shopName + ", shopDes=" + shopDes
				+ ", shopTime=" + shopTime + ", shopImg=" + shopImg + ", shopStatus=" + shopStatus + "]";
	}

	public int getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(int orderTotal) {
		this.orderTotal = orderTotal;
	}

	public List<Skills> getSkills() {
		return skills;
	}

	public void setSkills(List<Skills> skills) {
		this.skills = skills;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

}

