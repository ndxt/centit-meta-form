define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var MetaFormModelAdd = require('./metaformmodel.add');

	var MetaFormModelEdit = MetaFormModelAdd.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			form.form('disableValidation');
			var dataFieldTable=panel.find('#formmodel_dataField');
			var operationTable=panel.find('#formmodel_operation');
			dataFieldTable.cdatagrid({
				controller:_self
			}).datagrid('loadData',[]);
			operationTable.cdatagrid({
				controller:_self
			}).datagrid('loadData',[]);
			
			
			Core.ajax(Config.ContextPath+'service/metaform/metaformmodel/'+data.modelCode, {
				type: 'json',
				method: 'get' 
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('readonly', 'modelCode')
					.form('focus');
				dataFieldTable.datagrid('loadData',data.modelDataFields);
				operationTable.datagrid('loadData',data.modelOperations);
			});
			
			//保存
			panel.find('#metaform_sub').click(function(){
				var form = panel.find('form');
				// 开启校验
				form.form('enableValidation');
				var isValid = form.form('validate');
				
				var metaformmodel=form.form('value');
				
				var dataFieldTable=panel.find("#formmodel_dataField");
				var dataFieldTableRows=dataFieldTable.datagrid('getRows');
				
				var operationTable=panel.find("#formmodel_operation");
				var operationTableRows=operationTable.datagrid('getRows');
				
				$.extend(metaformmodel,{modelDataFields:dataFieldTableRows,modelOperations:operationTableRows});
				if (isValid && dataFieldTable.cdatagrid('endEdit') && operationTable.cdatagrid('endEdit')) {
					$.ajax({
						type: 'PUT',
					    url: Config.ContextPath + 'service/metaform/metaformmodel/'+data.modelCode,
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
					url: Config.ContextPath + 'service/metaform/metaformmodel/' + data.modelCode,
					method: 'put',
					data: data 
				}).then(closeCallback);
			}

			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return MetaFormModelEdit;
});