(function () {
    'use strict';

    angular
        .module('sbpmModeler')
        .controller('MainController', MainController);

    /** @ngInject */
    function MainController($scope, $rootScope, $log, $window, modelerStorage, joint, jointConfig) {
        var main = this;

        main.subjectElementDefaults = {};
        main.sendStateElementDefaults = {};
        main.receiveStateElementDefaults = {};
        main.functionStateElementDefaults = {};

        main.init = function () {
            $log.info('MainController - init()');

            main.subjectElementDefaults = jointConfig.getSubjectElementDefaults();
            main.sendStateElementDefaults = jointConfig.getSendStateElementDefaults();
            main.receiveStateElementDefaults = jointConfig.getReceiveStateElementDefaults();
            main.functionStateElementDefaults = jointConfig.getFunctionStateElementDefaults();

            joint.init();
        };

        $scope.$on('paper:created', main.init);

        $scope.$on('selectedView:changed', function (evt) {
            joint.handleViewChange();
        });

        $scope.$on('selectedSubject:reselect', function (evt, subject) {
            joint.handleSelectedSubjectChange(subject.id);
        });

        $scope.$on('subject:changed', function (evt, subject) {
            joint.handleSubjectUpdate(subject);
        });

        $scope.$on('messageConnector:changed', function (evt, messageConnector) {
            joint.handleMessageConnectorUpdate(messageConnector);
        });

        $scope.$on('state:changed', function (evt, state) {
            joint.handleStateUpdate(state);
        });

        $scope.$on('stateTransition:changed', function (evt, stateTransition) {
            joint.handleStateTransitionUpdate(stateTransition);
        });

        main.onDrop = function (target, source, ev) {
            if (source === 'subject-element') {
                main.subjectElementDefaults.position = {
                    x: ev.originalEvent.x - (main.subjectElementDefaults.size.width / 2),
                    y: ev.originalEvent.y - (main.subjectElementDefaults.size.height / 2)
                };
                joint.addSubjectElement(main.subjectElementDefaults);
                /*joint.addMessageConnectorElement({
                 source: {x: 500, y: 500},
                 target: {x: 800, y: 500}
                 });*/
            } else if (source === 'send-state-element') {
                main.sendStateElementDefaults.position = {
                    x: ev.originalEvent.x - (main.sendStateElementDefaults.size.width / 2),
                    y: ev.originalEvent.y - (main.sendStateElementDefaults.size.height / 2)
                };
                joint.addSendStateElement(main.sendStateElementDefaults);
            } else if (source === 'receive-state-element') {
                main.receiveStateElementDefaults.position = {
                    x: ev.originalEvent.x - (main.receiveStateElementDefaults.size.width / 2),
                    y: ev.originalEvent.y - (main.receiveStateElementDefaults.size.height / 2)
                };
                joint.addReceiveStateElement(main.receiveStateElementDefaults);
            } else if (source === 'function-state-element') {
                main.functionStateElementDefaults.position = {
                    x: ev.originalEvent.x - (main.functionStateElementDefaults.size.width / 2),
                    y: ev.originalEvent.y - (main.functionStateElementDefaults.size.height / 2)
                };
                joint.addFunctionStateElement(main.functionStateElementDefaults);
            }
        };
    }
})();
