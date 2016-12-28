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
        function PropertiesRightController($log, $mdSidenav, modeler, $rootScope, fabric, $scope) {
            var TAG = 'properties-right.directive: ';

            var self = this;

            self.canvas = null;

            var currentView;

            self.closeProperties = closeProperties;
            self.isCurrentViewSID = isCurrentViewSID;
            self.propertiesOpened = propertiesOpened;

            self.init = function() {

                $log.debug(TAG + 'init()');

                currentView = modeler.getCurrentView();

                self.canvas = fabric.getCanvas();

                /*
                 * Listen for fabric 'object:selected' event
                 */
                self.canvas.on('object:selected', function (element) {
                    hideProperties();
                    showProperties();
                });

                /*
                 * Listen for fabric 'selection:cleared' event
                 */
                self.canvas.on('selection:cleared', function (element) {
                    hideProperties();
                });
            };

            //$scope.$on('canvas:created', self.init);

            function showProperties() {
                $mdSidenav('properties-right').open();
            }

            function hideProperties() {
                $mdSidenav('properties-right').close();
            }

            function toogleProperties() {
                $mdSidenav('properties-right').toggle();
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

            self.init();
        }
    }

})();
