(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('storage', storage);

    /** @ngInject */
    function storage() {

        var storage = localStorage,
            service = {};

        service.set = function(key, value) {
            storage.setItem(key, JSON.stringify(value));
        };

        service.get = function(key) {
            return JSON.parse(storage.getItem(key) || 'null');
        };

        service.clear = function () {
            storage.clear();
        };

        return service;
    }

})();
