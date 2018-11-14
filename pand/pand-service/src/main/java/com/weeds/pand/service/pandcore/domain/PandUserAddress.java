package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * pand_user_address 实体类
 * 由GenEntityMysql类自动生成
 * Wed Nov 14 12:48:47 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_user_address")
public class PandUserAddress {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	/**
	* 创建时间
	*/ 
	@Column(name="create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime;

	/**
	* 更新日期
	*/ 
	@Column(name="update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;

	/**
	* 用户id
	*/ 
	@Column(name="pand_user_id")
	private String pandUserId;

	/**
	* 是否可用 0可用 1不可用  默认0
	*/ 
	@Column(name="status")
	private Integer status;

	/**
	* 联系人姓名
	*/ 
	@Column(name="person_name")
	private String personName;

	/**
	* 联系方式
	*/ 
	@Column(name="person_phone")
	private String personPhone;

	/**
	* 服务地址
	*/ 
	@Column(name="person_address")
	private String personAddress;

	/**
	* 服务地址纬度
	*/ 
	@Column(name="address_lat")
	private String addressLat;

	/**
	* 服务地址经度
	*/ 
	@Column(name="address_lng")
	private String addressLng;

	/**
	* 门牌号
	*/ 
	@Column(name="house_number")
	private String houseNumber;


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

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public String getPersonName(){
		return personName;
	}

	public void setPersonName(String personName){
		this.personName=personName;
	}

	public String getPersonPhone(){
		return personPhone;
	}

	public void setPersonPhone(String personPhone){
		this.personPhone=personPhone;
	}

	public String getPersonAddress(){
		return personAddress;
	}

	public void setPersonAddress(String personAddress){
		this.personAddress=personAddress;
	}

	public String getAddressLat(){
		return addressLat;
	}

	public void setAddressLat(String addressLat){
		this.addressLat=addressLat;
	}

	public String getAddressLng(){
		return addressLng;
	}

	public void setAddressLng(String addressLng){
		this.addressLng=addressLng;
	}

	public String getHouseNumber(){
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber){
		this.houseNumber=houseNumber;
	}

	@Override
	public String toString() {
		return "PandUserAddress [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", pandUserId=" + pandUserId + ", status=" + status + ", personName=" + personName + ", personPhone="
				+ personPhone + ", personAddress=" + personAddress + ", addressLat=" + addressLat + ", addressLng="
				+ addressLng + ", houseNumber=" + houseNumber + "]";
	}

}

