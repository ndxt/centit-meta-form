(function() {
    'use strict';

    angular.module('metaForm')
        .factory('FormAPI', FormAPI);

    /* @ngInject */
    function FormAPI(Restangular) {
        return Restangular.service('metaform/formaccess');
    }
})();
