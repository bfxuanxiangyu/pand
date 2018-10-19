package com.weeds.pand.service.system.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * pand_images 实体类
 * 由GenEntityMysql类自动生成
 * Thu Oct 18 15:03:41 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_images")
public class PandImages {

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
	* 图片类型  0通用型 1身份证正面，2反面  3持证  4港澳台通行证 5护照 
	*/ 
	@Column(name="img_type")
	private Integer imgType;

	/**
	* 所属模块  1用户审核   2评论 3服务图片
	*/ 
	@Column(name="img_model")
	private Integer imgModel;

	/**
	* 对应模块id号
	*/ 
	@Column(name="model_id")
	private String modelId;

	/**
	* 图片是否可用 0可用 1不可用  默认0
	*/ 
	@Column(name="img_status")
	private Integer imgStatus;

	/**
	* 图片访问路径
	*/ 
	@Column(name="img_url")
	private String imgUrl;

	/**
	* base64类型图片
	*/ 
	@Column(name="img_base64")
	private String imgBase64;


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

	public Integer getImgType(){
		return imgType;
	}

	public void setImgType(Integer imgType){
		this.imgType=imgType;
	}

	public Integer getImgModel(){
		return imgModel;
	}

	public void setImgModel(Integer imgModel){
		this.imgModel=imgModel;
	}

	public String getModelId(){
		return modelId;
	}

	public void setModelId(String modelId){
		this.modelId=modelId;
	}

	public Integer getImgStatus(){
		return imgStatus;
	}

	public void setImgStatus(Integer imgStatus){
		this.imgStatus=imgStatus;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}

	public String getImgBase64(){
		return imgBase64;
	}

	public void setImgBase64(String imgBase64){
		this.imgBase64=imgBase64;
	}

}

