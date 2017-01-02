(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .controller('SbpmModelerController', SbpmModelerController);

    /** @ngInject */
    function SbpmModelerController($window, fabricStorage) {
        var self = this;

        $window.onbeforeunload = function () {
            fabricStorage.beforeUnloadHandler();
        };

        $window.onload = function () {
            fabricStorage.afterUnloadHandler();
        };

        self.saveInLocalStorage = function () {
            fabricStorage.clearSidView();
        };

        self.loadFromJSON = function () {
            fabricStorage.loadSidView();
        };
    }
})();
