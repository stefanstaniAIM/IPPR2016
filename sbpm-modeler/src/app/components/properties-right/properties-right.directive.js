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
        function PropertiesRightController($log, $mdSidenav, modeler, $rootScope, fabric) {

            var TAG = 'properties-right.directive: ';

            var self = this;

            self.canvas = null;
            self.currentView = null;

            self.init = function() {

                $log.debug(TAG + 'init()');

                self.currentView = modeler.getCurrentView();
                self.canvas = fabric.getCanvas();

                /*
                 * Listen for fabric 'object:selected' event
                 */
                self.canvas.on('object:selected', function (element) {
                    self.hideProperties();
                    self.showProperties();
                });

                /*
                 * Listen for fabric 'selection:cleared' event
                 */
                self.canvas.on('selection:cleared', function (element) {
                    self.hideProperties();
                });
            };

            $rootScope.$on('currentView:changed', function () {

                $log.debug(TAG + "currentView:changed");

                self.init();
            });

            self.showProperties = function() {
                $mdSidenav('properties-right').open();
            };

            self.hideProperties = function() {
                $mdSidenav('properties-right').close();
            };

            self.isCurrentViewSID = function() {
                return self.currentView === 'SID' ? true : false;
            };

            self.propertiesOpened = function() {
                return $mdSidenav('properties-right').isOpen();
            };

            self.closeProperties = function() {
                $mdSidenav('properties-right').close();
            };

            self.init();
        }
    }

})();
