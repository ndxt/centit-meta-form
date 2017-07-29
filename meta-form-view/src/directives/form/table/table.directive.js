(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitFormTable', function() {
      return {
        restrict: 'EA',

        templateUrl: 'directives/form/table/table.directive.html',

        controller: 'FormTableController',

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
          cls: '@'
        },

        replace: true
      };
    });
})();
