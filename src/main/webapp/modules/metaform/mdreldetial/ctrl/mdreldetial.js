define(function(require) {
    var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

    
    var MdRelDetialAdd = require('./mdreldetial.add');
    var MdRelDetialEdit = require('./mdreldetial.edit');
    var MdRelDetialView = require('./mdreldetial.view');
    var MdRelDetialRemove = require('./mdreldetial.remove');

    // 业务信息
    var MdRelDetial = Page.extend(function() {
    	
    	this.injecte([
          new MdRelDetialAdd('mdreldetial_add'),
          new MdRelDetialEdit('mdreldetial_edit'),
          new MdRelDetialView('mdreldetial_view'),
          new MdRelDetialRemove('mdreldetial_remove')
    	]);
    	
    	// @override
    	this.load = function(panel) {
    		var table = this.table = panel.find('table');
    		
    		table.cdatagrid({
    			controller: this
    		});
    	};
    	
    });

    return MdRelDetial;
});