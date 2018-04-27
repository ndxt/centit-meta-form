(function() {
  'use strict';

  angular.module('metaForm')
    .controller('AccessInfoController', AccessInfoController);

  /* @ngInject */
  function AccessInfoController($state,$stateParams,FormAPI) {
    var vm = this;

    vm.operation = $state.current.data.operation;
    vm.modelCode = $stateParams.modelCode;
    vm.primaryKey = $stateParams.primaryKey;
    vm.primaryValue = $stateParams.primaryValue;

    vm.params = {
    };
    vm.params[vm.primaryKey] = vm.primaryValue;
    vm.items = [{modelCode:'firstSubTable'},{modelCode:'secondtSubTable'},{modelCode:'thirdSubTable'}];
    //////////////////////////////////////
  }
})();
