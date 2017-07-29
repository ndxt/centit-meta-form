(function() {
  'use strict';

  angular.module('metaForm')
    .factory('MainFrameAPI', MainFrameAPI);

  /* @ngInject */
  function MainFrameAPI(Restangular) {

    return Restangular.service('system/mainframe');

  }
})();
