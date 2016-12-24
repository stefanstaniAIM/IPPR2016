(function () {
    'use strict';

    angular.module('ui.fabric')
        .service('fabricCustomControl', fabricCustomControl);

    /** @ngInject */
    function fabricCustomControl($log, modeler) {
        const TAG = "ui-fabric-custom-service.service: ";

        var service = this;

        service.init = function () {

            $log.debug(TAG + 'init()');

        };

        var createId = function () {
            return '_' + Math.random().toString(36).substr(2, 9)
        };

        var createCustomControlDiv = function (customControlId) {
            return $("<div id='" + customControlId + "' style='width: 100px; height: 100px; background-color: #00b3ee; " +
                "z-index: 9999; position: absolute;'>Test</div>");
        };

        var createCustomControl = function (subjectElement) {
            var customControlId = 'customControl' + createId();
            modeler.addCustomControl(subjectElement.get('id'), customControlId);
            jQuery("#modeler-board").append(createCustomControlDiv(customControlId));
            //jQuery("#" + customControlId).hide();
            positionCustomControl(subjectElement, customControlId);
        };

        var positionCustomControl = function (subjectElement, customControlId) {
            jQuery("#" + customControlId).css({top: subjectElement.getTop() + 'px'});
            jQuery("#" + customControlId).css({left: (subjectElement.getLeft() + subjectElement.getWidth() + 20) + 'px'});
        };

        service.addCustomControl = function (subjectElement) {
            $log.debug(TAG + 'addCustomControl()');
            createCustomControl(subjectElement);
        };

        service.positionCustomControl = function (subjectElement) {
            $log.debug(TAG + 'positionCustomControl()');
            var customControlId = modeler.getCustomControlId(subjectElement.get('id'));
            positionCustomControl(subjectElement, customControlId);
        };

        service.setCustomControlVisibility = function (subjectElement) {
            $log.debug(TAG + 'setCustomControlVisibility()');
            var customControlId = modeler.getCustomControlId(subjectElement.get('id'));
            jQuery('#' + customControlId).is(":visible") ? jQuery('#' + customControlId).hide() : jQuery('#' + customControlId).show();
        };

        service.init();

        return service;

    }

})();
