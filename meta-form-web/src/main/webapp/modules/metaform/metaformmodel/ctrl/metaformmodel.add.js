define(function(require) {
	var Config = require('config');
	var Page = require('core/page');
	var DatafieldAdd=require("./metaform.datafield.add");
	var DatafieldEdit=require("./metaform.datafield.eidt");
	var DatafieldRemove=require("./metaform.datafield.remove");
	var OperationAdd=require("./metaform.operation.add");
	var OperationRemove=require("./metaform.operation.remove");
	var MetaFormModelAdd = Page.extend(function() {
		var _self=this;
		
		this.injecte([
		              new DatafieldAdd('formmodel_datafield_add'),
		              new DatafieldEdit('formmodel_datafield_edit'),
		              new DatafieldRemove('formmodel_datafield_remove'),
		              new OperationAdd('formmodel_operation_add'),
		              new OperationRemove('formmodel_operation_remove')
		        	]);
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			// 
			form.form('disableValidation')
			.form('focus');
			
			var dataFieldTable=panel.find('#formmodel_dataField');
			var operationTable=panel.find('#formmodel_operation');
			dataFieldTable.cdatagrid({
				controller:_self
			}).datagrid('loadData',[]);
			operationTable.cdatagrid({
				controller:_self
			}).datagrid('loadData',[]);
			
			
			
			
			
			panel.find('#metaform_sub').click(function(){
				var form = panel.find('form');
				// 开启校验
				form.form('enableValidation');
				var isValid = form.form('validate');
				var dataFieldTable=panel.find("#formmodel_dataField");
				var dataFieldTableRows=dataFieldTable.datagrid('getRows');
				var metaformmodel=form.form('value');
				var operationTable=panel.find("#formmodel_operation");
				var operationTableRows=operationTable.datagrid('getRows');
				$.extend(metaformmodel,{modelDataFields:dataFieldTableRows,modelOperations:operationTableRows})
				if (isValid && dataFieldTable.cdatagrid('endEdit') && operationTable.cdatagrid('endEdit')) {
					$.ajax({
						type: 'POST',
					    url: Config.ContextPath + 'service/metaform/metaformmodel/' ,
					    data: JSON.stringify(metaformmodel) ,
					    success: function(data){
							$.messager.alert('提示', '已保存！', 'info');
							var parentTable=_self.parent.panel.find('#metaformtable');
							parentTable.datagrid('reload');
						},
					    contentType:'application/json'

					});
				}
	    	});
			
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'service/metaform/metaformmodel',
					method: 'post'
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override 
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};
	});
	
	return MetaFormModelAdd;
});