define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var MdtableSubtableDetailsView=require("./mdtable.subtable.details.view");
	var MdTableView = Page.extend(function() {
		
		var _self = this;
		this.injecte([
		              new MdtableSubtableDetailsView('mdtable_subtable_details_view')
		        	]);
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var mdtable_columns=panel.find("#mdtable_columns_view");
			mdtable_columns.cdatagrid({
				controller:_self
			})
			
			var mdtable_subtables=panel.find("#mdtable_subtables_view");
			mdtable_subtables.cdatagrid({
				controller:_self
			});
			Core.ajax(Config.ContextPath+'service/metaform/mdtable/draft/'+data.tableId, {
				type: 'json',
				method: 'get' 
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('focus');
				mdtable_columns.datagrid('loadData',data.mdColumns);
				mdtable_subtables.datagrid('loadData',data.mdRelations);
			});
		};
	});
	
	return MdTableView;
});