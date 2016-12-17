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
            scope: {
                currentView: '='
            },
            controller: SidenavLeftController,
            controllerAs: 'sl',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function SidenavLeftController($log, $mdSidenav, newFileDialog, changeViewDialog, $rootScope, modeler) {
            var TAG = 'sidenav-left.directive: ';

            var self = this;

            var currentView;

            self.changeView = changeView;
            self.isCurrentViewSID = isCurrentViewSID;
            self.newFile = newFile;
            self.sideNavOpened = sideNavOpened;
            self.toogleSidenav = toogleSidenav;

            function init() {
                currentView = modeler.getCurrentView();
                $log.debug(TAG + "successfully initiated");
            }

            $rootScope.$on('currentView-changed', function () {
                $log.debug(TAG + "currentView was changed");
                $log.debug(TAG + "update view");
                init();
            });

            function changeView() {
                changeViewDialog.showDialog();
            }

            function isCurrentViewSID() {
                return currentView === 'SID' ? true : false;
            }

            function newFile() {
                newFileDialog.showDialog();
            }

            function sideNavOpened() {
                return $mdSidenav('sidenav-left').isOpen();
            }

            function toogleSidenav() {
                $mdSidenav('sidenav-left').toggle();
            }

            init();
        }
    }

})();
