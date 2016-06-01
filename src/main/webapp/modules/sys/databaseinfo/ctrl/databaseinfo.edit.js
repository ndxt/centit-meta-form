define(function(require) {
	var Core = require('core/core');
	var Page = require('core/page');
	var Config = require('config');
	var DatabaseInfoAdd = require('../ctrl/databaseinfo.add');
	
	// 编辑角色信息
	var DatabaseInfoEdit = DatabaseInfoAdd.extend(function() {
		var _self = this;
	
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath + 'service/sys/databaseinfo/' + data.databaseCode, {
            	type: 'json',
                method: 'get',
			}).then(function(data) {
				
				_self.data = $.extend(_self.object, data);
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'databaseUrl')
					//.form('readonly', 'password')
					.form('focus');
            });
			var formUrl=function(){
				var value=form.form('value');
	            var hostPort = $.trim(panel.find("#hostPort").textbox('getValue'));
	            if(null!=hostPort && ""!=hostPort)
	            if(!/:\d{1,5}$/.test(hostPort)) {
	                
	                alert("主机和端口号规则不正确");
	                return;
	            }


	            var databaseNames =value.databaseNames;
	            var databaseType = value.databaseType;
	            var databesrurl = null;
	            if (databaseType == '1') {
	                databesrurl = "jdbc:sqlserver://" + hostPort + ";databaseName=" + databaseNames;
	            }
	            if (databaseType == '2') {
	                databesrurl = "jdbc:oracle:thin:@" + hostPort + ":" + databaseNames;
	            }
	            if (databaseType == '3') {
	                databesrurl = "jdbc:db2://" + hostPort + "/" + databaseNames;
	            }
	            if (databaseType == '5') {
	                databesrurl = "jdbc:mysql://" + hostPort + "/" + databaseNames+"?useUnicode=true&characterEncoding=utf8";
	            }
	            panel.find('#databaseUrl').textbox('setValue',databesrurl);
			}
			
			panel.find("#databaseNames").textbox({onChange:function () {
				formUrl();
	        }});
			panel.find("#databaseType").combobox({onChange:function () {
				var hostPort = $.trim(panel.find("#hostPort").textbox('getValue'));
				var databaseNames = panel.find("#databaseNames").textbox('getValue')
				if(hostPort!="" && databaseNames!="")
					{
						formUrl();
					}
	        }});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			var isValid = form.form('enableValidation').form('validate');
			var value=form.form('value');
			//value.dataDesc=value.dataDesc.replace(/\+/g,"%2B");
			//value.dataDesc=value.dataDesc.replace(/\&/g,"%26");
			$.extend(data,value,{_method:'put',contentType:'application/json'});
			if (isValid) {
				Core.ajax(Config.ContextPath + 'service/sys/databaseinfo/' + data.databaseCode, {
	            	type: 'json',
	            	method:'POST',
	            	data:data
				}).then(closeCallback);
				/*form.form('ajax', {
					url: Config.ContextPath + 'service/sys/databaseinfo/' + data.databaseCode,
					method: 'put',
					data: data,
				})*/
			}
			
			return false;
		};

	});
	
	return DatabaseInfoEdit;
});