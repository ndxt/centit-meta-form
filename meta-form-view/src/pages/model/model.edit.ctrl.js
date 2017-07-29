(function() {
  'use strict';

  angular.module('metaForm')
    .controller('ModelEditController', ModelEditController);

  /* @ngInject */
  function ModelEditController($scope, FormModelAPI, model) {
    var vm = this;

    vm.model = model;

    vm.ok=ok;

    vm.cancel=cancel;

    ///////////////////////////////

    /**
     * 保存
     * @returns {*}
     */
    function ok () {
      return FormModelAPI.one(model.modelCode)
        .customPUT(vm.model)
        .then(function() {
          return FormModelAPI.one(model.modelCode).customGET()
        })
        .then(function(newModel) {
          $scope.$close(newModel);
        });

    }

    /**
     * 取消
     */
    function cancel() {
      $scope.$dismiss();
    }
  }
})();
