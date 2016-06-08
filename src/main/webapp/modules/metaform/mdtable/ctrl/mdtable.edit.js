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
			});
			
			
			panel.find('#mdtable_sub').click(function(){
				var form = panel.find('form');
				// 开启校验
				form.form('enableValidation');
				var isValid = form.form('validate');
				var mdtable_columns=panel.find("#mdtable_columns");
				var mdtablecolumns=mdtable_columns.datagrid('getRows');
				var mdtable=form.form('value');
				$.extend(mdtable,{mdColumns:mdtablecolumns})
				if (isValid) {
					
					$.ajax({
						type: 'put',
					    url:  Config.ContextPath + 'service/metaform/mdtable/draft/' + data.tableId,
					    data: JSON.stringify(mdtable) ,
					    success: function(data){
							$.messager.alert('提示', '已保存！', 'info');
							var parentTable=_self.parent.panel.find('#mdtable');
							parentTable.datagrid('reload');
						},
					    dataType: 'application/json',
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