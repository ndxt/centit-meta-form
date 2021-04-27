window.console = window.console || (function(){ 
var c = {}; c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile = c.clear = c.exception = c.trace = c.assert = function(){}; 
return c; 
})();
function base64toBlob(base64Data, contentType) 
{
        contentType = contentType || '';
        var sliceSize = 1024;
        var byteCharacters = atob(base64Data);
        var bytesLength = byteCharacters.length;
        var slicesCount = Math.ceil(bytesLength / sliceSize);
        var byteArrays = new Array(slicesCount);

        for (var sliceIndex = 0; sliceIndex < slicesCount; ++sliceIndex) {
            var begin = sliceIndex * sliceSize;
            var end = Math.min(begin + sliceSize, bytesLength);

            var bytes = new Array(end - begin);
            for (var offset = begin, i = 0; offset < end; ++i, ++offset) {
                bytes[i] = byteCharacters[offset].charCodeAt(0);
            }
            byteArrays[sliceIndex] = new Uint8Array(bytes);
        }
        return new Blob(byteArrays, { type: contentType });
}
   
function WebOffice2015() {
	this.SaveServlet = "/OfficeServer";
	this.ServerUrl = "ServerUrl";
	this.dVersion = "5.0.0.12";
	this.dUpdateDate = "2020-07-27";
	//高级功能：全文检索、文档对比

	// *****************************************************************************************************************************
	// 内部变量和方法
	this.PASSWORD = "123456";
	var ISO;								// 服务器返回值
	var root;
	var rootValue;
	this.Ashell = null;
    this.BookMark = "";						// 书签名称
    this.ImageName = "";					// 图片名称
	this.obj; 								// Office控件对象,可直接调用控件接口及属性
	this.DownloadedFileTempPathName = "";	// 下载保存的临时文件路径名
	this.DOWN = "\\WebOffice\\Down\\"; 		// 指定隐藏路径并设置名称
	this.UP = "\\WebOffice\\UP\\";			// 指定隐藏路径并设置名称
	this.FilePath = null; 					// 文档路径
	this.TmpFile = null; 					// 临时下载的文档名称（含完整路径）
	this.tempInsertName = ""; 				// 插入文件的文件名称
	this.DocTypeValue = {
		DOC : 0,
		DOCX : 16,
		XLS : 56,
		XLSX : 51,
		WPS : 0,
		ET : 56,
		PDF : 41,
		OFD : 42
		
	}; 										// 枚举所有的文档类型值用户保存用
	this.DocSuffixType = {
		0 : ".doc",
		12 : ".docx",
		56 : ".xls",
		51 : ".xlsx",
		0 : ".wps",
		56 : ".et",
		41 : ".pdf",
		42 : ".ofd"
	}; 										// 根据打开文档类型，来获取后缀名称
	this.setVersion = -1;					// Office版本号
	this.OfficeVersion = {
		v2003 : 1,
		vOther : 0
	}; 										// 判断Office版本，这个只要用于保存到本地
	this.sendMode = null;					// 设置异步调用模式
	this.iWebOfficeTempName = "iWebOfficeTempName.doc"
	// *****************************************************************************************************************************

	// *****************************************************************************************************************************
	// 对外公共属性
	this.WebUrl = ""; 						// 服务器应用程序Url路径 				
	this.RecordID = ""; 					// 文档的纪录号					
	this.Template = ""; 					// 模板编号					
	this.SingleFileName = "";
	this.FileName = ""; 					// 文档名称					
	this.UserName = ""; 					// 操作文档用户名				
	this.OfficeUserName = ""; 				// 记录本地office用户名 
	this.FileType = ""; 					// 文档类型 .doc .xls .wps		
	this.EditType = ""; 					//文档编辑类型
	this.DataBase = "";
	this.WebObject = null; 					// 设置WebObject对象，方便调用VBA 方法（）
	// 设置是否显示整个控件工具栏，包括OFFICE的工具栏
	// 0 : 自定义工具栏=false, Office工具栏=true;
	// 1 : 自定义工具栏=true,  Office工具栏=true;
	// 2 : 自定义工具栏=false, Office工具栏=false;
	// 3 : 自定义工具栏=true,  Office工具栏=false;
	this.ShowToolBar = 1;		//					
	this.CopyType = "1"; 					// 设置拷贝类型 "1": 允许拷贝 "0": 禁止拷贝			
	// 该属性控制的是操作系统的粘贴板，一旦设置为禁止拷贝则整个操作系统的拷贝也被禁止。
	this.ShowMenu = "1"; 					// 设置是否显示整个菜单 "1": 显示菜单 "0": 不显示菜单		
	this.Status = ""; 						// Status：状态信息								
	// “工具栏空间”即当所有OFFICE工具栏都隐藏时控件故意产生一个灰色条占住空间，在OFFICE2000、2003下有此功能
	this.MaxFileSize = 8 * 1024; 			// 设置文件最大允许值，单位k，默认为8M			
	this.ShowWindow = true; 				// 设置是否显示保存及打开窗口 True: 显示 False: 不显示    
	// 设置为显示，在打开文档和保存文档时会出现进度窗口
	this.RibbonUIXML = ""; 					// 用来详细设置Office2007选项卡工具界面			
	this.UIControl = false; 				// 控制OFFICE2010环境中是否可以使用另存为和保存功能，用于控制客户使用自定义快捷键和快捷工具栏的另存为功能下载文件	
	this.HiddenDirectory = false;			// 是否开启将文档保存在隐藏目录中  true：开启  false： 不开启 默认：false
	this.DelFileAfterSave = true;			// 保存文档后删除该目录中的文档   true：删除  false：不删除  默认：true
	this.Charset = true;					//后台数据编码,true为utf-8编码,false为gb2312编码
	// ******************************************************************************************************************************

	// ******************************************************************************************************************************//
	// ---------------------------------------------------对外接口-------------------------------------------------------------------//
	
	// 设置浏览器Cookie
	this.INetSetCookie = function(url, cookie)
	{
		this.obj.INetSetCookie(url, cookie);
	}
	
	// 设置控件标题
	this.SetCaption = function(captionName) 
	{
		this.obj.Caption = captionName;// this.Caption;
	}
	
	this.GetFileType = function(downloadLink)
	{
		var filename=downloadLink;
		var index1=filename.lastIndexOf(".");
		var index2=filename.length;
		var postf=filename.substring(index1,index2);//后缀名
		
		this.FileType = postf;
	}
	this.GetSingleFileName = function(downloadLink)
	{
		var filename=downloadLink;
		var index1=filename.lastIndexOf("/");
		var index2=filename.length;
		var postf=filename.substring(index1+1,index2);//后缀名
		
		this.SingleFileName = postf;
	}
	
	this.WebOpen2 = function(downloadLink)
	{
		this.Status = "成功";
		var httpclient = this.obj.Http; // 设置http对象
		
		
		this.FileName = downloadLink;
		this.GetFileType(downloadLink);
		this.GetSingleFileName(downloadLink);
		if (httpclient.Open(this.HttpMethod.Get, this.ServerUrl + downloadLink, false))
        {
            if (httpclient.Send()) 
            {
            	if (this.hiddenSaveLocal(httpclient, this, false, false)) {
					var mSaveResult = this.WebOpenLocalFile(this.DownloadedFileTempPathName);
					if (mSaveResult == 0) { // 打开本地磁盘文件
						this.getOfficeVersion();// 打开文档后，判断当前office版本
						return true;
					}else if(mSaveResult == 1){
						var windows = window.confirm("可能当前授权码错误，请确认iWebOffice2015.js的授权是否正确（或乱码）"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "可能当前授权码错误，请确认iWebOffice2015.js的授权是否正确（或乱码）";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 2){
						var windows = window.confirm("没有找到文档，请确认WebOpenLocalFile打开文档的路径是否正确"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "有找到文档，请确认WebOpenLocalFile打开文档的路径是否正确";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 3){
						var windows = window.confirm("没有权限导致文档打开失败，请用管理员身份运行浏览器后重试"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "没有权限导致文档打开失败，请用管理员身份运行浏览器后重试";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 4){
						var windows = window.confirm("文件可能损坏，请确定服务器文档是否已经损坏"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "文件可能损坏，请确定服务器文档是否已经损坏";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 5){
						var windows = window.confirm("未安装Office或者注册表有损坏"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "未安装Office或者注册表有损坏";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 6){
						var windows = window.confirm("文件被占用，请结束Office进程后重试"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "文件被占用，请结束Office进程后重试";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else {
						var windows = window.confirm("打开文档时未知错误！错误码为： "
										+ mSaveResult
										+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "打开文档时未知错误！错误码为： "
								+ mSaveResult;
						if (windows == 1) {
							window.close();
							return false;
						}
					}
				} else {
					// 失败后，this.Status的值由hiddenSaveLocal返回
					this.Status = "保存文档到本地 失败";
					return false;
				}
            }
        }
	}
	this.WebOpen3 = function(downloadLink)
	{
		this.Status = "成功";
		var httpclient = this.obj.Http; // 设置http对象
		
		this.ShowMenuBar(this.ShowMenu);  //控制菜单栏是否可以显示
		this.NewShowToolBar(this.ShowToolBar); //控制Office工具栏和自定义工具栏
		
		
		this.GetFileType(this.FileName);
		this.GetSingleFileName(this.FileName);
		//alert("want to download: " + downloadLink);
		if (httpclient.Open(this.HttpMethod.Get, downloadLink, false))
        {
            if (httpclient.Send()) 
            {
            	if (this.hiddenSaveLocal(httpclient, this, false, false)) {
					var mSaveResult = this.WebOpenLocalFile(this.DownloadedFileTempPathName);
					if (mSaveResult == 0) { // 打开本地磁盘文件
						this.getOfficeVersion();// 打开文档后，判断当前office版本
						return true;
					}else if(mSaveResult == 1){
						var windows = window.confirm("可能当前授权码错误，请确认iWebOffice2015.js的授权是否正确（或乱码）"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "可能当前授权码错误，请确认iWebOffice2015.js的授权是否正确（或乱码）";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 2){
						var windows = window.confirm("没有找到文档，请确认WebOpenLocalFile打开文档的路径是否正确"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "有找到文档，请确认WebOpenLocalFile打开文档的路径是否正确";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 3){
						var windows = window.confirm("没有权限导致文档打开失败，请用管理员身份运行浏览器后重试"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "没有权限导致文档打开失败，请用管理员身份运行浏览器后重试";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 4){
						var windows = window.confirm("文件可能损坏，请确定服务器文档是否已经损坏"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "文件可能损坏，请确定服务器文档是否已经损坏";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 5){
						var windows = window.confirm("未安装Office或者注册表有损坏"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "未安装Office或者注册表有损坏";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else if(mSaveResult == 6){
						var windows = window.confirm("文件被占用，请结束Office进程后重试"
								+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "文件被占用，请结束Office进程后重试";
						if (windows == 1) {
							window.close();
							return false;
						}
					}else {
						var windows = window.confirm("打开文档时未知错误！错误码为： "
										+ mSaveResult
										+ "\r\r单击“确定”关闭。单击“取消”继续。");
						this.Status = "打开文档时未知错误！错误码为： "
								+ mSaveResult;
						if (windows == 1) {
							window.close();
							return false;
						}
					}
				} else {
					// 失败后，this.Status的值由hiddenSaveLocal返回
					this.Status = "保存文档到本地 失败";
					return false;
				}
            }
        }
	}
	/* 从服务器上取文档并打开，打开RecordID指定的文件 */   //该功能已完整实现
	this.WebOpen = function(mBoolean) 
	{
		this.Status = "成功";
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.Clear();
		this.GetSingleFileName(this.FileName);
		this.WebSetMsgByName("USERNAME", this.UserName); // 加载UserName
		this.WebSetMsgByName("FILENAME", this.FileName); // 加载FileName
		this.WebSetMsgByName("FILETYPE", this.FileType); // 加载FileType
		this.WebSetMsgByName("RECORDID", this.RecordID); // 加载RecordID
		this.WebSetMsgByName("EDITTYPE", this.EditType); // 加载RecordID
		this.WebSetMsgByName("DATABASE", this.DataBase); // 加载数据库
		this.WebSetMsgByName("OPTION", "LOADFILE"); 	 // 发送请求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); // 这里是自定义json
		// 传输格式。
		this.WebClearMessage(); 						 // 清除所有WebSetMsgByName参数
		this.sendMode = "OpenFile";
		this.ShowMenuBar(this.ShowMenu);  //控制菜单栏是否可以显示
		this.NewShowToolBar(this.ShowToolBar); //控制Office工具栏和自定义工具栏
		if (this.LOADFILE(httpclient)) // Http下载服务器文件
		{
			this.NewCopyType(this.CopyType);  			 // 控制是否可以复制
			this.NewUIControl(this.UIControl);  		 // 控制 2010保存跟另存为
			if(this.FileType != ".ppt" && this.FileType != ".pptx" && this.FileType != ".pdf" && this.FileType != ".ofd"){
				this.VBASetUserName(this.UserName);		 // 设置Office用户名
				this.setEditType(this.EditType);			 // 设置文档编辑权限   0 、只读不能复制  1、无痕迹打开 2、有痕迹打开  
			}
			this.Status = "打开文档成功"; 				 // Status：状态信息
			return true;
		} else {
			//this.Status = "打开文档失败"; 			 // Status：状态信息 由This.LOADFILE返回
			return false;
		}
	}
	
	/* 从服务器上取文档并打开，打开RecordID指定的文件 */   //该功能已完整实现
	this.WebOpenBase64 = function()
	{
	    var jsWebOffice = this;
	    var officeobj = this.obj;
	    var filename = this.FileName;
	    var tmpDownPath = this.DownFilePath();
	    
		var send = "OPTION=LOADFILE&FILENAME="+this.FileName+"&FILETYPE="+this.FileType+"&RECORDID="+this.RecordID+"&EDITTYPE="+this.EditType;
/*		$.post("../../AJAXServer", send, function(data) {
			console.log(data);
		});*/
		$.ajax({
			  type: 'POST',
			  url: "../../AJAXServer",
			  data: send,
	          cache: false,
	          processData: false,
	          xhr: function(){ //这是关键 获取原生的xhr对象 做以前做的所有事情
	        	    console.log("xhr function eee");
		        	var xhr = jQuery.ajaxSettings.xhr(); 
		        	console.log(xhr);
		        	xhr.onprogress = function (evt) 
		        	{ 
		    	    	if (evt.lengthComputable) 
		    	    	{
		    	    		var percentComplete = evt.loaded / evt.total;
		    	    		console.log(percentComplete);
		    	    	}
		        	} 
		        	return xhr; 
	          }, 			  
			  success: function(data) {
	                //console.log(data);
	        	  var filePathName = tmpDownPath;
    	    	  filePathName += "\\";
    	    	  filePathName += filename;
    	    	  officeobj.Base64Text2File(data, filePathName);
    	    	  jsWebOffice.WebOpenLocalFile(filePathName);
	            }

			});
		
		return true;
	}
	
	this.WebSave2 = function(callback)
	{
		var rands = Math.round(Math.random()*1000000);
		var fileUpPathName = this.getFilePath() + rands + this.FileName;
		//alert(fileUpPathName);
		var mSaveResult = this.WebSaveLocalFile(fileUpPathName);
		if (!(mSaveResult == 0)) 
		{
			this.Status = "保存本地文档失败！错误代码为：" + mSaveResult;
			return false;
		}
		else
		{
//			alert("文档没有内容，是否确定保存");
		}
		
	    var jsWebOffice = this;
	    var officeobj = this.obj;
	    var filename = this.FileName;

		var base64file = officeobj.File2Base64Text(fileUpPathName);
        var formData = new FormData();
        
        var timestamp1 = new Date().getTime();
        var myfff = base64toBlob(base64file);

        var timestamp2 = new Date().getTime();
        
        //alert(myfff);
        formData.append("FileData", myfff, "myFileName");
        var aaa = '${pageContext.request.contextPath}';
        
        var strUrl = jsWebOffice.WebUrl + '/AJAXServer?OPTION=MultiPartUpload&FILENAME=' + filename;
        
        $.ajax({
            //url:  '${pageContext.request.contextPath}/AJAXServer?OPTION=MultiPartUpload',
        	url: strUrl,
            type: "POST",
            data: formData,
            contentType: false,
            cache: false,
            processData: false,
            xhr: function(){ //这是关键 获取原生的xhr对象 做以前做的所有事情
        	    console.log("xhr function");
	        	var xhr = jQuery.ajaxSettings.xhr(); 
	        	console.log(xhr);
	        	xhr.upload.onprogress = function (evt) 
	        	{ 
	    	    	if (evt.lengthComputable) 
	    	    	{
	    	    		var percentComplete = evt.loaded / evt.total;
	    	    		console.log(percentComplete);
	    	    		//callback(0);
	    	    	}
	        	} 
	        	return xhr; 
        	}, 
            success: function(data, textStatus) {
        		console.log('success');
        		console.log('textStatus: ' + textStatus);
                console.log('data: ' + data);
                callback(0);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown)
            {
            	console.log('essor');
            	console.log(XMLHttpRequest);
            	console.log('textStatus: ' + textStatus);
            	console.log('errorThrown: ' + errorThrown);
            	callback(1);
            }
        });
        
        
		return true;
	}

	this.WebSaveBase64 = function() 
	{
		//alert("this.FileName = " + this.FileName);
		var rands = Math.round(Math.random()*1000000);
		var fileUpPathName = this.getFilePath() + rands + this.FileName;
		
		var mSaveResult = this.WebSaveLocalFile(fileUpPathName);
		if (!(mSaveResult == 0)) 
		{
			this.Status = "保存本地文档失败！错误代码为：" + mSaveResult;
			return false;
		}
		else
		{
//			alert("文档没有内容，是否确定保存");
		}
		
	    var jsWebOffice = this;
	    var officeobj = this.obj;
	    var filename = this.FileName;

		var base64file = officeobj.File2Base64Text(fileUpPathName);
		//alert(base64file);
		var newBase64 = base64file.replace(/\+/g, "%2B");
		//alert(newBase64);
		
	    //创建XMLHttpRequest对象  
	    if(window.XMLHttpRequest)
		{  
	      //针对FireFox,Mozillar,Opera,Safari,IE7,IE8  
	       xmlhttp = new XMLHttpRequest();  
	         
	       //对某些特定版本的mozillar浏览器的bug进行修正  
	       if(xmlhttp.overrideMineType)
		   {  
	          xmlhttp.overrideMineType("text/xml");  
	       }  
	    }
		else if(window.ActiveXObject)
		{  
	       //针对IE5，IE5.5，IE6  
	       //两个可以用于创建XMLHTTPRequest对象的控件名称。保存在一个JS数组中。  
	       var activexName = ["MSXML2.XMLHTTP","Microsoft.XMLHTTP"];  
	       for(var i = 0; i<activeName.length; i++)
		   {  
	           //取出一个控件名进行创建，如果成功就终止循环  
	           try{  
	              xmlhttp = new ActiveXObject(activexName[i]);  
	              break;  
	           }catch(e){}
	       }         
	    }
	    
	    if(xmlhttp)
	    {  
	       // alert("XMLHttpRequest对象创建成功！");  
	    }
	    else
	    {  
	        //alert("XMLHttpRequest对象创建失败！");  
	 	    return false;  
	    }  
	    
	    var jsWebOffice = this;
	    var officeobj = this.obj;
	    var filename = this.FileName;
	    
//	    xmlhttp.onreadystatechange = function()
//	    {  
//	    	   //判断对象的状态是否交互完成  
//	    	   if(xmlhttp.readyState == 4)
//	    	   {  
//	    	      //判断http的交互是否成功  
//	    	      if(xmlhttp.status == 200)
//	    	      {  
//	    	      }  
//	    	        
//	    	   }  
//	   	}; 
	    
	    var postdata = "OPTION=SAVEFILE&FILENAME="+this.FileName+"&FILETYPE="+this.FileType+"&RECORDID="+this.RecordID+"&EDITTYPE="+this.EditType
		+"&TEMPLATE="+this.Template+"&SUBJECT="+this.Subject+"&AUTHOR="+this.Author+"&BASE64FILE="+newBase64;
		//alert("aaa.length = " +  aaa.length);
	    xmlhttp.open("POST","../../AJAXServer?name=", false);   // false 是同步
		//xmlhttp.setRequestHeader("Content-Length",aaa.length);
		xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");   //POST 必须要写这句，GET不用写
	    //发送数据，开始和服务器进行交互。  
	    //xmlhttp.send("OPTION=SAVEFILE&FILENAME="+this.FileName+"&FILETYPE="+this.FileType+"&RECORDID="+this.RecordID+"&EDITTYPE="+this.EditType
	    //		+"&TEMPLATE="+this.Template+"&SUBJECT="+this.Subject+"&AUTHOR="+this.Author+"&BASE64FILE="+newBase64);  
	    xmlhttp.send(postdata);
		this.Status = "成功将文档保存到服务器";
		return true;
	}
	
	
	
	/* 保存文件 */		//（该功能已完整实现）
	this.WebSave = function() 
	{
		this.Status = "";
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("USERNAME", this.UserName);
		this.WebSetMsgByName("RECORDID", this.RecordID);
		this.WebSetMsgByName("TEMPLATE", this.Template);
		this.WebSetMsgByName("SUBJECT", this.Subject);
		this.WebSetMsgByName("AUTHOR", this.Author);
		this.WebSetMsgByName("HTMLPATH", this.HTMLPath);
		this.WebSetMsgByName("FILETYPE", this.FileType);
		this.WebSetMsgByName("OPTION", "SAVEFILE");
		this.WebSetMsgByName("DATABASE", this.DataBase);
		this.WebSetMsgByName("FILENAME", this.FileName); // 加载FileName
		if(this.WebSetAllowEmpty()){
		this.GetSingleFileName(this.FileName);
		var mSavePath = this.getFilePath() + Math.random() * 100000 + this.SingleFileName;
		var mSaveResult = this.WebSaveLocalFile(mSavePath);

		if (!(mSaveResult == 0)) {
				this.Status = "保存本地文档失败！错误代码为：" + mSaveResult;
				return false;
			}
		}else{
			this.obj.FuncExtModule.Alert("文档没有内容，是否确定保存");
		}
		this.sendMode = "SaveFile";
			// 判断本地文件是否大于指定的文件大小，如果大于不保存
			if (this.WebSetMaxFileSize(mSavePath)) {
				//alert(this.FilePath + this.SingleFileName);
				if (this.SAVEFILE(httpclient, mSavePath)) {
					var ISO = httpclient.GetResponseHeader("ISO");// 获取返回值
					//this.Close();
				/*	if (this.DelFileAfterSave)
					{
						this.ClearDirectory(); //清除临时文件
					}*/
					this.Status = "成功将文档保存到服务器";
					return true;
				} else {
					//STATUS 由this.SAVEFILE返回
					return false;
				}
			} else {
			this.Status = "保存失败：MaxFileSize只能允许保存：<" + this.MaxFileSize / 1024
					+ ">" + "M";
			return false;
		}
	}

	/* 关闭文档 */
	this.WebClose = function() 
	{
		if(this.Close()){
			return true;
		}
		return false;
	}
	
	/* 保存上传到服务器的数据*/
	this.WebSetMsgByName = function(FieldName, FieldValue) 
	{
		this.ht.Add(FieldName, FieldValue);
	}
	
	/* 获取从服务器得到的数据*/
	this.WebGetMsgByName = function(FieldName) 
	{
		return this.ht.Get(FieldName);
	}

	/* 按json格式发送数据 */	//
	this.WebSendMessage = function() {
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("OPTION", "SENDMESSAGE");
		this.sendMode = "SendMessage";
		httpclient.ShowProgressUI = this.ShowWindow;
		httpclient.AddForm("FormData", this.GetMessageString());
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) // true 异步方式 false同步
		{
			if (httpclient.Send()) 
			{
				this.GetDataToSend(); //得到服务器setMsgByName的值并发送到前台
				httpclient.Clear();
				this.Status = this.WebGetMsgByName("STATUS");
				return true;
			} else {
				this.Status = this.WebGetMsgByName("STATUS");
				return false;
			}
		}else{
			this.Status = "网络通信异常";               //Status：状态信息
			return false;
		 }
		return this.ht.toString();
	}

	
	/* 清除由WebSetMsgByName设置的值 */
	this.WebClearMessage = function() 
	{
		this.ht.Clear();
	}

	/* 保存为PDF文件并上传至服务器 */ 
	this.WebSavePDF = function() 
	{
		var httpclient = this.obj.Http; // 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("RECORDID", this.RecordID);
		this.WebSetMsgByName("OPTION", "SAVEPDF");
		this.WebSetMsgByName("FILENAME", this.RecordID + ".pdf");// 加载FileName
		this.SaveAsPdf(this.getFilePath() + this.RecordID + ".pdf");
		if (this.SAVEFILE(httpclient, this.getFilePath() + this.RecordID + ".pdf")) {
			this.Status = "保存PDF文件成功";
			return true;
		} else {
			this.Status = "保存PDF文件失败";
			return false;
		}
	}

	
	/* 保存HTML到服务器          需要V12.4.0.450以上版本*/
	this.WebSaveAsHtml = function() 
	{
		if (this.SaveAsHtml(this.getFilePath() + this.RecordID + ".html")) {
			var strDirPath = this.getFilePath() + this.RecordID + '.files'; //获取html files路径
			var FindFile = this.obj.FileSystem.FindToDirAsFileEx(strDirPath);//获取html文件夹里的内容
			var DirPath = FindFile.split("\r\n");
			var httpclient = this.obj.Http; // 设置http对象
			httpclient.Clear();
			this.WebSetMsgByName("RECORDID", this.RecordID);
			this.WebSetMsgByName("OPTION", "SAVEHTML");
			this.WebSetMsgByName("DIRECTORY", this.RecordID + ".files");
			for(var i=0; i<DirPath.length;i++){
				var FilesName = DirPath[i];
				this.WebSetMsgByName("FILENAME", FilesName);// 加载FileName
				if (!this.SAVEFILE(httpclient, strDirPath + "\\" + FilesName)){
					this.Status = "保存HTML文件失败";
					return false;
				}
			}
			this.WebSetMsgByName("DIRECTORY", ""); //作用吧html和files区分开
			this.WebSetMsgByName("FILENAME", this.RecordID + ".html");// 加载FileName
			if (this.SAVEFILE(httpclient, this.getFilePath() + this.RecordID + ".html")){
				this.Status = "保存HTML文件成功";
				return true;
			} else {
				this.Status = "保存HTML文件失败";
				return false;
			}
		}
		this.Status = "保存HTML文件失败!";
		return false;
	}
	
	/* 保存模板文件 */ 
    this.WebSaveTemplate = function()
    {
    	this.Status = "";
    	var httpclient = this.obj.Http; //设置http对象 
    	httpclient.Clear();
    	this.WebSetMsgByName("OPTION", "SAVETEMPLATE");
    	this.WebSetMsgByName("TEMPLATE", this.Template);
    	var mSaveResult = this.WebSaveLocalFile(this.getFilePath() + this.FileName);
    	if (!(mSaveResult == 0))
    	{
    		this.Status = "保存本地文档失败！错误代码为：" + mSaveResult;
    		return false;
    	}
    	
    	this.sendMode = "WebSaveTemplate";
    	if (this.SAVEFILE(httpclient, this.FilePath + this.FileName)) 
    	{
    		this.Status="保存模板文件成功";
    		return true;
    	}
    	else
    	{
    		//STATUS 由this.SAVEFILE返回
    		return false;        	
    	}
    }
    
    /* 调入由Template指定的模版，该功能主要用于模版管理或模板套红 */ 
    this.WebUseTemplate = function()
    {
		this.ClearRevisions(); //清除正文痕迹的目的是为了避免痕迹状态下出现内容异常问题。
		this.WebDelLocalFile(this.getFilePath() + this.iWebOfficeTempName);	//删除临时文件
    	var mSaveResult = this.WebSaveLocalFile(this.getFilePath() + this.iWebOfficeTempName);//将当前文档保存下来
    	if (!(mSaveResult == 0)){
    		this.Status = "保存本地文档失败！错误代码为：" + mSaveResult;
    		return false;
    	}
    	var httpclient = this.obj.Http; //设置http对象
    	this.WebSetMsgByName("OPTION", "LOADTEMPLATE");
    	this.WebSetMsgByName("RECORDID", this.RecordID);
    	this.WebSetMsgByName("TEMPLATE", this.Template);
    	httpclient.AddForm("FormData", this.GetMessageString()); //这里是自定义json 传输格式。
    	this.WebClearMessage(); //清除所有WebSetMsgByName参数
    	
    	if(this.LOADFILE(httpclient)){
    		if(this.VBAInsertFile("Content",this.getFilePath() + this.iWebOfficeTempName)){
    			this.Status = "套红成功";
    			return true;
    		}else{
    			//this.Status = "套红失败"; //STATUS 由this.VBAInsertFile返回
    			return false;
    		}
    	}else{
			this.Status = "打开套红文档失败";               //Status：状态信息
			return false;
    	}
    }

	/* 将当前文档作为图片格式保存在服务器上  WebSaveImage(".gif","abc.gif"); */
	this.WebSaveImage = function (ImageType, ImageName) 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx" || this.FileType == ".wps"){
			var iFilePath = this.getFilePath() + this.RecordID + ".htm";
			var strDirPath = this.getFilePath() + this.RecordID + '.files'; //获取html files路径
			this.obj.ActiveDocument.SaveAs(iFilePath, 10, false, "", false, "", false, false, false, false, false, 0); //保存到本地html
			this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3;	//3是页面视图  
			var DirPath = new VBArray(this.obj.FileSystem.FindToDirAsFile(strDirPath)).toArray(); //获取html文件夹里的内容
			var httpclient = this.obj.Http; 							// 设置http对象
			httpclient.Clear();
			this.WebSetMsgByName("RECORDID", this.RecordID);
			this.WebSetMsgByName("OPTION", "SAVEIMAGE");
			this.WebSetMsgByName("DIRECTORY", this.RecordID + ".files");
			for(var i=0; i<DirPath.length;i++){
				var FilesName = DirPath[i];
				this.WebSetMsgByName("FILENAME", FilesName);			// 加载FileName
				if (!this.SAVEFILE(httpclient, strDirPath + "\\" + FilesName)){
					this.Status = "保存HTML图片文件失败";
					return false;
				}
			}
			this.WebSetMsgByName("DIRECTORY", ""); 						//作用把html和files区分开
			this.WebSetMsgByName("FILENAME", this.RecordID + ".htm");	// 加载FileName
			if (this.SAVEFILE(httpclient, this.getFilePath() + this.RecordID + ".htm")){
				this.Status = "保存HTML图片文件成功";
				return true;
			} else {
				this.Status = "保存HTML图片文件失败";
				return false;
			}
		}
		this.Status = "保存HTML图片文件失败!";
		return false;
	}
    /*  插入服务器上执行的文档 */  
	this.WebInsertFile = function () 
    {
        	var httpclient = this.obj.Http;
			this.WebSetMsgByName("TEMPLATE", this.Template); 					//在接口获取模板名称
			this.WebSetMsgByName("OPTION", "INSERTFILE");     					//发送请求LOADFILE
			httpclient.AddForm("FormData", this.GetMessageString()); 			//这里是自定义json 传输格式。
			this.WebClearMessage();                         					//清除所有WebSetMsgByName参数
			var URL = this.WebUrl.substring(0, this.WebUrl.lastIndexOf("/"));
			httpclient.ShowProgressUI = this.ShowWindow;						//隐藏进度条
			if (httpclient.Open(this.HttpMethod.Post, URL, false)) 									//Http下载服务器文件
			{  
				if(httpclient.Send()){
	        		if (httpclient.Status == 200) {
	        			if(this.hiddenSaveLocal(httpclient,this,false,false,this.Template)){
	        				if(this.obj.ActiveDocument.BookMarks.Exists(this.BookMark)){
		        				if(this.VBAInsertFile(this.BookMark,this.DownloadedFileTempPathName)){
		        	    			this.Status = "插入成功";               //Status：状态信息
		        	    			httpclient.Clear();
		        	    			return true;
		        				}else{
		        	    			//状态信息由 this.VBAInsertFILE返回
		        	    			return false;
		        				}
	        				}else{
	        					this.Status = "文档里没有对应书签";
	        					return false;
	        				}
	        			}else{
        					this.Status = "文档保存到本地失败";
        					return false;
	        			}
	                }else{
    					this.Status = "数据接收错误";
    					return false;
	                }
	        	}else{
					this.Status = "数据包发送失败";               //Status：状态信息
					return false;
	        	}
    		}else{
					this.Status = "打开连接失败";               //Status：状态信息
					return false;
				 }
    }

    /* 在指定的标签位置插入服务器上的图片，并决定是否做透明处理 */
	this.WebInsertImage = function(BookMark, ImageName, Transparent, ZOrder) //插入远程图片：BookMark插入的书签的位置;ImageName远程图片的名称
    {
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				var httpclient = this.obj.Http;
				this.BookMark = BookMark;
				this.ImageName = ImageName;
				this.WebSetMsgByName("OPTION", "INSERTIMAGE");
				this.WebSetMsgByName("IMAGENAME", this.ImageName);
				httpclient.AddForm("FormData", this.GetMessageString()); //这里是自定义json 传输格式。
				this.WebClearMessage(); //清除所有WebSetMsgByName参数
				httpclient.ShowProgressUI = this.ShowWindow;//隐藏进度条
				if(httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)){
					if(httpclient.Send()){
						if (httpclient.Status == 200) {
							if(this.hiddenSaveLocal(httpclient,this,false,false,this.ImageName)){
								if(this.InsertImageByBookMark(Transparent,ZOrder)){
									this.ImageName = null;
									this.BookMark = null;
									httpclient.Clear();
									this.Status = "插入服务器图片成功";
									return true;
								}else{
									this.Status = "书签插入图片失败";
									return false;
								}
							}else{
								this.Status = "文档保存到本地失败";
								return false;
							}
						}else{
							this.Status = "数据接收错误";
							return false;
						}
					}else{
						this.Status = "发送数据包失败";               //Status：状态信息
						return false;
					}
				}else{
					this.Status = "打开连接失败";               //Status：状态信息
					return false;
				}
			}else{
				this.Status = "文档锁定，插入失败";
				return false;
			}
		}else{
			this.Status = "非Word文档，插入失败";
			return false;
		}
    }

	/* 保存当前文档中所有的书签名称。该功能主要把当前文档中所使用的书签都保存到数据库里 */
	this.WebSaveBookMarks = function() 
	{
		var httpclient = this.obj.Http;
    	httpclient.Clear();
    	var BKCount = this.obj.ActiveDocument.Bookmarks.Count; //获取书签数量
    	for(var i = 1; i <= BKCount; i++){
    		var BookName = this.obj.ActiveDocument.Bookmarks.Item(i).Name;
    		var	BookValue =this.obj.ActiveDocument.Bookmarks.Item(BookName).Range.text;
    		this.WebSetMsgByName(BookName,BookValue); //存入书签名和值
    	}
    	this.WebSetMsgByName("OPTION", "SAVEBOOKMARKS"); 
    	this.WebSetMsgByName("TEMPLATE", this.RecordID); //在接口获取模板名称
    	httpclient.AddForm("FormData", this.GetMessageString()); //这里是自定义json 传输格式。
    	this.WebClearMessage();
    	httpclient.ShowProgressUI = this.ShowWindow;//隐藏进度条
        if(httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)){
        	if(httpclient.Send()){
        		this.Status = "书签保存成功";
        		httpclient.Clear();
        		return true;
        	}else{
        		this.Status = "数据包发送失败";
        		httpclient.Clear();
        		return false;
        	}
        }else{
        	this.Status = "打开连接失败";
    		httpclient.Clear();
    		return false;
        }
	}

	/* 打开书签管理窗口，显示书签列表，可以用于添加、删除和定位书签	*/
	this.WebOpenBookMarks = function() 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				this.obj.ActiveDocument.Application.Dialogs.Item(168).Show();
				return true;
			}
			this.Status = "文档被锁定，操作失败";
			return false;
		}
		this.Status = "非Word文档，操作失败";
		return false;
	}

	/* 取数据库内容填充文档中书签的值 */
	this.WebLoadBookMarks = function() {
		var httpclient = this.obj.Http;
    	httpclient.Clear();
    	this.WebSetMsgByName("RECORDID", this.RecordID); 			//在接口获取RecordID值
    	this.WebSetMsgByName("TEMPLATE", this.Template); 			//在接口获取模板名称
    	this.WebSetMsgByName("FILENAME", this.FileName); 			//在接口获取文件名称
    	this.WebSetMsgByName("FILETYPE", this.FileType); 			//在接口获取文件类型
    	this.WebSetMsgByName("OPTION", "LOADBOOKMARKS"); 			//在接口获取模板名称
    	httpclient.AddForm("FormData", this.GetMessageString()); 	//这里是自定义json 传输格式。
        this.WebClearMessage();                         			//清除所有WebSetMsgByName参数
        this.sendMode = "WebLoadBookMarks";
        httpclient.ShowProgressUI = this.ShowWindow;				//隐藏进度条
        if(httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)){
        	if(httpclient.Send()){
        		var ReturnValue = httpclient.GetResponseHeader("RName");// 获取返回值
        		var jsonObj = eval('(' + ReturnValue + ')'); 
        		try {
        			for(var i  in jsonObj){
        				var BookName = i;
        				var BookValue = jsonObj[i];
        				if(this.obj.ActiveDocument.BookMarks.Exists(BookName)){
        					var mBookRange = this.obj.ActiveDocument.Bookmarks.Item(BookName).Range;
        					mBookRange.text = BookValue;
        				}else{
        					this.Status = "没有找到" + BookName + "书签";
        					return false;
        				}
        			}
        		} catch (e) {
        			this.Status = e.description;
        			return false;
        		}
        		this.Status = "书签内容已插入到书签";
                return true;
        	}else{
        		this.Status = "向后台发送数据包错误";
        		return false;
        	}
        }else{
        	this.Status = "打开链接错误";
    		return false;
        }
	}

	/* 打开签名窗口，允许用户输入密码，来获取已经保存起来的印章或签名信息。确定签名后的信息将保存在服务器的数据库中以便将来验证使用*/
	this.WebOpenSignature = function() 
	{
		var FunExt = this.obj.FuncExtModule;
		FunExt.SetServerType(1);
		if(this.Charset != true){
			FunExt.SetCharset("UTF-8");					//根据后台传输数据的编码,判定是否启用,默认为gb2312
		}
		FunExt.WebUrl = this.WebUrl;
		FunExt.UserName = this.UserName;
		FunExt.FileName = this.FileName;
		FunExt.RecordID = this.RecordID;
		FunExt.FileType = this.FileType;
		FunExt.SetDocument(this.obj.ActiveDocument);
		FunExt.WebSetMsgByName("USERID","123456");
		FunExt.WebSetMsgByName("USERIDONE","123456");
		FunExt.WebSetMsgByName("USERIDTWO","123456");
		FunExt.WebOpenSignature();
		this.Status = FunExt.Status;
		return true;
	}

	/* 不打开签名或印章检验窗口，检验签名或印章是否合法有效 */
	this.WebCheckSignature = function() 
	{
		var FunExt = this.obj.FuncExtModule;
		FunExt.SetServerType(1);
		if(this.Charset != true){
			FunExt.SetCharset("UTF-8");					//根据后台传输数据的编码,判定是否启用,默认为gb2312
		}
		FunExt.WebUrl = this.WebUrl;
		FunExt.UserName = this.UserName;
		FunExt.FileName = this.FileName;
		FunExt.RecordID = this.RecordID;
		FunExt.FileType = this.FileType;
		FunExt.SetDocument(this.obj.ActiveDocument);
		var i=FunExt.WebCheckSignature();
		FunExt.Alert("检测结果："+i+"\r\n 注释: (=-1 有非法印章) (=0 没有任何印章) (>=1 有多个合法印章)");
		this.Status = FunExt.Status;
		return true;
	}

	/* 打开签名或印章检验窗口，检验签名或印章是否合法有效 */
	this.WebShowSignature = function() 
	{
		var FunExt = this.obj.FuncExtModule;
		FunExt.SetServerType(1);
		if(this.Charset != true){
			FunExt.SetCharset("UTF-8");					//根据后台传输数据的编码,判定是否启用,默认为gb2312
		}
		FunExt.WebUrl = this.WebUrl;
		FunExt.UserName = this.UserName;
		FunExt.FileName = this.FileName;
		FunExt.RecordID = this.RecordID;
		FunExt.FileType = this.FileType;
		FunExt.SetDocument(this.obj.ActiveDocument);
		FunExt.WebShowSignature();
		this.Status = FunExt.Status;
		return true;
	}

    /*下载服务器上的文件并保存在本地 */
    this.WebGetFile = function(LocalFile, RemoteFile)
    {
    	var httpclient = this.obj.Http; 							//设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("RECORDID", this.RecordID);			//加载RecordID
		this.WebSetMsgByName("LOCALFILE", LocalFile); 				//取得本地文件名称
		this.WebSetMsgByName("REMOTEFILE", RemoteFile); 			//取得远程文件名称
		this.WebSetMsgByName("OPTION", "GETFILE");     				//发送请求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); 	//这里是自定义json 传输格式。
		this.WebClearMessage();                         			//清除所有WebSetMsgByName参数
		 httpclient.ShowProgressUI = true;   						//显示进度条
		 if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) {	//这里采用同步方式接收文档数据。
			 if (httpclient.Send()) {
                 if (httpclient.GetResponseHeader("MsgError") == "404") { 	//判断服务器是否存在文件
                	 this.Status = "后台未找到对应文档"; 
                	 httpclient.Clear();
                	 return false;
                 }
                 httpclient.ResponseSaveToFile(LocalFile);
                 this.Status = "文档下载到本地成功";
                 httpclient.Clear();
                 return true;
             }else{
            	 this.Status = "数据包发送失败";            //Status：状态信息
            	 return false;
             }
         }else{
        	 this.Status = "打开连接失败";               	//Status：状态信息
        	 httpclient.Clear();
        	 return false;
         }
    }
    
    /* 将本地文件上传到服务器上，并保存为远程文件  */
    this.WebPutFile = function(LocalFile, RemoteFile)
    {
    	var httpclient = this.obj.Http; 					//设置http对象 
        httpclient.Clear();   
		this.WebSetMsgByName("REMOTEFILE", RemoteFile); 	//取得远程文件名称
		this.WebSetMsgByName("OPTION", "PUTFILE");     		//发送请求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString());
		httpclient.AddFile("FileData", LocalFile);    		//需要上传的文件 无法判断本地文档是否存在，导致上传时找不到文档情况下会生成空白文档)
		this.WebClearMessage();
		httpclient.ShowProgressUI = false;           		//隐藏进度条
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) //true 异步方式 false同步
		{
			if (!httpclient.Send()) {
				this.Status = "文档上传失败";
				httpclient.Clear();
				return false;
			}else{    
				this.Status = "文档上传成功";
				httpclient.Clear();
				return true;
			}
		}else{
			this.Status = "打开链接失败";
			httpclient.Clear();
			return false;
		}
    }
    
    /* 删除指定的本地文件或服务器上的文件 */  
    this.WebDelFile = function(LocalFile, RemoteFile)
    {
    	if(LocalFile == ""){
    		if(RemoteFile != ""){
    			var httpclient = this.obj.Http; 				//设置http对象 
    	        httpclient.Clear();   
    			this.WebSetMsgByName("REMOTEFILE", RemoteFile); //取得远程文件名称
    			this.WebSetMsgByName("OPTION", "DELFILE");     	//发送请求LOADFILE
    			httpclient.AddForm("FormData", this.GetMessageString());
    			this.WebClearMessage();
    			httpclient.ShowProgressUI = false;           	//隐藏进度条
    			if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) //true 异步方式 false同步
    			{
    				if (!httpclient.Send()) {
    					this.Status = "向后台发送数据包失败";
    					httpclient.Clear();
    					return false;
    				}else{
    					if(this.WebGetMsgByName("DelFileState") == "成功" ){
    						this.Status = "文档删除成功";
    						httpclient.Clear();
    						return true;
    					}else{
    						this.Status = "文档不存在";
    						httpclient.Clear();
    						return false;
    					}
    				}
    			}else{
    				this.Status = "打开链接失败";
    				return false;
    			}     
    		}else{
    			this.Status = "文件名为空，请输入要删除的文件名";
    			return false;
    		}
	    }else{
	    	var fs = this.obj.FileSystem;
	    	if(fs.DeleteFile(LocalFile)){
	    		this.Status = "删除本地文件成功";
	    		return true;
	    	}else{
	    		this.Status = "该文档不存在或者没有权限删除";
	    		return false;
	    	}
	    }
    }

	/* 建立新文件 */
	this.CreateFile = function() 
	{
		var docType = this.getDocType(this.FileType); 			// 获取文档类型
		switch (docType) {
		case this.DocType.WORD:
			this.obj.CreateNew("Word.Document"); 				// 创建word
			this.Status = "新建WORD成功";
			break;
		case this.DocType.EXECL:
			this.obj.CreateNew("Excel.Sheet"); 					// 创建execl
			this.Status = "新建EXECL成功";
			break;
		case this.DocType.PICTURE:
			this.obj.CreateNew("iWebPicture.iWebPictureMain"); 	// 创建tif
			this.Status = "新建TIF成功";
			break;
		case this.DocType.PPT:
			this.obj.CreateNew("PowerPoint.Show"); 				// 创建ppt
			this.Status = "新建PPT成功";
			break;
		default:
			this.obj.CreateNew("Word.Document"); 				// 默认创建word文档
			this.Status = "新建WORD成功";
			break;
		}
	}
	
	/* 使Office控件全屏 */
	this.FullSize = function(mValue) 
	{
		this.obj.FullSize = mValue;		//true为全屏,false为关闭全屏
	}

/*	 文档保存到本地（有对话框）
	this.WebSaveLocal = function() 
	{
		this.setShowDialog(this.ShowDialog.DialogSaveCopyAs);
		this.Status = "文档保存到本地成功";
	}*/
	
	this.WebSaveLocal = function(){
		var fileNamePath;
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			var exts =  "所有支持的文件格式(*.docx;*.doc)|*.docx;*.doc";
			exts += "|Word 文档(*.docx)|*.docx";
			exts += "|Word 97-2003文档(*.doc)|*.doc";
			exts += "||";
		}else if(this.FileType == ".xls" || this.FileType == ".xlsx"){
			var exts =  "所有支持的文件格式(*.xlsx;*.xls)|*.xlsx;*.xls";
			exts += "|Excel 工作簿(*.xlsx)|*.xlsx";
			exts += "|Excel 97-2003工作簿(*.xls)|*.xls";
			exts += "||";
		}
		fileNamePath  = this.obj.FileSystem.FileSaveAs(exts, this.FileName); //this.FileName 显示名称
		var ext = fileNamePath.substring(fileNamePath.length - 3,fileNamePath.length);
		if(fileNamePath == ""){
		   return true;
	    }   
		 try{
			this.obj.Save(fileNamePath, 0);
			}
			catch (e)
			{
				this.Status = e.description;
				this.obj.FuncExtModule.Alert(e.description);
				return false;
		  	}
				return true;
 	}
	
	

	/* 打开本地文档（有对话框）*/
	this.WebOpenLocal = function() 
	{
		this.setShowDialog(this.ShowDialog.DialogOpen, this.getOpenDocSuffix(this.FileType));
		this.Status = "打开本地文档成功";
	}
	

	/* 文档保存到本地 */	
	this.WebSaveLocalFile = function(FileName) 
	{
		try {
			this.WebDelLocalFile(FileName);
			if(this.FileType != ".pdf"){
				var saveState = this.Save(FileName, this.getOfficeVersion(), this.FileType.substring(1).toUpperCase());
			}else{
				var saveState = this.Save(FileName, 0, this.FileType.substring(1).toUpperCase());
			}
			return saveState;
			this.Status = "保存本地文件成功";
		} catch (e) {
			this.Status = e.description;
			this.Status = "保存本地文档失败";
			return -1;
		}
	}

	/* 打开本地文档	*/
	this.WebOpenLocalFile = function(filePath) 
	{
		if (this.getDocType(this.FileType) == this.DocType.PICTURE) {
			try {
				this.obj.ActiveDocument.WebOpenLocalFile(filePath);
				this.Status = "打开本地文档成功";
				return 0;
			} catch (e) {
				this.Status = "打开本地文档失败";
				return -1;
			}
		}
		return this.obj.Open(filePath);
	}

	/* 取得书签的值	*/
	this.WebGetBookMarks = function(BMarksName) 
	{
		if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) // 判断是否存在该书签
		{
			return this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Range.Text;
		} else {
			this.Status = "名称为" + BMarksName + "的书签在文档中不存在";
			return "";
		}
	}

	/* 光标处添加书签(新增功能)	*/
	this.WebAddBookMarks = function(BMarksName, BMarksValue) 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				if (!this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) // 判断是否存在该书签
				{
					var BMVLength = BMarksValue.length;
					this.obj.ActiveDocument.Application.Selection.TypeText(BMarksValue);// 插入内容
					this.obj.ActiveDocument.Application.Selection.MoveLeft(Unit = 1,
							Count = BMVLength);
					var StartR = this.obj.ActiveDocument.Application.Selection.Start;
					var EndR = this.obj.ActiveDocument.Application.Selection.Start + BMVLength;
					this.obj.ActiveDocument.Range(Start = StartR, End = EndR).Select();
					this.obj.ActiveDocument.Bookmarks.Add(BMarksName);// 添加书签
					this.Status = "光标处添加书签成功";
					return true;
				} else {
					this.Status = "名称为" + BMarksName + "的书签在文档中已存在";
					this.WebFindBookMarks(BMarksName);
					return false;
				}
			}
			this.Status = "文档被锁定，操作失败";
			return false;
		}
		this.Status = "非Word文档，操作失败";
		return false;
	}

	/* 书签定位	*/
	this.WebFindBookMarks = function(BMarksName) 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) // 判断是否存在该书签
				{
					var range = this.obj.Range;
					range = this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Range;
					range.Select();
					this.Status = "已定位到书签";
					return true;
				} else {
					this.Status = "名称为" + BMarksName + "的书签在文档中不存在";
					return false;
				}
			}
			this.Status = "文档被锁定，操作失败";
			return false;
		}
		this.Status = "非Word文档，操作失败";
		return false;
	}

	/* 删除书签(新增功能) */
	this.WebDelBookMarks = function(BMarksName) 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				if (this.obj.ActiveDocument.BookMarks.Exists(BMarksName)) 		// 判断是否存在该书签
				{
					this.obj.ActiveDocument.Bookmarks.Item(BMarksName).Delete();// 删除书签
					this.Status = "删除书签成功";
					return true;
				} else {
					this.Status = "名称为" + BMarksName + "的书签在文档中不存在";
					return false;
				}
			}
			this.Status = "文档被锁定，操作失败";
			return false;
		}
		this.Status = "非Word文档，操作失败";
		return false;
	}
	

	/* 将vbmValue值设置到书签vbmName位置 */
	this.WebSetBookmarks = function(vbmName, vbmValue) 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx"|| this.FileType == ".wps"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				try {
					if (this.obj.ActiveDocument.BookMarks.Exists(vbmName)) {				// 判断是否存在该书签
						var vRange = this.obj.ActiveDocument.Bookmarks.Item(vbmName).Range;
						vRange.text = vbmValue;
						this.obj.ActiveDocument.Bookmarks.Add(vbmName, vRange);
						this.Status = "书签赋值成功";
						return true;
					}
					this.Status = "名称为" + vbmName + "的书签在文档中不存在";
					return false;
				} catch (e) {
					this.Status = e.description;
					return false;
				}
			}else{
				this.Status = "插入失败，文档被锁定";
				return false;
			}
		}else{
			this.Status = "非Word文档，插入失败";
			return false;
		}
	}
	
	//设置书签字体
	this.SetBookMarksFont = function(bMarksName,fColor,fSize,fName,fBold,fItalic){
    	this.WebObject.ActiveDocument.Bookmarks.Item(bMarksName).Range.Select();
    	var Selection = this.WebObject.ActiveDocument.Application.Selection;
		Selection.Font.Color = fColor; //字体红色
    	Selection.Font.Size = fSize;  //字体大小
		Selection.Font.Name = fName; //字体类型
		Selection.Font.Bold = fBold; // 是否加粗（True加粗，False正常）
		Selection.Font.Italic = fItalic; // 是否斜体（True斜体，False正常）
    }

	/* 设置显示或隐藏工具栏				（需要修改控件代码） */
	this.WebToolsVisible = function(ToolName, Visibled) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 设置工具栏按钮是否有效			（需要修改控件代码） */
	this.WebToolsEnable = function(ToolName, ToolIndex, Enabled) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 打印文档 */
	this.WebOpenPrint = function() 
	{
		this.setShowDialog(this.ShowDialog.DialogPrint);
	}

	/* 打开插入图片窗口 */
	this.WebOpenPicture = function() 
	{
		if(this.FileType == ".doc" || this.FileType == ".docx" || this.FileType == ".wps")
		{
			var spDlg = this.obj.ActiveDocument.Application.Dialogs.Item(163);
			spDlg.Show();
		}else if(this.FileType == ".xls" || this.FileType == ".xlsx" || this.FileType == ".et")
		{
			var spDlg = this.obj.ActiveDocument.Application.Dialogs.Item(342);
			spDlg.Show();
		}
	}

	/* 使文档重新获得焦点刷新文档 */
	this.WebRefresh = function() 
	{
		this.obj.Activate(true);
	}

	/* 设置文档痕迹保留的状态 */
	this.WebSetRevision = function(Show, Track, Print, Tool) 
	{
		var strCustomUI;
		strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> '+
				 ' <ribbon startFromScratch="false"> '+
	             '   <tabs>'+
	              '      <tab idMso="TabReviewWord" visible="false">'+    //隐藏审阅工具栏
	             '     </tab> '+
	            '    </tabs> '+
	            '  </ribbon> '+
	           ' </customUI>';
		if (this.getDocType(this.FileType) == this.DocType.WORD
				&& this.obj.ActiveDocument.ProtectionType == "-1") {
			this.obj.ActiveDocument.TrackRevisions = Track; // 显示标记和隐藏标记
			this.obj.ActiveDocument.ShowRevisions = Show; // 显示痕迹或隐藏
			this.obj.ActiveDocument.PrintRevisions = Print; //控制是否打印痕迹
			if(Tool){
				this.obj.RibbonCustomUI =	strCustomUI;
				this.Status = "设置痕迹成功";
				return true;
			}else{
				this.Status = "设置痕迹成功";
				return true;
			}
		}else{
			this.Status = "非word文档，无法执行";
			return false;
		}
	}

	/* 使文档保护状态 */
	this.WebSetProtect = function(Boolean, password) 
	{
		var docType = this.getDocType(this.FileType);
		if (password == "") {
			password = this.PASSWORD;
		}
		Boolean ? this.VBAProtectDocument(docType, password) : this
				.VBAUnProtectDocument(docType, password);
	}

	/* 下载由Url指定的文件，保存为FileName路径的本地文件 */
	this.WebDownLoadFile = function(Url,FileName) 
	{
		var kwoHttpGet = 0;
		var kwoHttpPost = 1;
		var httpclient = this.obj.Http;
		httpclient.Clear();
		httpclient.ShowProgressUI = true;
		httpclient.Hidden = false;
		// 异步下载先调用OnSendEnd(),再调用OnRecEnd()
		var info = httpclient.Open(kwoHttpGet,Url, false);
		if (info) {
			var send = httpclient.Send();
			if (send) {
				if (httpclient.Status == 200) {
					httpclient.ResponseSaveToFile(FileName);
					this.Status = "下载成功";
					return true;
				}else{
					this.Status = "下载失败请检查URL是否正确";
					return false;
				}
			}else{
	        	 this.Status = "数据包发送失败";               // Status：状态信息
	        	 return false;
			}
		}else{
	    	 this.Status = "打开连接失败";               // Status：状态信息
	    	 httpclient.Clear();
	    	 return false;
		}
	}

	/* 建立本地目录	*/
	this.WebMkDirectory = function(DirName) 
	{
		var fs = this.obj.FileSystem; // 创建file对象：
		if(fs.CreateDirectory(DirName)){
			this.Status = "创建目录成功";
			return true;
		}else{
			this.Status = "创建目录失败检查是否已经有此目录或者路径不对";
			return false;
		}
	}

	/* 删除本地目录 */
	this.WebRmDirectory = function(DirName) 
	{
		var fs = this.obj.FileSystem; // 创建file对象：
		fs.ClearDirectory (DirName);  //此接口无返回值
		this.Status="删除目录成功";
		return true;
	}

	/* 判断本地文件是否存在		(需要控件修改代码) */
	this.WebFileExists = function(FileName) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 得到某文件的大小 */
	this.WebFileSize = function(FileName) 
	{
		var fs = this.obj.FileSystem; // 创建file对象：
		var fsize = fs.GetFileSize(FileName);
	    if(fsize == 0){
	    	this.Status = "请检查文件路径是否有误";
	    	return 0;
	    }else{
	    	this.Status = "获取成功大小为"+fsize+"字节";
	    	return fsize;
	    }
	}

	/* 得到某文件的修改日期		（需要控件修改代码） */
	this.WebFileDate = function(FileName) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 禁止指定菜单项 */
	this.DisableMenu = function(MenuName) 
	{
		var custommenu = this.obj.CustomMenu;
		return custommenu.DisableMenu(MenuFile, MenuName);
	}

	/* 允许菜单项有效 */
	this.EnableMenu = function(MenuName) 
	{
		var custommenu = this.obj.CustomMenu;
		return custommenu.EnableMenu(MenuFile, MenuName);
	}

	/* 禁止指定快捷键的功能		（需要控件修改代码） */
	this.DisableKey = function(KeyName) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 建立注册表对象 */
    this.WebOpenKey = function(mRoot, mValue)
    {
    	root = mRoot;
    	rootValue = mValue;
    	var register = this.obj.Register;
    	var res = register.QueryStringValue(root, rootValue, "");
    	if(!res){
    		register.SetStringValue(root, rootValue, "", "");
    		this.Status = "建立注册对象失败，已新建注册表对象";
    		return res;
    	}
    	this.Status = "建立注册对象成功";
    	return true;
    }
    
    /* 写入由Name指定的注册表项的值Value（字符串类型的值） */
    this.WebWriteString = function(mKey, mValue)
    {
    	var register = this.obj.Register;
    	if(root != undefined && rootValue != undefined){
    		var res = register.SetStringValue(root, rootValue, mKey, mValue);
    		this.Status = "写入注册表成功";
    		return res;
    	}
    	this.Status = "写入注册表失败，请先调用WebOpenKey接口";
    	return false;
    }
    
    /* 读取由Name指定的注册表项的值（字符串类型的值） */
    this.WebReadString = function(mKey)
    {
    	var register = this.obj.Register;
    	if(root != undefined && rootValue != undefined){
    		var res = register.QueryStringValue(root, rootValue, mKey);
    		if(res != null || res != ""){
    			this.Status = "读取注册表成功";
    			return res;
    		}else{
    			this.Status = "读取失败，没有对应的注册表";
    			return res;
    		}
    	}
    	this.Status = "读取注册表失败，请先调用WebOpenKey接口";
    	return false;
    }

    
    /* 写入由Name指定的注册表项的值Value */
    this.WebWriteInteger = function(mKey, mValue)
    {
    	var register = this.obj.Register;
    	if(root != undefined && rootValue != undefined){
    		var res = register.SetDWordValue(root, rootValue, mKey, mValue);
    		this.Status = "写入注册表成功";
    		return res;
    	}
    	this.Status = "写入注册表失败，请先调用WebOpenKey接口";
    	return false;
    }
    
    /* 读取由Name指定的注册表项的值Value */
    this.WebReadInteger = function(mKey)
    {
    	var register = this.obj.Register;
    	if(root != undefined && rootValue != undefined){
    		var res = register.QueryDWORDValue(root, rootValue, mKey);
    		if(res != null || res != ""){
    			this.Status = "读取注册表成功";
    			return res;
    		}else{
    			this.Status = "读取失败，没有对应的注册表";
    			return res;
    		}
    	}
    	this.Status = "读取注册表失败，请先调用WebOpenKey接口";
    	return false;
    }

	/* 盖章时自动载入日期信息附加在所盖的印章上	（需要控件修改代码） */
	this.ShowDate = function(DateString, Align) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 将文档中的印章变为黑白或彩色		（需要控件修改代码） */
	this.SignatureColor = function(Flag) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 返回当前文档中的印章或签名个数		（需要控件修改代码） */
	this.SignatureCount = function(Flag) 
	{
		this.Status = "该功能暂未提供!";
		return 0;
	}

	/* 用来定位某用户的最后一个印章。光标定位到该印章上	（需要控件修改代码） */
	this.GetSignature = function(UserName) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 接受当前文档中所有的痕迹 */
    this.ClearRevisions = function()
    {
    	if (this.getDocType(this.FileType) == this.DocType.WORD && this.obj.ActiveDocument.ProtectionType == "-1") {
    		try
    		{
    			this.obj.Activate(true);
                this.obj.ActiveDocument.AcceptAllRevisions(); 
    			this.obj.ActiveDocument.Revisions.AcceptAll();
    			this.Status = "痕迹接受成功";
    			return this.obj.ActiveDocument.Revisions.Count >= 0 ? true : false;
    		} 
    		catch(e)
    		{
    			this.Status = "痕迹接受失败，错误原因：" + e.description;
    			return false;
    		}
    	}else{
			this.Status = "痕迹接受失败，错误原因：" + e.description;
			return false;
    	}
    }
    

    /* 用于删除目录下的文件 */
    this.WebDelTree = function(Directory)
    {
    	var fs = this.obj.FileSystem;
    	fs.ClearDirectory (Directory); //此接口无返回值
    	return true;
    }

	/* 用于取得当前控件的版本号信息 */
	this.Version = function() 
	{
		return this.obj.Version;
	}

	// 用于取得当前控件的生产版本信息		（需要控件修改代码） */
	this.VersionEx = function() 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 按设置的打印份数进行打印 */
	this.PrintByCopies = function(Copies, Show) {
		parseInt(Copies);
		var mAPIObj;
    	var mApiName;
    	var mAppName;
    	mAppName = this.obj.ActiveDocument.Application.Name;
    	if(mAppName == "Microsoft Word"){
    		mAPIObj = this.obj.ActiveDocument.Application.Dialogs.Item(88);
    		if(Show){
    			mAPIObj.NumCopies = Copies;
    			if(mAPIObj.Display == -1){
    				mAPIObj.Execute;
    				this.Status = "打印成功";
    				return true;
    			}else{
    				this.Status = "启用打印机窗口失败";
    				return false;
    			}
    		}else{
    			mAPIObj.NumCopies = Copies;
    			mAPIObj.Execute;
    			this.Status = "打印成功";
    			return true;
    		}
    	}else{
    		this.Status = "文档不是word类型";
    		return false;
    	}
	}

	/* 用于获取中央处理器CUPID			（需要控件修改代码） */
	this.WebGetCpuID = function() 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 用于获取硬盘序列号IDEID			（需要控件修改代码） */
	this.WebGetIdeID = function() 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 将HTML文本生成Word可识别的格式，之后带格式可插入Word文档	（需要控件修改代码） */
	this.BuildContentFromHTML = function(HTMLText) 
	{
		this.Status = "该功能暂未提供!";
		return false;
	}

	/* 执行宏 */
	this.WebRunMacro = function(MarcroName, MacroValue) 
	{
		try {
			var VBAStr = MacroValue;
			var VBCom = this.obj.ActiveDocument.VBProject.VBComponents.Add(1);
			VBCom.CodeModule.AddFromString(VBAStr);
			this.obj.ActiveDocument.Application.Run(MarcroName);
			return true;
		} catch (e) {
			return false;
		}
	}

	/* 文档对比 */
	this.WebDocumentCompare = function(FileName1, FileName2) 
	{
		// 与当前打开的文档进行对比
		this.Status = '';
		var fs = this.obj.FileSystem; // 创建file对象：
		var filePath = this.DownFilePath(); //获取文件保存临时路劲
		var saveFilePath = filePath + Math.round(Math.random() * 100000000)
				+ '.doc';
		if ((FileName1 == null) || (FileName1 == '')) {
			if ((FileName2 == null) || (FileName2 === '')) {
				this.Status = '【FileName2】不能为空!';
				return false;
			} else {
				// 开始下载FileName2
				if (this.WebDownloadFile_int(FileName2)) {
					FileName1 = this.FileName;
					this.WebSaveLocalFile(filePath + FileName1);
					this.WebClose();
					var bRet = this.obj.FuncExtModule.WebDocumentCompare(
							filePath + FileName1,this.DownloadedFileTempPathName,
							saveFilePath);
					if (bRet == true) {
						this.obj.Open(saveFilePath);
						this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3;
						this.obj.ActiveDocument.Application.ActiveWindow.View.SplitSpecial = 20;
						return true;
					} else {
						this.Status = '文档对比失败,请确认待对比的文档是否能够正常打开!';
						return false;
					}
				} else {
					this.Status = '文件【' + FileName2 + '】下载失败，该文档可能在服务器不存在!';
					return false;
				}
			}
		}
		// 与指定的后台两个文档进行对比
		else {
			if ((FileName2 == null) || (FileName2 == '')) {
				this.Status = '【FileName2】不能为空!';
				return false;
			} else {
				// 开始分别下载指定的两个文件
				if (this.WebDownloadFile_int(FileName1)) {
					if ((this.WebDownloadFile_int(FileName2))) {
						this.WebClose();
						var bRet = this.obj.FuncExtModule.WebDocumentCompare(
								filePath + FileName1, filePath + FileName2,
								saveFilePath);
						if (bRet == true) {
							this.obj.Open(saveFilePath);
							this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3 //
							this.obj.ActiveDocument.Application.ActiveWindow.View.SplitSpecial = 20;
							return true;
						} else {
							this.Status = '文档对比失败,请确认待对比的文档是否能够正常打开!';
							return false;
						}
					} else {
						this.Status = '文件【' + FileName2 + '】下载失败，该文档可能在服务器不存在!';
						return false;
					}
				} else {
					this.Status = '文件【' + FileName1 + '】下载失败，该文档可能在服务器不存在!';
					return false;
				}
			}
		}
	}

	/* 设置控件皮肤 注:颜色格式为BGR格式*/
	this.WebSetSkin = function(titleBarColor/* 控件标题栏颜色 */,
			menuBarStartColor/* 自定义菜单开始颜色 */,
			menuBarButtonStartColor/* 自定义工具栏按钮开始颜色 */,
			menuBarButtonEndColor/* 自定义工具栏按钮结束颜色 */,
			menuBarButtonFrameColor/* 自定义工具栏按钮边框颜色 */,
			CustomToolbarStartColor/* 自定义工具栏开始颜色 */, TitleBarTextColor/* 控件标题栏文本颜色 */) {
		this.Status = '';
		var style = this.obj.Style;
		try {
			style.TitleBarColor = titleBarColor;
			if ((TitleBarTextColor == undefined) || (TitleBarTextColor == '')) // 设置默认标题文字颜色
			{
				style.TitleBarTextColor = 0x000000; // 黑色
			} else {
				style.TitleBarTextColor = TitleBarTextColor;
			}
		    titleBarColor = 0xf2f9ff;
		    CustomToolbarStartColor = 0xf2f9ff;
		    menuBarStartColor = 0xf2f9ff;
		    menuBarButtonStartColor = 0x90cbf4;
		    menuBarButtonEndColor = 0x90cbf4;
		    menuBarButtonFrameColor = 0x90cbf4;
			style.MenuBarStartColor = menuBarStartColor;
			style.MenuBarEndColor = 0xFFFFFF;
			style.MenuBarTextColor = 0x000000;
			style.MenuBarHighlightTextColor = 0x000000;
			style.MenuBarButtonStartColor = menuBarButtonStartColor;
			style.MenuBarButtonEndColor = menuBarButtonEndColor;
			style.MenuBarButtonFrameColor = menuBarButtonFrameColor;
			style.CustomToolbarStartColor = CustomToolbarStartColor;
			style.CustomToolbarEndColor = CustomToolbarStartColor;
			style.Invalidate();
			return true;
		} catch (e) {
			this.Status = '皮肤设置错误，错误信息为：' + e.description;
			return false;
		}
	}

	/* 是否启用iWebOffice对象内的拷贝功能。非控件的不受影响 */
	this.WebEnableCopy = function(mValue) 
	{
		switch (mValue) {
		case 0:
		case false:
		case "0":
			this.obj.CopyEnabled = false;
			this.Status = "禁止复制";
			break; // 启用
		case 1:
		case "1":
		case true:
			this.obj.CopyEnabled = true;
			this.Status = "允许复制";
			break; // 关闭
		default:
			;
			return;
		}
	}

	/* 显示痕迹和隐藏痕迹 */
	this.VBAShowRevisions = function(mValue) 
	{
		if (this.getDocType(this.FileType) == this.DocType.WORD
				&& this.obj.ActiveDocument.ProtectionType == "-1") {
			this.obj.ActiveDocument.TrackRevisions = mValue; 	// 显示标记和隐藏标记
			this.obj.ActiveDocument.ShowRevisions = mValue; 	// 显示痕迹或隐藏
			this.Status = "隐藏痕迹成功";
			return true;
		} else {
			this.Status = "非Office文档或文档已被锁定，无法执行操作";
			return false;
		}
	}

	/* 保护文档 */
	this.VBAProtectDocument = function(docType, password) 
	{
		if (docType == this.DocType.WORD) // word 保护模式
		{
			if (this.obj.ActiveDocument.ProtectionType == "-1") {
				this.obj.ActiveDocument.Protect(2, false, password); //第一个参数： 3可以复制  2不能复制
				this.Status = "文档已被锁定";
				return true;
			} else {
				this.Status = "文档锁定失败";
				return false;
			}
		} else if (docType == this.DocType.EXECL) // Excel
		// 保护模式，这里只保护表单1,其他的按自己需求编写
		{
			//获取sheet总量
			var count = this.obj.ActiveDocument.Application.ActiveWorkbook.Sheets.Count
			
			for (var i=1;i<count+1;i++){
				//如果有锁定的sheet就不做处理
			if (this.obj.ActiveDocument.Application.ActiveWorkbook.Sheets.Item(i).ProtectContents) // 判断表单是否是保护的
			{
				this.Status = "sheet"+i+"已锁定";
			}
				//没有锁定的就默认用我们的密码锁定
			else {
				this.obj.ActiveDocument.Application.ActiveWorkbook.Sheets.Item(i).Protect(password);
				this.Status = "文档锁定成功";
				
			}
			}
			return true;
		} else {
			this.Status = "非Office文档，无法执行锁定操作";
			return false;
		}
		
	}

	/* 根据密码解除保护 */
	this.VBAUnProtectDocument = function(docType, password) 
	{
		var docType = this.getDocType(this.FileType);
		if (docType == this.DocType.WORD) // word 保护模式
		{
			this.obj.ActiveDocument.Unprotect(password);
			this.Status = "WORD解除保护";
			return true;
		} else if (docType == this.DocType.EXECL) {
			this.obj.ActiveDocument.Application.Sheets(1).Unprotect(password);
			this.Status = "解除保护";
			return true;
		} else {
			this.Status = "非Office文档，无法执行解锁操作";
			return false;
		}
	}
	
	/* 把文档插入到指定的书签位置 */
	this.VBAInsertFile = function(Position, FileName) 
	{
		try {
			var docType = this.getDocType(this.FileType);
			if (docType == this.DocType.WORD) {
				this.obj.Activate(true);
				if (this.obj.ActiveDocument.BookMarks.Exists(Position)) {
					setTimeout(this.obj.ActiveDocument.Application.Selection.GoTo(-1, 0, 0, Position),200);
				}
				this.obj.Activate(true);
				this.obj.ActiveDocument.Application.Selection.InsertFile(FileName, "", false, false, false);
				return true;
			} else {
				this.Status = "非Office文档，无法执行插入文档操作";
				return false;
			}
		} catch (e) {
			this.Status = "插入文档失败，错误原因：" + e.description;
			return false;
		}
	}

	/* 接受本地文档中的所有痕迹 */
	this.WebAcceptAllRevisions = function(FileName) 
	{
		this.obj.FuncExtModule.WebAcceptAllRevisions(FileName);
		this.Status = "接受所有痕迹成功!";
		return false;
	}

	/* 设置WORD用户名 */
	this.VBASetUserName = function(UserName) 
	{	
		if(this.FileType != ".ppt" && this.FileType != ".pptx"){
			try {
				//this.OfficeUserName = this.obj.ActiveDocument.Application.UserName;			//保存office本身用户名以做还原用
				this.obj.ActiveDocument.Application.UserName = UserName; 					//痕迹名称
				if(this.FileType != ".xls" && this.FileType != ".xlsx"){
					this.WebObject.ActiveDocument.Application.Options.UseLocalUserInfo = true;	//设置不管是否登录Office都始终使用UserName值
					this.obj.ActiveDocument.Application.UserInitials = UserName; 				//批注名称
				}
			} catch (e) {
				this.OfficeUserName = this.obj.ActiveDocument.Application.UserName;			//保存office本身用户名以做还原用
				this.obj.ActiveDocument.Application.UserName = UserName; 					//痕迹名称
				if(this.FileType != ".xls" && this.FileType != ".xlsx"){
					this.obj.ActiveDocument.Application.UserInitials = UserName; 				//批注名称
				}
			}
		}
	}

	/* 设置域 */
	this.ShowField = function() 
	{
		try {
			this.obj.ActiveDocument.ActiveWindow.View.ShowDrawings = true;
			this.obj.ActiveDocument.ActiveWindow.View.ShowFieldCodes = false;
			return true;
		} catch (e) {
			this.Status = "设置域失败，错误原因：" + e.description;
			return false;
		}
	}

	
	/* 区域保护 */
	this.WebAreaProtect = function(BMarksName) 
	{
		if (this.FileType == ".doc" || this.FileType == ".docx") // word 保护模式
		{
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				var mMarksName = BMarksName.split(",");
				var tag = "";
				for(var i=0;i<=mMarksName.length;i++){
					if (!this.obj.ActiveDocument.BookMarks.Exists(mMarksName[i])) {
						tag += mMarksName[i];
	                    continue;
					}
					var range = this.obj.Range;
					range = this.obj.ActiveDocument.Bookmarks.Item(mMarksName[i]).Range;
					range.Select();
					this.obj.ActiveDocument.bookmarks.Item(mMarksName[i]).range.editors.add(-1); 	// 常量：wdeditoreveryone=-1
				}
				
				this.obj.ActiveDocument.Protect(3, false, "123", false, false);				// 常量：wdAllowOnlyReading=3
				this.obj.ActiveDocument.Application.Selection.MoveLeft(Unit = 1,
						Count = 1);
				this.obj.ActiveDocument.ActiveWindow.View.ShadeEditableRanges = false;		// 取消"突出显示可编辑区域"
				if(tag != ""){
					this.Status = tag + "书签名不存在";
				}else{
					this.Status = "操作成功，书签区域可编辑";
				}
				return true;
			}else{
				this.Status = "文档以被保护，请解除保护后操作!";
				return false;
			}
		} else {
			this.Status = "非Office文档，无法执行区域保护操作!";
			return false;
		}
	}

	/* 取消区域保护 */
	this.WebAreaUnprotect = function(BMarksName) 
	{
		if (this.FileType == ".doc" || this.FileType == ".docx") {
			var mMarksName = BMarksName.split(",");
			for(var i=0;i<=mMarksName.length;i++){
				if (this.obj.ActiveDocument.BookMarks.Exists(mMarksName[i])) 		// 判断是否存在该书签
				{
					try {
						this.obj.ActiveDocument.Unprotect("123");				// 解保护
						var range = this.obj.Range;
						range = this.obj.ActiveDocument.Bookmarks.Item(mMarksName[i]).Range;
						range.Select();											// 选定书签内容
						this.obj.ActiveDocument.DeleteAllEditableRanges(-1); 	// 去掉突出显示
						this.Status = "书签区域" + mMarksName[i] + "可以编辑";
						return true;
					} catch (e) {
						this.Status = "执行取消区域保护时出现错误，错误原因：" + e.description;
						return false;
					}
				} else {
					this.Status = "文档中不存在<" + mMarksName[i] + ">的书签";
					return false;
				}
			}
		} else {
			this.Status = "非Office文档，无法执行取消区域保护操作!";
			return false;
		}
	}

	/* 获取焦点 */
	this.Activate = function(blnValue) 
	{
		this.obj.Activate(blnValue);
	}

	/* 设置word的页码 */
	this.WebPageCode = function() 
	{
		this.obj.ActiveDocument.Application.Dialogs.Item(294).Show();
	}

	/* 控制2015标题栏 */		
	this.ShowTitleBar = function(mValue) 
	{
		var style = this.obj.Style;
		style.ShowTitleBar = mValue;
	}

	/* 控制2015自定义菜单栏 */	
	this.ShowCustomToolBar = function(mValue) 
	{
		var style = this.obj.Style;
		style.ShowCustomToolbar = mValue;
	}

	/* 控制2015菜单栏 */		
	this.ShowMenuBar = function(mValue) 
	{
		var style = this.obj.Style;
    	switch(mValue)
    	{
	    	case 0:
			case false:
			case "0": style.ShowMenuBar = false; break; //隐藏菜单栏
			case 1:
			case "1":
			case true: style.ShowMenuBar = true; break; //显示菜单栏
			default: ; return;
    	}
	}

	/* 控制Office工具栏 */	
	this.ShowToolBars = function(mValue)
	{
		var style = this.obj.Style;
		style.ShowToolBars = mValue;
		style.ShowToolSpace = mValue;
	}

	/* 控制2015状态栏 */		
	this.ShowStatusBar = function(mValue)
	{
		var style = this.obj.Style;
		style.ShowStatusBar = mValue;
	}
	
    /* 显示/隐藏手写签批工具栏 */
    this.ShowCustomToolbar = function(bVal)
    {
    	this.obj.Style.ShowCustomToolbar=bVal;
    }
	
	/* 显示和隐藏痕迹 隐藏痕迹时之前的痕迹不受影响 */		
	this.WebShow = function(blnValue) 
	{
		if (this.getDocType(this.FileType) == this.DocType.WORD) {
			this.VBAShowRevisions(blnValue);
		}
	}

	/* 是否启用iWebOffice对象内文档的保存功能 */
	this.SaveEnabled = function(mBoolean)
	{
		this.obj.SaveEnabled = mBoolean;
	}

	/* 是否启用iWebOffice对象内文档的打印功能 */
	this.PrintEnabled = function(mBoolean) 
	{
		this.obj.PrintEnabled = mBoolean;
	}

	/* 解决在Firefox和chrome浏览器下调用iWebPlugin崩溃崩溃的问题需要在load()函数下加如下代码 */
	this.HookEnabled = function()    
	{
		if (this.getDocType(this.FileType) == this.DocType.WORD) {
			this.obj.Style.ShowToolSpace = true;
			this.obj.SelectionInformationEnabled = false;
		}
		if (!((window.ActiveXObject != undefined)
				|| (window.ActiveXObject != null) || "ActiveXObject" in window)) {
			this.obj.HookEnabled = false;
		}
	}
	// ---------------------------------------------------对外接口-------------------------------------------------------------------//
	// ******************************************************************************************************************************


	// ******************************************************************************************************************************//
	// ---------------------------------------------------内部方法-------------------------------------------------------------------//
	
	/* 清理本地临时文件 */
    this.ClearDirectory = function()
    {
    	var fs = this.obj.FileSystem;
    	/*老版本插件临时目录*/
    	fs.ClearDirectory(this.DownFilePath());
    	fs.ClearDirectory(this.getFilePath());  //此接口无返回值
    }
    
	/* 删除本地文件 */
    this.WebDelLocalFile = function (FileName) 
    {
        var fs = this.obj.FileSystem;
        fs.DeleteFile(FileName);
    }
    
	/* 获取json格式数据包 */
	this.GetMessageString = function()
	{
		return this.ht.toString();
	}
	
	/* 设置控件Ribbon */
	this.WebSetRibbonUIXML = function(strCustomUI)
	{
		this.obj.RibbonCustomUI = strCustomUI;
	}
	
	/* 设置控件保存文档的限制 */
	this.WebSetMaxFileSize = function(mFileName)
	{
		if(this.WebFileSize(mFileName) > this.MaxFileSize*1024){
			return false;
		}else{
			return true;
		}
	}
	
	/* 设置临时目录路径(DOWN) */
	this.DownFilePath = function()
	{
		var fs = this.obj.FileSystem;					// 获取file对象
		var NewDownFilePath;
		if (this.HiddenDirectory)						//将文档保存在隐藏目录
		{
			NewDownFilePath = fs.GetSpecialFolderLocation(0x20) + this.DOWN 
				+ fs.GetFolderEncryptCode(1) + "\\" ; 	// 设置临时路径
		}
		else
		{
			NewDownFilePath = fs.GetSpecialFolderPath(0x1a) + this.DOWN; //设置临时路径
		} 
		return NewDownFilePath;
	}
	
	/* 设置临时目录路径(UP) */
	this.getFilePath = function() 
	{
		var fs = this.obj.FileSystem; 					// 获取file对象
		var filePath;
		if (this.HiddenDirectory)						//将文档保存在隐藏目录
		{
			filePath = fs.GetSpecialFolderLocation(0x20) + this.UP
				+ fs.GetFolderEncryptCode(1) + "\\"; 	// 设置临时路径
		}
		else
		{
			filePath = fs.GetSpecialFolderLocation(0x1a) + this.UP;
		}
		
		fs.CreateDirectory(filePath); 					// 创建路径
		this.FilePath = filePath; 						// 给对象赋值，方便删除和打开
		return this.FilePath;
	}
	
	/* 判断当前文档是否为空文档	*/
	this.WebSetAllowEmpty = function()
	{
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			var WebText = this.WebObject.ActiveDocument.Content.Text; //获取word文档内容
			if(WebText.length != 1){
				return true;   	//文档有内容
			}
			return false;   	//文档没容
		}
		return true;  			//不是word
	}
	
	/* 控制是否可以复制 */
	this.NewCopyType = function(mValue)
    {
    	switch(mValue)
    	{
    		case 0:
    		case false:
    		case "0": this.obj.CopyEnabled = false; break; //启用
    		case 1:
    		case "1":
    		case true: this.obj.CopyEnabled = true; break; //关闭
    		default: ; return;
    	}
    }

    /* 设置是否显示整个控件工具栏，包括OFFICE的工具栏 */  
    this.NewShowToolBar = function(mValue) 
    {
    	var style = this.obj.Style;
    	switch(mValue)
    	{
    		case false : 
    		case 0: style.ShowCustomToolbar = false; style.ShowToolBars = true; break; 		//自定义工具栏隐藏，office工具栏显示
			case true : 
    		case 1: style.ShowCustomToolbar = true; style.ShowToolBars = true; break; 		//自定义工具栏显示，office工具栏显示
    		case 2: style.ShowCustomToolbar = false; style.ShowToolBars = false; break; 	//自定义工具栏隐藏，office工具栏隐藏
    		case 3: style.ShowCustomToolbar = true; style.ShowToolBars = false; break; 		//自定义工具栏显示，office工具栏隐藏
    	}
    }

	/* 控制OFFICE另存为和保存功能 */
    this.NewUIControl = function(mValue)
    {
    	var strCustomUI;
    	switch(mValue)
    	{
    	case false: strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
			            <commands> \
			      	    	<command idMso="FileSave" enabled="false" /> \
			      	  		<command idMso="FileSaveAs" enabled="false" /> \
			            </commands> \
		    		</customUI>';break;
    	case true: strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
				        <commands> \
				  	    	<command idMso="FileSave" enabled="true" /> \
				  	  		<command idMso="FileSaveAs" enabled="true" /> \
				        </commands> \
					</customUI>';break;	
        
    	}
        this.obj.RibbonCustomUI = strCustomUI;
    }
    
	//禁用修订，更正
    //ReviewShowMarkupMenu显示标记
    //ReviewReviewingPaneMenu审阅窗格
    //ReviewTrackChangesMenu修订
    //ReviewAcceptChangeMenu接受
    //ReviewRejectChangeMenu拒绝
    //ReviewNewComment新建批注
    //ReviewDeleteCommentsMenu删除批注
    this.ModifyReview = function()
    {
	    var strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
	        <commands> \
	    	    <command idMso="ReviewReviewingPaneMenu" enabled="false"/> \
	    		<command idMso="ReviewShowMarkupMenu" enabled="false"/> \
	        	<command idMso="ReviewTrackChangesMenu" enabled="false"/> \
	        	<command idMso="ReviewRejectChangeMenu" enabled="false"/> \
				<command idMso="ReviewAcceptChangeMenu" enabled="false"/> \
	        	<command idMso="ReviewNewComment" enabled="false"/> \
	    		<command idMso="ReviewDeleteCommentsMenu" enabled="false"/> \
	        </commands> \
	      </customUI>';
	    for ( i = 1 ; i <= this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Count; i++ )
		{
			if( this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).ID == 1715 )
				this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).Enabled = false;
			if( this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).ID == 1716 )
				this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).Enabled = false;
			if( this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).ID == 2041 )
				this.obj.ActiveDocument.Application.CommandBars.Item("Track Changes").Controls.Item(i).Enabled = false;
		}
    	this.obj.RibbonCustomUI = strCustomUI;
    }
    
 	/* 禁用offcie小图标 */
    this.OfficeFastUI = function(){
		var strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
	        <commands> \
 				<command idMso="PrintPreviewAndPrint" enabled="false" /> \
	        	<command idMso="TabReviewWord" enabled="false" /> \
	  	    	<command idMso="FileSave" enabled="false" /> \
	  	  		<command idMso="FileSaveAs" enabled="false" /> \
	  	  		<command idMso="FileNewDefault" enabled="false" /> \
	  	  		<command idMso="FileOpen" enabled="false" /> \
	  	  		<command idMso="FilePrintQuick" enabled="false" /> \
	  	  		<command idMso="RedoOrRepeat" enabled="false" /> \
	  	  		<command idMso="Undo" enabled="false" /> \
	        </commands> \
		</customUI>';
 		this.obj.RibbonCustomUI = strCustomUI;
 	}
    //  startFromScratch不显示所有选项卡控制 false显示选项卡；true不显示选项卡
    //TabReviewWord关闭视图工具栏//TabInsert关闭插入工具栏//TabHome关闭开始工具栏
    this.WebSetRibbonUIXML = function(){
    	var strCustomUI = '<customUI xmlns="http://schemas.microsoft.com/office/2006/01/customui" onLoad="OnLoad" loadImage="LoadImage"> \
    		<ribbon startFromScratch="false"> \
    			<tabs> \
		    		<tab idMso="TabReviewWord" visible="false" /> \
    				<tab idMso="TabInsert" visible="false" /> \
    				<tab idMso="TabHome" visible="false" /> \
    			</tabs> \
    		</ribbon> \
    		</customUI>';
    	this.obj.RibbonCustomUI = strCustomUI;
    	/*
        最常用的内置选项卡名称
        选项卡名称      idMso（Excel）      idMso（Word）       idMso（Access）
        开始            TabHome             TabHome             TabHomeAccess
        插入            TabInsert           TabInsert           （none）
        页面布局        TabPageLayoutExcel  TabPageLayoutWord   （none）
        公式            TabFormulas         （none）            （none）
        数据            TabData             （none）            （none）
        审阅            TabReview           TabReviewWord       （none）
        创建            （none）            （none）            TabCreate
        外部数据        （none）            （none）            TabExternalData
        数据库工具      （none）            （none）            TabDatabaseTools
        iWebOffice控件的RibbonUIXML属性，是基于OFFICE2007的RibbonX的应用。关于RibbonX的相关资料，需要自己另行查询。
    */
    }
	
	/* 枚举显示系统和控件定义的相关对话框内容 */
	this.ShowDialog = 
	{
		DialogNew 		:0, 	// 新建对象
		DialogOpen 		:1, 	// 打开
		DialogSaveAs 	:2, 	// 另存为
		DialogSaveCopyAs:3, 	// 另存为拷贝
		DialogPrint 	:4, 	// 打印
		DialogPageSetup :5, 	// 打印设置
		DialogProperties:6		// 文档属性
	}

	/* 设置2015对象 */
	this.setObj = function(object)
	{
		this.obj = object;
		this.WebObject = this.obj; // 设置VBA调用对象
	}

	/* 枚举所有文档类型这里只列举word 0,execl 1 */
	this.DocType = 
	{
		WORD : 0,
		EXECL : 1,
		PPT : 2,
		PDF : 3,
		OFD : 4
	};

	/* Http对象post方式 */
	this.HttpMethod = 
	{
		Get  : 0, 		// Http对象get方式
		Post : 1
	};
	
	/* 另存为pdf文件 */
	this.SaveAsPdf = function(FilePath) 
	{
		var OfficeVersion = this.obj.ActiveDocument.Application.Version;
		if(OfficeVersion !=  "11.0"){
			if ((this.FileType == ".doc") || (this.FileType == ".docx")
					|| (this.FileType == ".wps")) {
				try {
					//this.obj.ActiveDocument.ExportAsFixedFormat(FilePath, 17,true);
					this.obj.ActiveDocument.ExportAsFixedFormat(FilePath, 17,
							false, 0, 0, 1, 1, 0, true, true, 0, true, true, true);
							this.Status = "保存PDF到本地成功";
							return true;
				} catch (e) {
					this.Status = e.description;
					this.obj.FuncExtModule.Alert("保存PDF异常,可能Office没有保存PDF功能，建议安装SaveAsPDFandXPS插件");
					return false;
				}
			}else if((this.FileType == ".xls") || (this.FileType == ".xlsx")|| (this.FileType == ".et")){
				try {
					this.obj.ActiveDocument.Application.ActiveSheet.ExportAsFixedFormat(0,FilePath,0);
							this.Status = "保存PDF到本地成功";
							return true;
				} catch (e) {
					this.Status = e.description;
					this.obj.FuncExtModule.Alert("保存PDF异常,可能Office没有保存PDF功能，建议安装SaveAsPDFandXPS插件");
					return false;
				}
			}else{
			}
		}else{
			this.Status = "保存PDF异常,Office没有保存PDF功能";
			this.obj.FuncExtModule.Alert("保存PDF异常,Office没有保存PDF功能");
		}
	}

	/* 保存为html */
	this.SaveAsHtml = function(FilePath) 
	{
		if ((this.FileType == ".doc") || (this.FileType == ".docx")
				|| (this.FileType == ".wps")) {
			try {
				var ret = this.obj.ActiveDocument.SaveAs(FilePath, 8, false,
						"", false, "", false, false, false, false, false, 0);
				this.obj.ActiveDocument.Application.ActiveWindow.View.type = 3;//3是页面视图  
				return true;
			} catch (e) {
				this.Status = e.description;
				return false;
			}
		}
	}

	/* 用来存储Http发送的表单数据 */
	this.ArrayList = function() 
	{
		this.ObjArr = {}; // 列表
		this.Count = 0; // 数量
		this.Add = function(key, value) // 添加
		{
			this.ObjArr[key] = value;
			this.Count++;
			return true;
		}

		this.Get = function(key) {
			return this.ObjArr[key];
		}

		this.Clear = function() // 清空
		{
			this.ObjArr = {};
			this.Count = 0;
		}

		// 按json格式输出
		this.toString = function() 
		{
			var newArray = new Array(); // 存储json字符串
			var i = 0;
			for ( var i in this.ObjArr) {
				newArray.push("'" + i + "':'" + this.ObjArr[i] + "'");
			}
			return "{" + newArray + "}";
		}
	}
	this.ht = new this.ArrayList();

	/* 获取打开窗口的后缀 */
	this.getOpenDocSuffix = function(fileType) 
	{
		if (fileType.length == 5) {
			fileType = fileType.substring(0, 4);
		}
		var exts;
		exts = "";
		if (this.isWPS() || !this.getOfficeVersion()) // 如果是office2003是不支持x格式的文档
		{
			exts += "*" + fileType + "x|*" + fileType + "x|";
		}
		exts += "*" + fileType + "|*" + fileType + "|";
		exts += this.getOpenSuffixName(fileType) + "(*" + fileType;
		if (this.isWPS() || !this.getOfficeVersion()) // 如果是office2003是不支持x格式的文档
		{
			exts += ";*" + fileType + "x";
		}
		exts += ")|*" + fileType;
		if (this.isWPS() || !this.getOfficeVersion()) // 如果是office2003是不支持x格式的文档
		{
			exts += ";*" + fileType + "x";
		}
		exts += "|";
		return exts;
	}
	
	/* 对比文档里面所用到的 下载文档方法 */
	this.WebDownloadFile_int = function(fn) 
	{
		this.Status = '';
		var httpclient = this.obj.Http; 					// 设置http对象
		httpclient.Clear();
		this.WebSetMsgByName("USERNAME", this.UserName); 	// 加载UserName
		this.WebSetMsgByName("FILENAME", fn); 				// 加载FileName
		this.WebSetMsgByName("FILETYPE", this.FileType); 	// 加载FileType
		this.WebSetMsgByName("RECORDID", this.RecordID); 	// 加载RecordID
		this.WebSetMsgByName("OPTION", "LOADFILE"); 		// 发送请求LOADFILE
		httpclient.AddForm("FormData", this.GetMessageString()); // 这里是自定义json传输格式。
		this.WebClearMessage(); 							// 清除所有WebSetMsgByName参数
		httpclient.ShowProgressUI = this.ShowWindow;		// 显示进度条
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) // 这里采用同步方式打开文档。
		{
			if (httpclient.Send()) {
				if (httpclient.GetResponseHeader("MsgError") == "404") // 判断服务器是否存在文件
				{
					this.Status = '文档【' + fn + '】下载失败，请确认该文档在服务器上是否存在';
					httpclient.Clear();
					return false;
				}
				httpclient.Clear();
				if (this.hiddenSaveLocal(httpclient, this, false, false, fn)) // 下载成功时
				{
					this.Status = '文档下载成功';
					return true;
				} else {
					this.Status = '文档下载失败';
					return false;
				}
			}
		}
	}
	
	/* 保存文件至服务 */
	this.SAVEFILE = function(httpclient, FileName) // 与后台发生交互OPTION值为：SAVEFILE
	{
		httpclient.AddForm("FormData", this.GetMessageString());
		httpclient.AddFile("FileData", FileName); // 需要上传的文件
		this.WebClearMessage();
		httpclient.ShowProgressUI = this.ShowWindow; // 隐藏进度条
		this.WebUrl = this.ServerUrl + this.SaveServlet;
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) //true 异步方式 false同步
		{
			if (!httpclient.Send()) {
				//this.Status = "数据包发送失败！请检查链接<" + this.WebUrl + ">是否正确或网络是否畅通。";
				this.Status = httpclient.GetResponseHeader("STATUS");
				return false;
			} else {
				this.Status = httpclient.GetResponseHeader("STATUS");
				return true;
			}
		} else {
			//this.Status = "打开链接<" + this.WebUrl + ">失败！请检查网络是否畅通。";
			this.Status = httpclient.GetResponseHeader("STATUS");
			return false;
		}
	}
	
	/* 加载服务上的文档 */
	this.LOADFILE = function(httpclient) 
	{
		this.Status = "";
		httpclient.ShowProgressUI = this.ShowWindow;
		this.WebUrl = this.ServerUrl + this.SaveServlet;
		if (httpclient.Open(this.HttpMethod.Post, this.WebUrl, false)) // true 异步方式 false同步
		{
			if (httpclient.Send()) {
				aaa = httpclient.GetResponseHeader("MsgError");
				if (aaa == "404") {
					this.CreateFile();
					this.getOfficeVersion();// 打开文档后，判断当前office版本
					httpclient.Clear();
					return true;
				}
				else if (aaa == "405") {
					this.CreateFile();
					this.getOfficeVersion();// 打开文档后，判断当前office版本
					httpclient.Clear();
					return true;
				}

				if (this.hiddenSaveLocal(httpclient, this, false, false)) {
					var mSaveResult = this
							.WebOpenLocalFile(this.DownloadedFileTempPathName);
					if (mSaveResult == 0) { // 打开本地磁盘文件
						this.getOfficeVersion();// 打开文档后，判断当前office版本
						return true;
					}else if(mSaveResult == 1){
						this.obj.FuncExtModule.Alert("可能当前授权码错误，请确认iWebOffice2015.js的授权是否正确（或乱码）!");
						this.Status = "可能当前授权码错误，请确认iWebOffice2015.js的授权是否正确（或乱码）";
						window.close();
						return false;
					}else if(mSaveResult == 2){
						this.obj.FuncExtModule.Alert("没有找到文档，请确认WebOpenLocalFile打开文档的路径是否正确!");
						this.Status = "有找到文档，请确认WebOpenLocalFile打开文档的路径是否正确";
						window.close();
						return false;
					}else if(mSaveResult == 3){
						this.obj.FuncExtModule.Alert("没有权限导致文档打开失败，请用管理员身份运行浏览器后重试!");
						this.Status = "没有权限导致文档打开失败，请用管理员身份运行浏览器后重试";
						window.close();
						return false;
					}else if(mSaveResult == 4){
						this.obj.FuncExtModule.Alert("文件可能损坏，请确定服务器文档是否已经损坏!");
						this.Status = "文件可能损坏，请确定服务器文档是否已经损坏";
						window.close();
						return false;
					}else if(mSaveResult == 5){
						this.obj.FuncExtModule.Alert("未安装Office或者注册表有损坏!");
						this.Status = "未安装Office或者注册表有损坏";
						window.close();
						return false;
					}else if(mSaveResult == 6){
						this.obj.FuncExtModule.Alert("文件被占用，请结束Office进程后重试!");
						this.Status = "文件被占用，请结束Office进程后重试";
						window.close();
						return false;
					}else {
						this.obj.FuncExtModule.Alert("打开文档时未知错误！错误码为： " + mSaveResult);
						this.Status = "打开文档时未知错误！错误码为： " + mSaveResult;
						window.close();
						return false;
					}
				} else {
					// 失败后，this.Status的值由hiddenSaveLocal返回
					this.Status = "保存文档到本地 失败";
					return false;
				}
			} else {
				this.Status = "数据包发送失败！请检查链接<" + this.WebUrl + ">是否正确或网络是否畅通。";
				return false;
			}
		} else {
			this.Status = "打开链接<" + this.WebUrl + ">失败！请检查网络是否畅通。";
			return false;
		}
	}

	/* 保存到本地 isHidden 是否隐藏文件；isInsertFile是否是插入文件 */	
	this.hiddenSaveLocal = function(httpclient, webOffice, isHidden,
			isInsertFile, OtherName) {
		try {
			this.Status = "";
			if (isHidden) {
				httpclient.Hidden = true; 		// 隐藏文件
			}
			var tempName = "";
			var fs = webOffice.obj.FileSystem; 	// WebOffice外面对象名称：
			var filePath = this.DownFilePath(); // 获取临时文件保存路径
			fs.CreateDirectory(filePath);		// 创建生成指定目录

			this.FilePath = filePath; 			// 这个保存的路径方便打开的时候再取。
			if (isInsertFile == undefined || isInsertFile == true) {
				tempName = "temp" + webOffice.RecordID;
				this.tempInsertName = tempName + this.FileName;
			}
			if (OtherName == undefined || OtherName == "") {
				OtherName = this.FileName;
				OtherName = Math.random() * 100000 + this.FileType;
			}
			this.DownloadedFileTempPathName = filePath + tempName + OtherName;
			httpclient.ResponseSaveToFile(this.DownloadedFileTempPathName);
			httpclient.Clear();
			return true;
		} catch (e) {
			return false;
		}
	}

	/* office2003的接口和其他的不一样，所有保存到本地要区分开来 */	
	this.Save = function(FileName, is2003, FileType) {
		if (this.getDocType(this.FileType) == this.DocType.PICTURE) {
			if (this.obj.ActiveDocument.WebSaveLocalFile(FileName)) {
				return 0;
			} else {
				return -1;
			}
		}
		if (is2003) {
			return this.obj.Save(FileName);
		}
		var SaveFalg = this.obj.Save(FileName, eval("this.DocTypeValue." + FileType),
				true);
		if(SaveFalg == 80 ){
			return "文件路径无效";
		}else if(SaveFalg == 81){
			return "参数无效";
		}else if(SaveFalg == 82){
			return "文件创建失败";
		}else{
			return SaveFalg;
		}
	}

	/* 判断浏览器类型 */
	this.blnIE = function() 
	{
		return (window.ActiveXObject != undefined)
				|| (window.ActiveXObject != null)
				|| ("ActiveXObject" in window)
	}

	/* 判断是否是WPS */		
	this.isWPS = function() 
	{
		return this.FileType.toUpperCase() == ".WPS"
				|| this.FileType.toUpperCase() == ".ET";
	}

	/* 获取office版本信息 */
	this.getOfficeVersion = function() 
	{
		var getVersion = 0.0;
		try {
			if (this.setVersion == -1) {
				getVersion = parseFloat(this.obj.ActiveDocument.Application.Version);
				this.setVersion = getVersion;
			} else {
				getVersion = this.setVersion;
			}
			if (getVersion == 11.0) {
				return this.OfficeVersion.v2003;
			} else {
				return this.OfficeVersion.vOther;
			}
		} catch (e) {
			return this.OfficeVersion.v2003;
		}
	}
	
	/* 获取文档类型 */		
	this.getDocType = function(fileType) {
		if (fileType == ".doc" || fileType == ".docx" || fileType == ".wps") {
			return this.DocType.WORD;
		}
		if (fileType == ".xls" || fileType == ".xlsx" || fileType == ".et") {
			return this.DocType.EXECL;
		}

		if (fileType == ".tif" || fileType == ".jpg") {
			return this.DocType.PICTURE;
		}
		if (fileType == ".ppt" || fileType == ".pptx") {
			return this.DocType.PPT;
		}
		if (fileType == ".pdf") {
			return this.DocType.PDF;
		}
		if (fileType == ".ofd") {
			return this.DocType.OFD;
		}
	}

	// 获取打开文档类型名称	
	this.getOpenSuffixName = function(fileType) 
	{
		var openSuffixName;
		switch (fileType) {
		case this.DocType.WORD:
			openSuffixName = "Word Files";
			break; // 创建word后缀名称
		case this.DocType.EXECL:
			openSuffixName = "Excel Files";
			break; // 创建execl后缀名称
		default:
			openSuffixName = "Word Files";
			break;
		}
		return openSuffixName;
	}

	/* 设置打开窗口的类型 */
	this.setShowDialog = function(thisType, exts) 
	{
		switch (thisType) {
		case this.ShowDialog.DialogOpen:
			this.obj.ShowDialog(thisType, exts, 0);break; 	// 打开本地文档
		case this.ShowDialog.DialogNew: 					// 新建对象
		case this.ShowDialog.DialogPageSetup: 				// 打印设置
		case this.ShowDialog.DialogPrint: 					// 打印
		case this.ShowDialog.DialogProperties: 				// 文档属性
		case this.ShowDialog.DialogSaveCopyAs: 				// 另存为拷贝
		case this.ShowDialog.DialogSaveAs: 					// 另存为
			this.obj.ShowDialog(thisType);break;			// 打开窗口
		default:
			break;
		}
	}

	/* 设置文档的保护状态 */
	this.setEditType = function(type) 
	{
		try {
			switch (type) {
			case 0:
			case "0":
				this.VBAProtectDocument(this.getDocType(this.FileType), "123");
				break;
			case 1:
			case "1":
				this.WebShow(false);
				break;
			case 2:
			case "2":
				this.WebShow(true);
				break;
			default:
				;
			}
		} catch (e) {
			return false;
		}
	}

	/* 返回当前编辑器 */
	this.getEditVersion = function() 
	{
		return this.obj.AppName;
	}

	/* 设置手写签批用户 */
	this.SetUser = function(username) 
	{
		this.obj.User = username;
	}
	
	/* 检测异常文件的接口 */
	this.CheckFile = function(vaul) 
	{
		this.obj.bCheckFile = vaul;
	}
	
	/* 禁用wps混合签章按钮 */
	this.EnableSignature = function(vaul)
	{
		try {
			this.obj.ActiveDocument.Application.COMAddIns
					.Item("iSignatureWord.WordApp").Connect = vaul;
		} catch (e) {
			return;
		}
	}
	
	/* --------------------------------------------水印功能 --------------------------------------------*/
	/* 添加水印 */
	this.AddWaterMark = function(WaterMarkNmae){
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.blnIE()){
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties(14);
			}else{
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties.Item(14).Value();
			}
			intPage = parseInt(intPageTotal);
			var selection = this.WebObject.ActiveDocument.Application.Selection;
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				this.DelWaterMark(WaterMarkNmae);
				for(var i=1;i<=intPage;i++){
					selection.GoTo (What = 1, Which = 1, Count = i);
					try {
						//插入水印前需更改视图样式为页眉视图
						this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9; //wdSeekCurrentPageHeader
						this.WebObject.ActiveDocument.Application.Selection.ClearFormatting(); //去页面横线
						//由于一个文档中只允许添加一个水印，因此在添加水印之前，需检测文档中是否存在水印，如果存在，则先删除
						//设置插入水印，语法：表达式.AddTextEffect(预设文字效果【0..49】, 文字内容, 字体名, 字体大小, 是否粗体, 是否斜体, 左侧位置, 顶部位置)
						selection.HeaderFooter.Shapes.AddTextEffect(0, '金格科技', '宋体', 36, false, false, 0, 0).Select();
						var shapeRange = selection.ShapeRange;
						shapeRange.Name = WaterMarkNmae+i;  				//水印对象名
						shapeRange.TextEffect.NormalizedHeight = false 		//文字效果
						shapeRange.Line.Visible = false;					//线条是否可见
						shapeRange.Fill.Visible = true;						//填充是否可见
						shapeRange.Fill.Solid();							//填充类型（本例为纯色）
						shapeRange.Fill.ForeColor.RGB = 0x0000FF; 			//设定填充的颜色RGB值
						shapeRange.Fill.Transparency = 0.5;					//设置透明度50%
						shapeRange.Rotation = -45;							//设置旋转角度
						shapeRange.LockAspectRatio = true;					//锁定纵横比
						//shapeRange.Height = WebOffice.WebObject.ActiveDocument.Application.CentimetersToPoints(2); //高度
						//shapeRange.Width = WebOffice.WebObject.ActiveDocument.Application.CentimetersToPoints(16); //宽度
						shapeRange.Height = 2 * 72;
						shapeRange.Width = 8 * 72;
						shapeRange.WrapFormat.AllowOverlap = true;			//是否允许重叠
						shapeRange.WrapFormat.Side = 3; 					//是否设置文字环绕(wdWrapNone)
						shapeRange.WrapFormat.type = 3;						//设置折回样式（本例设为不折回）
						//设置水平位置与纵向页边距关联 wdRelativeVerticalPositionMargin
						shapeRange.RelativeHorizontalPosition = 0;
						//设置垂直位置与横向页边距关联 wdRelativeVerticalPositionMargin
						shapeRange.RelativeVerticalPosition = 0;
						//水平居中  wdShapeCenter
						shapeRange.Left = -999995;
						//垂直居中
						shapeRange.Top = -999995;
						//恢复视图样式到原来样式
						this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0; //wdSeekMainDocument
						this.Status = "插入水印成功";
					}catch(e) {
						this.Status = "插入水印失败" + e.description;
						return false;
					}
				}
				return true;
			}else{
				this.Status = "文档被锁定，插入水印失败";
				return false;
			}
		}
		this.Status = "非Word文档，插入水印失败";
		return false;
	}
	
	/* 删除水印 */
	this.DelWaterMark = function(WaterMarkNmae) {
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9;
				var selection = this.WebObject.ActiveDocument.Application.Selection;
				//查找文档中是否存在名称为【WaterMarkObjectName】的水印对象，如果存在，则删除
				var WaterCount = selection.HeaderFooter.Shapes.Count;
				if ( WaterCount > 0){
					for (var i = WaterCount; i >= 1; i--){
						if (selection.HeaderFooter.Shapes.Item(i).Name = WaterMarkNmae + i){
							selection.HeaderFooter.Shapes.Item(i).Delete();
						}
					}
				}
				this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0;
				this.Status = "删除水印成功";
				return true;
			}
			this.Status = "文档被锁定，删除水印失败";
			return false;
		}
		this.Status = "非Word文档，删除水印失败";
		return false;
	}
	
	/* 插入图片水印 */
	this.AddGraphicWaterMark = function(WaterMarkNmae) {
		if(this.FileType == ".doc" || this.FileType == ".docx"){
			if(this.blnIE()){
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties(14);
			}else{
				var intPageTotal = this.WebObject.ActiveDocument.Application.ActiveDocument.BuiltInDocumentProperties.Item(14).Value();
			}
			intPage = parseInt(intPageTotal);
			var selection = this.WebObject.ActiveDocument.Application.Selection;
			if(this.obj.ActiveDocument.ProtectionType == "-1"){
				this.DelWaterMark(WaterMarkNmae);
				for(var i=1;i<=intPage;i++){
					selection.GoTo (What = 1, Which = 1, Count = i);
				try {
					this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9; //wdSeekCurrentPageHeader
					this.WebObject.ActiveDocument.Application.Selection.ClearFormatting(); //去页面横线
					
					//获取本地临时目录
					var ImagePath = this.DownFilePath();  
					//项目路径下Document的"WaterMark.jpg"
					var ImageName = "WaterMark.jpg";
					//下载图片开始 第一个参数是图片名称在Document下存在，第二个是本地临时路径
					this.DownloadToFile(ImageName,ImagePath);	
					/////插入服务器上下载下来的图片，第一个参数为本地路径图片///////
					selection.HeaderFooter.Shapes.AddPicture(ImagePath + ImageName , false, true).Select(); //水印位置
					
					var shapeRange = selection.ShapeRange;
					
					shapeRange.Name = WaterMarkNmae+i;  		//水印对象名
					shapeRange.PictureFormat.Brightness = 0.85;
					shapeRange.PictureFormat.Contrast = 0.15;
					shapeRange.LockAspectRatio = true;
					
					shapeRange.Height = 60.95 * 72; 			//高度
					shapeRange.Width = 40.63 * 72; 				//宽度
					
					shapeRange.WrapFormat.AllowOverlap = true;
					shapeRange.WrapFormat.Side = 3;				//wdWrapNone
					shapeRange.WrapFormat.Type = 3;
					shapeRange.RelativeHorizontalPosition = 0;	//wdRelativeVerticalPositionMargin
					shapeRange.RelativeVerticalPosition = 0;	//wdRelativeVerticalPositionMargin
					shapeRange.Left = -999995;					//wdShapeCenter
					shapeRange.Top = -999995;					//wdShapeCenter
					this.WebObject.ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0; //wdSeekMainDocument
					this.Status = "水印添加成功";
				}catch(e) {
					this.Status = "水印添加成功" + e.description;
					return false;
				}
			}
				return true;
			}else{
				this.Status = "文档被锁定，插入水印失败";
				return false;
			}
		}
		this.Status = "非Word文档，插入水印失败";
		return false;
	}
	
	//设置段落
	this.ParagraphSettings = function(){
    	var paragraphFormat = this.WebObject.ActiveDocument.Application.Selection.ParagraphFormat;
        paragraphFormat.Alignment = 3; //常规：对齐方式,0：左对齐，1：居中，2：右对齐，以此类推
        paragraphFormat.OutlineLevel = 10; //常规：大纲级别,值为1-10，10为正文文本，
        
        //paragraphFormat.LeftIndent =  28.3 / 2; 	//缩进：左侧，28.3等于1cm需要多少根据比例乘/除
        //paragraphFormat.RightIndent =  28.3 * 2;	//缩进：右侧，28.3等于1cm需要多少根据比例乘/除
        //paragraphFormat.FirstLineIndent = 28.3 * 2;	//缩进：缩进值，28.3等于1cm需要多少根据比例乘/除
        paragraphFormat.CharacterUnitLeftIndent = 0;//缩进：左侧，按照字符缩进
        paragraphFormat.CharacterUnitRightIndent = 0;//缩进：右侧，按照字符缩进
        paragraphFormat.CharacterUnitFirstLineIndent = 0;//缩进：缩进值，按照字符缩进
        paragraphFormat.MirrorIndents = false; //缩进：对称缩进设置false不勾选true勾选
        paragraphFormat.AutoAdjustRightIndent = true; //缩进:如果定义了文档网格，则自动调整右缩进 false不勾选true勾选
        
        //paragraphFormat.SpaceBefore = 2.5;  //间距段前 ：按照磅设置
        //paragraphFormat.SpaceBeforeAuto = false; //设置间距段前非自动，改ture的话间距段前设置无效默认为自动
        //paragraphFormat.SpaceAfter = 2.5;//间距:段后 ：按照磅设置
        //paragraphFormat.SpaceAfterAuto = false; //间距:断后非自动，改ture的话间距段后设置无效默认为自动
        paragraphFormat.LineUnitBefore = 0;//间距:段前：按照行设置
        paragraphFormat.LineUnitAfter = 0;//间距:段后：按照行设置
        paragraphFormat.LineSpacingRule = 1; // 设置行距，0：单行，1：1.5倍，2：2倍，3：最小，以此类推
        paragraphFormat.DisableLineHeightGrid = false; //间距:如果定义了文档网格，则对齐到网格 false勾选true不勾选        
        
        paragraphFormat.WidowControl = false; //分页：弧形控制 false不勾选true勾选
        paragraphFormat.KeepWithNext = false; //分页：与下段同页 false不勾选true勾选
        paragraphFormat.KeepTogether = false; //分页：段中不分页 false不勾选true勾选
        paragraphFormat.PageBreakBefore = false; //分页：段前分页 false不勾选true勾选
        
        paragraphFormat.NoLineNumber = false; //格式化例外向：取消行号 false不勾选true勾选
        paragraphFormat.Hyphenation = true; //格式化例外向：取消断字 false勾选true不勾选
        
        paragraphFormat.FarEastLineBreakControl = true;//换行：按中文习惯控制尾首字符 false不勾选true勾选
        paragraphFormat.WordWrap = true;//换行：允许西文在单词中间换行 false勾选true不勾选
        paragraphFormat.HangingPunctuation = true; //换行：允许标点溢出边界 true勾选false不勾选
        
        paragraphFormat.HalfWidthPunctuationOnTopOfLine = false; //字符间距：允许行首标点压缩true勾选false不勾选
        paragraphFormat.AddSpaceBetweenFarEastAndAlpha = true;//字符间距：自动调整中文与西文的间距 true勾选false不勾选
        paragraphFormat.AddSpaceBetweenFarEastAndDigit = true;//字符间距：自动调整中文与数字的间距 true勾选false不勾选
        paragraphFormat.BaseLineAlignment = 4; //字符间距：文本对齐方式， 0：顶端 ，1：居中，2：基线，以此类推
        this.WebObject.ActiveDocument.Application.Selection.MoveLeft(Unit =1, Count =1); //返回定位最前端 ; 
	}
	
	//清理底纹
	this.CleanBackground = function(){
		this.WebObject.ActiveDocument.Application.Selection.WholeStory(); //选中全文
		//清理文字
		var sfShading = this.WebObject.ActiveDocument.Application.Selection.Font.Shading;
        sfShading.Texture = 0;
        sfShading.ForegroundPatternColor = -16777216;
        sfShading.BackgroundPatternColor = -16777216;
        //清理段落
        var pfShading = this.WebObject.ActiveDocument.Application.Selection.ParagraphFormat.Shading;
        pfShading.Texture = 0;
        pfShading.ForegroundPatternColor = -16777216;
        pfShading.BackgroundPatternColor = -16777216;
        this.WebObject.ActiveDocument.Application.Selection.MoveLeft(Unit =1, Count =1); //返回定位最前端 ; 
	}
	
    //Word字体设置
	this.WordFontSet = function(){
		if(this.FileType == ".doc" || this.FileType == ".docx"){
	    	var sFont = this.WebObject.ActiveDocument.Application.Selection.Font;
	        sFont.Name = "微软雅黑"; //设置字体
	        sFont.Size = 10.5; //字号 
	        sFont.Bold = false; //加粗
	        sFont.Italic = false; //倾斜
	        sFont.Underline = 0; // 下划线线行
	        sFont.UnderlineColor = 0;//下划线颜色
	        sFont.StrikeThrough = false; //删除线
	        sFont.DoubleStrikeThrough = false; //双删除线 
	        sFont.Hidden = false; //隐藏
	        sFont.SmallCaps = false; //小型大写字母
	        sFont.AllCaps = false; //全部大写字母
	        sFont.Color = -16777216;//字体颜色 255红色
	        sFont.Superscript = false; //上标
	        sFont.Subscript = false; //下标
	        sFont.Spacing = 0; //磅值b
	        sFont.Scaling = 100;  //缩放 100%
	        sFont.Position = 0; //磅值y
	        sFont.DisableCharacterSpaceGrid = false; //如果定义了文档网格，则对齐到网格 false勾选，true不勾选
	        sFont.EmphasisMark = 0; //看重号,下拉框往下一个值+1
	        sFont.Ligatures = 0; //连字，下拉框往下一个值+1
	        sFont.NumberSpacing = 0;//数字间距，下拉框往下一个值+1
	        sFont.NumberForm = 0;//数字形式，下拉框往下一个值+1
	        sFont.StylisticSet = 0; //样式集 ，下拉框往下一个值+1
	        sFont.ContextualAlternates = 0; //使用上下文替换 0不勾选，1勾选
	        this.WebObject.ActiveDocument.Application.Selection.MoveLeft(Unit =1, Count =1); //返回定位最前端 ; 
        }else{
        	this.Status = "文档不是word类型";
        }
    }
	
	//删除文档链接
    this.HyperDelete = function(){
       if(this.FileType == ".doc" || this.FileType == ".docx"){
		   var selection = this.WebObject.ActiveDocument.Application.Selection; 
		   selection.WholeStory();//选中全文
		   var HyperCount = this.WebObject.ActiveDocument.Hyperlinks.Count; //获取连接数量
		   for(var i=1;i<=HyperCount;i++){
			   this.WebObject.ActiveDocument.Hyperlinks.Item(1).Delete(); //删除连接
		   }
		   selection.MoveLeft(Unit =1, Count =1); //返回定位最前端 
       }else if(this.FileType == ".xls" || this.FileType == ".xlsx"){
    	   this.WebObject.ActiveDocument.Application.Cells.Hyperlinks.Delete(); //excel删除连接
       }
    }
	
	/* ----------------------------------------------水印功能end----------------------------------------*/
	
	/* 关闭文档 */
	this.Close = function() 
	{
		if(this.obj.Close() != 0){
			return false;
		}
		return true;
	}
	// ******************************************************************************************************************************//
	// ******************************************************************************************************************************//
	// ******************************************************************************************************************************//

	/** ****以下是打开本地窗口代码***** */
	/** ****End 打开本地窗口代码***** */

	/* 2015自带有窗口的页面设置 */	
	this.WebPageSetup = function() 
	{
		this.setShowDialog(this.ShowDialog.DialogPageSetup);
	}
	
	/* 2015自带有窗口的打印设置 */	
	this.WebOpenPrint = function()
	{
		this.setShowDialog(this.ShowDialog.DialogPrint);
	}

	/* 打印预览 */
	this.PrintPreview = function() 
	{
		this.obj.PrintPreview();
	}
	
	/* 退出打印预览 */
	this.PrintPreviewExit = function() 
	{
		this.obj.PrintPreviewExit();
		this.obj.ActiveDocument.ActiveWindow.View.ShowFieldCodes = false;
	}
	
	/* 增加自定义工具栏按钮 */
    this.AppendTools = function(Index, Caption, Icon)
    {
    	parseInt(Index);
    	var customtoolbar = this.obj.CustomToolbar;
    	customtoolbar.AddToolButton(Index, Caption, Icon, Caption, 0); //Icon为图片路径
    }

	/* 按钮是否有效 */
	this.DisableTools = function(Caption, Flag) 
	{
		var customtoolbar = this.obj.CustomToolbar;
    	customtoolbar.DisableToolsButton(Caption, Flag);
    	this.obj.Style.Invalidate();
	}

	/* 自定义工具栏按钮是否显示 */
    this.VisibleTools = function(Caption, Flag)
    {
    	var customtoolbar = this.obj.CustomToolbar;
    	customtoolbar.VisibleToolsButton(Caption, Flag);
    	this.obj.Style.Invalidate();
    }
	
    /* 增加菜单 */
    var MenuFile;
    this.AppendMenu = function(Index, Caption)
    {
    	var custommenu = this.obj.CustomMenu;
    	if (MenuFile == undefined || MenuFile == null) 
    	{
    		custommenu.Clear();
    		MenuFile = custommenu.CreatePopupMenu();
    		custommenu.Add(MenuFile, "文件(&F)");
    	}
    	custommenu.AppendMenu(MenuFile, Index, false, Caption);
    	custommenu.Update();
    }
    
    this.AddCustomMenu = function () {
        var custommenu = this.obj.CustomMenu;
        //创建文件菜单的条目
        var menufile = custommenu.CreatePopupMenu();
        var menufilelv2 = custommenu.CreatePopupMenu();
        custommenu.AppendMenu(menufilelv2, 6, false, "自定义二级菜单一");
        custommenu.AppendMenu(menufilelv2, 7, false, "自定义二级菜单二");
        custommenu.AppendMenu(menufilelv2, 0, false, "-");
        var menufilelv3 = custommenu.CreatePopupMenu();
        custommenu.AppendMenu(menufilelv3, 8, false, "自定义三级菜单一");
        custommenu.AppendMenu(menufilelv3, 0, false, "-");
        custommenu.AppendMenu(menufilelv3, 9, false, "自定义三级菜单二");

        custommenu.AppendMenu(menufilelv2, menufilelv3, true, "自定义三级菜单");

        custommenu.AppendMenu(menufilelv2, 10, false, "自定义二级菜单三");
        custommenu.AppendMenu(menufile, menufilelv2, true, "自定义二级菜单");
        //将文件菜单添加到顶级主菜单
        custommenu.AppendMenu(menufile, 17, false, "启用保存");
        custommenu.AppendMenu(menufile, 18, false, "禁止保存");
        custommenu.AppendMenu(menufile, 0, false, "-");
        custommenu.AppendMenu(menufile, 19, false, "启用打印");
        custommenu.AppendMenu(menufile, 20, false, "禁止打印");
        custommenu.Add(menufile, "编辑(&E)");

        //创建语言
      /*  var menuLang = custommenu.CreatePopupMenu();
        custommenu.AppendMenu(menuLang, 22, false, "简体中文");
        custommenu.AppendMenu(menuLang, 23, false, "繁体中文(TW)");
        custommenu.AppendMenu(menuLang, 24, false, "繁体中文(HK)")
        custommenu.AppendMenu(menuLang, 25, false, "英文");
        custommenu.Add(menuLang, "多语言(&N)");*/

        //通知系统更新菜单
        custommenu.Update();
    }

	/* 获取当前文档打开类型，以后缀名来区别 */		
	this.WebGetDocSuffix = function() 
	{
		try {
			var docType = this.getDocType(this.FileType); 	// 判断是文档还是表格
			var FileTypeValue = 0; 							// 判断打开文档的值 0：doc，12：docx，51：xls，56：xlsx
			if (docType == this.DocType.WORD) 				// word 获取vba值的方法
			{
				this.Activate(true);
				FileTypeValue = this.obj.ActiveDocument.SaveFormat;
			}
			if (docType == this.DocType.EXECL) { 			// Execl 获取方法
				this.obj.ExitExcelEditMode(); 				// 退出当前编辑模式
				FileTypeValue = this.obj.ActiveDocument.FileFormat;
				if (FileTypeValue < 0) { 					// 2003不支持该属性
					FileTypeValue = 56;
				}
			}
			return this.DocSuffixType[FileTypeValue];
		} catch (e) {
			return this.FileType;
		}
	}
	
	/* iWebOffice打开的文档全路径 */
	this.WebFullName = function() 
	{
		return this.obj.FullName;
	}

	/* 书签插入图片 */
	this.InsertImageByBookMark = function(Transparent,ZOrder) {
		if(this.obj.ActiveDocument.BookMarks.Exists(this.BookMark)){
        	this.obj.ActiveDocument.Bookmarks.Item(this.BookMark).Select();
        }
		var SelectionImage = this.obj.ActiveDocument.Application.Selection.InlineShapes.AddPicture(this.DownloadedFileTempPathName);
		SelectionImage.Select();
		SelectionImage.PictureFormat.TransparentBackground = Transparent; //是否透明处理
		var ShapeImage = SelectionImage.ConvertToShape(); 	// 转为浮动型
		ShapeImage.WrapFormat.Type = 3; 
		ShapeImage.ZOrder(ZOrder); 							// 5：衬于文字下方 4：浮于文字上方
		return true;
	}

	/* 下载文档	*/
	this.DownloadToFile = function(DownFileName, SavePathName) 
	{
		var httpclient = this.obj.Http;
		var URL = this.WebUrl.substring(0, this.WebUrl.lastIndexOf("/"));
		httpclient.ShowProgressUI = this.ShowWindow;// 隐藏进度条
		if (httpclient.Open(this.HttpMethod.Get, URL + "/Document/"
				+ DownFileName, false)) {			// 指定下载模板的名称
			if (httpclient.Send()) {
				if (httpclient.Status == 200) {
					httpclient.ResponseSaveToFile(SavePathName + DownFileName);
					httpclient.Clear();
					return true;
				}
			}
		}
		httpclient.Close();
		return false;
	}

	/* 手写签批	*/
	this.HandWriting = function(penColor, penWidth) 
	{
		var handwritting = this.obj.Handwritting;
		var handsetting = handwritting.DrawingSetting;
		handsetting.PenThicker = penWidth;
		handsetting.PenColor = penColor;
		handwritting.AnnotateDraw();
		this.ShowMenuBar(false);	// 签批时隐藏菜单栏
		this.ShowToolBars(false);	// 签批时隐藏工具栏
	}
	
	/* 停止手写签批	*/
	this.StopHandWriting = function() 
	{
		var handwritting = this.obj.Handwritting;
		handwritting.StopAnnotate();
		this.ShowMenuBar(true);		// 停止签批时显示菜单栏
		this.ShowToolBars(true);	// 停止签批时显示工具栏
	}
	
	/* 文字签名	*/
	this.TextWriting = function() 
	{
		var handwritting = this.obj.Handwritting;
		var textsetting = handwritting.TextSetting;
		textsetting.TextSize = 32;
		textsetting.TextColor = 0xbb00ff;
		textsetting.FontName = "宋体";
		handwritting.AnnotateText();
		this.ShowMenuBar(false);	// 签批时隐藏菜单栏
		this.ShowToolBars(false);	// 签批时隐藏工具栏
	}
	
	/* 图形签批	*/
	this.ShapeWriting = function() 
	{
		var handwritting = this.obj.Handwritting;
		var shapesetting = handwritting.ShapeSetting;
		shapesetting.ShapeType = 0;
		shapesetting.BackgroundColor = 0xffffff;
		shapesetting.BorderColor = 0xff0000;
		shapesetting.BorderWidth = 6;
		handwritting.AnnotateShape();
		this.ShowMenuBar(false);	// 签批时隐藏菜单栏
		this.ShowToolBars(false);	// 签批时隐藏工具栏
	}
	
	/* 取消上一次签批 */
	this.RemoveLastWriting = function() 
	{
		var handwritting = this.obj.Handwritting;
		handwritting.RemoveLast();
	}
	
	/* 显示某用户的签批 */
	this.ShowWritingUser = function(bVal, username) 
	{
		var strxml = this.obj.GetAnnotations();
		var json = eval('(' + strxml + ')');
		if (username != "" && username != null && username != undefined) {
			for (var i = 0; i < json.Annotations.length; i++) {
				if (json.Annotations[i].Annotation.User != username) {
					var id = json.Annotations[i].Annotation.ID;
					this.obj.GetAnnotationByID(id).Visible = bVal;
				}
			}
		} else {
			for (var i = 0; i < json.Annotations.length; i++) {
				var id = json.Annotations[i].Annotation.ID;
				this.obj.GetAnnotationByID(id).Visible = bVal;
			}
		}
	}
	
	/* 得到服务器setMsgByName的值并发送到前台 */
	this.GetDataToSend= function()
	{
		var httpclient = this.obj.Http; 						// 设置http对象
		httpclient.Clear();
		var ReturnValue = httpclient.GetResponseHeader("RName");// 获取返回值
		var jsonObj = eval('(' + ReturnValue + ')');    
		for(var i  in jsonObj){
			this.WebSetMsgByName(i,jsonObj[i]);
		}
	}
	
	//手写签批控件属性和方法
	this.IWR = null;
	/* 创建手写签批控件对象 */
	this.CreateRevision = function()
	{
		//判断iWebOffice2015对象是否存在
		if(this.obj == null){
			this.Status = "iWebOffice2015对象未初始化";
			return false;
		}
		//判断手写签批控件是否已创建
		if(this.IWR != null){
			this.Status = "手写组件对象已存在";
			return false;
		}
		//创建手写签批控件
		var ret = this.obj.CreateNew("iWebRevision.iWebRevisionCtrl.1");
		if(ret != 0){
			this.Status = "创建手写组件对象失败";
			return false;
		}
		//隐藏标题栏、状态栏、菜单栏、自定义工具栏
		this.ShowTitleBar(false);
		this.ShowMenuBar(false);
		this.ShowStatusBar(false);
		this.ShowCustomToolbar(false);
		this.obj.Style.BorderStyle = 0;
		//获取手写控件对象
		this.IWR = this.obj.Application;
		return true;
	}
	
	/* 解决打印预览里打印按钮没反应问题 */
    this.OleDialogPrint =function(OLEFlag, bCancel){
    	 if (OLEFlag == 1) {
    		 this.obj.ShowDialog(0);	//新建文档
    	    }
    	    else if (OLEFlag == 2) {
    	        var exts;
    	        exts =  "Word Files(*.doc;*.docx;*.docm;*.dot;*.dotx;*.dotm;*.rtf)|*.doc;*.docx;*.docm;*.dot;*.dotx;*.dotm;*.rtf";
    	        exts += "|Excel Files(*.xls;*.xlsx;*.xlsm;*.xlt;*.xltx;*.xltm)|*.xls;*.xlsx;*.xlsm;*.xlt;*.xltx;*.xltm";
    	        exts += "|PowerPoint Files(*.ppt;*.pptx;*.pptm;*.pot;*.potx;*.potm)|*.ppt;*.pptx;*.pptm;*.pot;*.potx;*.potm";
    	        exts += "|Project Files(*.mpp;*.mpt)|*.mpp;*.mpt";
    	        exts += "|Visio Files(*.vsd;*.vdx;*.vss;*.vsx;*.vst;*.vtx;*.vsw;*.vdw;*.vsdx;*.vssx;*.vstx;*.vsdm;*.vssm;*.vstm)|*.vsd;*.vdx;*.vss;*.vsx;*.vst;*.vtx;*.vsw;*.vdw;*.vsdx;*.vssx;*.vstx;*.vsdm;*.vssm;*.vstm";
    	        exts += "|All File(*.*)|*.*";
    	        exts += "||";
    	        this.obj.ShowDialog(1, exts, 0);//打开本地文档(弹窗)
    	    }
    	    else if (OLEFlag == 4) {
    	    	this.obj.ShowDialog(2);	//另存为本地文档
    	    }
    	    else if (OLEFlag == 8) {
    	    	this.obj.ShowDialog(3);	//另存为拷贝
    	    }
    	    else if (OLEFlag == 16) {
    	    	this.obj.ShowDialog(4);	//打印
    	    }
    	    else if (OLEFlag == 32) {
    	    	this.obj.ShowDialog(5);	//打印设置
    	    }
    	    else if (OLEFlag == 64) {
    	    	this.obj.ShowDialog(6);	//文档属性
    	    }
    	    else if (OLEFlag == 128) {
    	        this.obj.ActiveDocument.PrintPreview();	//打印预览
    	    }
    }
    
    //避免谷歌浏览器Alert被插件挡住
    this.Alert = function(value){
    	 this.obj.FuncExtModule.Alert(value);
    }
    
    //隐藏插件避免在非ie浏览器弹出窗口被挡住问题。 0隐藏，1显示
    this.HidePlugin = function(value){
    	if(!this.blnIE()){
		 this.obj.HidePlugin(value);
    	}
    }
   
}


