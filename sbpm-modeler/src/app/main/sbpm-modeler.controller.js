(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .controller('SbpmModelerController', SbpmModelerController);

    /** @ngInject */
    function SbpmModelerController($log, fabric,modeler) {
        var TAG = 'sbpm-modeler.controller: ';

        var self = this;

        self.testJson = function () {
            $log.debug(TAG + 'testJson()');
            var canvas = fabric.getCanvas();
            // save json
            var json = JSON.stringify(canvas);

            $log.debug(TAG);
            $log.debug(json);

            $log.debug(TAG);
            $log.debug(canvas);

            // clear canvas
            canvas.clear();

            // and load everything from the same json
            canvas.loadFromJSON(json, function () {

                // making sure to render canvas at the end
                canvas.renderAll();
                $log.debug(TAG);
                // and checking if object's "name" is preserved
                $log.debug(canvas.item(0));
            });
        };

        self.saveInLocalStorage = function () {
            var canvas = fabric.getCanvas();
            var json = canvas.toJSON();
            modeler.setSidViewObjects(json);
            $log.debug(modeler.getModelerSettings());
        };

        self.loadFromJSON = function () {
            var canvas = fabric.getCanvas();
            canvas.loadFromJSON(JSON.stringify(modeler.getSidViewObjects()), function () {
                canvas.renderAll();
                $log.debug(TAG + 'get all elements');
            });
        };
    }
})();
