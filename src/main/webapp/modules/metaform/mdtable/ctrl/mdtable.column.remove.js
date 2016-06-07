define(function(require) {
	var Page = require('core/page');
	
	// 新增角色信息
	var MdtableColumnRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, row) {
			var index = table.datagrid('getRowIndex', row);
			table.datagrid('deleteRow', index);
		};
		
       
	});
	return MdtableColumnRemove;
});