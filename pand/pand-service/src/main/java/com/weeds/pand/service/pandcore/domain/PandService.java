package com.weeds.pand.service.pandcore.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weeds.pand.service.mechanic.domain.PandUserJson;
import com.weeds.pand.service.system.domain.PandImages;
import com.weeds.pand.utils.jsonutil.CustomDoubleOneSerialize; 
/**
 * pand_service 实体类
 * 服务表
 * 由GenEntityMysql类自动生成
 * Mon Oct 22 16:04:37 CST 2018
 * @xuanxy
 */ 
@Entity
@Table(name="pand_service")
public class PandService {

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
	* 服务类型id
	*/ 
	@Column(name="service_type_id")
	private String serviceTypeId;

	/**
	* 服务标题
	*/ 
	@Column(name="service_tilte")
	private String serviceTilte;

	/**
	* 服务描述
	*/ 
	@Column(name="service_des")
	private String serviceDes;

	/**
	* 服务须知
	*/ 
	@Column(name="service_info")
	private String serviceInfo;

	/**
	* 服务城市
	*/ 
	@Column(name="service_city")
	private String serviceCity;

	/**
	* 服务区域
	*/ 
	@Column(name="service_area")
	private String serviceArea;

	/**
	* 服务价格
	*/ 
	@Column(name="service_price")
	private String servicePrice;
	
	/**
	 * 服务范围
	 */ 
	@Column(name="service_scope")
	private String serviceScope;

	/**
	 * 服务地址
	 */ 
	@Column(name="service_address")
	private String serviceAddress;
	/**
	 * 服务地址纬度
	 */ 
	@Column(name="service_lat")
	private String serviceLat;
	/**
	* 服务地址经度
	*/ 
	@Column(name="service_lng")
	private String serviceLng;

	/**
	* 是否开具发票 0不开具   1开具   默认0
	*/ 
	@Column(name="service_invoice")
	private Integer serviceInvoice;

	/**
	* 服务状态 1发布  2发布审核通过  3审核不通过 4下架 默认1
	*/ 
	@Column(name="service_status")
	private Integer serviceStatus;
	/**
	* 二维码分析地址
	*/ 
	@Column(name="qr_url")
	private String qrUrl;
	
	@Transient
	private List<PandImages> images;//服务图片集合
	
	@Transient
	private Double serviceDistance;//服务距离
	
	@Transient
	private Double serviceStar;//星级评分
	
	@Transient
	private int serviceTotal;//售出总个数
	
	@Transient
	private int serviceTimeLength;//到达时长
	@Transient
	private PandUserJson issuser;

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

	public String getServiceTypeId(){
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId){
		this.serviceTypeId=serviceTypeId;
	}

	public String getServiceTilte(){
		return serviceTilte;
	}

	public void setServiceTilte(String serviceTilte){
		this.serviceTilte=serviceTilte;
	}

	public String getServiceDes(){
		return serviceDes;
	}

	public void setServiceDes(String serviceDes){
		this.serviceDes=serviceDes;
	}

	public String getServiceInfo(){
		return serviceInfo;
	}

	public void setServiceInfo(String serviceInfo){
		this.serviceInfo=serviceInfo;
	}

	public String getServiceCity(){
		return serviceCity;
	}

	public void setServiceCity(String serviceCity){
		this.serviceCity=serviceCity;
	}

	public String getServiceArea(){
		return serviceArea;
	}

	public void setServiceArea(String serviceArea){
		this.serviceArea=serviceArea;
	}

	public String getServicePrice(){
		return servicePrice;
	}

	public void setServicePrice(String servicePrice){
		this.servicePrice=servicePrice;
	}

	public String getServiceScope(){
		return serviceScope;
	}

	public void setServiceScope(String serviceScope){
		this.serviceScope=serviceScope;
	}

	public Integer getServiceInvoice(){
		return serviceInvoice;
	}

	public void setServiceInvoice(Integer serviceInvoice){
		this.serviceInvoice=serviceInvoice;
	}

	public Integer getServiceStatus(){
		return serviceStatus;
	}

	public void setServiceStatus(Integer serviceStatus){
		this.serviceStatus=serviceStatus;
	}

	public List<PandImages> getImages() {
		return images;
	}

	public void setImages(List<PandImages> images) {
		this.images = images;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getServiceLat() {
		return serviceLat;
	}

	public void setServiceLat(String serviceLat) {
		this.serviceLat = serviceLat;
	}

	public String getServiceLng() {
		return serviceLng;
	}

	public void setServiceLng(String serviceLng) {
		this.serviceLng = serviceLng;
	}

	@Override
	public String toString() {
		return "PandService [id=" + id + ", createTime=" + createTime + ", updateTime=" + updateTime + ", pandUserId="
				+ pandUserId + ", serviceTypeId=" + serviceTypeId + ", serviceTilte=" + serviceTilte + ", serviceDes="
				+ serviceDes + ", serviceInfo=" + serviceInfo + ", serviceCity=" + serviceCity + ", serviceArea="
				+ serviceArea + ", servicePrice=" + servicePrice + ", serviceScope=" + serviceScope
				+ ", serviceAddress=" + serviceAddress + ", serviceLat=" + serviceLat + ", serviceLng=" + serviceLng
				+ ", serviceInvoice=" + serviceInvoice + ", serviceStatus=" + serviceStatus + "]";
	}
	@JsonSerialize(using = CustomDoubleOneSerialize.class)
	public Double getServiceDistance() {
		if(serviceDistance==null){
			serviceDistance=1.00;
		}
		return serviceDistance;
	}

	public void setServiceDistance(Double serviceDistance) {
		this.serviceDistance = serviceDistance;
	}
	@JsonSerialize(using = CustomDoubleOneSerialize.class)
	public Double getServiceStar() {
		if(serviceStar==null){
			serviceStar=1.00;
		}
		return serviceStar;
	}

	public void setServiceStar(Double serviceStar) {
		this.serviceStar = serviceStar;
	}

	public int getServiceTotal() {
		return serviceTotal;
	}

	public void setServiceTotal(int serviceTotal) {
		this.serviceTotal = serviceTotal;
	}

	public int getServiceTimeLength() {
		return serviceTimeLength;
	}

	public void setServiceTimeLength(int serviceTimeLength) {
		this.serviceTimeLength = serviceTimeLength;
	}

	public PandUserJson getIssuser() {
		return issuser;
	}

	public void setIssuser(PandUserJson issuser) {
		this.issuser = issuser;
	}

	public String getQrUrl() {
		return qrUrl;
	}

	public void setQrUrl(String qrUrl) {
		this.qrUrl = qrUrl;
	}

}

