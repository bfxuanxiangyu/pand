package com.weeds.pand.service.system.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore; 
/**
 * skills 实体类
 * 由GenEntityMysql类自动生成
 * Fri Oct 12 12:13:09 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="skills")
public class Skills {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	@JsonIgnore
	@Column(name="create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime;

	@JsonIgnore
	@Column(name="update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date updateTime;

	/**
	* 是否可用 0不可用 1可用 默认1
	*/ 
	@JsonIgnore
	@Column(name="status")
	private Integer status;

	/**
	* 图标路径
	*/ 
	@Column(name="icon_url")
	private String iconUrl;

	/**
	* 技能标题
	*/ 
	@Column(name="title")
	private String title;

	/**
	* 技能排序
	*/ 
	@Column(name="icon_order")
	private Integer iconOrder;

	/**
	* 技能描述
	*/ 
	@Column(name="content")
	private String content;


	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id=id;
	}
	@JsonIgnore
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	@JsonIgnore
	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	@JsonIgnore
	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public String getIconUrl(){
		return iconUrl;
	}

	public void setIconUrl(String iconUrl){
		this.iconUrl=iconUrl;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public Integer getIconOrder(){
		return iconOrder;
	}

	public void setIconOrder(Integer iconOrder){
		this.iconOrder=iconOrder;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content=content;
	}

}

