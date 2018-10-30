package com.weeds.pand.service.pandcore.mapper;


import org.springframework.data.repository.CrudRepository; 


import com.weeds.pand.service.pandcore.domain.PandAuditLog; 
/**
 * jpa扫描接口
 * 由GenEntityMysql类自动生成
 * Tue Oct 30 15:53:07 CST 2018
 * @xuanxy
 */ 
public interface PandAuditLogJpaDao extends CrudRepository<PandAuditLog,String>{

}

