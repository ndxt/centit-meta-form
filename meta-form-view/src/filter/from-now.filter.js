(function() {
  'use strict';

  angular.module('metaForm')
    .filter('fromNow', function() {
      return function(date, format) {
        if (date) {
          return moment(date, format).fromNow();
        }

        return date;
      };
    })
})();
