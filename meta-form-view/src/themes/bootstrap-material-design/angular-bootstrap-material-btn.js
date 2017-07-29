(function() {
  'use strict';

  angular.module('metaForm')
    .directive('btn', function() {
      return {
        restrict: 'C',
        link: function($scope, $element) {
          $element.addClass('btn-raised');
        }
      };
    })
})();
