(function() {
  'use strict';

  angular.module('metaForm')
    .controller('PageSortController', PageSortController);

  /* @ngInject */
  function PageSortController($scope) {
    var vm = this;

    var DEFAULT_SORT_NAME = '默认排序';

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
      // 对分隔符不响应
      if (item.isDivider) return;

      var isActive = !item.isActive;
      vm.items.forEach(function(o) {
        item === o ? o.isActive = isActive : o.isActive = false;
      });

      delete $scope.sort.sort;
      delete $scope.sort.order;
      // 选中排序item
      if (isActive) {
        angular.extend($scope.sort, {
          sort: item.sort,
          order: item.order
        });
        vm.currentSortName = item.name;
      }
      // 取消排序item
      else {
        vm.currentSortName = DEFAULT_SORT_NAME;
      }
    }

  }
})();
