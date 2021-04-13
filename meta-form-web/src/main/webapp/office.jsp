<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.io.*,java.text.*,java.util.*,java.sql.*,java.text.SimpleDateFormat,java.text.DateFormat,java.util.Date,javax.servlet.*,javax.servlet.http.*" %>
<%
  //项目名称 比如 /iWebOffice2015/index.jsp
  String mHttpUrlName=request.getRequestURI();
  //System.out.println("mHttpUrlName :" +mHttpUrlName);
  //得到当前页面所在目录下全名称 比如 index.jsp
  String mScriptName=request.getServletPath();
//  System.out.println("mScriptName :" +mScriptName);
  String mServerUrl="http://"+request.getServerName()+":"+request.getServerPort()+mHttpUrlName.substring(0,mHttpUrlName.lastIndexOf(mScriptName));
//  System.out.println("mServerUrl: "+mServerUrl);
  String mSessionID = request.getSession().getId();
 // System.out.println("Word.js SessionID = " + mSessionID);
%>
<html>

<head>
<title>在线打开/保存Word文档</title>
<meta name="renderer" content="webkit" /> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
    html
    {
        height: 100%;
    }
</style>
<script src="js/WebOffice.js"></script>
<script src="js/jquery-1.11.2.min.js"></script>
<script type="text/javascript">
 	var WebOfficeObj = new WebOffice2015(); //创建WebOffice对象
</script>

<script language="javascript">
	  // 金格MultiPart方式在线打开文档
	  function Load()
		{
			try
			{
			WebOfficeObj.ServerUrl = "<%=mServerUrl%>";
			WebOfficeObj.UserName = "演示人";
	      	WebOfficeObj.FileName = "sample.doc";
		    WebOfficeObj.FileType = ".doc";             //FileType:文档类型  .doc  .xls
		    WebOfficeObj.EditType = "1";				//设置加载文档类型 0 锁定文档，1无痕迹模式，2带痕迹模式
		    //WebOfficeObj.RecordID = "123456789";   // //文档存数据库id，唯一标识
	      	//WebOfficeObj.FileName = "sample.xlsx";
		    //WebOfficeObj.FileType = ".xlsx";             //FileType:文档类型  .doc  .xls
		   //WebOfficeObj.DataBase = "MYSQL"; //启用数据库打开保存数据 MYSQL/ORACLE
		    WebOfficeObj.ShowWindow = true;				//true显示进度条//false隐藏进度条
		    WebOfficeObj.obj.Style.ShowOpenProgress = true; //开启、关闭打开文档时的进度条
		    WebOfficeObj.obj.WebCreateProcess();          //创建空进程避免打开慢
		    WebOfficeObj.ShowMenu = 0;
		    WebOfficeObj.ShowToolBar = 0;
		   // WebOfficeObj.SetCaption(WebOfficeObj.UserName + "正在编辑文档");
		    SetGraySkin();			//设置控件皮肤
				if (browser == "chrome"){
			    	// 下载文件时访问OfficeServer与其他网页页面session保持一致的说明 （开始）
					// 为了让/OfficeServer这个Servlet和浏览器的其他页面保持session一致，需要在访问OfficeServer之前把其他页面的cookie发送到服务端去，通过调用
					// INetSetCookie方法，参数是服务器url和cookie值
					var MyCookie = "JSESSIONID=" + "<%=mSessionID%>";
					WebOfficeObj.INetSetCookie(WebOfficeObj.ServerUrl, MyCookie);
			    	//WebOfficeObj.INetSetCookie(WebOfficeObj.ServerUrl, document.cookie); 
					// 但如果浏览器设置了禁止cookie，那么document.cookie则获取不到有效cookie值，这时候可以通过在客户端创建一个
				    // 跟cookie内容值一样的字符串，再把这个字符串当成cookie发送给服务器，也能实现session保持一致
				    // 创建的字符串是这样的形式：JSESSIONID=XXXXXXXXXXXXXXXXXXXXXX,
				    // XXXX...XXX是服务端的sessionid，可以通过jsp变量的形式发送给客户端
				    // varMyCookie = "JSESSIONID=" + "<%=mSessionID%>";	
				    // WebOfficeObj.INetSetCookie(WebOfficeObj.ServerUrl, varMyCookie);	
				    // 下载文件时访问OfficeServer与其他网页页面session保持一致的说明 （结束）
				} 
				if(WebOfficeObj.WebOpen())
				{
				 	StatusMsg(WebOfficeObj.Status);
				}
			}
			catch(e){
 	     	StatusMsg(e.description);
 	    }
		}
		
		//URL地址打开文档
		function LoadURL()
		{
			try
			{
			WebOfficeObj.ServerUrl = "<%=mServerUrl%>"; //服务器地址
		    WebOfficeObj.ShowMenu = 0;
		    WebOfficeObj.ShowToolBar = 0;
		    SetGraySkin();			//设置控件皮肤
			  if(WebOfficeObj.WebOpen2("/Document/sample.doc"))  // 文件在服务器上的相对路径 FileName
			  {
			  	StatusMsg(WebOfficeObj.Status);
			  }
			}
			catch(e){
 	     	StatusMsg(e.description);
 	    }
		}
		
		//服务端Servlet方式打开文档
		function LoadServlet()
		{
			try
			{
			WebOfficeObj.ServerUrl = "<%=mServerUrl%>";               // 用来保存文件的Server
			var downloadLink = "<%=mServerUrl%>" + "/FileDownload?FileName=" + "/Document/sample.doc";
		    WebOfficeObj.ShowMenu = 0;
		    WebOfficeObj.ShowToolBar = 0;
		    SetGraySkin();			//设置控件皮肤
			  if(WebOfficeObj.WebOpen3(downloadLink))  // 文件在服务器上的相对路径 FileName
			  {
			  	StatusMsg(WebOfficeObj.Status);
			  }
			}
			catch(e){
 	     	StatusMsg(e.description);
 	    }
		}
		
		//保存文档
		function SaveDocument()
		{
			WebOfficeObj.FileName = "sample.doc";
			WebOfficeObj.FileType = ".doc";
		  if (WebOfficeObj.WebSave()){    //交互OfficeServer的OPTION="SAVEFILE"
		    WebOfficeObj.WebClose();
		    window.close();
	  	}else{
	  		WebOfficeObj.Alert(WebOfficeObj.Status);
	     	StatusMsg(WebOfficeObj.Status);
	  	}
		}

	 	//设置页面中的状态值
 		 function StatusMsg(mValue) {
		  try {
			  document.getElementById('StatusBar').value = mValue;
		  } catch (e) {
			  return false;
		  }
		 }
		
		//烟枪灰皮肤
 		function SetGraySkin(){
 			//参数顺序依次为：控件标题栏颜色、自定义菜单开始颜色、自定义工具栏按钮开始颜色、自定义工具栏按钮结束颜色、
 			//自定义工具栏按钮边框颜色、自定义工具栏开始颜色、控件标题栏文本颜色（默认值为：0x000000）
 			if (!WebOfficeObj.WebSetSkin(0xdbdbdb, 0xeaeaea, 0xeaeaea, 0xdbdbdb, 0xdbdbdb, 0xdbdbdb, 0x000000))
 				WebOfficeObj.Alert(WebOfficeObj.Status);
		}
		//关闭控件
		function OnUnLoad(){
			WebOfficeObj.WebClose();
		}
		
		
		//前后台交互，key在后台接收
		function SendMessage()
		{
		  //如果非ie浏览器调用IsModify隐藏插件避免窗体被遮挡
			WebOfficeObj.HidePlugin(0);
		  var info = window.prompt("请输入要传到服务器处理页面上的内容:","参数内容");
		  //如果非ie浏览器调用IsModify隐藏插件避免窗体被遮挡
		  if (info==null){return false}
			WebOfficeObj.WebSetMsgByName("TESTINFO",info);  //USERNAME在后获取
			if(WebOfficeObj.WebSendMessage()){  		// 交互信息为INPORTTEXT
			  WebOfficeObj.Alert(WebOfficeObj.WebGetMsgByName("RETURNINFO")); //USERNAME值为对应后台的key
			}else{
		        WebOfficeObj.Alert("客户端Web发送数据包命令没有合适的处理函数");
		        }
			WebOfficeObj.HidePlugin(1);
		}
		
