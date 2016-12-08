(function() {
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
        function BasicElementSelectorController() {}
    }

})();
