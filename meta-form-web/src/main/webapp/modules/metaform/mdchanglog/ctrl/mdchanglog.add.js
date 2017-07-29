define(function(require) {
	var Config = require('config');
	var Page = require('core/page');
	
	var MdChangLogAdd = Page.extend(function() {
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			// 
			form.form('disableValidation')
			.form('focus');
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'service/metaform/mdchanglog',
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
	
	return MdChangLogAdd;
});