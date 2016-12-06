(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .controller('SbpmModelerController', SbpmModelerController);

    /** @ngInject */
    function SbpmModelerController($log, $mdSidenav, $mdMedia) {
        var self = this;

        var test = true;

        self.sideNavOpened = function () {
            return $mdSidenav('sidenav-left').isOpen();
        };

        self.toogleTest = function () {
          test = !test;
        };

        self.openSidenav = function () {
          $mdSidenav('sidenav-left').open();
        };

        self.closeSidenav = function () {
            $mdSidenav('sidenav-left').close();
        };

        self.toogleSidenav = function () {
            $mdSidenav('sidenav-left')
                .toggle()
                .then(function () {
                    $log.debug("toggle sidenav-left is done");
                });
        };
    }
})();
