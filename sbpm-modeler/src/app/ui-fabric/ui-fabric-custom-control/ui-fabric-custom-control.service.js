(function () {
    'use strict';

    angular.module('ui.fabric')
        .service('fabricCustomControl', fabricCustomControl);

    /** @ngInject */
    function fabricCustomControl($log, modeler, $compile, $rootScope) {
        const TAG = "ui-fabric-custom-control.service: ";

        var service = this;

        var createId = function () {
            return '_' + Math.random().toString(36).substr(2, 9)
        };

        var addSubjectCustomControl = function (customControlId) {
            return "<subject-custom-control custom-control-id='" + customControlId + "'></subject-custom-control>";
        };

        var positionCustomControl = function (object, customControlId) {
            jQuery("#" + customControlId).css({top: object.getTop() + 'px'});
            jQuery("#" + customControlId).css({left: (object.getLeft() + object.getWidth() + 20) + 'px'});
        };

        service.addCustomControl = function (object) {

            $log.debug(TAG + 'addCustomControl()');

            /*
             * Create a custom control Id
             * Add object Id and custom control Id to localStorage
             */
            var customControlId = 'customControl' + createId();

            /*
             * Add custom controls to SubjectElement or StateElement
             */
            if (object.get('type') === 'subjectElement') {
                modeler.addCustomControl(object.get('id'), customControlId);
                jQuery('#modeler-board').append($compile(addSubjectCustomControl(customControlId))($rootScope.$new()));
            } else {
                //TODO: Add custom controls to StateElement
            }

            setTimeout(function () {
                jQuery("#" + customControlId).hide();
                positionCustomControl(object, customControlId);
            }, 50);
        };

        service.removeCustomControl = function (objectId) {

            $log.debug(TAG + 'removeCustomControl()');

            var customControlId = modeler.getCustomControlId(objectId);
            modeler.removeCustomControl(objectId);
            jQuery('#' + customControlId).remove();
        };

        service.removeCustomControls = function () {

            $log.debug(TAG + 'removeCustomControls()');

            if (modeler.getCurrentView() === 'SID') {

                var customControlIds = modeler.getCustomControlIds();

                customControlIds.forEach(function (customControlId) {
                    jQuery('#' + customControlId).remove();
                });

            } else {
                //TODO: SBD view
            }

            modeler.removeCustomControls();
        };

        service.positionCustomControl = function (object) {

            $log.debug(TAG + 'positionCustomControl()');

            var customControlId = modeler.getCustomControlId(object.get('id'));
            positionCustomControl(object, customControlId);
        };

        service.setCustomControlVisibility = function (objectId, show) {

            $log.debug(TAG + 'setCustomControlVisibility()');

            var customControlId = modeler.getCustomControlId(objectId);

            if (show) {
                jQuery('#' + customControlId).show();
            } else {
                jQuery('#' + customControlId).hide();
            }
        };

        return service;

    }

})();
