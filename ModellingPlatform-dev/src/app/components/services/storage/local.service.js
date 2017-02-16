(function () {
    'use strict';

    angular.module('storage.local', []).factory('local', local);

    /** @ngInject */
    function local() {

        var storage = localStorage,
            service = {};

        service.set = function(key, value) {
            value = JSON.stringify(value);
            storage.setItem(key, value);
        };

        service.get = function(key) {
            return JSON.parse(storage.getItem(key) || 'null');
        };

        service.removeItem = function (key) {
            storage.removeItem(key);
        };

        return service;
    }

})();
