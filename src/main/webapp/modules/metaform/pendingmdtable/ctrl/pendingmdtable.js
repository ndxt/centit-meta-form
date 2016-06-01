define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var PendingMdTableAdd = require('./pendingmdtable.add');
    var PendingMdTableEdit = require('./pendingmdtable.edit');
    var PendingMdTableView = require('./pendingmdtable.view');
    var PendingMdTableRemove = require('./pendingmdtable.remove');

    // 业务信息
    var PendingMdTable = Page.extend(function() {
    	
    	this.injecte([
          new PendingMdTableAdd('pendingmdtable_add'),
          new PendingMdTableEdit('pendingmdtable_edit'),
          new PendingMdTableView('pendingmdtable_view'),
          new PendingMdTableRemove('pendingmdtable_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return PendingMdTable;
});