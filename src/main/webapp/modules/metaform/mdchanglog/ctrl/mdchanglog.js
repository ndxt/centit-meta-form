define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var MdChangLogAdd = require('./mdchanglog.add');
    var MdChangLogEdit = require('./mdchanglog.edit');
    var MdChangLogView = require('./mdchanglog.view');
    var MdChangLogRemove = require('./mdchanglog.remove');

    // 业务信息
    var MdChangLog = Page.extend(function() {
    	
    	this.injecte([
          new MdChangLogAdd('mdchanglog_add'),
          new MdChangLogEdit('mdchanglog_edit'),
          new MdChangLogView('mdchanglog_view'),
          new MdChangLogRemove('mdchanglog_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return MdChangLog;
});