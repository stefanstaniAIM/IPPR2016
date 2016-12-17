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
        function PropertiesRightController($log, $mdSidenav, modeler, $rootScope) {
            var TAG = 'properties-right.directive: ';

            var self = this;

            var currentView;

            self.closeProperties = closeProperties;
            self.isCurrentViewSID = isCurrentViewSID;
            self.propertiesOpened = propertiesOpened;

            function init() {
                currentView = modeler.getCurrentView();
                $log.debug(TAG + "successfully initiated" + currentView);
            }

            $rootScope.$on('currentView-changed', function () {
                $log.debug(TAG + "currentView was changed");
                $log.debug(TAG + "update view");
                init();
            });

            function isCurrentViewSID() {
                return currentView === 'SID' ? true : false;
            }

            function propertiesOpened() {
                return $mdSidenav('properties-right').isOpen();
            }

            function closeProperties() {
                $mdSidenav('properties-right').close();
            }

            init();
        }
    }

})();
