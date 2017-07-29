define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var MdTableAdd = require('./mdtable.add');
    var MdTableEdit = require('./mdtable.edit');
    var MdTableView = require('./mdtable.view');
    var MdTableRemove = require('./mdtable.remove');
    var MdTablePublish = require('./mdtable.publish');
    
    // 业务信息
    var MdTable = Page.extend(function() {
    	
    	this.injecte([
          new MdTableAdd('mdtable_add'),
          new MdTableEdit('mdtable_edit'),
          new MdTableView('mdtable_view'),
          new MdTableRemove('mdtable_remove'),
          new MdTablePublish('mdtable_publish')
         
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return MdTable;
});