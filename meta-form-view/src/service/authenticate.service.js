(function () {
  'use strict';

  angular
    .module('metaForm')
    .factory('Authenticate', Authenticate);

  /* @ngInject */
  function Authenticate ($q, CacheAPI) {
    var data = {
      user: undefined,

      options: {},

      roles: {}
    };

    return {
      login: login,
      logout: logout
    };

    ///////////

    function logout() {
      data = {
        user: undefined,

        options: {},

        roles: {}
      };

      return $q.resolve();
    }

    function login () {
      return CacheAPI
        .one('userdetails')
        .customGET()
        .then(saveUser)
        .catch(_error);

      function _error () {
        return $q.reject('requireLogin');
      }
    }

    function saveUser(user) {
      if (!user)
        return $q.reject();

      data.user = user;
      return $q.resolve(user);
    }
  }
})();
