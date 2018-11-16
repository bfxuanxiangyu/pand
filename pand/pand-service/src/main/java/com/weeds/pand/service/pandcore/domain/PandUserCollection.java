package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * pand_user_collection 实体类
 * 由GenEntityMysql类自动生成
 * Fri Nov 16 12:39:33 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_user_collection")
public class PandUserCollection {

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
	* 服务id
	*/ 
	@Column(name="service_id")
	private String serviceId;

	/**
	* 是否可用 0可用 1不可用  默认0
	*/ 
	@Column(name="status")
	private Integer status;


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

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	@Override
	public String toString() {
		return "PandUserCollection [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", pandUserId=" + pandUserId + ", serviceId=" + serviceId + ", status=" + status + "]";
	}

}

