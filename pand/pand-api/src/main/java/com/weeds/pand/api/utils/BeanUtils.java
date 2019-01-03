package com.weeds.pand.api.utils;

import java.util.Map;

import com.weeds.pand.utils.PandStringUtils;

/**
 * Created by Administrator on 2016/10/14.
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    public static void mapToBean(Map<String, Object> map, Object object) throws Exception {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            key = PandStringUtils.underlineToCamel(key);
            org.apache.commons.beanutils.BeanUtils.copyProperty(object, key, entry.getValue());
        }
    }
}
