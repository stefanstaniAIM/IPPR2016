(function () {
    'use strict';

    angular.module('uiJoint.jointPaper', [])
        .factory('jointPaper', jointPaper);

    /** @ngInject */
    function jointPaper($log, $rootScope, jointWindow, jointGraph) {
        var service = this;

        service.paper = null;

        service.createPaper = function (element) {
            var graph = jointGraph.createGraph();

            $log.info('jointPaper - createPaper()');

            service.paper = new jointWindow.dia.Paper({
                el: element,
                width: '100%',
                height: '100%',
                gridSize: 1,
                model: graph
            });

            $rootScope.$broadcast('paper:created');

            return service.paper;
        };

        service.getPaper = function () {
            return service.paper;
        };

        return service;
    }

})();
