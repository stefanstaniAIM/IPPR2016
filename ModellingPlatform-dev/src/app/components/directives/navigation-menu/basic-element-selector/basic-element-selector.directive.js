(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('basicElementSelector', basicElementSelector);

    /** @ngInject */
    function basicElementSelector() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/navigation-menu/basic-element-selector/basic-element-selector.template.html',
            controller: BasicElementSelectorController,
            controllerAs: 'bes',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function BasicElementSelectorController($log, $rootScope, modelerStorage) {
            var self = this;

            self.selectedView = null;

            self.init = function() {
                self.selectedView = modelerStorage.getSelectedView();
            };

            $rootScope.$on('selectedView:changed', self.init);

            self.selectedViewSID = function() {
                return self.selectedView === 'SID' ? true : false;
            };

            self.init();
        }
    }

})();
