(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('fastNav', fastNav);

    /** @ngInject */
    function fastNav() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/fast-nav/fast-nav.template.html',
            controller: FastNavController,
            controllerAs: 'fn',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function FastNavController($log) {
            var self = this;

        }
    }

})();