</script>

<script language="javascript" for="WebOffice2015" event="OnReady()">
   WebOfficeObj.setObj(document.getElementById('WebOffice2015'));//给2015对象赋值
   Load();//避免页面加载完，控件还没有加载情况
</script>

<!--以下是多浏览器的事件方法 -->
<script type="text/javascript">
function OnReady(){
 WebOfficeObj.setObj(document.getElementById('WebOffice2015'));//给2015对象赋值
 //Load();//避免页面加载完，控件还没有加载情况
 setTimeout(function(){Load();}, 500);
}
</script>

</head>
<body onUnload="OnUnLoad();" onbeforeunload="OnUnLoad();">
	<div style="width: 100%; height: 100%">
		<div style="width: 100%;">
			<!-- 以MultiPart方式下载文档,设置文件处理Servelet，打开和保存都用这个Servelet处理 -->
			<input style="color:Red;" type=button value="MultiPart方式打开文档" onclick="OnUnLoad();Load()">
			<!-- URL地址打开文档，WebOpen2的参数是文件在服务器上的相对路径 -->
			<input style="color:Red;" type=button value="URL地址打开文档" onclick="OnUnLoad();LoadURL()">
			<!-- 服务端Servlet打开文档，WebOpen3的参数是文件在服务器上的相对路径 -->
			<!-- <input style="color:Red;" type=button value="服务端Servlet打开文档" onclick="OnUnLoad();LoadServlet()">-->
			<input style="color:Red;" type=button value="保存文档到服务器" onclick="SaveDocument();">
			<input style="color:Red;" type=button value="打开本地文档(有窗口)" onclick="WebOfficeObj.WebOpenLocal()">
			<input style="color:Red;" type=button value="保存本地文档(有窗口)" onclick="WebOfficeObj.WebSaveLocal()">
			<input style="color:Red;" type=button value="前后台交互信息" onclick="SendMessage()">
			<input style="color:Red;" id="StatusBar" type="text" name="StatusBar" readonly  style="WIDTH:10%">&nbsp;|←状态信息
		</div>
		<div id="OfficeDiv" style="width: 100%; height: 98%;" ><script src="js/iWebOffice2015.js"></script></div>
	</div>
</body>
</html>