define(function (require) {
    var Config = require('config');
    var Page = require('core/page');
    var MdtableColumnAdd = require("./mdtable.column.add");
    var MdtableColumnEdit = require("./mdtable.column.eidt");
    var MdtableColumnRemove = require("./mdtable.column.remove");
    var MdtableSubtableAdd = require("./mdtable.subtable.add");
    var MdtableSubtableRemove = require("./mdtable.subtable.remove");
    var MdtableSubtableDetails = require("./mdtable.subtable.details");
    var Core = require('core/core');

    var MdTableAdd = Page.extend(function () {

        var _self = this;
        // @override
        this.injecte([
            new MdtableColumnAdd('mdtable_column_add'),
            new MdtableColumnEdit('mdtable_column_edit'),
            new MdtableColumnRemove('mdtable_column_remove'),
            new MdtableSubtableAdd('mdtable_subtable_add'),
            new MdtableSubtableRemove('mdtable_subtable_remove'),
            new MdtableSubtableDetails('mdtable_subtable_details')
        ]);

        // @override
        this.load = function (panel, data) {
            var form = panel.find('form');
            var mdtable_columns = panel.find("#mdtable_columns");
            mdtable_columns.cdatagrid({
                controller: _self
            });

            var mdtable_subtables = panel.find("#mdtable_subtables");
            mdtable_subtables.cdatagrid({
                controller: _self
            });

            //
            form.form('disableValidation')
                .form('focus');
            mdtable_columns.datagrid('loadData', []);

            panel.find('#mdtable_sub').click(function () {
                var form = panel.find('form');
                // 开启校验
                form.form('enableValidation');
                var isValid = form.form('validate');
                var mdtable_columns = panel.find("#mdtable_columns");
                var mdtablecolumns = mdtable_columns.datagrid('getRows');
                var mdtable = $.extend({
                    tableState: 'N', // 状态 系统 S / R 查询(只读)/ N 新建(读写)
                    tableType: 'T'  // 类别 表 T table /视图 V view /大字段 C LOB/CLOB  目前只能是表
                }, form.form('value'));
                var mdtable_subtables = panel.find("#mdtable_subtables");
                var mdtablesubtables = mdtable_subtables.datagrid('getRows');
                $.extend(mdtable, {mdColumns: mdtablecolumns, mdRelations: mdtablesubtables});
                if (isValid && mdtable_columns.cdatagrid('endEdit') && mdtable_subtables.cdatagrid('endEdit')) {
                    $.ajax({
                        type: 'POST',
                        url: Config.ContextPath + 'service/metaform/mdtable/draft/',
                        data: JSON.stringify(mdtable),
                        success: function (data) {
                            $.messager.alert('提示', '已保存！', 'info');
                            var parentTable = _self.parent.panel.find('#mdtable');
                            parentTable.datagrid('reload');
                        },
                        contentType: 'application/json'

                    });
                    /*Core.ajax(Config.ContextPath + 'service/metaform/mdtable/draft/', {
                        type:'json',
                        method:'post',
                        data: mdtable,
                    }).then(function(data){
                        $.messager.alert('提示', '已保存！', 'info');
                        var parentTable=_self.parent.panel.find('#mdtable');
                        parentTable.datagrid('reload');
                    });*/
                }
            });
        };


        // @override
        this.onClose = function (table, data) {
            table.datagrid('reload');
        };
    });

    return MdTableAdd;
});