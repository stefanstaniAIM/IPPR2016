(function() {
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
        function SidenavLeftController($log, $mdSidenav) {
            var self = this;

            self.sideNavOpened = function () {
                return $mdSidenav('sidenav-left').isOpen();
            };

            self.toogleSidenav = function () {
                $mdSidenav('sidenav-left')
                    .toggle()
                    .then(function () {
                        $log.debug("toggle sidenav-left is done");
                    });
            };
        }
    }

})();
