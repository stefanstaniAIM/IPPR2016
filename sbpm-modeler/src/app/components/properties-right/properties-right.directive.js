(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('propertiesRight', propertiesRight);

    /** @ngInject */
    function propertiesRight() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/properties-right/properties-right.template.html',
            controller: PropertiesRightController,
            controllerAs: 'pr',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function PropertiesRightController($log, $mdSidenav) {
            var self = this;

            self.propertiesOpened = function () {
                return $mdSidenav('properties-right').isOpen();
            };

            self.closeProperties = function () {
                $mdSidenav('properties-right')
                    .close()
                    .then(function () {
                        $log.debug("close properties-right is done");
                    });
            };
        }
    }

})();
