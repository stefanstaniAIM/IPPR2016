(function() {
    'use strict';

    angular
        .module('components.directives')
        .directive('navigationMenuItem', navigationMenuItem);

    /** @ngInject */
    function navigationMenuItem() {

        var directive = {
            restrict: 'E',
            transclude: true,
            scope: {
                itemName: '@itemName'
            },
            templateUrl: 'app/components/directives/navigation-menu/navigation-menu-item/navigation-menu-item.template.html',
            controller: NavigationMenuItemController,
            controllerAs: 'nmic',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function NavigationMenuItemController() {
            var self = this;

            self.isOpen = true;
        }
    }

})();
