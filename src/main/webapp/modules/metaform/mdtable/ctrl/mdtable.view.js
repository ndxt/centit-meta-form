define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var MdTableView = Page.extend(function() {
		
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var mdtable_columns=panel.find("#mdtable_columns_view");
			mdtable_columns.cdatagrid({
				controller:_self
			})
			Core.ajax(Config.ContextPath+'service/metaform/mdtable/draft/'+data.tableId, {
				type: 'json',
				method: 'get' 
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('focus');
				mdtable_columns.datagrid('loadData',data.mdColumns);
			});
		};
	});
	
	return MdTableView;
});