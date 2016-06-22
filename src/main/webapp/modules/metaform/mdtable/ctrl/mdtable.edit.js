define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var MdTableAdd = require('./mdtable.add');

	var MdTableEdit = MdTableAdd.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			form.form('disableValidation');
			var mdtable_columns=panel.find("#mdtable_columns");
			mdtable_columns.cdatagrid({
				controller:_self
			})
			
			var mdtable_subtables=panel.find("#mdtable_subtables");
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
					.form('readonly', 'tableId')
					.form('focus');
				mdtable_columns.datagrid('loadData',data.mdColumns);
				mdtable_subtables.datagrid('loadData',data.mdRelations);
			});
			
			
			panel.find('#mdtable_sub').click(function(){
				var form = panel.find('form');
				// 开启校验
				form.form('enableValidation');
				var isValid = form.form('validate');
				var mdtable=form.form('value');
				var mdtable_columns=panel.find("#mdtable_columns");
				var mdtablecolumns=mdtable_columns.datagrid('getRows');
				var mdtable_subtables=panel.find("#mdtable_subtables");
				var mdtablesubtables=mdtable_subtables.datagrid('getRows');
				
				
				$.extend(mdtable,{mdColumns:mdtablecolumns,mdRelations:mdtablesubtables})
				if (isValid && mdtable_columns.cdatagrid('endEdit') && mdtable_subtables.cdatagrid('endEdit')) {
					
					$.ajax({
						type: 'put',
					    url:  Config.ContextPath + 'service/metaform/mdtable/draft/' + data.tableId,
					    data: JSON.stringify(mdtable) ,
					    success: function(data){
							$.messager.alert('提示', '已保存！', 'info');
							var parentTable=_self.parent.panel.find('#mdtable');
							parentTable.datagrid('reload');
						},
					    contentType:'application/json'

					});
				}
			});
		};
		
		
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return MdTableEdit;
});