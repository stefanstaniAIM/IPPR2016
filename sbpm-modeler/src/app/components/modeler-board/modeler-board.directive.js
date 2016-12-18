(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('modelerBoard', modelerBoard);

    /** @ngInject */
    function modelerBoard() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/modeler-board/modeler-board.template.html',
            controller: ModelerBoardController,
            controllerAs: 'mb',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function ModelerBoardController() {
            //var self = this;

            var canvas = new fabric.Canvas('canvas-container');

            //canvas.setWidth(jQuery('#modeler-board').width()).setHeight(jQuery('#modeler-board').height());
            canvas.setWidth(window.innerWidth).setHeight(window.innerHeight);
            //canvas.backgroundColor = 'grey';

            var rect = new fabric.Rect({
                left: 500,
                top: 500,
                fill: 'red',
                width: 200,
                height: 200
            });
            canvas.add(rect);
            canvas.renderAll();
        }
    }

})();
