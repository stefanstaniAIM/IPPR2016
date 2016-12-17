(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('stateProperties', stateProperties);

    /** @ngInject */
    function stateProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/properties-right/state-properties/state-properties.template.html',
            controller: StatePropertiesController,
            controllerAs: 'stp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function StatePropertiesController($log) {
            var self = this;


        }
    }

})();
