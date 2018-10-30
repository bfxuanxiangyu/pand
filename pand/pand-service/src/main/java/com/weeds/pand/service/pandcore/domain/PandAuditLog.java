package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import java.sql.*;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * pand_audit_log 实体类
 * 服务表
 * 由GenEntityMysql类自动生成
 * Tue Oct 30 15:53:07 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_audit_log")
public class PandAuditLog {

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
	* 商家用户id
	*/ 
	@Column(name="pand_user_id")
	private String pandUserId;

	/**
	* 原因
	*/ 
	@Column(name="content")
	private String content;

	/**
	* 审核状态 0审核通过  1不通过
	*/ 
	@Column(name="audit_type")
	private Integer auditType;


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

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content=content;
	}

	public Integer getAuditType(){
		return auditType;
	}

	public void setAuditType(Integer auditType){
		this.auditType=auditType;
	}

}

