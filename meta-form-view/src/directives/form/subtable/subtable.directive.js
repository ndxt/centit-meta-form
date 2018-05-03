(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitFormSubTable', function() {
      return {
        restrict: 'EA',

        templateUrl: 'directives/form/subtable/subtable.directive.html',

        controller: 'FormSubTableController',

        scope: {
          operations: '=?',
          columns: '=?',
          items: '=?',
          filters: '=?',
          options: '=?',
          showOperation: '=?',
          params: '=?',
          cascade: '@',
          modelCode: '@',
          primaryKey: '@',
          primaryValue: '@',
          paramsData:'=paramsData',
          cls: '@'
        },

        replace: true
      };
    });
})();
