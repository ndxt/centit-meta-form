(function() {
    'use strict';

    angular.module('metaForm')
        .controller('ListController', ListController);

    /* @ngInject */
    function ListController($state, $stateParams, FormAPI) {
        var vm = this;

        /**
         * 查询列表
         *
         * @param id 模块id
         * @param isInitial 是否初始化获取表单信息
         */
        vm.search = function(params, isInitial) {
            var modelCode = $stateParams.modelCode;

            FormAPI.one('list')
                .customGETLIST(modelCode, angular.extend({
                    addMeta: !!isInitial
                }, params))
                .then(function(data) {
                    vm.items = data;

                    if (data.listDesc) {
                        vm.columns = data.listDesc.columns;
                        vm.filters = data.listDesc.filters;
                        vm.operations = data.listDesc.operations;

                        // 主键
                        vm.primaryKey = (vm.columns.filter(function(c) {
                            return c.primaryKey;
                        }) || vm.columns)[0].name;
                    }
                })
        };

        vm.search({}, true);


        /**
         * 详情页面
         *
         * @param item 数据
         * @param operator 操作
         */
        vm.operate = function(item, operator) {

            $state.go('form.info', {
                id: item[vm.primaryKey],
                modelCode: $stateParams.modelCode,
                operation: operator.operation
            });

        };

        /**
         * 创建对象
         */
        vm.create = function() {
            $state.go('form.info', {
                modelCode: $stateParams.modelCode,
                operation: 'create'
            });
        };
    }
})();
