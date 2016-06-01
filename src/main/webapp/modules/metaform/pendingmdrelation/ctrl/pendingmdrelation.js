define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var PendingMdRelationAdd = require('./pendingmdrelation.add');
    var PendingMdRelationEdit = require('./pendingmdrelation.edit');
    var PendingMdRelationView = require('./pendingmdrelation.view');
    var PendingMdRelationRemove = require('./pendingmdrelation.remove');

    // 业务信息
    var PendingMdRelation = Page.extend(function() {
    	
    	this.injecte([
          new PendingMdRelationAdd('pendingmdrelation_add'),
          new PendingMdRelationEdit('pendingmdrelation_edit'),
          new PendingMdRelationView('pendingmdrelation_view'),
          new PendingMdRelationRemove('pendingmdrelation_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return PendingMdRelation;
});