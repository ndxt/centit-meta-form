(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitSelect', function() {
      return {
        restrict: 'EA',
        templateUrl: 'directives/select/select.directive.html',
        controller: 'SelectController as vm',
        scope: {
          sort: '=centitSelect',
          items: '=selectItems'
        }
      };
    });
})();
