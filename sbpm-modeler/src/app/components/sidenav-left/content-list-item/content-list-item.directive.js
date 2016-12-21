(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('contentListItem', contentListItem);

    /** @ngInject */
    function contentListItem() {
        var directive = {
            restrict: 'E',
            transclude: true,
            scope: {
                itemName: '@itemName'
            },
            templateUrl: 'app/components/sidenav-left/content-list-item/content-list-item.template.html',
            controller: ContentListItemController,
            controllerAs: 'cli',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function ContentListItemController() {
            var self = this;

            self.isOpen = true;
        }
    }

})();
