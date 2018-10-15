package com.weeds.pand.service.system.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore; 
/**
 * banner 实体类
 * 由GenEntityMysql类自动生成
 * Fri Oct 12 11:04:17 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="banner")
public class Banner {

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
	* 是否可用 0不可用  1可用   默认1
	*/ 
	@Column(name="status")
	private Integer status;

	/**
	* 图片访问地址
	*/ 
	@Column(name="img_url")
	private String imgUrl;

	/**
	* 排序
	*/ 
	@Column(name="img_order")
	private Integer imgOrder;


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
	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}

	public Integer getImgOrder(){
		return imgOrder;
	}

	public void setImgOrder(Integer imgOrder){
		this.imgOrder=imgOrder;
	}

}

