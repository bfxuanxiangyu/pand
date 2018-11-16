package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat; 
/**
 * pand_user_comment 实体类
 * 由GenEntityMysql类自动生成
 * Thu Nov 15 12:31:50 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_user_comment")
public class PandUserComment {

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

	/**
	* 技术熟练分数
	*/ 
	@Column(name="skilled_score")
	private Double skilledScore;

	/**
	* 服务态度分数
	*/ 
	@Column(name="attitude_score")
	private Double attitudeScore;

	/**
	* 工作效率分数
	*/ 
	@Column(name="efficiency_score")
	private Double efficiencyScore;

	/**
	* 是否匿名  0是  1否
	*/ 
	@Column(name="anonymous")
	private Integer anonymous;

	/**
	* 评论内容
	*/ 
	@Column(name="comment")
	private String comment;


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

	public Double getSkilledScore(){
		return skilledScore;
	}

	public void setSkilledScore(Double skilledScore){
		this.skilledScore=skilledScore;
	}

	public Double getAttitudeScore(){
		return attitudeScore;
	}

	public void setAttitudeScore(Double attitudeScore){
		this.attitudeScore=attitudeScore;
	}

	public Double getEfficiencyScore(){
		return efficiencyScore;
	}

	public void setEfficiencyScore(Double efficiencyScore){
		this.efficiencyScore=efficiencyScore;
	}

	public Integer getAnonymous(){
		return anonymous;
	}

	public void setAnonymous(Integer anonymous){
		this.anonymous=anonymous;
	}

	public String getComment(){
		return comment;
	}

	public void setComment(String comment){
		this.comment=comment;
	}

	@Override
	public String toString() {
		return "PandUserComment [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", pandUserId=" + pandUserId + ", serviceId=" + serviceId + ", status=" + status + ", skilledScore="
				+ skilledScore + ", attitudeScore=" + attitudeScore + ", efficiencyScore=" + efficiencyScore
				+ ", anonymous=" + anonymous + ", comment=" + comment + "]";
	}
	
}
