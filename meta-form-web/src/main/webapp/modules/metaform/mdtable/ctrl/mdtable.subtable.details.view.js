define(function(require) {
	var Page = require('core/page');

	// 新增子表
	var MdtableSubtableDetails = Page.extend(function() {
		
		var _self=this;
		// @override
		var editRow=undefined;
		
		// @override
		this.load = function(panel, data) {
			var subtableDetailsTable=panel.find("#mdtable_subtable_details");
			var mdtable_columns=this.parent.panel.find("#mdtable_columns_view");
			var mdtablecolumns=mdtable_columns.datagrid('getRows');
			var list=[];
			$.ajax({
				type: 'get',
				async:false, 
			    url: '/centit-meta-form/service/metaform/mdtable/draft/'+data.childTableId,
			    success: function(res){
			    	if(null!=res.data && null!=res.data.mdColumns)
			    		list=res.data.mdColumns;
				},
			    contentType:'application/json'
				});
			
			
			subtableDetailsTable.datagrid({
				height:'100%',
				editable:false,
				width:'100%',
				frozenColumns:[[
				          {field:'parentColumnName',title:'parentColumnName',width: 180,
				        	  editor:{
				        			type:'combobox',
				        			options:{
				        					required:true,
				        					valueField:'columnName',textField:'fieldLabelName',
											data:mdtablecolumns
											}},
				          },
				          {field:'childColumnName',title:'childColumnName',width: 180,
				        	  
				        	  editor:{
				        			type:'combobox',
				        			options:{
				        					required:true,
				        					valueField:'columnName',textField:'fieldLabelName',
											data:list
											}},
				          },
				          {field:'',title:'childColumnName'}
				          
				          
				         ]]
				      
	                  
			}).datagrid('loadData',$.extend([], data.relationDetails));
		};
		
		this.submit = function(table, data) {
			
			var subtableDetailsTable=_self.panel.find("#mdtable_subtable_details");
			subtableDetailsTable.datagrid('endEdit',editRow)
			if(editRow==undefined)
			{
				var details=subtableDetailsTable.datagrid('getRows');
				data.relationDetails=$.extend([], details);
				return true;
			}
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			editRow=undefined;
			table.datagrid('reload');
		};
	});
	return MdtableSubtableDetails;
});