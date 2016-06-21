define(function(require) {
	var Config = require('config');
	var Page = require('core/page');
	var MdtableColumnAdd=require("./mdtable.column.add");
	var MdtableColumnRemove=require("./mdtable.column.remove");
	var Core = require('core/core');
	
	var MdTableAdd = Page.extend(function() {
		
		var _self=this;
		// @override
		this.injecte([
		              new MdtableColumnAdd('mdtable_column_add'),
		              new MdtableColumnRemove('mdtable_column_remove')
		        	]);
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var mdtable_columns=panel.find("#mdtable_columns");
			mdtable_columns.cdatagrid({
				controller:_self
			});
			
			// 
			form.form('disableValidation')
			.form('focus');
			mdtable_columns.datagrid('loadData',[]);
			
			panel.find('#mdtable_sub').click(function(){
				var form = panel.find('form');
				// 开启校验
				form.form('enableValidation');
				var isValid = form.form('validate');
				var mdtable_columns=panel.find("#mdtable_columns");
				var mdtablecolumns=mdtable_columns.datagrid('getRows');
				var mdtable=form.form('value');
				$.extend(mdtable,{mdColumns:mdtablecolumns})
				if (isValid && mdtable_columns.cdatagrid('endEdit')) {
					$.ajax({
						type: 'POST',
					    url: Config.ContextPath + 'service/metaform/mdtable/draft/' ,
					    data: JSON.stringify(mdtable) ,
					    success: function(data){
							$.messager.alert('提示', '已保存！', 'info');
							var parentTable=_self.parent.panel.find('#mdtable');
							parentTable.datagrid('reload');
						},
					    contentType:'application/json'

					});
					/*Core.ajax(Config.ContextPath + 'service/metaform/mdtable/draft/', {
						type:'json',
						method:'post',
						data: mdtable,
					}).then(function(data){
						$.messager.alert('提示', '已保存！', 'info');
						var parentTable=_self.parent.panel.find('#mdtable');
						parentTable.datagrid('reload');
					});*/
				}
	    	});
			
			
			
		};
		
		
		// @override 
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};
	});
	
	return MdTableAdd;
});