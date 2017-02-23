(function() {
    'use strict';

    angular.module('uiJoint.jointConfig', [])
        .factory('jointConfig', jointConfig);

    /** @ngInject */
    function jointConfig() {
        var service = this;

        var subjectElementDefaults = {
            position: {x: 0, y: 0},
            size: {width: 210.35, height: 130},
            attrs: { rect: { cursor: 'pointer', stroke: 'none', 'fill-opacity': 1, fill: 'blue'} },
            customAttrs: {
                name: 'Subject',
                startSubject: false
            }
        };

        var messageConnectorElementDefaults = {
            source: {},
            target: {},
            attrs: {
                '.connection': {stroke: '#222138', 'stroke-width': 3},
                '.connection-wrap': {fill: 'yellow', d: 'M 10 0 L 0 5 L 10 10 z'},
                '.marker-target': {fill: '#000000', d: 'M 10 0 L 0 5 L 10 10 z'}
            },
            //router: { name: 'orthogonal' },
            customAttrs: {
                isBidirectional: false,
                sourceToTarget: {
                    messageTypes: []
                },
                targetToSource: {
                    messageTypes: []
                }
            }
        };

        var sendStateElementDefaults = {
            position: {x: 0, y: 0},
            size: {width: 210.35, height: 130},
            attrs: { rect: { cursor: 'pointer', stroke: 'none', 'fill-opacity': 0, fill: 'blue'} },
            customAttrs: {
                name: 'Send',
                startState: false,
                endState: false
            }
        };

        var receiveStateElementDefaults = {
            position: {x: 0, y: 0},
            size: {width: 210.35, height: 130},
            attrs: { rect: { cursor: 'pointer', stroke: 'none', 'fill-opacity': 0, fill: 'blue'} },
            customAttrs: {
                name: 'Receive',
                startState: false,
                endState: false
            }
        };

        var functionStateElementDefaults = {
            position: {x: 0, y: 0},
            size: {width: 210.35, height: 130},
            attrs: { rect: { cursor: 'pointer', stroke: 'none', 'fill-opacity': 0, fill: 'blue'} },
            customAttrs: {
                name: 'Function',
                startState: false,
                endState: false
            }
        };

        var fromSendStateConnectorElementDefaults = {
            source: {},
            target: {},
            attrs: {
                '.connection': {stroke: '#222138', 'stroke-width': 3},
                '.connection-wrap': {fill: 'yellow', d: 'M 10 0 L 0 5 L 10 10 z'},
                '.marker-target': {fill: '#000000', d: 'M 10 0 L 0 5 L 10 10 z'}
            },
            //router: { name: 'orthogonal' },
            customAttrs: {
                toSubject: {},
                toMessage: {}
            }
        };

        var fromReceiveStateConnectorElementDefaults = {
            source: {},
            target: {},
            attrs: {
                '.connection': {stroke: '#222138', 'stroke-width': 3},
                '.connection-wrap': {fill: 'yellow', d: 'M 10 0 L 0 5 L 10 10 z'},
                '.marker-target': {fill: '#000000', d: 'M 10 0 L 0 5 L 10 10 z'}
            },
            //router: { name: 'orthogonal' },
            customAttrs: {
                fromSubject: {},
                fromMessage: {}
            }
        };

        var fromFunctionStateConnectorElementDefaults = {
            source: {},
            target: {},
            attrs: {
                '.connection': {stroke: '#222138', 'stroke-width': 3},
                '.connection-wrap': {fill: 'yellow', d: 'M 10 0 L 0 5 L 10 10 z'},
                '.marker-target': {fill: '#000000', d: 'M 10 0 L 0 5 L 10 10 z'}
            },
            //router: { name: 'orthogonal' },
            customAttrs: {
                transitionName: ''
            }
        };

        service.getSubjectElementDefaults = function () {
            return subjectElementDefaults;
        };

        service.getMessageConnectorElementDefaults = function () {
            return messageConnectorElementDefaults;
        };

        service.getSendStateElementDefaults = function () {
            return sendStateElementDefaults;
        };

        service.getReceiveStateElementDefaults = function () {
            return receiveStateElementDefaults;
        };

        service.getFunctionStateElementDefaults = function () {
            return functionStateElementDefaults;
        };

        service.getFromSendStateConnectorElementDefaults = function () {
            return fromSendStateConnectorElementDefaults;
        };

        service.getFromReceiveStateConnectorElementDefaults = function () {
            return fromReceiveStateConnectorElementDefaults;
        };

        service.getFromFunctionStateConnectorElementDefaults = function () {
            return fromFunctionStateConnectorElementDefaults;
        };

        return service;
    }

})();
