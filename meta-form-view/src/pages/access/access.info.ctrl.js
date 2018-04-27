(function() {
  'use strict';

  angular.module('metaForm')
    .controller('AccessInfoController', AccessInfoController);

  /* @ngInject */
  function AccessInfoController($state,$http,$stateParams,FormAPI) {
    var vm = this;

    vm.operation = $state.current.data.operation;
    vm.modelCode = $stateParams.modelCode;
    vm.primaryKey = $stateParams.primaryKey;
    vm.primaryValue = $stateParams.primaryValue;

    vm.params = {
        primaryKey:vm.primaryKey,
        primaryValue:vm.primaryValue
    };
    //vm.params[vm.primaryKey] = vm.primaryValue;
    //////////////////////////////////////


    return FormAPI.one(vm.modelCode)
        .customGET('viewAll', vm.params)
        .then(function(data) {
          var formModel = data.formModel || {};
          console.log(data);

          // 重新定义数据格式
/*          return $q.resolve({
            data: data.obj || {},
            fields: formModel.fields ? formModel.fields : [],
            formType: formModel.formType,
            modelName: formModel.modelName
          });*/
        });
  }
})();
