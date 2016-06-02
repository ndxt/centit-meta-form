define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var MdRelationAdd = require('./mdrelation.add');
    var MdRelationEdit = require('./mdrelation.edit');
    var MdRelationView = require('./mdrelation.view');
    var MdRelationRemove = require('./mdrelation.remove');

    // 业务信息
    var MdRelation = Page.extend(function() {
    	
    	this.injecte([
          new MdRelationAdd('mdrelation_add'),
          new MdRelationEdit('mdrelation_edit'),
          new MdRelationView('mdrelation_view'),
          new MdRelationRemove('mdrelation_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return MdRelation;
});