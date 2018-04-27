(function() {
  'use strict';

  angular.module('metaForm')
    .controller('FormSubTableController', FormSubTableController);

  /* @ngInject */
  function FormSubTableController($scope, $state, FormAPI) {

    $scope.showOperation = $scope.showOperation === false ? false : true;

    $scope.options = {
      search: search,
      operate: operate,
      create: create
    };

    $scope.cascade = $scope.cascade === "true" ? true : false;

    $scope.operate = operate;

    // 非级联列表，直接读取数据
    if (!$scope.cascade) {
      search();
    }
    else {
      $scope.$watch(function() {
        return $scope.params;
      }, function(params) {
        if (params) {
          search(params);
        }
      });
    }


    ///////////////////////////////

    /**
     * 查询列表
     *
     * @param params 模块id
     */
    function search(params) {
      var modelCode = $scope.modelCode;
      var primaryKey = $scope.primaryKey;
      var primaryValue = $scope.primaryValue;
      var paramsobj ={};
      paramsobj[primaryKey] = primaryValue;
      return $scope.tablePromise = FormAPI.one(modelCode)
        .customGETLIST('list', angular.extend({noMeta: $scope.formModel ? true : undefined}, params,paramsobj))
        .then(function(data) {
          $scope.items = data;
          if (data.formModel) {
            $scope.columns = data.formModel.columns;
            $scope.filters = data.formModel.fields;

            // 操作按钮布局：T top显示在表格顶上， L list显示在列表页面
            $scope.operations = {
              top: [],
              list: []
            };
            $scope.operations = data.formModel.operations.reduce(function(map, obj) {
              if ('T' == obj.displayPostion) {
                map.top.push(obj);
              }
              else {
                map.list.push(obj);
              }
              return map;
            }, $scope.operations);
          }
          $scope.formModel = data.formModel || $scope.formModel || {};
        });
    }

    /**
     * 操作
     *
     * @param item 数据
     * @param operator 操作
     */
    function operate(item, operator) {
      var primaryKey = $scope.formModel.primaryKey || [],
        primaryValue = primaryKey.map(function(key) {
          return item[key]
        });

      $state.go('form.access.' + operator.method, {
        primaryKey: primaryKey,
        primaryValue: primaryValue,
        modelCode: $scope.modelCode
      });
    }

    /**
     * 创建对象
     */
    function create() {
      $state.go('form.access.create', {
        modelCode: $scope.modelCode
      });
    }
  }
})();
