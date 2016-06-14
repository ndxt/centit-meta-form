package com.centit.metaform.fromaccess;

import com.alibaba.fastjson.JSON;
import com.centit.metaform.formaccess.FormField;

public class TestJSON {

	public static void main(String[] args) {
		FormField ff = new FormField();
		ff.setKey("iPAddess");
		ff.setType("input");
		System.out.println(JSON.toJSONString(ff));
	}

}
