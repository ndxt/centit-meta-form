package com.centit.metaform.formaccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.utils.FieldType;
import com.centit.support.xml.XMLObject;

public class TestJSON {

    public static void getAString(){
        SimpleTableField pc = new SimpleTableField();
        pc.setPropertyName( FieldType.mapPropName(
                "USER_NAME"));
        pc.setFieldLabelName("object");
        pc.setJavaType(FieldType.TEXT);
        pc.setColumnType("CLOB");
        pc.setColumnName( "USER_NAME");
        pc.setColumnComment("存储对象的大字段");
        JSONObject obj =(JSONObject) JSON.toJSON(pc);
        System.out.println(obj.toJSONString());
        System.out.println(XMLObject.jsonObjectToXMLString(obj));

    }

    public static void main(String[] args) {
        //FormField ff = new FormField();
        //ff.setKey("iPAddess");
        //ff.setType("input");
        getAString();
    }

}
