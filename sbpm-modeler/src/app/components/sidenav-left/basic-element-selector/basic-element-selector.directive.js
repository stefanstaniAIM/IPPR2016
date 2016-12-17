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

            var currentView;

            self.isCurrentViewSID = isCurrentViewSID;

            function init() {
                currentView = modeler.getCurrentView();
                $log.debug(TAG + "successfully initiated");
            }

            $rootScope.$on('currentView-changed', function () {
                $log.debug(TAG + "currentView was changed");
                $log.debug(TAG + "update view");
                init();
            });

            function isCurrentViewSID() {
                return currentView === 'SID' ? true : false;
            }

            init();
        }
    }

})();
