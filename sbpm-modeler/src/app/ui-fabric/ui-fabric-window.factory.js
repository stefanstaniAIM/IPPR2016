(function() {
    'use strict';

    angular.module('ui.fabric')
        .factory('fabricWindow', fabricWindow);

    // fabric.js must be included in your application's host file 'index.html'
    // For example: <script src="bower_components/fabric.js/dist/fabric.js"></script>
    // We need to wrap it in a service so that we don't reference global objects inside AngularJS components.

    /** @ngInject */
    function fabricWindow($window) {
        return $window.fabric;
    }

})();
