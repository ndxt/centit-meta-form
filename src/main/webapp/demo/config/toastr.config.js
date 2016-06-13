(function() {
    'use strict';

    angular.module('metaForm')
        .config(ToastrConfig);

    /* @ngInject */
    function ToastrConfig(toastrConfig) {
        angular.extend(toastrConfig, {
            //autoDismiss: false,
            //containerId: 'toast-container',
            //maxOpened: 0,
            //newestOnTop: true,
            positionClass: 'toast-bottom-right',
            //preventDuplicates: false,
            //preventOpenDuplicates: false,
            //target: 'body'
        });
    }
})();
