define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var ModelDataFieldAdd = require('./modeldatafield.add');
    var ModelDataFieldEdit = require('./modeldatafield.edit');
    var ModelDataFieldView = require('./modeldatafield.view');
    var ModelDataFieldRemove = require('./modeldatafield.remove');

    // 业务信息
    var ModelDataField = Page.extend(function() {
    	
    	this.injecte([
          new ModelDataFieldAdd('modeldatafield_add'),
          new ModelDataFieldEdit('modeldatafield_edit'),
          new ModelDataFieldView('modeldatafield_view'),
          new ModelDataFieldRemove('modeldatafield_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return ModelDataField;
});