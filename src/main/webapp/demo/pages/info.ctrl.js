(function() {
    'use strict';

    angular.module('metaForm')
        .controller('InfoController', InfoController);

    /* @ngInject */
    function InfoController($rootScope, $stateParams, toastr, FormAPI) {

        var vm = this;
        var modelCode = $stateParams.modelCode, id = $stateParams.id;
        vm.operation = $stateParams.operation;

        FormAPI.one('get')
            .customGET(modelCode, {
                id: id
            })
            .then(function(data) {
                vm.item = data.obj || {};
                vm.fields = data.fields;
            });

        /**
         * 返回
         */
        vm.back = function() {
            window.history.back();
        };

        /**
         * 创建对象
         */
        vm.create = function() {
            $rootScope.busyPromise = FormAPI.one('create')
                .customPOST(vm.item, modelCode)
                .then(function() {
                    toastr.success('创建成功！');
                });
        };

        /**
         * 修改对象
         */
        vm.modify = function() {
            $rootScope.busyPromise = FormAPI.one('edit')
                .customPUT(vm.item, modelCode)
                .then(function() {
                    toastr.success('修改成功！');
                });
        };

        /**
         * 删除对象
         */
        vm.remove = function() {
            $rootScope.busyPromise = FormAPI.one('delete')
                .customDELETE(modelCode, vm.item)
                .then(function() {
                    toastr.success('删除成功！');
                });
        };
    }
})();
