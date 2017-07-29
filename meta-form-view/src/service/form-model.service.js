(function() {
  'use strict';

  angular.module('metaForm')
    .factory('FormModelAPI', FormModelAPI);

  /* @ngInject */
  function FormModelAPI(Restangular) {
    return Restangular.service('service/metaform/metaformmodel');
  }
})();
