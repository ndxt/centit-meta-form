define(function(require) {
	var Page = require('core/page');
	
	// 删除一条字段信息
	var DatafieldRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, row) {
			var index = table.datagrid('getRowIndex', row);
			table.datagrid('deleteRow', index);
		};
		
       
	});
	return DatafieldRemove;
});