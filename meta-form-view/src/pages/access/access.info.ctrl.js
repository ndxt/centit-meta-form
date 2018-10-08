(function() {
  'use strict';

  angular.module('metaForm')
    .controller('AccessInfoController', AccessInfoController);

  /* @ngInject */
  function AccessInfoController($state,$stateParams,FormAPI) {
    var vm = this;

    vm.operation = $state.current.data.operation;
    vm.modelCode = $stateParams.modelCode;
    if($stateParams.primaryKey && $stateParams.primaryValue){
        vm.primaryKey = $stateParams.primaryKey;
        vm.primaryValue = $stateParams.primaryValue;
    }else{ //没有主键就找流程实例ID
        vm.primaryKey = 'flowInstId';
        vm.primaryValue = $stateParams.flowInstId;
    }
    vm.params = {
    };
    vm.params[vm.primaryKey] = vm.primaryValue;
    //////////////////////////////////////
  }
})();
