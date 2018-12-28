package com.everwing.server.payment.wxminiprogram.common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zgrf
 * @Description:  xml处理相关
 * @Date: Create in 2018-08-16 15:05
 **/

public class XMLUtil {


     /**
      * Body的XML解析为Map
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseRequestXmlToMap(HttpServletRequest request) throws Exception {
        // 解析结果存储在HashMap中
        Map<String, String> resultMap;
        InputStream inputStream = request.getInputStream();
//        resultMap = parseInputStreamToMap(inputStream);
        resultMap = parseRequestXmlToMapNew(inputStream);
        return resultMap;
    }

    public static Map<String,String> parseRequestXmlToMapNew(InputStream inputStream) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            org.w3c.dom.Document doc = documentBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                inputStream.close();
            } catch (Exception ex) {

            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 将输入流中的XML解析为Map
     *
     * @param inputStream
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static Map<String, String> parseInputStreamToMap(InputStream inputStream) throws DocumentException, IOException {
        // 解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        //得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();
        //遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        //释放资源
        inputStream.close();
        return map;
    }

    /**
     * 将String类型的XML解析为Map
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXmlStringToMap(String str) throws Exception {
        Map<String, String> resultMap;
        InputStream inputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
        resultMap = parseInputStreamToMap(inputStream);
        return resultMap;
    }
}
