define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var PendingMdColumnAdd = require('./pendingmdcolumn.add');
    var PendingMdColumnEdit = require('./pendingmdcolumn.edit');
    var PendingMdColumnView = require('./pendingmdcolumn.view');
    var PendingMdColumnRemove = require('./pendingmdcolumn.remove');

    // 业务信息
    var PendingMdColumn = Page.extend(function() {
    	
    	this.injecte([
          new PendingMdColumnAdd('pendingmdcolumn_add'),
          new PendingMdColumnEdit('pendingmdcolumn_edit'),
          new PendingMdColumnView('pendingmdcolumn_view'),
          new PendingMdColumnRemove('pendingmdcolumn_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return PendingMdColumn;
});