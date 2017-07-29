(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitFormOperation', function() {
      return {
        restrict: 'EA',

        templateUrl: 'directives/form/operation/operation.directive.html',

        controller: 'FormOperationController',

        scope: {
          operation: '=centitFormOperation',
          formModel: '=',
          item: '=',
          modelCode: '@',
          modelOperation: '@',
          cls: '@'
        },

        replace: true
      };
    });
})();
