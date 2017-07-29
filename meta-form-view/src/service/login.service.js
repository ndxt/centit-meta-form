(function() {
  'use strict';

  angular.module('metaForm')
    .factory('LoginAPI', LoginAPI);

  /* @ngInject */
  function LoginAPI(Restangular) {

    return Restangular.service('login');

  }
})();
