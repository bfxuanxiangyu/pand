package com.weeds.pand.service.pay.mapper;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weeds.pand.service.pay.domain.Wxpay;

/**
 * Created by admin on 2016/6/1.
 */
@Repository
public interface WxpayJpaDao extends CrudRepository<Wxpay, String> {
}
