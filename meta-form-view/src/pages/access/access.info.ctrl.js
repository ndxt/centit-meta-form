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
    console.log(vm.data);

    vm.params = {
    };
    vm.params[vm.primaryKey] = vm.primaryValue;
    vm.items = [{name:'第一个子表',modelCode:'firstSubTable'},{name:'第二个子表',modelCode:'secondtSubTable'},{name:'第三个子表',modelCode:'thirdSubTable'}];
    //////////////////////////////////////
  }
})();
