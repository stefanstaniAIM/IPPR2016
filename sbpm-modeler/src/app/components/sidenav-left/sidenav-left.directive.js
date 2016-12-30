(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('sidenavLeft', sidenavLeft);

    /** @ngInject */
    function sidenavLeft() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/sidenav-left/sidenav-left.template.html',
            controller: SidenavLeftController,
            controllerAs: 'sl',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function SidenavLeftController($log, $mdSidenav, newFileDialog, changeViewDialog, $rootScope, modeler) {
            var TAG = 'sidenav-left.directive: ';

            var self = this;

            self.currentView = null;

            self.init = function() {

                $log.debug(TAG + "init()");

                self.currentView = modeler.getCurrentView();
            };

            $rootScope.$on('currentView:changed', function () {

                $log.debug(TAG + "currentView:changed");

                self.init();
            });

            self.changeView = function() {
                changeViewDialog.showDialog();
            };

            self.isCurrentViewSID = function() {
                return self.currentView === 'SID' ? true : false;
            };

            self.newFile = function() {
                newFileDialog.showDialog();
            };

            self.sideNavOpened = function() {
                return $mdSidenav('sidenav-left').isOpen();
            };

            self.toogleSidenav = function() {
                $mdSidenav('sidenav-left').toggle();
            };

            self.init();
        }
    }

})();
