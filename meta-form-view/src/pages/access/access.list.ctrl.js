(function() {
  'use strict';

  angular.module('metaForm')
    .controller('AccessListController', AccessListController);

  /* @ngInject */
  function AccessListController($stateParams) {
    var vm = this;

    vm.visible = false;

    vm.modelCode = $stateParams.modelCode;

    vm.toggle = toggle;

    ////////////////////////////////

    /**
     * toggle sub-panel
     */
    function toggle() {
      vm.visible =! vm.visible;
    }
  }
})();
