package com.centit.metaform.fromaccess;

import com.alibaba.fastjson.JSON;

public class TestJSON {

	public static void main(String[] args) {
		FormField ff = new FormField();
		ff.setKey("iPAddess");
		ff.setType("input");
		System.out.println(JSON.toJSONString(ff));
	}

}
