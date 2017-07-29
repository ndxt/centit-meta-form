(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitPageSort', function() {
      return {
        restrict: 'EA',
        templateUrl: 'directives/page-sort/page-sort.directive.html',
        controller: 'PageSortController as vm',
        scope: {
          sort: '=centitPageSort',
          items: '=sortItems'
        }
      };
    });
})();
