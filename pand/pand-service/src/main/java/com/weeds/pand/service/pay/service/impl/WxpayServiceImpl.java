package com.weeds.pand.service.pay.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.weeds.pand.service.pay.domain.Wxpay;
import com.weeds.pand.service.pay.mapper.WxpayJpaDao;
import com.weeds.pand.service.pay.mapper.WxpayMapper;
import com.weeds.pand.service.pay.service.WxpayService;
import com.weeds.pand.utils.PandDateUtils;
import com.weeds.pand.utils.PandStringUtils;

@Service
public class WxpayServiceImpl implements WxpayService{
	
	
	@Resource
	private WxpayJpaDao wxpayJpaDao;
	@Resource
    private WxpayMapper wxpayMapper;
	
	 /**
     * 保存
     * @param question
     * @return
     */
    public String saveOrUpdateWxpay(Wxpay wxpay){
    	String id = wxpay.getId();
    	if(PandStringUtils.isBlank(id)){
    		wxpay.setCreateTime(new Date());
    		wxpay.setUpdateTime(new Date());
    	}else{
    		wxpay.setUpdateTime(new Date());
    	}
    	wxpay = wxpayJpaDao.save(wxpay);
    	return wxpay.getId();
    }
    
    /**
     * 获取单条
     * @param question
     * @return
     */
    public Wxpay getWxpayObject(String id){
    	return wxpayJpaDao.findOne(id);
    }
    /**
     * 获取单条
     * @param question
     * @return
     */
    public Wxpay getWxpayObject(Map<String, Object> parameters){
    	return wxpayMapper.getWxpayObject(parameters);
    }
    
    /**
     * 生成订单号
     *
     * @return
     */
    public synchronized String generateOutTradeNo() {
    	Map<String, Object> parameters = Maps.newHashMap();
    	String prefixOrderNo = PandDateUtils.dateToStr(new Date(), "yyyyMMdd");
    	parameters.put("prefixOrderNo", prefixOrderNo+"%");
        String maxOutTradeNo = wxpayMapper.getMaxOutTradeNo(parameters);
        if (maxOutTradeNo == null) {
            return prefixOrderNo + "0001";
        }
        return (new Long(maxOutTradeNo) + 1) + "";
    }

}
