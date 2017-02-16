(function () {
    'use strict';

    angular.module('uiJoint.jointGraph', [])
        .factory('jointGraph', jointGraph);

    /** @ngInject */
    function jointGraph($log, jointWindow) {
        var service = this;

        service.graph = null;

        service.createGraph = function () {
            $log.info('jointGraph - createGraph()');
            service.graph = new jointWindow.dia.Graph();
            return service.graph;
        };

        service.getGraph = function () {
            return service.graph;
        };

        return service;
    }

})();
