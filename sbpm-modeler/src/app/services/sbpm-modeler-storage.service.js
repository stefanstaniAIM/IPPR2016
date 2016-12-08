(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('modelerStorage', modelerStorage);

    /** @ngInject */
    function modelerStorage($log) {

        var storage = localStorage,
            service = {};

        service.initModelerSettings = function() {
            storage.setItem('modelerSettings', JSON.stringify({
                initiated: true
            }));
        };

        service.getModelerSettings = function() {
            return JSON.parse(storage.getItem('modelerSettings') || 'null');
        };

        return service;
    }

})();
