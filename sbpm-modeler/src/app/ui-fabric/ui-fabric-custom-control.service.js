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

        service.addCustomControl = function (subjectId) {
            $log.debug('add custom control: ' + subjectId);

            var customControlId = 'customControl' + createId();

            var newdiv1 = $("<div id='" + customControlId + "' style='width: 100px; height: 100px; background-color: #00b3ee; z-index: 9999; position: absolute;'>Test</div>");

            jQuery("#modeler-board").append(newdiv1);
            //jQuery("#" + customControlId).css({top: this.getTop() + 'px'});
            //jQuery("#" + customControlId).css({left: (this.getLeft() + this.getWidth() + 20) + 'px'});
            //$log.debug(this.getLeft());

        };

        service.init();

        return service;

    }

})();
