define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var PendingMdRelDetialAdd = require('./pendingmdreldetial.add');
    var PendingMdRelDetialEdit = require('./pendingmdreldetial.edit');
    var PendingMdRelDetialView = require('./pendingmdreldetial.view');
    var PendingMdRelDetialRemove = require('./pendingmdreldetial.remove');

    // 业务信息
    var PendingMdRelDetial = Page.extend(function() {
    	
    	this.injecte([
          new PendingMdRelDetialAdd('pendingmdreldetial_add'),
          new PendingMdRelDetialEdit('pendingmdreldetial_edit'),
          new PendingMdRelDetialView('pendingmdreldetial_view'),
          new PendingMdRelDetialRemove('pendingmdreldetial_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return PendingMdRelDetial;
});