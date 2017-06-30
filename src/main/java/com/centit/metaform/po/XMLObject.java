package com.centit.metaform.po;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Map;

/**
 * Created by codefan on 17-6-30.
 */
public class XMLObject {

    public static Element createXMLElement(String elementName , String valueType, Object value){
        Element element = DocumentHelper.createElement(elementName);
        element.addAttribute("type",valueType);
        element.setData(value);
        return element;
    }

    public static Element createXMLElementFromMap(String elementName , Map<String,Object> jsonMap){
        Element element = DocumentHelper.createElement(elementName);

        //element.add(element);
        return element;
    }

    public static Element createXMLElementFromObject(String elementName , Object object){
        Element element = DocumentHelper.createElement(elementName);

        //element.add(element);
        return element;
    }

    public static Document createXMLObjectFromJSONObject(JSONObject json){
        Element element = createXMLElementFromMap("object",json);
        return DocumentHelper.createDocument(element);
    }
}
