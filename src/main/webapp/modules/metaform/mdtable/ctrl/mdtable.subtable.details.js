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
			var mdtable_columns=this.parent.panel.find("#mdtable_columns");
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
				height:'80%',
				width:'90%',
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
				          
				          
				         ]],
		         
		         toolbar:[
							{
							    text:'Add',
							    iconCls:'icon-add',
							    handler:function(){
							    	if (editRow != undefined) {
							    		subtableDetailsTable.datagrid("endEdit", editRow);
							    	}
							    	if (editRow == undefined) {
							    		var obj={};
								    	subtableDetailsTable.datagrid('appendRow', obj);
										var index = subtableDetailsTable.datagrid('getRows').length - 1;
										editRow=index;
										subtableDetailsTable.datagrid('beginEdit', index);
				                    }
							    	
							    	
							    	
									
							    	}
							    
							},
							{
							    text:'delete',
							    iconCls:'icon-remove',
							    handler:function(){
							    	if (editRow != undefined) {
							    		subtableDetailsTable.datagrid("endEdit", editRow);
							    	}
							    	
							    	var rows = subtableDetailsTable.datagrid("getSelections");
									 //选择要删除的行
									 if (rows.length > 0) {
										for (var i = 0; i < rows.length; i++) {
													 var index = subtableDetailsTable.datagrid('getRowIndex', rows[i]);
													 subtableDetailsTable.datagrid('deleteRow', index);
										 }
									 }
									 else{
										 $.messager.alert("提示", "请选择要删除的行", "error");
									 }
									
							    }
							    	
							    
							}
		                  ],
	                  onDblClickRow: function (rowIndex, rowData) {
	                          if (editRow != undefined) {
	                        	  subtableDetailsTable.datagrid("endEdit", editRow);
	                          }
	                          if (editRow == undefined) {
	                        	  subtableDetailsTable.datagrid("beginEdit", rowIndex);
	                              editRow = rowIndex;
	                          } 
	                  },
	                  onAfterEdit: function (rowIndex, rowData, changes) {
	                      editRow = undefined;
	                  },
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