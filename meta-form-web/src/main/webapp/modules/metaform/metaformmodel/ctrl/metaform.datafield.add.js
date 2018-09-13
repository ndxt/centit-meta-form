define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 新增角色信息
	var DatafieldAdd = Page.extend(function() {
		_self=this;	
		this.load = function(panel, data) {
			
			var parentTable = this.parent.panel.find('#formmodel_dataField');
			var selectedData = parentTable.datagrid('getRows').map(function(o) {
				return o.columnName;
			});
//					
			var table=panel.find("table");
			table.cdatagrid({
				controller:_self
			});

			var _tableId = $('#tableId').combobox('getValue');
			var tableId = this.parent.data.tableId || _tableId;

			if(!tableId){
				return false
			}else {
				Core.ajax(Config.ContextPath+'service/metaform/mdtable/'+tableId+'/getField', {
					type: 'json',
					method: 'get'
				}).then(function(data) {
					data = data.filter(function(o) {
						return selectedData.indexOf(o.columnName) == -1;
					});
					_self.data = data;
					table.datagrid('loadData',data);
				});
			}

		};
		
		this.submit = function(panel, data) {
			var table = panel.find('#mdtable_columns_view');
			var items = table.datagrid('getSelections');
			var parentTable = this.parent.panel.find("#formmodel_dataField");
			var tableId = this.parent.data.tableId;
			items.forEach(function(d){
				d.columnType='T';
				d.displayOrder=d.columnOrder;
				d.columnLabel = d.fieldLabelName;
				parentTable.datagrid('appendRow', $.extend({tableId:tableId}, d));
			});
		};
		
       
	});
	return DatafieldAdd;
});
