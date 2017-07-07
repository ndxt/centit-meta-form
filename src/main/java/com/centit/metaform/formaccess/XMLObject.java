package com.centit.metaform.formaccess;

import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;

import java.util.*;

/**
 * Created by codefan on 17-6-30.
 */
public class XMLObject {

    public static Element createXMLElement(String elementName , String valueType, Object value){
        Element element = DocumentHelper.createElement(elementName);
        element.addAttribute("type",valueType);
        element.setText(StringBaseOpt.objectToString(value));
        return element;
    }

    public static Element createXMLElementFromJSONMap(String elementName , Map<String,Object> jsonMap){
        Element element = DocumentHelper.createElement(elementName);
        element.addAttribute("type","Object");
        for(Map.Entry<String,Object> jo : jsonMap.entrySet()){
            if(jo.getValue()!=null)
                element.add(createXMLElementFromObject(jo.getKey(), jo.getValue()));
        }
        return element;
    }

    public static Element createXMLElementFromMap(String elementName , Map<Object,Object> jsonMap){
        Element element = DocumentHelper.createElement(elementName);
        element.addAttribute("type","Object");
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

        if(object instanceof Long){
            return createXMLElement(elementName ,"Long", object);
        }

        if(object instanceof Integer){
            return createXMLElement(elementName ,"Integer", object);
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
            element.addAttribute("type","Array");
            for(Object obj: (Object[]) object){
                if(obj!=null) {
                    element.add(createXMLElementFromObject("item", obj));
                }
            }
        }else if(object instanceof Collection){
            Element element = DocumentHelper.createElement(elementName);
            element.addAttribute("type","Array");
            for(Object obj: (Collection<?>) object){
                if(obj!=null) {
                    element.add(createXMLElementFromObject("item", obj));
                }
            }
        }
        return createXMLElement(elementName ,"String", StringBaseOpt.objectToString(object));
    }


    public static String jsonObjectToXMLString(Map<String, Object> json){
        Element element = createXMLElementFromJSONMap("object",json);
        return element.asXML();
        //return DocumentHelper.createDocument(element).asXML();
    }

    public static Object elementToObject(Element element ){
        //Map<String, Object> objectMap = new HashMap<>();
        Attribute attr = element.attribute("type");
        String stype = attr==null?null:element.attribute("type").getValue();
        if(StringUtils.equals("Date", stype)){
            return DatetimeOpt.smartPraseDate(element.getTextTrim());
        }else if(StringUtils.equals("Long", stype)){
            return NumberBaseOpt.castObjectToLong(element.getTextTrim());
        }else if(StringUtils.equals("Integer", stype)){
            return NumberBaseOpt.castObjectToInteger(element.getTextTrim());
        }else if(StringUtils.equals("Number", stype)){
            return NumberBaseOpt.castObjectToDouble(element.getTextTrim());
        }else if(StringUtils.equals("Array", stype)){
            List<Element> subElements = element.elements();
            if(subElements==null)
                return null;
            List<Object> objs = new ArrayList<>(subElements.size());
            for(Element subE : subElements ){
                if(StringUtils.equals("item",element.getName())) {
                    objs.add(
                            elementToObject(subE));
                }
            }
            return objs;
        }else if(StringUtils.equals("Object", stype)){
            Map<String, Object> objectMap = new HashMap<>();
            List<Element> subElements = element.elements();
            if(subElements==null)
                return null;
            for(Element subE : subElements ){
                objectMap.put(element.getName(),
                        elementToObject( subE ));
            }
            return objectMap;
        }else {
            return element.getTextTrim();
        }
    }


    public static Map<String, Object> elementToJSONObject(Element element ){
        Object obj = elementToObject(element);
        if(obj instanceof Map)
            return (Map<String, Object> ) obj;
        return null;
    }

    public static Map<String, Object> xmlStringToJSONObject(String xmlString){
        try {
            Document doc = DocumentHelper.parseText(xmlString);
            return elementToJSONObject(doc.getRootElement());
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object xmlStringToObject(String xmlString){
        try {
            Document doc = DocumentHelper.parseText(xmlString);
            return elementToObject(doc.getRootElement());
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
