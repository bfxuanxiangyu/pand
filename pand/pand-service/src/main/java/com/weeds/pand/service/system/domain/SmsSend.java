package com.weeds.pand.service.system.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * sms_send 实体类
 * 由GenEntityMysql类自动生成
 * Mon Jan 21 18:46:33 CST 2019
 * @xuanxy
 */ 
@Entity
@Table(name="sms_send")
public class SmsSend {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@Column(name="create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime;

	/**
	* 发送人手机
	*/ 
	@Column(name="phone")
	private String phone;

	/**
	* 发送内容
	*/ 
	@Column(name="content")
	private String content;

	/**
	* 发送类型  1验证码  2找回密码  3下单通知  4系统通知
	*/ 
	@Column(name="type")
	private Integer type;


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

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone=phone;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content=content;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type=type;
	}

}

