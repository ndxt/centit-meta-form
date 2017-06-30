package com.centit.metaform.po;

import com.alibaba.fastjson.JSONObject;
import com.centit.support.algorithm.StringBaseOpt;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Collection;
import java.util.Date;
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

    public static Element createXMLElementFromJSONMap(String elementName , Map<String,Object> jsonMap){
        Element element = DocumentHelper.createElement(elementName);
        for(Map.Entry<String,Object> jo : jsonMap.entrySet()){
            if(jo.getValue()!=null)
                element.add(createXMLElementFromObject(jo.getKey(), jo.getValue()));
        }
        return element;
    }

    public static Element createXMLElementFromMap(String elementName , Map<Object,Object> jsonMap){
        Element element = DocumentHelper.createElement(elementName);
        for(Map.Entry<Object,Object> jo : jsonMap.entrySet()){
            if(jo.getValue()!=null)
                element.add(createXMLElementFromObject(
                        StringBaseOpt.objectToString(jo.getKey()), jo.getValue()));
        }
        return element;
    }

    public static Element createXMLElementFromObject(String elementName , Object object){

        if(object instanceof String){
            return createXMLElement(elementName ,"String", object);
        }

        if(object instanceof Number){
            return createXMLElement(elementName ,"Number", object);
        }

        if(object instanceof Date){
            return createXMLElement(elementName ,"Date", object);
        }

        if(object instanceof Map){
            return createXMLElementFromMap(elementName , (Map<Object,Object>) object);
        }
        if(object instanceof Object[]){
            Element element = DocumentHelper.createElement(elementName);
            for(Object obj: (Object[]) object){
                if(obj!=null) {
                    element.add(createXMLElementFromObject("item", obj));
                }
            }
        }else if(object instanceof Collection){
            Element element = DocumentHelper.createElement(elementName);
            for(Object obj: (Collection<?>) object){
                if(obj!=null) {
                    element.add(createXMLElementFromObject("item", obj));
                }
            }
        }
        return createXMLElement(elementName ,"String", StringBaseOpt.objectToString(object));
    }

    public static Document createXMLObjectFromJSONObject(JSONObject json){
        Element element = createXMLElementFromJSONMap("object",json);
        return DocumentHelper.createDocument(element);
    }
}
