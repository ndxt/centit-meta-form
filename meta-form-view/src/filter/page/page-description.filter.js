(function() {
  'use strict';

  angular.module('metaForm')
    .filter('pageDescription', function() {
      return function(page, name, unit) {
        if (!page) return '';

        unit = unit || '个';

        if (page.totalRows) {
          return '为您找到 ' + page.totalRows + unit + name;
        }
        else {
          return '没有找到任何' + name;
        }


      };
    })
})();
