define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var MdColumnAdd = require('./mdcolumn.add');
    var MdColumnEdit = require('./mdcolumn.edit');
    var MdColumnView = require('./mdcolumn.view');
    var MdColumnRemove = require('./mdcolumn.remove');

    // 业务信息
    var MdColumn = Page.extend(function() {
    	
    	this.injecte([
          new MdColumnAdd('mdcolumn_add'),
          new MdColumnEdit('mdcolumn_edit'),
          new MdColumnView('mdcolumn_view'),
          new MdColumnRemove('mdcolumn_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return MdColumn;
});