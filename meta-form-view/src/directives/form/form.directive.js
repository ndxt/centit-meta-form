(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitForm', function() {
      return {

        restrict: 'EA',

        templateUrl: 'directives/form/form.directive.html',

        scope: {
          data: '=centitForm',
          form: '=',
          handlers: '=?',

          primaryKey: '@',
          primaryValue: '@',

          dataId: '@',
          modelCode: '@',
          operation: '@',

          name: '@',
          legend: '@'
        },

        replace: true,

        controller: 'MetaFormController'

      };
    });
})();
