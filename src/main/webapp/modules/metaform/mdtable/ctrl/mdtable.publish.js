define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var MdTablePublish = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'service/metaform/mdtable/publish/'+data.tableId, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'POST'
                }
			}).then(function() {
				table.datagrid('reload');
            });
		}
	});
	
	return MdTablePublish;
});