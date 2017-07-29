(function() {
  'use strict';

  angular.module('metaForm')
    .factory('CacheAPI', CacheAPI);

  /* @ngInject */
  function CacheAPI(Restangular) {

    return Restangular.service('system/cp');

  }
})();
