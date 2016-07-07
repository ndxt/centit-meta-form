package com.centit.metaform.fromaccess;

public class TestJSON {

	public static String getAString(){
		try{
			int a=0;
			int b=4/a;
			return "333";
		}catch(Exception e){
			return "222";
		}
		finally{
			return "111";
		}
	}
	
	public static void main(String[] args) {
		//FormField ff = new FormField();
		//ff.setKey("iPAddess");
		//ff.setType("input");
		System.out.println(getAString());
	}

}