//////////////////////////////////////////////////////////////////////////////KGBrowser
function basePath() {
    //获取当前网址，如： http://localhost:8080/ems/Pages/Basic/Person.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： /ems/Pages/Basic/Person.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    var localhostPath = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/ems
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    //获取项目的basePath   http://localhost:8080/ems/
    var basePath = localhostPath + projectName + "/";
    return basePath;
};

/*
* url: 相对地址  : /sampes/OpenAndSave/word.jsp
* param: 跟随在url后面的get形式的参数： ?key1=value1&key2=value2&key3=value3
* onlyONE: true:如果有已经打开的弹出窗口就返回提示信息，并终止本次弹窗; false: 
*/	
function KGBrowser() 
{
    var kgbrowser = this;
    this.uuid = "";
  
    this.getUUID = function()
    {
    	if(this.uuid=="")
    		this.uuid = this.kg_uuid(16,32);
    	return this.uuid;
    }
    
    this.openWindowSPE = function(url, param, onlyONE) {   // speed up 不做安装检测 , 好像效果不明显

        if (onlyONE == undefined)
            onlyONE = true;

	    //var strParam = '$parent_uuid=' + kgbrowser.kg_uuid;
	    //strParam += param;
	    
        var link = "KGBrowser://$link:" + basePath() + url + param;
        //	alert("link: " + link);
        location.href = link;
        this.LongConnect();

    }

    this.openWindow = function(url, param, onlyONE)
    {
	    if(onlyONE == undefined)
		    onlyONE = true;
	
	    //var strParam = '$parent_uuid=' + kgbrowser.kg_uuid;
	    //strParam += param;
	    
	    // 检测KGBrowser是否安装
	    $.ajax({
		    type: "get",
		    async: false,
		    url: "http://127.0.0.1:9588/QueryOpen", //此代码ip固定，端口号与Edit页面该方法一致，其他固定。
		    jsonp: "hookback",
		    dataType: "jsonp",
		    success: function (data) {
			    var jsonobj = eval(data);
			    if (jsonobj.ret == "none" || onlyONE == false) 
			    {
				    var  link = "KGBrowser://$link:" + basePath() + url + param;
			    //	alert("link: " + link);
				    location.href = link;
				    kgbrowser.LongConnect();
			    }
			    else
			    {
				    //alert("有已打开的金格浏览器!");
			    }
		    },
		    error: function(){  
		    	var answer = confirm("未安装支持多浏览器应用程序是否点击安装\n提示：安装的时候360全部点击允许程序所有操作");
                if (answer) {//判断是否点击确定
                	 var curPath = window.document.location.href;
                     var pathName = window.document.location.pathname;
                     var pos = curPath.indexOf(pathName);
                     var localhostPath = curPath.substring(0, pos);
                     var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
                     var webPath = localhostPath + projectName;
                     window.location.href = webPath + "/KGBrowserSetup.exe"; //安装KGBrower
                } 
		    }
	    });
	    // ie 8+, chrome and some other browsers
	    var head = document.head || $('head')[0] || document.documentElement;// code from jquery
	    var script = $(head).find('script')[0];
	    script.onerror = function(evt) 
	    { 
	    	var answer = confirm("未安装支持多浏览器应用程序是否点击安装\n提示：安装的时候360全部点击允许程序所有操作");
            if (answer) {//判断是否点击确定
                var curPath = window.document.location.href;
                var pathName = window.document.location.pathname;
                var pos = curPath.indexOf(pathName);
                var localhostPath = curPath.substring(0, pos);
                var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
                var webPath = localhostPath + projectName;
                window.location.href = webPath + "/KGBrowserSetup.exe"; //安装KGBrower
            } 
		    // do some clean  
		    // delete script node  
		    if (script.parentNode) 
		    {     
			    script.parentNode.removeChild(script);
		    }
		    // delete jsonCallback global function
		    var src = script.src || '';  
		    var idx = src.indexOf('hookback='); 
		    if (idx != -1) 
		    {
			    var idx2 = src.indexOf('&');
			    if (idx2 == -1)
			    {     
				    idx2 = src.length;
			    }      
			    var hookback = src.substring(idx + 13, idx2);
			    delete window[hookback];
		    }
	    }; 							
    }


    this.LongConnect = function () {
    	var strUrl = "http://127.0.0.1:9588/LongListen?id=" + kgbrowser.uuid;
        $.ajax({
            type: "get",
            async: false,
            url: strUrl, //此代码ip固定，端口号与Edit页面该方法一致，其他固定。
            jsonp: "hookback",
            dataType: "jsonp",
            success: function (data) {
                var jsonobj = eval(data);
                console.log(jsonobj.ret);
                if (jsonobj.ret == "save") { //此判断处理Edit页面Msg传过来的值，判断之后下面做响应处理即可
                    //alert("save");
                    setTimeout("location.reload();", 100);
                }
                else if (jsonobj.ret == "returnlist") { //此判断处理Edit页面Msg传过来的值，判断之后下面做响应处理即可
                    //alert("returnlist");
                    setTimeout("location.reload();", 100);
                }
                else if (jsonobj.ret == "none" || jsonobj.ret == "unload") {
                    kgbrowser.LongConnect();    //这里一定要调用，不可删除
                }
                else {
                    //console.log(data);
                    console.log(jsonobj.ret);
                    //var jsonobj2 = eval('(' + jsonobj.ret + ')');
                    var jsonobj2 = eval(jsonobj.ret);
                    console.log(jsonobj2.action);
                    console.log(jsonobj2.func);
                    if (jsonobj2.action == "save") {
                        setTimeout("location.reload();", 100);
                    }
                    else if (jsonobj2.action == "CallParentFunc") {
                       // alert(jsonobj2.action);
                        //alert(jsonobj2.func);
                        eval(jsonobj2.func);
                        kgbrowser.LongConnect();
                    }
                    else if(jsonobj2.action == "ReturnList"){
                       // alert(jsonobj2.action);
                    	setTimeout("location.reload();", 100);
                    }
                }
            },
            error: function (a, b, c) {
            }
        });

    }
    
    this.kg_uuid = function (len, radix) {
    	  var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    	  var uuid = [], i;
    	  radix = radix || chars.length;
    	 
    	  if (len) {
    	   // Compact form
    	   for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
    	  } else {
    	   // rfc4122, version 4 form
    	   var r;
    	 
    	   // rfc4122 requires these characters
    	   uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
    	   uuid[14] = '4';
    	 
    	   // Fill in random data. At i==19 set the high bits of clock sequence as
    	   // per rfc4122, sec. 4.1.5
    	   for (i = 0; i < 36; i++) {
    	    if (!uuid[i]) {
    	     r = 0 | Math.random()*16;
    	     uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
    	    }
    	   }
    	  }
    	 
    	  return uuid.join('');
    	}
}