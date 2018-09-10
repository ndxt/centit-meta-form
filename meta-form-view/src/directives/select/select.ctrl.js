(function() {
  'use strict';

  angular.module('metaForm')
    .controller('SelectController', SelectController);

  /* @ngInject */
  function SelectController($scope,$state) {
    var vm = this;

    var DEFAULT_SORT_NAME = '石灰';

    // 供选择的排序
    vm.items = $scope.items || [];

    // 没有选中时显示的排序字段
    vm.currentSortName = DEFAULT_SORT_NAME;

    vm.select = select;

    init();

    //////////////////////

    /**
     * 初始化
     */
    function init() {


      // items 中为 null 的对象转换为分隔符
      vm.items = vm.items.map(function(item) {
        return item ? item : {isDivider: true};
      });

      var defaultItem = vm.items.filter(function(item) {
        return item.default;
      })[0];

      // 默认选中
      if (defaultItem) {
        vm.select(defaultItem);
      }
    }

    /**
     * 选择排序
     * @param item
     */
    function select(item) {
      // 对分隔符不响应form.access.create
      $state.go('form.access.list',{modelCode: item.modelCode});
      //$state.go('form.access.create',{modelCode: item.modelCode});

    }

  }
})();
