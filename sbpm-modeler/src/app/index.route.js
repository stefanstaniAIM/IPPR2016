(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .config(routeConfig);

    /** @ngInject */
    function routeConfig($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'app/main/sbpm-modeler.template.html',
                controller: 'SbpmModelerController',
                controllerAs: 'smctrl'
            });

        $urlRouterProvider.otherwise('/');
    }

})();
