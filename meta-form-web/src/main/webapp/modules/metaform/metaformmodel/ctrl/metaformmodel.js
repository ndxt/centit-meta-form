define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var MetaFormModelAdd = require('./metaformmodel.add');
    var MetaFormModelEdit = require('./metaformmodel.edit');
    var MetaFormModelView = require('./metaformmodel.view');
    var MetaFormModelRemove = require('./metaformmodel.remove');

    // 业务信息
    var MetaFormModel = Page.extend(function() {
    	
    	this.injecte([
          new MetaFormModelAdd('metaformmodel_add'),
          new MetaFormModelEdit('metaformmodel_edit'),
          new MetaFormModelView('metaformmodel_view'),
          new MetaFormModelRemove('metaformmodel_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return MetaFormModel;
});