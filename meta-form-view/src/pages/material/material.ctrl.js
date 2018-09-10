(function() {
  'use strict';

  angular.module('metaForm')
      .controller('MaterialController', MaterialController);

  /* @ngInject */
  function MaterialController($rootScope, $scope, FormModelAPI,$uibModal) {
    var vm = this;

    vm.query = query;
    vm.remove = remove;
    vm.edit = edit;

    init();

    ///////////////////////////////



    /**
     * 初始化
     */
    function init() {
      vm.query({
        pageSize: 12,
        pageNo: 1
      });

      // 该页面排序字段
/*      vm.sortItems = [
        {sort: 'lastModifyDate', order: 'desc', name: '最近的更新', default: true},
        {sort: 'lastModifyDate', order: 'asc', name: '更早的更新'},
        null,
        {sort: 'modelName', order: 'asc', name: '模块名升序'},
        {sort: 'modelName', order: 'desc', name: '模块名降序'}
      ];*/

/*      vm.params = {
        pageSize: 12,
        pageNo: 1
      };*/

      // 监控 params 当分页、排序或者查询条件改变时，进行查询
/*      $scope.$watch(function() {
        return vm.params;
      }, function(params) {
        if (params) {
          vm.query(params);
        }
      }, true);*/
    }

    /**
     * 分页
     * @param page
     */
/*    function goPage(page) {
      vm.params.pageNo = page;
    }*/

    /**
     * 删除
     * @param item
     * @param index
     */
    function remove(item, index) {
      FormModelAPI.one(item.modelCode)
          .remove()
          .then(function() {
            if (vm.models) {
              vm.models.splice(index, 1);
            }
          });

    }

    /**
     * 查询
     * @param params
     * @returns {*}
     */
    function query(params) {
      var fields = ['modelCode', 'modelComment', 'modelName', 'lastModifyDate'];

      return FormModelAPI.getList(angular.extend({field: fields}, params))
          .then(function(data) {
            vm.models = data;
            vm.pageDesc = data.pageDesc;
            vm.sortItems = data.map(function (items) {
              return {modelCode: items.modelCode,name: items.modelName,tableId: items.tableId,tableLabelName: items.tableLabelName,route: items.route}
            });
          });
    }

    /**
     * 编辑
     */
    function edit(model) {
      var modalInstance = $uibModal.open({
        templateUrl: 'pages/model/model.edit.html',
        controller: 'ModelEditController as vm',
        resolve: {
          model: function() {
            return FormModelAPI.one(model.modelCode).customGET().$object;
          }
        }
      });
      modalInstance.result.then(function (newModel) {
        angular.extend(model, newModel)
      });
    }

  }
})();
