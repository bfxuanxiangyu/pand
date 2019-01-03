package com.weeds.pand.api.wxpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/10/13.
 */
public class WxPayData {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public WxPayData() {

    }

    //采用排序的Dictionary的好处是方便对数据包进行签名，不用再签名之前再做一次排序
    private TreeMap<String, Object> m_values = new TreeMap<String, Object>();

    /**
     * 设置某个字段的值
     *
     * @param key   字段名
     * @param value 字段值
     */
    public void setValue(String key, Object value) {
        m_values.put(key, value);
    }

    /**
     * 根据字段名获取某个字段的值
     *
     * @param key 字段名
     * @return key对应的字段值
     */
    public Object getValue(String key) {
        return m_values.get(key);
    }

    /**
     * 判断某个字段是否已设置
     *
     * @param key 字段名
     * @return 若字段key已被设置，则返回true，否则返回false
     */
    public boolean isSet(String key) {
        Object value = m_values.get(key);
        if (null != value)
            return true;
        else
            return false;
    }

    /**
     * @return 经转换得到的xml串
     * @throws
     * @将Dictionary转成xml
     **/
    public String toXml() {
        //数据为空时不能转化为xml格式
        if (0 == m_values.size()) {
            logger.error("WxPayData数据为空!");
            throw new RuntimeException("WxPayData数据为空!");
        }

        String xml = "<xml>";
        for (Map.Entry<String, Object> pair : m_values.entrySet()) {
            //字段值不能为null，会影响后续流程
            if (pair.getValue() == null) {
                logger.error("WxPayData内部含有值为null的字段!");
                throw new RuntimeException("WxPayData内部含有值为null的字段!");
            }

            if (pair.getValue() instanceof Integer) {
                xml += "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">";
            } else if (pair.getValue() instanceof String) {
                xml += "<" + pair.getKey() + ">" + "<![CDATA[" + pair.getValue() + "]]></" + pair.getKey() + ">";
            } else//除了string和int类型不能含有其他数据类型
            {
                logger.error("WxPayData字段数据类型错误!");
                throw new RuntimeException("WxPayData字段数据类型错误!");
            }
        }
        xml += "</xml>";
        return xml;
    }

    /**
     * @param xml 待转换的xml串
     * @return 经转换得到的Dictionary
     * @throws
     * @将xml转为WxPayData对象并返回对象内部的数据
     */
    public TreeMap<String, Object> fromXml(String xml) {
        if (StringUtils.isBlank(xml)) {
            logger.error("将空的xml串转换为WxPayData不合法!");
            throw new RuntimeException("将空的xml串转换为WxPayData不合法!");
        }
        try {
            Document xmlDoc = DocumentHelper.parseText(xml);
            Element xmlNode = xmlDoc.getRootElement();//获取到根节点<xml>
            List nodes = xmlNode.elements();
            for (Object xn : nodes) {
                Element xe = (Element) xn;
                m_values.put(xe.getName(), xe.getText());//获取xml的键值对到WxPayData内部的数据中
            }

            //2015-06-29 错误是没有签名
            if (!"SUCCESS".equals(m_values.get("return_code"))) {
                return m_values;
            }
            checkSign();//验证签名,不通过会抛异常
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return m_values;
    }

    /**
     * 格式转化成url参数格式
     *
     * @ return url格式串, 该串不包含sign字段值
     */
    public String toUrl() {
        String buff = "";
        for (Map.Entry<String, Object> pair : m_values.entrySet()) {
            if (pair.getValue() == null) {
                logger.error("WxPayData内部含有值为null的字段!");
                throw new RuntimeException("WxPayData内部含有值为null的字段!");
            }
            if (pair.getKey() != "sign" && pair.getValue().toString() != "") {
                buff += pair.getKey() + "=" + pair.getValue() + "&";
            }
        }
        buff = buff.substring(0, buff.length() - 1);
        return buff;
    }


    /**
     * @return json串数据
     * @Dictionary格式化成Json
     */
    public String toJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(m_values);
        return jsonStr;
    }

    /**
     * 格式化成能在Web页面上显示的结果（因为web页面上不能直接输出xml格式的字符串）
     */
    public String toPrintStr() {
        String str = "";
        for (Map.Entry<String, Object> pair : m_values.entrySet()) {
            {
                if (pair.getValue() == null) {
                    logger.error("WxPayData内部含有值为null的字段!");
                    throw new RuntimeException("WxPayData内部含有值为null的字段!");
                }

                str += String.format("{0}={1}<br>", pair.getKey(), pair.getValue().toString());
            }
            logger.debug("Print in Web Page : " + str);
        }
        return str;
    }

    /**
     * @return 签名, sign字段不参加签名
     * @生成签名，详见签名生成算法
     */

    public String MakeSign() {
        //转url格式
        String str = toUrl();
        //在string后加入API KEY
        str += "&key=" + WxPayConfig.KEY;
        //MD5加密
        return DigestUtils.md5Hex(str).toUpperCase();
    }

    /**
     * 检测签名是否正确
     * 正确返回true，错误抛异常
     */
    public boolean checkSign() {
        //如果没有设置签名，则跳过检测
        if (!isSet("sign")) {
            logger.error("WxPayData签名不存在!");
            throw new RuntimeException("WxPayData签名不存在!");
        }
        //如果设置了签名但是签名为空，则抛异常
        else if (getValue("sign") == null || "".equals(getValue("sign").toString())) {
            logger.error("WxPayData签名存在但不合法!");
            throw new RuntimeException("WxPayData签名存在但不合法!");
        }

        //获取接收到的签名
        String return_sign = getValue("sign").toString();

        //在本地计算新的签名
        String cal_sign = MakeSign();

        if (cal_sign.equals(return_sign)) {
            return true;
        }
        logger.error("WxPayData签名验证错误!");
        throw new RuntimeException("WxPayData签名验证错误!");
    }

    /**
     * @获取Dictionary
     */
    public TreeMap<String, Object> getValues() {
        return m_values;
    }
}