(function () {
    'use strict';

    angular.module('uiJoint.jointDirective', [])
        .directive('joint', joint);

    /** @ngInject */
    function joint($log, jointPaper) {
        return {

            restrict: 'A',
            link: function link(scope, element) {
                $log.info('jointDirective - link()');
                jointPaper.createPaper(element);
            }
        };
    }

})();
