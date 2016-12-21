(function() {
    'use strict';

    angular.module('ui.fabric')
        .directive('fabric', fabric);

    /** @ngInject */
    function fabric($log, fabricCanvas) {
        return {

            restrict: 'A',
            scope: {
                options: '='
            },
            link: function link(scope, element) {
                var TAG = 'ui-fabric.directive: ';

                $log.debug(TAG + 'link()');

                var options = scope.options;

                // var options = scope.options || angular.copy(fabricService.getCanvasDefaults());
                // $log.info('options: ' + JSON.stringify(['e', options], null, '\t'));

                fabricCanvas.setElement(element);
                fabricCanvas.createCanvas(options);

            }
        };
    }

})();
