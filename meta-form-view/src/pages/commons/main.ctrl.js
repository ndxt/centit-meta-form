(function() {
  'use strict';

  angular.module('metaForm')
    .controller('MainController', MainController);

  /* @ngInject */
  function MainController($rootScope) {
    $rootScope.appTitle = '自定义表单';

    // 流动布局 or 固定布局
    $rootScope.isFluid = true;

    // flex direction: Row or Column
    $rootScope.isColumn = true;
  }
})();
