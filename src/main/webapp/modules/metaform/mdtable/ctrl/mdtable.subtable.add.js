define(function(require) {
	var Page = require('core/page');
	
	// 新增子表
	var MdtableSubtableAdd = Page.extend(function() {
		
		this.submit = function(table, data) {
			if (!table.cdatagrid('endEdit')) {
				return;
			}
			var form=this.parent.panel.find('form');
			var value=form.form('value');
			table.datagrid('appendRow', $.extend({parentTableId:this.parent.data.tableId,relationDetails:[]}, this.object));
			var index = table.datagrid('getRows').length - 1;
			table.datagrid('selectRow', index);
			table.cdatagrid('beginEdit', index);
			var rows=table.datagrid('getRows');
			console.log(rows);
		};
		
       
	});
	return MdtableSubtableAdd;
});