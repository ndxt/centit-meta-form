define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var MdTablePublish = Page.extend(function() {
		
		// @override
		this.load = function(panel, data) {
			Core.ajax(Config.ContextPath+'service/metaform/mdtable/beforePublish/'+data.tableId, {
				type: 'json',
				method: 'POST' 
			}).then(function(data) {
				var sqlArr = data.objList;
				var sqls="";
				if(sqlArr!=null && sqlArr.length>0){
					for(var i=0;i<sqlArr.length;i++){
						sqls+=sqlArr[i]+';</br>';
					}
					panel.find('#sqlstr').html(sqls);
				}else{
					panel.find('#sqlstr').html("无");
					
				}
			});
		};
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'service/metaform/mdtable/publish/'+data.tableId, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'POST'
                }
			}).then(function() {
				$('#mdtable').datagrid('reload');
            });
		};
	});
	
	return MdTablePublish;
});