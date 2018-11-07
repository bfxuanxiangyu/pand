package com.weeds.pand.service.mechanic.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weeds.pand.service.system.domain.PandImages; 
/**
 * pand_user 实体类
 * 由GenEntityMysql类自动生成
 * Fri Oct 19 12:34:00 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_user")
public class PandUser {

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
	* 用户类型 1普通用户 2商家用户 默认1
	*/ 
	@Column(name="user_type")
	private Integer userType;

	/**
	* 用户名
	*/ 
	@Column(name="user_name")
	private String userName;

	/**
	* 用户密码
	*/ 
	@Column(name="user_password")
	private String userPassword;

	/**
	* 手机号
	*/ 
	@Column(name="user_phone")
	private String userPhone;

	/**
	 * 用户昵称
	 */ 
	@Column(name="user_nickname")
	private String userNickname;
	
	/**
	* 用户昵称
	*/ 
	@Column(name="user_realname")
	private String userRealname;

	/**
	* 用户签名
	*/ 
	@Column(name="user_signature")
	private String userSignature;

	/**
	* 绑定微信
	*/ 
	@Column(name="user_weixin")
	private String userWeixin;

	/**
	* 绑定QQ
	*/ 
	@Column(name="user_qq")
	private String userQq;

	/**
	* 绑定邮箱
	*/ 
	@Column(name="user_email")
	private String userEmail;

	/**
	* 证件类型 1身份证 2护照  3港澳台通行证 4驾照
	*/ 
	@Column(name="user_cardtype")
	private Integer userCardtype;

	/**
	* 证件号码
	*/ 
	@Column(name="user_cardcode")
	private String userCardcode;

	/**
	* 性别 0男  1女  默认0
	*/ 
	@Column(name="user_sex")
	private Integer userSex;

	/**
	* 所在省份
	*/ 
	@Column(name="user_provinces")
	private String userProvinces;

	/**
	* 所在城市
	*/ 
	@Column(name="user_city")
	private String userCity;

	/**
	* 所在区域
	*/ 
	@Column(name="user_area")
	private String userArea;

	/**
	* 详细地址
	*/ 
	@Column(name="user_address")
	private String userAddress;

	/**
	* 头像链接
	*/ 
	@Column(name="user_headpng")
	private String userHeadpng;

	/**
	 * 用户状态  0可用 1商家待审核 2商家审核通过 3商家审核不通过 4封号
	 */ 
	@Column(name="user_status")
	private Integer userStatus;
	/**
	 * 服务所在纬度
	 */ 
	@Column(name="user_lat")
	private String userLat;
	/**
	* 服务所在经度
	*/ 
	@Column(name="user_lng")
	private String userLng;

	@Transient
	private List<PandImages> shoperImages;

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

	public Integer getUserType(){
		return userType;
	}

	public void setUserType(Integer userType){
		this.userType=userType;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName=userName;
	}

	public String getUserPassword(){
		return userPassword;
	}

	public void setUserPassword(String userPassword){
		this.userPassword=userPassword;
	}

	public String getUserPhone(){
		return userPhone;
	}

	public void setUserPhone(String userPhone){
		this.userPhone=userPhone;
	}

	public String getUserNickname(){
		return userNickname;
	}

	public void setUserNickname(String userNickname){
		this.userNickname=userNickname;
	}

	public String getUserSignature(){
		return userSignature;
	}

	public void setUserSignature(String userSignature){
		this.userSignature=userSignature;
	}

	public String getUserWeixin(){
		return userWeixin;
	}

	public void setUserWeixin(String userWeixin){
		this.userWeixin=userWeixin;
	}

	public String getUserQq(){
		return userQq;
	}

	public void setUserQq(String userQq){
		this.userQq=userQq;
	}

	public String getUserEmail(){
		return userEmail;
	}

	public void setUserEmail(String userEmail){
		this.userEmail=userEmail;
	}

	public Integer getUserCardtype(){
		return userCardtype;
	}

	public void setUserCardtype(Integer userCardtype){
		this.userCardtype=userCardtype;
	}

	public String getUserCardcode(){
		return userCardcode;
	}

	public void setUserCardcode(String userCardcode){
		this.userCardcode=userCardcode;
	}

	public Integer getUserSex(){
		return userSex;
	}

	public void setUserSex(Integer userSex){
		this.userSex=userSex;
	}

	public String getUserProvinces(){
		return userProvinces;
	}

	public void setUserProvinces(String userProvinces){
		this.userProvinces=userProvinces;
	}

	public String getUserCity(){
		return userCity;
	}

	public void setUserCity(String userCity){
		this.userCity=userCity;
	}

	public String getUserArea(){
		return userArea;
	}

	public void setUserArea(String userArea){
		this.userArea=userArea;
	}

	public String getUserAddress(){
		return userAddress;
	}

	public void setUserAddress(String userAddress){
		this.userAddress=userAddress;
	}

	public String getUserHeadpng(){
		return userHeadpng;
	}

	public void setUserHeadpng(String userHeadpng){
		this.userHeadpng=userHeadpng;
	}

	public Integer getUserStatus(){
		return userStatus;
	}

	public void setUserStatus(Integer userStatus){
		this.userStatus=userStatus;
	}

	public String getUserRealname() {
		return userRealname;
	}

	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}

	public List<PandImages> getShoperImages() {
		return shoperImages;
	}

	public void setShoperImages(List<PandImages> shoperImages) {
		this.shoperImages = shoperImages;
	}

	public String getUserLat() {
		return userLat;
	}

	public void setUserLat(String userLat) {
		this.userLat = userLat;
	}

	public String getUserLng() {
		return userLng;
	}

	public void setUserLng(String userLng) {
		this.userLng = userLng;
	}

	@Override
	public String toString() {
		return "PandUser [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", userType="
				+ userType + ", userName=" + userName + ", userPassword=" + userPassword + ", userPhone=" + userPhone
				+ ", userNickname=" + userNickname + ", userRealname=" + userRealname + ", userSignature="
				+ userSignature + ", userWeixin=" + userWeixin + ", userQq=" + userQq + ", userEmail=" + userEmail
				+ ", userCardtype=" + userCardtype + ", userCardcode=" + userCardcode + ", userSex=" + userSex
				+ ", userProvinces=" + userProvinces + ", userCity=" + userCity + ", userArea=" + userArea
				+ ", userAddress=" + userAddress + ", userHeadpng=" + userHeadpng + ", userStatus=" + userStatus
				+ ", userLat=" + userLat + ", userLng=" + userLng + "]";
	}

}

