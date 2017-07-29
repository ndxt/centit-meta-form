define(function(require) {
//	var Config = require('config');
//	var Core = require('core/core');
	var Page = require('core/page');

	var DatafieldEdit = Page.extend(function() {
//		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			form.form('disableValidation');
			if(data.mandatory==null || data.mandatory=="")
				data.mandatory = 'F';//默认为否
			if(data.primarykey==null || data.primarykey=="")
				data.primarykey = 'F';//默认为否
			form.form('load', data)
				.form('focus');

		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			var formvalue =serializeObject(form);
			if (isValid) {
				var parentTable = this.parent.panel.find("#mdtable_columns");
				var index = parentTable.datagrid('getRowIndex', data);
				parentTable.datagrid('updateRow',{
					index:index,
					row:formvalue
				});
				closeCallback();
			}
			return false;
		};
		
		function serializeObject (form){
		     var obj = {};
		     $.each( form.serializeArray(), function(i,o){
		         var n = o.name, v = o.value;
		         obj[n] = obj[n] === undefined ? v
		         : $.isArray( obj[n] ) ? obj[n].concat( v )
		         : [ obj[n], v ];
		     });
		     return obj;
		 };
		
	});

	return DatafieldEdit;
});