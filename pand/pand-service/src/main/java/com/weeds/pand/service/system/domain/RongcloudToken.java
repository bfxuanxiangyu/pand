package com.weeds.pand.service.system.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * rongcloud_token 实体类
 * 由GenEntityMysql类自动生成
 * Tue Jan 22 10:29:32 CST 2019
 * @xuanxy
 */ 
@Entity
@Table(name="rongcloud_token")
public class RongcloudToken {

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
	@Column(name="user_id")
	private String userId;

	/**
	* 用户名
	*/ 
	@Column(name="nick_name")
	private String nickName;

	/**
	* 融云token
	*/ 
	@Column(name="token")
	private String token;

	/**
	* 头像图片
	*/ 
	@Column(name="head_img")
	private String headImg;


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

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId=userId;
	}

	public String getNickName(){
		return nickName;
	}

	public void setNickName(String nickName){
		this.nickName=nickName;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token=token;
	}

	public String getHeadImg(){
		return headImg;
	}

	public void setHeadImg(String headImg){
		this.headImg=headImg;
	}

}

