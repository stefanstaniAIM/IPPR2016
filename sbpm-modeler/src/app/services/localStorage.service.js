(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('storage', storage);

    /** @ngInject */
    function storage($log) {

        var storage = localStorage,
            service = {};


        function init() {
            if (typeof(Storage) !== "undefined") {
                $log.debug('Storage defined');
            } else {
                $log.debug('Storage undefined');
            }

        }

        init();

        return service;
    }

})();
