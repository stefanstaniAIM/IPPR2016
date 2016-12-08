(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('modeler', modeler);

    /** @ngInject */
    function modeler($log, storage) {

        var service = {};

        service.checkLocalStorage = function () {
            //
        };

        return service;

    }

})();
