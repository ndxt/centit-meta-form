define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var MdRelDetialAdd = require('./mdreldetial.add');

	var MdRelDetialEdit = MdRelDetialAdd.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath+'service/metaform/mdreldetial/'+data.cid, {
				type: 'json',
				method: 'get' 
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'cid')
					.form('focus');
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
					url: Config.ContextPath + 'service/metaform/mdreldetial/' + data.cid,
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

	return MdRelDetialEdit;
});