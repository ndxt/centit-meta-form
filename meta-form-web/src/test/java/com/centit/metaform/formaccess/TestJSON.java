package com.centit.metaform.formaccess;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.utils.FieldType;
import com.centit.support.security.AESSecurityUtils;
import com.centit.support.xml.XMLObject;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

public class TestJSON {

    public static void getAString(){
        SimpleTableField pc = new SimpleTableField();
        pc.setPropertyName( FieldType.mapPropName(
                "USER_NAME"));
        pc.setFieldLabelName("object");
        pc.setFieldType(FieldType.TEXT);
        pc.setColumnType("CLOB");
        pc.setColumnName( "USER_NAME");
        pc.setColumnComment("存储对象的大字段");
        JSONObject obj =(JSONObject) JSON.toJSON(pc);
        System.out.println(obj.toJSONString());
        System.out.println(XMLObject.jsonObjectToXMLString(obj));

    }

    public static void createPassword() throws GeneralSecurityException {
        String passwd = "fdemo2";
        String key = "0123456789abcdefghijklmnopqrstuvwxyzABCDEF";
        Cipher cipher = AESSecurityUtils.createEncryptCipher("0123456789abcdefghijklmnopqrstuvwxyzABCDEF");
        String encodePwd = AESSecurityUtils.encryptAndBase64(passwd,key);
        System.out.println("encodePwd:"+encodePwd);
        String decodePwd = AESSecurityUtils.decryptBase64String(encodePwd,key);
        System.out.println("decodePwd:"+decodePwd);
    }

    public static void main(String[] args) {
        //FormField ff = new FormField();
        //ff.setKey("iPAddess");
        //ff.setType("input");
        //getAString();
        JSONObject obj = new JSONObject();
        if (obj.isEmpty()){
            System.out.println("12");
        }
    }

}
