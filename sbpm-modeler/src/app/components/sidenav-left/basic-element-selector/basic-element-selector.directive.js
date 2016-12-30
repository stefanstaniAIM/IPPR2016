(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('basicElementSelector', basicElementSelector);

    /** @ngInject */
    function basicElementSelector() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/sidenav-left/basic-element-selector/basic-element-selector.template.html',
            controller: BasicElementSelectorController,
            controllerAs: 'bes',
            bindToController: true
        };

        return directive;

        /** @ngInject */
            function BasicElementSelectorController($log, modeler, $rootScope) {

            var TAG = "basic-element-selector.directive: ";

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

            self.isCurrentViewSID = function() {
                return self.currentView === 'SID' ? true : false;
            };

            self.init();
        }
    }

})();
